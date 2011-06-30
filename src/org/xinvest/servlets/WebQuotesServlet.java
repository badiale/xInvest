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
 * Servlet to handle WebQuotes operations.
 * @author Gabriel Gimenes
 */
public class WebQuotesServlet extends HttpServlet {
    private final int LISTQUOTES = 0;


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
			case LISTQUOTES:
				try {
						Session session = DBManager.getSession();
						session.beginTransaction();
	
								WebQuotes w = WebQuotes.find(new Integer(1));
								out.println("<table>");
								out.println("<tr class=\"labelRow\"><th>Quote</th><th>Nome</th><th>Tick</th><th>50day Avg</th></tr>");
								Iterator it = w.getQuotes().iterator();
								while (it.hasNext()) {
										Quote q = (Quote) it.next();
										out.println("<tr><td>"+q.getQuote()+"</td>");
										out.println("<td>"+q.getName()+"</td>");
										out.println("<td>"+ ( (Tick) q.getTicks().iterator().next() ).getTick()+"</td>");
										out.println("<td>"+q.getFiftydayMovingAverage()+"</td>");
										out.println("<td><a href=\"/xInvest/quote/index.jsp?quote="+q.getQuote()+"\">Ver</a></td></tr>");
										
								}
								out.println("</table>");
				
						session.getTransaction().commit();
						System.out.println("Quotes Listadas.");
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