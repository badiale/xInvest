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
 * Servlet to handle transaction operations.
 * @author Rodrigo Leonavas
 */
public class ClienteServlet extends HttpServlet {
    private final int CONFIRMLOGIN  = 0;
    private final int REGISTER	    = 1;
    private final int LOGIN	    = 2;
    private final int LOGOUT	    = 3;
    private final int LOGINBOX	    = 4;
    private final int UNREGISTER    = 5;
    private final int MODIFY	    = 6;
    
    public void doGet (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {


	
    }
    public void doPost (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doGet(request,response);
    }

}
