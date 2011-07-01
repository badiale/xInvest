package org.xinvest.servlets;

// Servlet Imports
import java.io.*;
import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
// xInvest Imports
import org.xinvest.beans.User;
import org.xinvest.db.DBManager;
// Hibernate Imports
import org.hibernate.Session;
// Apache Commons for file uploading
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
// Other Imports
import java.util.*;
import java.text.SimpleDateFormat;
import org.xinvest.beans.Investment;

/**
* Servlet to handle user operations within the stock simulator application.
* @author Fábio Abrão Luca
*/
public class UserServlet extends HttpServlet {
	private final int CONFIRMLOGIN = 0;
	private final int LOGIN = 1;
	private final int LOGOUT = 2;
	private final int REGISTER = 3;
	private final int MODIFY = 4;
	private final int USERBOX = 5;

	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PageContext pageContext = null;
		try {
			pageContext = JspFactory.getDefaultFactory().getPageContext(this,request,response,null,true,8192,true);
		} catch(Exception e) { e.printStackTrace(); }

		PrintWriter out = response.getWriter();
		HttpSession httpSession = request.getSession();

		String targetUrl = null;
		int operation = -1;

		String name = null;
		String mail = null;
		String pass = null;
		FileItem fitem = null;
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			try {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = null;
				items = upload.parseRequest(request);

				Iterator itr = items.iterator();

				while (itr.hasNext()) {
					FileItem item = (FileItem) itr.next();
					if (item.isFormField()) { // form field
						if (item.getFieldName().equals("op")) {
							operation = Integer.parseInt(item.getString());
						} else if (item.getFieldName().equals("name")) {
							name = item.getString();
						} else if (item.getFieldName().equals("email")) {
							mail = item.getString();
						} else if (item.getFieldName().equals("pass")) {
							pass = item.getString();
						}
					} else { // is a file
						fitem = item;
					}
				}
			} catch (FileUploadException e) {
				targetUrl = "/xInvest/message.jsp?msg=200";
			}
		} else {
			operation = Integer.parseInt(request.getParameter("op"));
			name = request.getParameter("name");
			mail = request.getParameter("email");
			pass = request.getParameter("pass");
		}

		User user = null;
		Session session = DBManager.getSession();

		switch (operation) {
			case CONFIRMLOGIN:
				try {
					user = (User) httpSession.getAttribute("user");
					if (user != null) {
						session.beginTransaction();
						user = User.authenticate(user.getEmail(),user.getPassword());
						session.getTransaction().commit();
					}
					if (user == null) {
						// redirecionar de forma forcada
						if (pageContext != null)
							pageContext.forward("/index.jsp");
						return;
					}
				} catch (Exception e) {
					targetUrl = "/xInvest/message.jsp?msg=201";
					e.printStackTrace();
				}
			break;

			case LOGIN:
				try {
					session.beginTransaction();
					user = User.authenticate(mail,pass);
					session.getTransaction().commit();

					if (user != null) {
						httpSession.setAttribute("user",user);
						targetUrl = "/xInvest/user";
					} else {
						targetUrl = "/xInvest/message.jsp?msg=0";
					}
				} catch (Exception e) {
					targetUrl = "/xInvest/message.jsp?msg=0";
				}
			break;

			case LOGOUT:
				httpSession.invalidate();
				targetUrl = "/xInvest/index.jsp";
			break;

			case REGISTER:
				user = new User();
				try {
					File picture = new File(User.imagesFolder+"/"+mail);
					if (fitem.getContentType().equals("image/jpeg") || 
							fitem.getContentType().equals("image/png") || 
							fitem.getContentType().equals("image/bmp")) { 
						File path = new File(User.imagesFolder);
						if (!path.exists()) path.mkdirs();

						fitem.write(picture);

						user.setName(name);
						user.setEmail(mail);
						user.setPassword(pass);

						session.beginTransaction();
						user.insert();
						session.getTransaction().commit();

						httpSession.setAttribute("user",user);
						targetUrl = "/xInvest/message.jsp?msg=202";
					} else {
						targetUrl="/xInvest/message.jsp?msg=203";
					}
				} catch (Exception e) {
					targetUrl = "/xInvest/message.jsp?msg=204";
					e.printStackTrace();
				}
			break;

			case MODIFY:
				try {
					user = (User) httpSession.getAttribute("user");
					session.beginTransaction();
					user = User.authenticate(user.getEmail(),user.getPassword());
					if (user != null) {
					if (fitem.getContentType().equals("image/jpeg") || 
							fitem.getContentType().equals("image/png") || 
							fitem.getContentType().equals("image/bmp")) {

							File picture = new File(User.imagesFolder+"/"+user.getEmail());
							picture.delete();
							fitem.write(new File(User.imagesFolder+"/"+user.getEmail()));
							user.setName(name);
							user.setPassword(pass);
							user.update();
							httpSession.setAttribute("user",user);
							targetUrl = "/xInvest/message.jsp?msg=205";
						} else {
							targetUrl="/xInvest/message.jsp?msg=203";
						}
					} else {
						targetUrl = "/xInvest/message.jsp?msg=206";
					}
					session.getTransaction().commit();
				} catch (Exception e) {
					targetUrl = "/xInvest/message.jsp?msg=205";
					System.out.println(e);
				}
			break;

			case USERBOX:
				user = (User) httpSession.getAttribute("user");
                
                session.beginTransaction();
                Float invested = new Float(0.0);
                List list = Investment.findByActive(user);
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    Investment i = (Investment) it.next();
                    invested += i.getValue();
                }
                session.getTransaction().commit();
                
                Locale currentLocale = request.getLocale();
				ResourceBundle msg = ResourceBundle.getBundle("org.xinvest.bundles.message", currentLocale);
				out.println("<table>");
				out.println("<tr><td>"+msg.getString("NAME")+":</td><td>"+user.getName()+"</td>");
				out.println("<tr><td>"+msg.getString("MONEY")+":</td><td>"+user.getMoney()+"</td>");
				out.println("<tr><td>"+msg.getString("INVESTED")+":</td><td>"+invested+"</td>");
				out.println("<tr><td>"+msg.getString("DEBTS")+":</td><td>"+user.getMoney()+"</td>");
				out.println("<tr><td>"+msg.getString("LOANS_OFFERED")+":</td><td>"+user.getMoney()+"</td>");
				out.println("</table>");
			break;

			default:
				targetUrl = "/xInvest/message.jsp?msg=404";
			break;
		}
		if (targetUrl != null) response.sendRedirect(targetUrl);
		return;
	}

	public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

} /* close class */
