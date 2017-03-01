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

/**
 *
 * @author hp
 */
@WebServlet(name="AuthServlet", urlPatterns={"/AuthServlet"})
public class AuthServlet extends HttpServlet {
   
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
            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AuthServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AuthServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
            */
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            ServiceDbInteracter sdb = new ServiceDbInteracter();

            String type = null;
            int[] userId = new int[1];
            type = sdb.doesExist(userId,username, password);
            if(type != null){
                request.getSession().setAttribute("logged_type", type);
                request.getSession().setAttribute("logged_user", username);
                request.getSession().setAttribute("message", null);
                request.getSession().setAttribute("user_id", userId[0]); 
            }else{
                request.getSession().setAttribute("message", "Login failed!");
            }
            response.sendRedirect(request.getContextPath()+"");
            
        } finally { 
            out.close();
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
