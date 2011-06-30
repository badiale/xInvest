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
public class TickJurosServlet extends HttpServlet {
    private final int GRAPH = 0;


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
					//Bank q = Bank.find(request.getParameter("bank"));
					Bank b = Bank.find("bank@bank.com");
					
					// conjunto de dados que vamos plotar
					XYSeriesCollection dataset = new XYSeriesCollection();

					// dados de uma "curva"
					XYSeries curva1 = new XYSeries("Ticks");
					
			
					
					Iterator it = b.getTicks().iterator();
					while (it.hasNext()) {
						TickJuros tj = (TickJuros) it.next();
						curva1.add(tj.getTimestamp().getTime(),tj.getTickJuros()); // (x, y)
					}
				
					// coloca a curva no conjunto de dados
					dataset.addSeries(curva1);

					// retorna uma abstracao do grafo
					JFreeChart chart = ChartFactory.createXYLineChart( 
							msg.getString("NOME_GRAFICO_TICKJUROS"),      // titulo do grafico
							msg.getString("X_GRAFICO_TICKJUROS"),                        // descricao do eixo X
							msg.getString("Y_GRAFICO_TICKJUROS"),                        // descricao do eixo Y
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
			}

		}
		
    public void doPost (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
				doGet(request,response);
    }

}
