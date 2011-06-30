package org.xinvest.servlets;

// Servlet Imports
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
// xCommerce Imports
import org.xinvest.beans.*;
import org.xinvest.config.*;
import org.xinvest.db.DBManager;
// Hibernate Imports
import org.hibernate.Session;
// Other Imports
import java.util.*;

/**
 * Servlet to handle loan operations.
 * @author Rodrigo Leonavas
 */
public class LoanServlet extends HttpServlet {
	private ResourceBundle msg;
	private PrintWriter out;

	private final int GETINTEREST   = 0;
    private final int USERLOAN 		= 1;
    private final int BANKLOAN	    = 2;
    private final int USERPAY       = 3; 
    private final int BANKPAY       = 4;

	private void printPage() {
		float interest = new Float(0.0);		
		String html = "";

		Session dbSession = DBManager.getSession();
		dbSession.beginTransaction();	

		Bank b = new Bank();
		b = Bank.find("bank@bank.com");


		html += "<h1>"+msg.getString("LOAN_TITLE")+"</h1><br><br>"+
			"<center><table cellspacing=\"50\"><tr><td>"+
				"<form action=\"loanservlet\" method=\"post\" enctype=\"multipart/form-data\">"+
				"<p><b>"+msg.getString("LOAN_BANK_TITLE")+"</b></p><br><br>"+
					"<table>"+
					"<tr><td>"+msg.getString("LOAN_ATT1")+":</td><td><input type=\"text\" name=\"valor\"></td></tr>"+
					"<tr><td>"+msg.getString("LOAN_ATT2")+":</td><td><label for=\"juros\">"+b.getInterest()+"</label></td></tr>"+
					"</table>"+
					"<input type=\"submit\" value=\"Submit\">"+
				"</form></td>"+
			
			"<td><form action=\"loanservlet\" method=\"post\" enctype=\"multipart/form-data\">"+
				"<p><b>"+msg.getString("LOAN_CREATE")+"</b></p><br><br>"+
				"<table>"+
				"<tr><td>"+msg.getString("LOAN_ATT1")+":</td><td><input type=\"text\" name=\"valor\"></td></tr>"+
				"<tr><td>"+msg.getString("LOAN_ATT2")+":</td><td><input type=\"text\" name=\"juros\"></td></tr>"+
				"</table>"+
				"<input type=\"submit\" value=\"Submit\">"+
			"</form></tr></td>"+
			"</tr></td></table></center>";

		dbSession.getTransaction().commit();
		out.println(html);
	}

    public void doGet (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		
		Locale currentLocale = request.getLocale();
		this.msg = ResourceBundle.getBundle("org.xinvest.bundles.message", currentLocale);
		this.out = response.getWriter();

		//PrintWriter out = response.getWriter();
	
		HttpSession session = request.getSession();

		Loan l = null;
		User passive = null;
		User active = null;
		Bank b = null;	
		String targetUrl = null;
		String html = null;

		active = (User) session.getAttribute("user");
		int operation = -1;
		
		if (active == null) {
			targetUrl = "index.jsp";
			response.sendRedirect(targetUrl);
			operation = 0;
		}
		
		try {
			 operation = Integer.parseInt(request.getParameter("op"));
		} catch (Exception e) {
			targetUrl = "/xInvest/message.jsp?msg=100";
			response.sendRedirect(targetUrl);
		}

		switch (operation) {
			case GETINTEREST:
				printPage();
				break;

			case USERLOAN:
			try {				
				if ((passive != null) && (active != null)) {
					l = new	Loan();		

					passive = new User();
					passive.find(request.getParameter("passive"));

					l.setValue(Float.parseFloat(request.getParameter("value")));
					l.setPassive(passive);

					l.setActive(active);
					l.setInterest(Float.parseFloat(request.getParameter("interest")));

					l.insert();

					targetUrl = "/xInvest/message.jsp?msg=105";
				}
				else {
					targetUrl = "/xInvest/message.jsp?msg=101";
				}
				response.sendRedirect(targetUrl);
				

			} catch (Exception e) {
				targetUrl = "/xInvest/message.jsp?msg=101";
				response.sendRedirect(targetUrl);
			}
			break;

			case BANKLOAN:
				try {
					float interest = 0;
					float totalLend = 0 ;
					
					active = (User) session.getAttribute("user");
				
					if ((passive != null) && (active != null)) {
						l = new	Loan();		

						l.setValue(Float.parseFloat(request.getParameter("value")));
						l.setActive(active);
						//l.setInterest(Float.parseFloat(request.getParameter("interest")))

						b = new Bank();
						b = Bank.find("bank@bank.com");
						l.setPassive(b);

						Session dbSession = DBManager.getSession();
						dbSession.beginTransaction();					

						Iterator it = b.getTransactionPassives().iterator();
						while (it.hasNext()) {
							Transaction t = (Transaction) it.next();
							totalLend = totalLend + t.getValue();
						}
						dbSession.getTransaction().commit();

						//TODO calcular o interest de forma mais coerente
						interest = (totalLend / 100) * (float) 0.1;
						l.setInterest(new Float(interest));
						l.insert();				
						b.setInterest(interest);
		   				
						
						targetUrl = "/xInvest/message.jsp?msg=105";
					}
					else {
						targetUrl = "/xInvest/message.jsp?msg=101";
					}
					response.sendRedirect(targetUrl);
									


				} catch (Exception e) {
					targetUrl = "/xInvest/message.jsp?msg=101";
					response.sendRedirect(targetUrl);
				}
				break;
		}

	
    }
    public void doPost (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doGet(request,response);
    }

}
