/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.uiAppInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import webservicerank.configuration.DBAttributes;
import webservicerank.configuration.ExecConfigHandler;
import webservicerank.configuration.FileNames;
import webservicerank.configuration.NetworkConfigHandler;
import webservicerank.configuration.NetworkStatus;
import webservicerank.configuration.OntologyInfoManager;
import webservicerank.serviceDB.ServiceDbInteracter;

/**
 *
 * @author webservicerank
 */
@WebServlet(name="AdminInterface", urlPatterns={"/AdminInterface"})
public class AdminInterface extends HttpServlet {
   
    String[] newOntologies = null;
    String[] newProperties = null;


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException{
        PrintWriter out = null;
        response.setContentType("text/html;charset=UTF-8");        
        try {
            out = response.getWriter();
            String configuration = request.getParameter("configuration");

            if (configuration != null){
                //Initial setup of the System
                if("setup".equals(configuration)) {
                    String workDir = request.getParameter("workingDir");
                    initialSetup(out, workDir);
                }

                //Setting the WordNet's dictionay directory
                else if ("wordnet".equals(configuration)){
                    String path = request.getParameter("directory");
                    if (!(new File(path)).isDirectory()){
                        out.print("The path is not a valid directory.:error");
                        return;
                    }
                    try {
                        ExecConfigHandler.configWordnetDir(path);
                        out.print("The path has been successfully set:success");
                    } catch (Throwable ex) {
                        out.print("Error occurred:error");
                    }
                }

                //Setting the network parameters
                else if ("network".equals(configuration)){
                    NetworkStatus nw = new NetworkStatus();
                    if ("yes".equals(request.getParameter("isProxy"))){
                        nw.setIsProxy(true);
                        nw.setIP(request.getParameter("ip"));
                        nw.setPort(request.getParameter("port"));
                        if ("yes".equals(request.getParameter("isLogin"))){
                            nw.setIsLogin(true);
                            nw.setUsername(request.getParameter("username"));
                            nw.setPassword(request.getParameter("password"));
                        }else{
                            nw.setIsLogin(false);
                        }
                    }else{
                        nw.setIsProxy(false);
                    }
                    try {
                        NetworkConfigHandler.configNetwork(nw);
                        out.print("Network options have been configured successfully:success");
                    } catch (Throwable ex) {
                        out.print("Error occurred:error");
                    }
                }

                //Setting the parameters of Service Database
                else if ("serviceDB".equals(configuration)){
                    DBAttributes db = new DBAttributes();
                    db.setDriver(request.getParameter("driver"));
                    db.setHost(request.getParameter("host"));
                    db.setPort(request.getParameter("port"));
                    if ("yes".equals(request.getParameter("isLogin"))){
                        db.setUsername(request.getParameter("username"));
                        db.setPassword(request.getParameter("password"));
                    }else{
                        db.setUsername(null);
                        db.setPassword(null);
                    }
                    try {
                        ExecConfigHandler.configureServiceDB(db);
                        out.print("Service database parameters have been successfully set:success");
                    } catch (Throwable ex) {
                        out.print("Error occurred:error");
                    }
                }

                //Setting ontology repository parameters
                else if ("ontologyDB".equals(configuration)){
                    DBAttributes db = new DBAttributes();
                    db.setDriver(request.getParameter("driver"));
                    db.setHost(request.getParameter("host"));
                    db.setPort(request.getParameter("port"));
                    if ("yes".equals(request.getParameter("isLogin"))){
                        db.setUsername(request.getParameter("username"));
                        db.setPassword(request.getParameter("password"));
                    }else{
                        db.setUsername(null);
                        db.setPassword(null);
                    }
                    try {
                        ExecConfigHandler.configureOntologyDB(db, request.getParameter("dbType"));
                        out.print("Ontology repository parameters have been successfully set:success");
                    } catch (Throwable ex) {
                        out.print("Error occurred:error");
                    }
                } else {
                    out.print("Configuration not recognized");
                }
            }else if("show_services".equals(request.getParameter("query"))){
                ServiceDbInteracter sdb = new ServiceDbInteracter();
                String[][] services = sdb.listServices();
                System.out.println("Printing messages="+services);
                System.out.println("Printing messages="+services.length);
                for(int i=0; i<services.length; i++){
                    for(int j=0; j<services[i].length; j++){
                        System.out.println(services[i][j]);
                    }
                }
                request.getSession().setAttribute("show_service", "true");
                request.getSession().setAttribute("service_list", services);
                response.sendRedirect(request.getContextPath()+"/admin/index.jsp");
            }else if ("viewdetail".equals(request.getParameter("query"))){
                int s_id = Integer.valueOf(request.getParameter("service_id"));
                if (s_id == 0){
                    out.print("The service ID is not recognized!");
                }else{
                    ServiceDbInteracter dbi = new ServiceDbInteracter();
                    boolean doesExist = false;
                    String toSend = "";
                    try {
                        String[][] service = dbi.getServiceDetails(s_id);
                        for (int i=0; service != null && i<service.length; i++){
                            if (service[i][1] != null){
                                toSend +=service[i][0]+"####"+service[i][1]+"$$$$$";
                                doesExist = true;
                            }
                        }
                    } catch (Throwable ex) {
//                        Logger.getLogger(AdminInterface.class.getName()).log(Level.SEVERE, null, ex);
                        doesExist = true;
                        toSend = (ex.getMessage() != null)?ex.getMessage():"Error while reading database";

                    }
                    if(doesExist)
                        out.print(toSend);
                    else
                        out.print("There are no services having no properties!$$$$$");
                }
            }else if ("checkAsMarked".equals(request.getParameter("action"))){
                int s_id = Integer.valueOf(request.getParameter("service_id"));
                if (s_id == 0){
                    out.print("The service ID is not recognized!");
                }else{
                    try {
                        String message = (new ServiceDbInteracter()).markAsChecked(s_id);
                        out.print(message);
                    } catch (Throwable ex) {
                        String error = (ex.getMessage() != null)?ex.getMessage():"Error while updating";
                        out.print(error);
                    }
                }
            }else if ("deleteService".equals(request.getParameter("action"))){
                int s_id = Integer.valueOf(request.getParameter("service_id"));
                if (s_id == 0){
                    out.print("The service ID is not recognized!");
                }else{
                    try {
                        String message = (new ServiceDbInteracter()).deleteService(s_id);
                        out.print(message);
                    } catch (Throwable ex) {
                        String error = (ex.getMessage() != null)?ex.getMessage():"Error while deleting";
                        out.print(error);
                    }
                }
            }else if ("useService".equals(request.getParameter("action"))){
                int service_id = Integer.valueOf(request.getParameter("service_id"));
                if (service_id != 0){
                    boolean isAdmin = ("admin".equals(request.getParameter("source")))?true:false;
                    ServiceDbInteracter sdb= new ServiceDbInteracter();
                    String[] service = sdb.getServiceDetail(service_id);
                    request.getSession().setAttribute("service_id", service[4]);
                    request.getSession().setAttribute("webservice", service[0]);
                    request.getSession().setAttribute("wsdl", service[1]);
                    request.getSession().setAttribute("description", service[2]);
                    request.getSession().setAttribute("reputation", service[3]);
                    if (isAdmin){
                        request.getSession().setAttribute("isAdmin", "true");
                    }else{
                        request.getSession().removeAttribute("isAdmin");
                    }
                    response.sendRedirect(request.getContextPath()+"/client/index.jsp");
                }else{
                    request.getSession().setAttribute("message", "The service is not found.");
                    response.sendRedirect(request.getContextPath()+"/admin/index.jsp");
                }
            }else if ("cleanup".equals(request.getParameter("action"))){
                String message = (new ServiceDbInteracter()).performCleanup();
                out.print(message);
            }else if("propLessServices".equals(request.getParameter("query"))){
                String[][] services = null;
                String outText = "Error: No services are found";
                try {
                    services = (new ServiceDbInteracter()).getServiceWithNoProperties();
                } catch (Throwable ex) {
                    String error = (ex.getMessage() != null)?ex.getMessage():"Error due to exception";
                    outText += error;
                }
                if(services!=null){
                    outText = "";
                    for (int i=0; i<services.length; i++){
                        outText += services[i][0]+"####"+services[i][1]+"$$$$";
                    }
                }
                out.print(outText); 
            }
        } catch (Exception ex) {
            String error = (ex.getMessage() != null)?ex.getMessage()+":failure":"The process couldn't be succeeded(IOException):failure";
            out.print(error);
        }finally {
            out.close();
        }
    }


