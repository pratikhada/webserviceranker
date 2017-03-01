/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.uiAppInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import webservicerank.ontologyDB.OntologyManager;
import webservicerank.servicePublish.ServicePublish;

/**
 *
 * @author vhishma
 */
public class PublisherInterface extends HttpServlet {
//    public String message;


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

        //set the MIME type of the response, "text/html"
        response.setContentType("text/html");

        //use a PrintWriter to send text data to the client
        PrintWriter out = response.getWriter( );

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet UIAppInterface</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet UIAppInterface at " + request.getContextPath () + "</h1>");
        out.println("This servlet is for form processsing (<a href=\"publish/index.jsp\" >WSR/publish/publish.jsp</a>), "+
                    "direct access of this page may not be useful.<br/>"
                    );
        out.println("</body>");
        out.println("</html>");

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

        String message[] = new String[2];
        message[0] = "";
        message[1] = "";
        response.setContentType("text/html");
        PrintWriter out = response.getWriter( );

        ServiceDetail serviceDetail = null;



        //Check if the publishing is only ontology
        if ("PUBLISH_ONTOLOGY_ONLY".equals(request.getParameter("option_to_publish"))){
            String owl = request.getParameter("owlURL");
            OntologyManager om = null;
            message[0] = "Something went wrong!";
            try {
                om = new OntologyManager();
            } catch (Throwable ex) {
                Logger.getLogger(PublisherInterface.class.getName()).log(Level.SEVERE, null, ex);
                String error =ex.getMessage();
                error = (error != null)?"Error :"+error:"";
                message[1] = "Error occured while publishing ontology! "+error;
            }
            try {
                message[1] = om.publish(owl);
                if ("The ontology URL was Empty".equals(message[1])){
                    message[0] = "Try entering correct URL of the ontology.";
                }else{
                    message[0] = "";
                }
            } catch (Throwable ex) {
                Logger.getLogger(PublisherInterface.class.getName()).log(Level.SEVERE, null, ex);
                String error =ex.getMessage();
                error = (error != null)?"Error :"+error:"";
                message[1] = "Error occured while publishing ontology! "+error;
            }
        }


        //If the publishing is all the service details
        else {

            serviceDetail = new ServiceDetail();
            serviceDetail.setCategory(request.getParameterValues("category"));
            serviceDetail.setName(request.getParameter("servicename"));
            serviceDetail.setWsdlURL(request.getParameter("wsdlURL"));

            //Check whether the properties are categorized or not
            if(!"true".equals(request.getParameter("categorised"))){
                serviceDetail.categorizeProperties(false);
                serviceDetail.setProperties(request.getParameter("properties").split("\\;"));
            }else{
                serviceDetail.categorizeProperties(true);
                serviceDetail.setInputProperties(request.getParameter("input").split("\\;"));
                serviceDetail.setOutputProperties(request.getParameter("output").split("\\;"));
                serviceDetail.setPreconditions(request.getParameter("precondition").split("\\;"));
                serviceDetail.setEffects(request.getParameter("effect").split("\\;"));
            }

            serviceDetail.setHomeURL(request.getParameter("homeURL"));
            serviceDetail.setOwlURL(request.getParameter("owlURL"));
            serviceDetail.setCost(Float.valueOf(request.getParameter("cost")) ); //cost attribute is not available at ServiceDetail.
            serviceDetail.setDescription(request.getParameter("description"));

    //        serviceDetail.serviceDisplay(out); //displays service parameter and values.


            long serviceDuration[] = new long[1];
            int[] invoke_counts = new int[]{0,0,0};
            boolean validate[] = new boolean[2];
            int publisherId = (Integer)request.getSession().getAttribute("user_id");
            validate[0] = false;
            validate[1] = false;
            boolean isValidated = false;
            ServicePublish servicePublish = null;

            if (publisherId <= 0){
                message[0] = "The user ID seems to be invalid!";
                message[1] = "Either login is required, or the system has problems. Please, report about this.";
            }else{
                //Perform the service validation
                try {
                    servicePublish = new ServicePublish();
                    validate = servicePublish.validate(serviceDetail.getWsdlURL(), serviceDetail.getName(), serviceDuration, invoke_counts);
                    isValidated = true;
                } catch (Throwable ex) {
                    Logger.getLogger(PublisherInterface.class.getName()).log(Level.SEVERE, null, ex);
                    message[0] = "The system encountered an error! ";
                    String error =ex.getMessage();
                    message[1] = (error != null)?error:"";
                }
            }

            //If validation is successful
            if (isValidated){

                //If the service is valid and isn't already published
                if (validate[0] && validate[1]){
                    /*request.getSession().setAttribute("sd0", serviceDetail.getName());
                    request.getSession().setAttribute("PUBLISHED", "true");
                    request.getSession().setAttribute("sd1", serviceDetail.getCategory());
                    request.getSession().setAttribute("sd2", serviceDetail.getName());
                    request.getSession().setAttribute("sd3", serviceDetail.getWsdlURL());
                    request.getSession().setAttribute("sd4", serviceDetail.getProperties());
                    request.getSession().setAttribute("sd5", serviceDetail.getHomeURL());
                    request.getSession().setAttribute("sd6", serviceDetail.getOwlUrl());
                    request.getSession().setAttribute("sd7", serviceDetail.getDescription());
                    */
                    serviceDetail.setServiceDuration((int)serviceDuration[0]);
                    try {
                        //Publish the service details (including properties and OWL)
                        message = servicePublish.publish(serviceDetail, publisherId);
                    } catch (Throwable ex) {
                        message[0] = "Sorry! Error encountered while publishing";
                        Logger.getLogger(PublisherInterface.class.getName()).log(Level.SEVERE, null, ex);
                        String error =ex.getMessage();
                        error = (error != null)?"Error :"+error:"";
                        message[1] = error;
                    }
                }

                //If the service was already published
                else if (!validate[0]){
                    message[0] += "The service was already published.";
                    message[1] += "Thank you for trying";
                }

                //If the service is invalid or validation encountered problems such as network problem
                else{
                    message[0] += "Error in service validation! Either the service doesn't exist, or";
                    message[1] += "the entered service name doesn't match with service name specified in WSDL ";
                }
            }
        }
        
        //Set the messages in session object and redirect to confirmation page
        request.getSession().setAttribute("message[0]", message[0]); //succedss publishing
        request.getSession().setAttribute("message[1]", message[1]); //success validating only
        response.sendRedirect(request.getContextPath()+"/publish/confirmation.jsp");

        
  }// doPost

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
