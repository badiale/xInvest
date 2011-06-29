package org.xinvest.examples;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.xy.*;
import org.jfree.chart.servlet.ServletUtilities;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

/**
 * Classe que desenha um grafico qualquer.<br>
 * Basicamente para se fazer um grafico, soh eh necessario criar um dataset, 
 * inserir dados nele, plotar e entao salvar isso.<br>
 * Esse exemplo mostra como fazer um grafico em forma de uma curva, dados pontos (x, y).<br>
 * Pra fazer algum outro grafico, eh soh ver as implementacoes disponiveis de datasets 
 * <a href="http://www.jfree.org/jfreechart/api/javadoc/org/jfree/data/general/Dataset.html">aqui</a>.<br>
 * <br>
 * Isso nao vai funcionar direto. <br>
 * Para isso funcionar, eh necessario rodar este comando:<br>
 * <code>xhost +</code>
 * */

public class Chart extends HttpServlet {
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
		// conjunto de dados que vamos plotar
		XYSeriesCollection dataset = new XYSeriesCollection();

		// dados de uma "curva"
		XYSeries curva1 = new XYSeries("Uma linha");
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

		OutputStream out = response.getOutputStream();
		response.setContentType("image/png");
		try {
			// metodo que salva o grafo em um arquivo temporario
			ChartUtilities.writeChartAsPNG(
					out,   // output stream
					chart, // grafico
					500,   // largura
					300);  // altura
		} catch (Exception e) {
			System.out.println("Problem occurred creating chart.");
		}
		out.close();
	}

  	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
