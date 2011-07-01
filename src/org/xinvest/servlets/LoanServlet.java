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

// Graficos
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.xy.*;
import org.jfree.chart.servlet.ServletUtilities;

/**
 * Servlet to handle loan operations.
 * @author Rodrigo Leonavas
 */
public class LoanServlet extends HttpServlet {
	private ResourceBundle msg;


	private final int GETINTEREST   = 0;
    private final int USERLOAN 		= 1;
    private final int BANKLOAN	    = 2;
    private final int PAY           = 3; 
    private final int CREATELOAN    = 5;
    private final int LISTACTIVE    = 6;
    private final int LISTPASSIVE   = 7;
    private final int GRAPH   = 4;

	// must be inside a transaction!!!
	private void refreshInterest() {		

		float totalLend = new Float(0.0);
		float interest = new Float(0.0);

		Bank b = Bank.find("bank@bank.com");

		TickJuros tj = new TickJuros();

		tj.setBank(b);

		Iterator it = b.getTransactionPassives().iterator();
		while (it.hasNext()) {
			Transaction t = (Transaction) it.next();
			totalLend = totalLend + t.getValue();
		}
	
		//TODO calcular o interest de forma mais coerente
		interest = ((totalLend / 1000) * new Float(0.01)) + new Float(0.01);

		tj.setTickJuros(interest);
		tj.insert();
	
		b.getTicks().add(tj);

		b.setInterest(interest);
		b.update();		   
	}

	private void printPage(PrintWriter out) {
		float interest = new Float(0.0);		
		
		String html = "";		

		Session dbSession = DBManager.getSession();
		dbSession.beginTransaction();	

		Bank b = new Bank();
		b = Bank.find("bank@bank.com");


		html += "<h1>"+msg.getString("LOAN_TITLE")+"</h1><br><br>"+
			"<center><table cellspacing=\"50\"><tr><td>"+
				"<form action=\"loanservlet\" method=\"get\" enctype=\"multipart/form-data\">"+
				"<input type=\"hidden\" value=\"2\" name=\"op\">"+
				"<p><b>"+msg.getString("LOAN_BANK_TITLE")+"</b></p><br><br>"+
					"<table>"+
					"<tr><td>"+msg.getString("LOAN_ATT1")+":</td><td><input type=\"text\" name=\"value\"></td></tr>"+
					"<tr><td>"+msg.getString("LOAN_ATT2")+":</td><td><label for=\"juros\">"+b.getInterest()+"</label></td></tr>"+
					"</table>"+
					"<input type=\"submit\" value=\"Submit\">"+
				"</form></td>"+
			
			"<td><form action=\"loanservlet\" method=\"get\" enctype=\"multipart/form-data\">"+
				"<input type=\"hidden\" value=\"5\" name=\"op\">"+
				"<p><b>"+msg.getString("LOAN_CREATE")+"</b></p><br><br>"+
				"<table>"+
				"<tr><td>"+msg.getString("LOAN_ATT1")+":</td><td><input type=\"text\" name=\"value\"></td></tr>"+
				"<tr><td>"+msg.getString("LOAN_ATT2")+":</td><td><input type=\"text\" name=\"interest\"></td></tr>"+
				"</table>"+
				"<input type=\"submit\" value=\"Submit\">"+
			"</form></tr></td>"+
			"</tr></td></table></center><br><br>";
		

		html += "<h1>"+msg.getString("LOAN_MORE_TITLE")+"</h1><br>";
		
		html += "<table><tr class=\"labelRow\"><th>Email</th> <th>"+msg.getString("LOAN_VALOR")+"</th><th>"+msg.getString("LOAN_JUROS")+"</th></tr>"+
		"<tr><td>";

		List l = Loan.findAll();

		Iterator it = l.iterator();
		Loan lo = null;

		while (it.hasNext()) {
			lo = (Loan) it.next();
			html += "<tr><td>"+lo.getPassive().getEmail()+"</td>";
			html += "<td>"+lo.getValue()+"</td>";
			html += "<td>"+lo.getInterest()+"</td>";
			html += "<td>&nbsp&nbsp<a href=/xInvest/loan/loanservlet?op=1&passive="+lo.getPassive().getEmail()+"&id="+lo.getId()+">"+msg.getString("LOAN_LEND")+"</a></td>";
		}
		html += "</table><br><br>";

		dbSession.getTransaction().commit();

		out.println(html);
	}

