package org.xinvest.servlets;

// Servlet Imports
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
// xCommerce Imports
import org.xinvest.beans.*;
import org.xinvest.db.DBManager;
// Hibernate Imports
import org.hibernate.Session;
// Other Imports
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Servlet to handle Quote operations.
 * @author Gabriel Gimenes
 */
public class QuoteServlet extends HttpServlet {
    private final int QUOTEINFO = 0;


    public void doGet (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
	
	//	HttpSession session = request.getSession();

		String targetUrl = null;
		String html = null;

		int operation = -1;
		try {
			 operation = Integer.parseInt(request.getParameter("op"));
		} catch (Exception e) {
			targetUrl = "/xInvest/message.jsp?msg=100";
			response.sendRedirect(targetUrl);
		}

		switch (operation) {
			case QUOTEINFO:
				try {
					Session session = DBManager.getSession();
					session.beginTransaction();
					Quote q = Quote.find(request.getParameter("quote"));
					
					out.println("<h1>"+q.getQuote()+" - "+q.getName()+"</h1>");
					
					
					session.getTransaction().commit();
				} catch (Exception e) {//redirect 
														}
			break;
			}
		}
		
    public void doPost (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
				doGet(request,response);
    }

}
