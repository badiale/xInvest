package org.xinvest.examples;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.xy.*;
import org.jfree.chart.servlet.ServletUtilities;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class Path extends HttpServlet {
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
		PrintWriter out = response.getWriter();
		out.println(request.getRealPath("/"));
	}

  	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