    public void doGet (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		
		String targetUrl = null;
		Locale currentLocale = request.getLocale();
		this.msg = ResourceBundle.getBundle("org.xinvest.bundles.message", currentLocale);

		
	
		HttpSession session = request.getSession();

		Loan l = null;
		User passive = null;
		User active = null;
		Bank b = null;	
		String html = null;

		active = (User) session.getAttribute("user");
		int operation = -1;
		
		if (active == null) {
			targetUrl = "index.jsp";
			operation = -2; //deve ser algum valor invalido mas para testar assumimos 0 pois n tem session ainda
		}
		
		try {
			 operation = Integer.parseInt(request.getParameter("op"));
		} catch (Exception e) {
			targetUrl = "/xInvest/message.jsp?msg=100";
		}

		switch (operation) {
			case GETINTEREST:
				PrintWriter out = response.getWriter();
				printPage(out);
				break;

			case USERLOAN:
			try {				
				passive = new User();					

				Session dbSession = DBManager.getSession();
				dbSession.beginTransaction();				

				passive = User.find(request.getParameter("passive"));
				//System.out.println(passive.getName());
		
				User active2 = User.find(active.getEmail());
				if ((passive != null) && (active2 != null)) {
					l = Loan.find(Integer.parseInt(request.getParameter("id")));

					l.setActive(active2);
					active2.setMoney(active2.getMoney() + l.getValue());
					active2.getTransactionActives().add(l);
					active2.update();
					passive.getTransactionPassives().add(l);

					l.update();
					session.setAttribute("user", active2);

					targetUrl = "/xInvest/message.jsp?msg=106";

				}
				else {
					targetUrl = "/xInvest/message.jsp?msg=102";
				}
				
				
				dbSession.getTransaction().commit();				
				//out.println(html);

			} catch (Exception e) {
				targetUrl = "/xInvest/message.jsp?msg=101";
			}
			response.sendRedirect(targetUrl);
			break;

			case BANKLOAN:
				try {
					Session dbSession = DBManager.getSession();
					dbSession.beginTransaction();										

					User active2 = new User(); 
					active2 = User.find(active.getEmail());
				
					if (active2 != null) {
						l = new	Loan();		
						l.setValue(Float.parseFloat(request.getParameter("value")));
						l.setActive(active2);
						//l.setInterest(Float.parseFloat(request.getParameter("interest")));

						b = new Bank();
						b = Bank.find("bank@bank.com");
						l.setPassive(b);											

						
						l.setInterest(new Float(b.getInterest()));
						l.insert();		

						b.getTransactionPassives().add(l);
						active2.getTransactionActives().add(l);
						
						active2.setMoney(active2.getMoney()+l.getValue());
						active2.update();
	
						refreshInterest();	
						
						session.setAttribute("user", active2);

						targetUrl = "/xInvest/message.jsp?msg=105";
					}
					else {
						targetUrl = "/xInvest/message.jsp?msg=101";
					}
					session.setAttribute("user", active2);
					dbSession.getTransaction().commit();

				} catch (Exception e) {
					e.printStackTrace();
					targetUrl = "/xInvest/message.jsp?msg=101";
				}
				response.sendRedirect(targetUrl);
			break;

			case CREATELOAN:
				try {
				
					Session dbSession = DBManager.getSession();
					dbSession.beginTransaction();	

					User active2 = User.find(active.getEmail());
					
					l = new Loan();
					l.setValue(Float.parseFloat(request.getParameter("value")));
					l.setInterest(Float.parseFloat(request.getParameter("interest")));
					l.setPassive(active2);
					l.setActive(null);
					
					if (active2.getMoney() >= l.getValue()) {

						l.insert();

						active2.getTransactionPassives().add(l);
						active2.setMoney(active2.getMoney() - l.getValue());
						active2.update();			
	
						session.setAttribute("user", active2);
						targetUrl = "/xInvest/message.jsp?msg=106";

					}
					else {
						targetUrl = "/xInvest/message.jsp?msg=102";
					}
					
					dbSession.getTransaction().commit();

				} catch (Exception e) {
					e.printStackTrace();
					targetUrl = "/xInvest/message.jsp?msg=102";
				}
				response.sendRedirect(targetUrl);
				break;

			case PAY:
				try {
					Session dbSession = DBManager.getSession();
					dbSession.beginTransaction();										

					l = Loan.find(Integer.parseInt(request.getParameter("id")));

					User active2 = User.find(active.getEmail());

					passive = l.getPassive();

					if (active2.getMoney() >= l.getValue()) {
						active2.setMoney(active2.getMoney() - l.getValue());
						passive.setMoney(passive.getMoney() + l.getValue());

						l.remove();

						if (l.getPassive().getEmail().equals("bank@bank.com")) {
							refreshInterest();
						}
						//added
						active2.update();	
						
						session.setAttribute("user", active2);
						//fim added
						targetUrl = "/xInvest/message.jsp?msg=107";
					}					
					else {
						targetUrl = "/xInvest/message.jsp?msg=103";
					}
					session.setAttribute("user", active2);
					dbSession.getTransaction().commit();

				} catch (Exception e) {
					e.printStackTrace();
					targetUrl = "/xInvest/message.jsp?msg=103";
				}
				response.sendRedirect(targetUrl);
			break;

			case LISTACTIVE:
			  out = response.getWriter();
				String html2 = "";	
				try {
					Session dbSession = DBManager.getSession();
					dbSession.beginTransaction();													

					User active2 = User.find(active.getEmail());

					html2 += "<b><h2>"+msg.getString("LOAN_LIST_TITLE1")+"</b></h2><br>";
					html2 += "<table><tr class=\"labelRow\"><th>"+msg.getString("LOAN_SOURCE")+"</th><th>"+msg.getString("LOAN_QUANTITY")+"</th> <th>"+msg.getString("LOAN_JUROS")+"</th><th>"+msg.getString("LOAN_DATE")+"</th><th>"+msg.getString("LOAN_VALUE")+"</th></tr>"+
					"<tr><td>";

					List li = Loan.findByActive(active2);

					Iterator it = li.iterator();
					Loan lo = null;

					while (it.hasNext()) {
						lo = (Loan) it.next();
						html2 += "<tr><td>"+lo.getPassive().getEmail()+"</td>";
						html2 += "<td>"+lo.getValue()+"</td>";
						html2 += "<td>"+lo.getInterest()+"</td>";
						html2 += "<td>"+lo.getTimestamp()+"</td>";
						html2 += "<td>"+"TODO"+"</td>";
						html2 += "<td>&nbsp&nbsp<a href=/xInvest/loan/graph.jsp?op=4&id="+lo.getId()+">"+msg.getString("LOAN_GRAPH")+"</a></td>";
						html2 += "<td>&nbsp&nbsp<a href=/xInvest/loan/loanservlet?op=3&id="+lo.getId()+">"+msg.getString("LOAN_PAY")+"</a></td></tr>";
					}
					html2 += "</table><br><br>";

					dbSession.getTransaction().commit();

				} catch (Exception e) {
					e.printStackTrace();
					targetUrl = "/xInvest/message.jsp?msg=103";
				}
				out.println(html2);
				//response.sendRedirect(targetUrl);
			break;

			case LISTPASSIVE:
				out = response.getWriter();
				String html3 = "";
				try {
					Session dbSession = DBManager.getSession();
					dbSession.beginTransaction();														

					User active2 = User.find(active.getEmail());

					html3 += "<b><h2>"+msg.getString("LOAN_LIST_TITLE2")+"</b></h2><br>";
					html3 += "<table><tr class=\"labelRow\"><th>"+msg.getString("LOAN_SOURCE")+"</th><th>"+msg.getString("LOAN_QUANTITY")+"</th> <th>"+msg.getString("LOAN_JUROS")+"</th><th>"+msg.getString("LOAN_DATE")+"</tr>"+
					"<tr><td>";

					List li = Loan.findByPassive(active2);

					Iterator it = li.iterator();
					Loan lo = null;

					while (it.hasNext()) {
						lo = (Loan) it.next();
						html3 += "<tr><td>"+lo.getPassive().getEmail()+"</td>";
						html3 += "<td>"+lo.getValue()+"</td>";
						html3 += "<td>"+lo.getInterest()+"</td>";
						html3 += "<td>"+lo.getTimestamp()+"</td>";
					}
					html3 += "</table><br><br>";

					dbSession.getTransaction().commit();

				} catch (Exception e) {
					e.printStackTrace();
					targetUrl = "/xInvest/message.jsp?msg=103";
				}
				out.println(html3);
				//response.sendRedirect(targetUrl);
			break;
			
		case GRAPH:
				try {
					Session dbSession = DBManager.getSession();
					dbSession.beginTransaction();
					l = Loan.find(Integer.parseInt(request.getParameter("id")));
					
					// conjunto de dados que vamos plotar
					XYSeriesCollection dataset = new XYSeriesCollection();

					// dados de uma "curva"
					XYSeries curva1 = new XYSeries("Ticks");
					
					int offset = (int ) (l.getTimestamp().getTime() / (86400 * 1000));
					
					int start = (int ) ((l.getTimestamp().getTime() / (86400 * 1000)) - offset)  ;
					int end = (int ) ((new Date().getTime() / (86400 * 1000)) - offset);
					float valor = l.getValue();
					float interest = l.getInterest();
					
					for(int i=start;i<end;i++) {
						curva1.add(i,valor); // (x, y)
						valor = ((valor*interest)+valor);
					}
				
					// coloca a curva no conjunto de dados
					dataset.addSeries(curva1);

					// retorna uma abstracao do grafo
					JFreeChart chart = ChartFactory.createXYLineChart( 
							msg.getString("NOME_GRAFICO_LOAN"),      // titulo do grafico
							msg.getString("X_GRAFICO_LOAN"),                        // descricao do eixo X
							msg.getString("Y_GRAFICO_LOAN"),                        // descricao do eixo Y
							dataset,                    // dados
							PlotOrientation.VERTICAL,   // orientacao do grafico
							false,                       // mostrar legendas
							false,                       // mostrar tooltips
							false);                     // mostrar urls (nao sei o q eh isso)

					OutputStream outS = response.getOutputStream();
					response.setContentType("image/png");

					// metodo que salva o grafo em um arquivo temporario
					ChartUtilities.writeChartAsPNG(
							outS,   // output stream
							chart, // grafico
							500,   // largura
							300);  // altura
	
				
					
					outS.close();
					dbSession.getTransaction().commit();
				} catch (Exception e) {
					e.printStackTrace();
				}
			break;
		}
    }
    public void doPost (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		doGet(request, response);
    }
}
