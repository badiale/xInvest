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
import java.io.*;
import java.text.SimpleDateFormat;

// Graficos
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.xy.*;
import org.jfree.chart.servlet.ServletUtilities;

/**
 * Servlet to handle Quote operations.
 * @author Gabriel Gimenes
 */
public class QuoteServlet extends HttpServlet {
    private final int GRAPH = 0;
    private final int QUOTEINFO = 1;


    public void doGet (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		
	
	//	HttpSession session = request.getSession();

		String targetUrl = null;
		String html = null;
	  Locale currentLocale = request.getLocale();
	  ResourceBundle msg = ResourceBundle.getBundle("org.xinvest.bundles.message", currentLocale);

		int operation = -1;
		try {
	
			 operation = Integer.parseInt(request.getParameter("op"));
		} catch (Exception e) {
			targetUrl = "/xInvest/message.jsp?msg=100";
			response.sendRedirect(targetUrl);
		}
		
		

		switch (operation) {
			case GRAPH:
				try {
					Session session = DBManager.getSession();
					session.beginTransaction();
					Quote q = Quote.find(request.getParameter("quote"));
					
					// conjunto de dados que vamos plotar
					XYSeriesCollection dataset = new XYSeriesCollection();

					// dados de uma "curva"
					XYSeries curva1 = new XYSeries("Ticks");
					
					long valor = 0;
					long offset = 0;
					Iterator it = q.getTicks().iterator();
					// HARDCODED POR CAUSA DA DATA INICIAL DO ROBOT
					offset = (long ) (new Date(2010,6,1).getTime() / (86400 * 1000));
					while (it.hasNext()) {
						Tick t = (Tick) it.next();
						//FIXED BY VALUE 694000
						valor = ((long ) ((t.getTimestamp().getTime() / (86400 * 1000)) - offset)) + 694000;
						curva1.add(valor,t.getTick()); // (x, y)
					}
				
					// coloca a curva no conjunto de dados
					dataset.addSeries(curva1);

					// retorna uma abstracao do grafo
					JFreeChart chart = ChartFactory.createXYLineChart( 
							msg.getString("NOME_GRAFICO"),      // titulo do grafico
							msg.getString("X_GRAFICO"),                        // descricao do eixo X
							msg.getString("Y_GRAFICO"),                        // descricao do eixo Y
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
					session.getTransaction().commit();
				} catch (Exception e) {e.printStackTrace(); 
														}
			break;
			
			case QUOTEINFO:
				try {
					PrintWriter out = response.getWriter();

					response.setContentType("text/html");
					Session session = DBManager.getSession();
					session.beginTransaction();
					Quote q = Quote.find(request.getParameter("quote"));
						
					out.println("<h1>"+q.getQuote()+" - "+q.getName()+"</h1>");
					
					out.println("<br/>");
					out.println("<br/>");
					out.println("<br/>");
					out.println("<table>");
					out.println("<tr class=\"labelRow\"><th>TICK</th><th>"+msg.getString("50_AVG")+"</th><th>"+msg.getString("DAYS_LOW")+"</th><th>"+msg.getString("DAYS_HIGH")+"</th><th>"+msg.getString("YEAR_LOW")+"</th><th>"+msg.getString("YEAR_HIGH")+"</th><th>Volume</th><th>"+msg.getString("STOCK_EXCHANGE")+"</th></tr>");
					out.println("<tr><td>"+q.getLastestTick()+"</td>");
					out.println("<td>"+q.getFiftydayMovingAverage()+"</td>");
					out.println("<td>"+q.getDaysLow()+"</td>");					
					out.println("<td>"+q.getDaysHigh()+"</td>");		
					out.println("<td>"+q.getYearLow()+"</td>");		
					out.println("<td>"+q.getYearHigh()+"</td>");		
					out.println("<td>"+q.getVolume()+"</td>");		
					out.println("<td>"+q.getStockExchange()+"</td></tr>");		
					out.println("</table>");
					out.println("<br/>");
					out.println("<br/>");
					out.println("<br/>");
					
						
					session.getTransaction().commit();
				} catch (Exception e) {e.printStackTrace(); 
					}
			break;
			}

		}
		
    public void doPost (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
				doGet(request,response);
    }

}
