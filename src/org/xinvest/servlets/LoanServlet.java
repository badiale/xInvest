package org.xinvest.servlets;

// Servlet Imports
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
// xCommerce Imports
import org.xinvest.beans.Cliente;
import org.xinvest.db.DBManager;
// Hibernate Imports
import org.hibernate.Session;
// Other Imports
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Servlet to handle loan operations.
 * @author Rodrigo Leonavas
 */
public class ClienteServlet extends HttpServlet {
    private final int USERLOAN 		= 0;
    private final int BANKLOAN	    = 1;
    private final int USERPAY       = 2; 
    private final int BANKPAY       = 3;

    public void doGet (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
	
		HttpSession session = request.getSession();

		String targetUrl = null;
		String html = null;

		int operation = -1;
		try {
			 operation = Integer.parseInt(request.getParameter("op"));
		} catch (Exception e) {
			targetUrl = "/xInvest/message.jsp?msg=100";
			response.sendRedirect(targetUrl);
		}

		Loan l = null;
		User passive = null;
		User active = null;
		Bank b = null;		

		switch (operation) {
			case USERLOAN:
			try {
				active = (User) session.getAttribute("user");
				
				if ((passive != null) && (active != null)) {
					l = new	Loan();		

					passive = new User();
					passive.find(request.getParameter("passive"));

					l.setValue(Float.parseFloat(request.getParameter("value")));
					l.setPassive(passive);
					l.setActive(user);
					l.setInterest(Float.parseFloat(request.getParameter("interest")))

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

			case BANKLOAN:
				try {
					float interest;
					float totalLend;
					
					active = (User) session.getAttribute("user");
				
					if ((passive != null) && (active != null)) {
						l = new	Loan();		

						l.setValue(Float.parseFloat(request.getParameter("value")));
						l.setActive(active);
						//l.setInterest(Float.parseFloat(request.getParameter("interest")))

						b = new Bank();
						b.find("bank@bank.com");
						l.setPassive(b);

						Session dbSession = DBManager.getSession();
						dbSession.beginTransaction();					

						Iterator it = b.getTransactionPassives().iterator();
						while (it.hasNext()) {
							Transaction t = (Transaction) it.next();
							totalLend += t.getValue();
						}
						
						//TODO calcular o interest de forma mais coerente
						interest = (totalLend / 100) * 0.1;
						l.setInterest(new Float(interest));
						l.insert();				

		   				session.getTransaction().commit();
						
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
		}

	
    }
    public void doPost (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doGet(request,response);
    }

}
