/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.uiAppInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import webservicerank.serviceDB.ServiceDbInteracter;

/**
 *
 * @author hp
 */
@WebServlet(name="QuerySuggestions", urlPatterns={"/QuerySuggestions"})
public class QuerySuggestions extends HttpServlet {
   
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
        try {
            //TODO output your page here
            ArrayList suggestions = new ArrayList();
            String str = new String();
            str = request.getParameter("suggest");
            if( "".equals(str) || str == null){
                out.println("No Suggestions...");
            }else{
                try {
                    suggestions = (new ServiceDbInteracter()).getSuggestions(str);
                } catch (Exception ex) {
                }catch (Throwable ex){}

                for(int i=0;i<suggestions.size();i++){
                    out.print(suggestions.get(i));
                    out.print("<br/>");
                }

                if(suggestions.isEmpty()){
                    out.print("No Suggestions Found...");
                }
            }
            

        } finally { 
            out.close();
        }
    }

    
    public static void main(String[] args) throws ClassNotFoundException {
        QuerySuggestions qs = new QuerySuggestions();
        //qs.getSuggestions("met");
//        ArrayList suggestions = qs.getSuggestions("m");
//        for(int i=0;i<suggestions.size();i++){
//            System.out.println(suggestions.get(i));
//        }
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