    private void initialSetup(PrintWriter out, String workDir) throws IOException{
        String separator = System.getProperty("file.separator");
        if (!(new File(workDir)).isDirectory()){
            out.print("Error! The path is not available or it is not a directory"
                    +"\nEnter correct path of the directory:failure");
            return;
        }else{ //The path is a directory and that exists
            workDir += separator + "webservicerank";
            File temp = new File(workDir);
            if (temp.exists()){
                out.print("Error! The specified directory already contains file or"+
                        " directory named 'webservicerank':failure");
                return;
            }else{
                boolean mkdir = temp.mkdir();
                if (!mkdir){
                    out.print("Error! Directory couldn't be created in entered path:failure");
                    return;
                }else{
                    String ontDir = workDir + separator + "ontologies";
                    if(!(new File(ontDir)).mkdir()){
                        out.print("Sorry! The process couldn't be succeeded:failure");
                        return;
                    }
                    ExecConfigHandler.prepareConfiguration(new FileWriter(workDir+separator+FileNames.EXECUTION_CONFIG));
                    NetworkConfigHandler.prepareNetworkConfig(new FileWriter(workDir + separator +FileNames.NETWORK_CONFIG));
                    OntologyInfoManager.prepareOntologyInfo(new FileWriter(workDir + separator +FileNames.ONT_INFO));
                    FileNames.rootSetup(new FileWriter(FileNames.ROOT_CONFIG), workDir);
                    out.print("Initial setup has been successfully completed.:success");
                }
            }
        }
    }
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
