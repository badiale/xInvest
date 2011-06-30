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
					
					// conjunto de dados que vamos plotar
					XYSeriesCollection dataset = new XYSeriesCollection();

					// dados de uma "curva"
					XYSeries curva1 = new XYSeries("Ticks");
					curva1.add(1.0, 1.0); // (x, y)
					curva1.add(2.0, 4.0);
					curva1.add(3.0, 3.0);
					curva1.add(4.0, 5.0);
					curva1.add(5.0, 5.0);
					curva1.add(6.0, 7.0);
					curva1.add(7.0, 7.0);
					curva1.add(8.0, 8.0);
	
					// coloca a curva no conjunto de dados
					dataset.addSeries(curva1);

					// retorna uma abstracao do grafo
					JFreeChart chart = ChartFactory.createXYLineChart( 
							"Grafico muito loko!",      // titulo do grafico
							"X",                        // descricao do eixo X
							"Y",                        // descricao do eixo Y
							dataset,                    // dados
							PlotOrientation.VERTICAL,   // orientacao do grafico
							true,                       // mostrar legendas
							true,                       // mostrar tooltips
							false);                     // mostrar urls (nao sei o q eh isso)

					OutputStream outS = response.getOutputStream();
					response.setContentType("image/png");

					// metodo que salva o grafo em um arquivo temporario
					ChartUtilities.writeChartAsPNG(
							outS,   // output stream
							chart, // grafico
							500,   // largura
							300);  // altura
	
				
					
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
