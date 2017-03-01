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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import webservicerank.serviceDB.ServiceDbInteracter;
import webservicerank.serviceRanker.ServiceRanker;
/**
 *
 * @author hp
 */
@WebServlet(name="SearchInterface", urlPatterns={"/SearchInterface"})
public class SearchInterface extends HttpServlet {

    ServiceRanker ranker;
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

        ServiceDetail serviceDetail = new ServiceDetail();

        PrintWriter out = response.getWriter();
        serviceDetail.setCategory(request.getParameterValues("category"));
        if("false".equals(request.getParameter("ispropertycategorised"))){
            serviceDetail.categorizeProperties(false);
            serviceDetail.setProperties(request.getParameter("properties").split("\\;"));
        }else{
            serviceDetail.categorizeProperties(true);
            serviceDetail.setInputProperties(request.getParameter("input").split("\\;"));
            serviceDetail.setOutputProperties(request.getParameter("output").split("\\;"));
            serviceDetail.setPreconditions(request.getParameter("precondition").split("\\;"));
            serviceDetail.setEffects(request.getParameter("effect").split("\\;"));
        }
//        serviceDetail.serviceDisplay(out);
        boolean isPriority;
        if ("on".equals(request.getParameter("option"))){
            isPriority = true;
        }else{
            isPriority = false;
        }
        boolean isQosParamsWeighted = false;
        if ("true".equals(request.getParameter("isweightQos"))){
            isQosParamsWeighted = true;
        }
        boolean isAvoidSemanticMatchmake;
        if("on".equals(request.getParameter("semantic"))){
            isAvoidSemanticMatchmake = true;
        }else{
            isAvoidSemanticMatchmake = false;
        }

        //initialize and normalize the weight parameters of QoS parameters
        float[] weight = new float[5];
        if (isQosParamsWeighted){
            weight[0] = Float.valueOf(request.getParameter("reliability"));
            weight[1] = Float.valueOf(request.getParameter("efficiency"));
            weight[2] = Float.valueOf(request.getParameter("availability"));
            weight[3] = Float.valueOf(request.getParameter("cost_effectiveness"));
            weight[4] = Float.valueOf(request.getParameter("reputaion"));
            float sum = 0.0f;
            for (int i=0; i<weight.length; i++){
                sum += weight[i];
            }
            for (int i=0; i<weight.length; i++){
                weight[i] /= sum;
            }
        }else{
            for (int i=0; i<weight.length; i++){
                weight[i] = 1.0f/5.0f;
            }
        }
        String responseText = null;
        try {
            //Initialize and call ranking object
            ranker = new ServiceRanker();
            responseText = ranker.rank(serviceDetail, weight, isPriority, isAvoidSemanticMatchmake);
        } catch (Throwable ex) {
            responseText = "Error:";
            responseText += (ex.getMessage()!=null)?ex.getMessage():"Error encountered";
        }
//        serviceDetail.serviceDisplay(out);
//        out.println("<br/>Ispreference="+isPriority);
//        out.println("<br/>isAvoidSemanticMatchmake="+isAvoidSemanticMatchmake);
//        out.println("<br/>isQosParamsWeighted="+isQosParamsWeighted);
//        for (int i=0; i<weight.length; i++)
//            out.println(weight[i]);
//        out.print(responseText);
        request.getSession().setAttribute("rankedServices", responseText);
        System.out.print(responseText);
        response.sendRedirect(request.getContextPath()+"/search/result.jsp");

  }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String rankBy = request.getParameter("rankBy");
        if (rankBy != null){
            String responseText = "";
            if (ranker != null ){
                try {
                    if ("Availability".equals(rankBy)){
                        responseText = ranker.rankByOptions(1);
                    }else if ("Relaibility".equals(rankBy)){
                        responseText = ranker.rankByOptions(2);
                    }else if ("Efficiency".equals(rankBy)){
                        responseText = ranker.rankByOptions(3);
                    }else if ("Cost-Effectiveness".equals(rankBy)){
                        responseText = ranker.rankByOptions(4);
                    }else if ("Reputation".equals(rankBy)){
                        responseText = ranker.rankByOptions(5);
                    }else if ("QoS-Value".equals(rankBy)){
                        responseText = ranker.rankByOptions(6);
                    }else if ("Degree-of-match".equals(rankBy)){
                        responseText = ranker.rankByOptions(7);
                    }else if ("DOMNQoS".equals(rankBy)){
                        responseText = ranker.rankByOptions(0);
                    }else{
                        responseText = ranker.rankByOptions(1);
                    }
                } catch (Throwable ex) {
//                    Logger.getLogger(SearchInterface.class.getName()).log(Level.SEVERE, null, ex);
                    responseText = "Exception:";
                }
            }else if(ranker !=null){
                try {
                    responseText = ranker.rankByOptions(1);
                } catch (Throwable ex) {
//                    Logger.getLogger(SearchInterface.class.getName()).log(Level.SEVERE, null, ex);
                    responseText = "Exception:";
                }
            }
            response.getWriter().print(responseText);
        }else if (ranker != null) {
            String service_id= request.getParameter("service_id");
            String rank = request.getParameter("rank");
            if (service_id != null && rank != null){
                int service_id_int = Integer.parseInt(service_id);
                int rankNo = Integer.parseInt(rank);
//                response.getWriter().print("rank1="+service_id);
                try {
//                    response.getWriter().print("rank2="+service_id);
                    String[] service = ranker.getService(service_id_int, rankNo);
                    request.getSession().setAttribute("service_id", service_id);
                    request.getSession().setAttribute("webservice", service[0]);
                    request.getSession().setAttribute("wsdl", service[1]);
                    request.getSession().setAttribute("description", service[2]);
                    request.getSession().setAttribute("reputation", service[3]);
                    request.getSession().removeAttribute("isAdmin"); 
//                    response.getWriter().print("</br>Service="+service[0]);
//                    response.getWriter().print("</br>WSDL="+service[1]);
                } catch (Throwable ex) {
//                    Logger.getLogger(SearchInterface.class.getName()).log(Level.SEVERE, null, ex);
                    request.getSession().setAttribute("webservice", "Error encountered! Try again.");
                }
//                response.getWriter().print("rank3="+service_id);
                response.sendRedirect(request.getContextPath()+"/client/index.jsp");
            }
 
                
            
 
        }else{
                String servicename=request.getParameter("servicename");
                if(servicename!=null){
                    boolean isAdmin = ("admin".equals(request.getParameter("source")))?true:false;
                    ServiceDbInteracter sdb= new ServiceDbInteracter();
                    String[] service = sdb.getService(servicename);
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
                    response.sendRedirect(request.getContextPath()+"/search/index.jsp");
                }
        }
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
