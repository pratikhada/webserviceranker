/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.uiAppInterface;

import com.wsr.factory.ServiceDaoFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import webservice.webserviceclient.ServiceUser;
import webservicerank.configuration.NetworkConfigHandler;

/**
 *
 * @author webservicerank
 */
@WebServlet(name="ClientInterface", urlPatterns={"/ClientInterface"})
public class ClientInterface extends HttpServlet {

    ServiceUser sUser;
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();        
        int method = Integer.parseInt(request.getParameter("method"));
        String serviceid = request.getParameter("service_id");
        int s_id = 0;
        if (serviceid != null)
            s_id = Integer.parseInt(serviceid);
        String output[];
        int[] invoke_counts = new int[]{0, 0, 0};
        long[] serviceTime = new long[1];
        serviceTime[0] = 0;
        try {            
            if (method == 0){
                (new NetworkConfigHandler()).proxyAuthenticate();
                String sn = request.getParameter("servicename");
                String wUrl = request.getParameter("wsdlUrl");
                sUser = new ServiceUser(wUrl, sn);
                output = sUser.listOperations();
                for (int i=0; i<output.length; i++){
                    out.print(output[i]+";");
                }
            }else if (method == 1){
                if (sUser == null){
                    output = new String[]{"Error encountered"};
                    for (int i=0; i<output.length; i++)
                        out.println("Error message: "+output[i]);
                }
                else {
                   String methodName = request.getParameter("methodname");
                    ArrayList<String[]>[] listInputs = sUser.listInputs(methodName);
                    for (int i=0; i<listInputs.length; i++){                        
                        for (int j=0; j<listInputs[i].size(); j++){
                            for (int k=0; k<2; k++){
                                out.print(listInputs[i].get(j)[k]+",");
                            }
                            out.print(";");
                        }
                        out.print("#");
                    }
                }
               // sUser = null;
            }else if( method == 2){
                String[] inputs = request.getParameter("parameters").split("\\:");
                ArrayList<String[]> result = sUser.callMethod(inputs, serviceTime, invoke_counts);
                String message = "";
                for (int i=0; i<result.size(); i++){
                    message += result.get(i)[0]+":"+result.get(i)[1]+";";
                }
                out.print(message+"$#$ Service Time = "+(serviceTime[0]/1000000.0)+" milliSeconds");
            }else if (method == 3){
                int rating = Integer.parseInt(request.getParameter("rating"));                
                sUser.insertRating(s_id, rating);
                out.print("Thank you for rating.");
            }
        } catch (Exception ex){
//            Logger.getLogger(ClientInterface.class.getName()).log(Level.SEVERE, null, ex);
            String error = (ex.getMessage() != null)?ex.getMessage():"Error in reading";
            out.print("Error:"+error);
        } catch (Throwable ex) {
//            Logger.getLogger(ClientInterface.class.getName()).log(Level.SEVERE, null, ex);
            String error = (ex.getMessage() != null)?ex.getMessage():"Error in reading";
            out.print("Error:"+error);
        } finally {
            out.close();
        }
        if (invoke_counts[0] != 0 && s_id != 0){
            try {
                ServiceDaoFactory.create().updateRequestCounts(s_id, invoke_counts, serviceTime[0]);
            } catch (Throwable ex) {
//                Logger.getLogger(ClientInterface.class.getName()).log(Level.SEVERE, null, ex);
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
