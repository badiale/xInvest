package org.xinvest.servlets;

// Servlet Imports
import java.io.*;
import javax.servlet.*;
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

/**
 * Servlet to handle user operations within the stock simulator application.
 * @author Fábio Abrão Luca
 */
public class UserServlet extends HttpServlet {
    
private final int CONFIRMLOGIN  = 0;
private final int LOGIN         = 1;
private final int LOGOUT	    = 2;
private final int REGISTER	    = 3;
private final int MODIFY	    = 4;
private final int UNREGISTER    = 5;
    
public void doGet (HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	
    PrintWriter out = response.getWriter();
	HttpSession httpSession = request.getSession();

	String targetUrl = null;
	int operation = -1;
	try {
	     operation = Integer.parseInt(request.getParameter("op"));
	} catch (Exception e) {
	    targetUrl = "/xInvest/message.jsp?msg=404";
	    response.sendRedirect(targetUrl);
	}
    
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
            targetUrl = "/xInvest/message.jsp?msg=100";
            response.sendRedirect(targetUrl);
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

                session.beginTransaction();
                if (user == null || ((user != null) &&
                        (User.authenticate(user.getEmail(),user.getPassword())==null))) {
                    targetUrl = "/xInvest/user";
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                targetUrl = "/xInvest/message.jsp?msg=200";
                response.sendRedirect(targetUrl);
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
                response.sendRedirect(targetUrl);
            } catch (Exception e) {
                targetUrl = "/xInvest/message.jsp?msg=0";
                response.sendRedirect(targetUrl);
            }
		break;
            
	    case LOGOUT:
            httpSession.invalidate();
            targetUrl = "/xInvest/index.jsp";
            response.sendRedirect(targetUrl);
		break;

	    case REGISTER:
            user = new User();
            try {
                File picture = new File(User.imagesFolder+"/"+user.getEmail());
                if (fitem.getContentType().equals("image/jpeg") ||
                        fitem.getContentType().equals("image/png") ||
                        fitem.getContentType().equals("image/bmp")) {
                    
                    fitem.write(picture);
                    
                    user.setName(name);
                    user.setEmail(mail);
                    user.setPassword(pass);

                    session.beginTransaction();
                    user.insert();
                    session.getTransaction().commit();

                    targetUrl = "/xInvest/message.jsp?msg=201";
                } else {
                    targetUrl="/xInvest/message.jsp?msg=106";
                }
            } catch (Exception e) {
                targetUrl = "/xInvest/message.jsp?msg=202";
                response.sendRedirect(targetUrl);
            }
		break;

        case MODIFY:
            try {
                user = (User) httpSession.getAttribute("user");
                
                session.beginTransaction();
                if ((user != null) &&
                        (User.authenticate(user.getEmail(),user.getPassword())!=null)) {
                    user.setName(request.getParameter("name"));
                    user.setPassword(request.getParameter("pass"));
                    user.update();
                    targetUrl = "/xInvest/user";
                } else {
                    targetUrl = "/xInvest/index.jsp";
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                targetUrl = "/xInvest/message.jsp?msg=204";
                response.sendRedirect(targetUrl);
            }
        break;

	    case UNREGISTER:
		try {
		    user = (User) httpSession.getAttribute("user");
		    if ((user != null) &&
	    		    (User.authenticate(user.getEmail(),user.getPassword())!=null)) {
		        httpSession.invalidate();
            
                session.beginTransaction();
                user.remove();
                session.getTransaction().commit();

                targetUrl = "/xInvest/index.jsp";
		    }
		} catch (Exception e) {
		    targetUrl = "/xInvest/message.jsp?msg=203";
		    response.sendRedirect(targetUrl);
		}
		break;

        default:
            targetUrl = "/xCommerce/message.jsp?msg=404";
		break;
	}
    if (targetUrl != null) response.sendRedirect(targetUrl);
}

public void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
}

} /* close class */
