package org.xinvest.servlets;

// Servlet Imports
import java.io.*;
import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
// xInvest Imports
import org.xinvest.beans.*;
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
* Servlet to handle trade operations within the stock simulator application.
* @author Fábio Abrão Luca
*/
public class InvestmentServlet extends HttpServlet {
	private final int BUY           = 0;
	private final int BUYMARKET     = 7;
	private final int SELL          = 1;
	private final int SELLBANK      = 2;
	private final int LIST          = 3;
	private final int LISTONSALE    = 4;
	private final int MARKET        = 5;
	private final int BANKPRICE     = 6;

	public void doGet (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
        PrintWriter out = response.getWriter();
		HttpSession httpSession = request.getSession();
        Session session = DBManager.getSession();

		String targetUrl = null;
		int operation = -1;
		try {
			operation = Integer.parseInt(request.getParameter("op"));
		} catch (Exception e) { e.printStackTrace(); }

		User active = null;
		User passive = null;
        Quote quote = null;
        Investment i = null;
		
		switch (operation) {
			case BUY:
				try {
                    session.beginTransaction();

                    active = (User) httpSession.getAttribute("user");
                    active = User.authenticate(active.getEmail(),active.getPassword());
                    quote = Quote.find(request.getParameter("quote"));
                    Integer amount = Integer.parseInt(request.getParameter("amount"));
                    Float value = new Float(quote.getLastestTick()*amount);
                    
                    if (active.getMoney() > value) {
                        i = Investment.findByQuoteActive(quote,active);
                        if (i != null) {
                            i.setAmount(i.getAmount()+amount);
                            i.setValue(i.getValue()+value);
                            i.setTimestamp(new Date());
                            i.update();
                        } else {
                            i = new Investment();
                            i.setActive(active);
                            i.setQuote(quote);
                            i.setAmount(amount);
                            i.setValue(new Float(value));
                            i.insert();
                        }
                        active.setMoney(active.getMoney()-value);
                        active.update();
                        httpSession.setAttribute("user",active);
                        targetUrl = "/xInvest/message.jsp?msg=300"; // BUY SUCCESS
                    } else {
                        targetUrl = "/xInvest/message.jsp?msg=301"; // NO CASH
                    }                    

                    session.getTransaction().commit();
                } catch (Exception e) {
					targetUrl = "/xInvest/message.jsp?msg=302"; // BUY ERROR
                    e.printStackTrace();
				}
			break;

            case BUYMARKET:
                try {
                    session.beginTransaction();

                    active = (User) httpSession.getAttribute("user");
                    active = User.authenticate(active.getEmail(),active.getPassword());
                    Investment ibuy = Investment.find(Integer.parseInt(request.getParameter("id")));
                    passive = ibuy.getPassive();
                    quote = ibuy.getQuote();
                    Integer amount = ibuy.getAmount();
                    Float value = ibuy.getValue();
                    
                    if (active.getMoney() > value) {
                        i = Investment.findByQuoteActive(quote,active);
                        if (i != null) {
                            i.setAmount(i.getAmount()+amount);
                            i.setValue(i.getValue()+value);
                            i.setTimestamp(new Date());
                            i.update();
                        } else {
                            i = new Investment();
                            i.setActive(active);
                            i.setQuote(quote);
                            i.setAmount(amount);
                            i.setValue(new Float(value));
                            i.insert();
                        }
                        passive.setMoney(passive.getMoney()+value);
                        ibuy.remove();
                        active.setMoney(active.getMoney()-value);
                        active.update();
                        httpSession.setAttribute("user",active);
                        targetUrl = "/xInvest/message.jsp?msg=300"; // BUY SUCCESS
                    } else {
                        targetUrl = "/xInvest/message.jsp?msg=301"; // NO CASH
                    }                    

                    session.getTransaction().commit();
                } catch (Exception e) {
					targetUrl = "/xInvest/message.jsp?msg=302"; // BUY ERROR
                    e.printStackTrace();
				}
            break;
            
            case SELL:
				try {
                    session.beginTransaction();
                    
                    active = (User) httpSession.getAttribute("user");
                    active = User.authenticate(active.getEmail(),active.getPassword());
                    quote = Quote.find(request.getParameter("quote"));
                    Integer amount = Integer.parseInt(request.getParameter("amount"));
                    Float price = new Float(request.getParameter("price"));
                    Float value = price*amount;
                    
                    i = Investment.findByQuoteActive(quote,active);
                    if (i != null) {
                        if (i.getAmount() > amount) {
                            Investment isplit = new Investment();
                            isplit.setPassive(active);
                            isplit.setQuote(quote);
                            isplit.setAmount(amount);
                            isplit.setValue(value);
                            isplit.insert();
                            i.setAmount(i.getAmount()-amount);
                            i.update();
                            targetUrl = "/xInvest/message.jsp?msg=303"; // SELL SUCCESS
                        } else if (i.getAmount() == amount) {
                            i.setPassive(active);
                            i.setActive(null);
                            i.setValue(value);
                            i.update();
                            targetUrl = "/xInvest/message.jsp?msg=303"; // SELL SUCCESS
                        } else {
                            targetUrl = "/xInvest/message.jsp?msg=304"; // NOT ENOUGH
                        }
                    } else {
                        targetUrl = "/xInvest/message.jsp?msg=305"; // DONT HAVE
                    }
                    
                    session.getTransaction().commit();
                } catch (Exception e) {
					targetUrl = "/xInvest/message.jsp?msg=306";
				}
			break;

			case SELLBANK:
				try {
                    session.beginTransaction();
                    
                    active = (User) httpSession.getAttribute("user");
                    active = User.authenticate(active.getEmail(),active.getPassword());
                    quote = Quote.find(request.getParameter("quote"));
                    Integer amount = Integer.parseInt(request.getParameter("amount"));
                    Float price = new Float(quote.getDaysLow());
                    Float value = price*amount;
                    
                    i = Investment.findByQuoteActive(quote,active);
                    if (i != null) {
                        
                        if (i.getAmount() > amount) {
                            i.setAmount(i.getAmount()-amount);
                            i.update();
                            active.setMoney(active.getMoney()+value);
                            active.update();
                            httpSession.setAttribute("user",active);
                            targetUrl = "/xInvest/message.jsp?msg=307"; // SELL SUCCESS
                        } else if (i.getAmount().equals(amount)) {
                            i.remove();
                            active.setMoney(active.getMoney()+value);
                            active.update();
                            httpSession.setAttribute("user",active);
                            targetUrl = "/xInvest/message.jsp?msg=307"; // SELL SUCCESS
                        } else {
                            targetUrl = "/xInvest/message.jsp?msg=304"; // NOT ENOUGH
                        }
                    } else {
                        targetUrl = "/xInvest/message.jsp?msg=305"; // DONT HAVE
                    }
                    
                    session.getTransaction().commit();
                } catch (Exception e) {
					targetUrl = "/xInvest/message.jsp?msg=306";
                    System.out.println(e);
				}
			break;

			case LIST:
				try {
                    session.beginTransaction();
                    
                    active = (User) httpSession.getAttribute("user");
                    List list = Investment.findByActive(active);
                    
                    Locale currentLocale = request.getLocale();
                    ResourceBundle msg = ResourceBundle.getBundle("org.xinvest.bundles.message", currentLocale);
                    out.println("<table>");
                    out.println("<tr class=\"labelRow\"><th>"
                            +msg.getString("QUOTE")+"</th><th>"
                            +msg.getString("NAME")+"</th><th>"
                            +msg.getString("AMOUNT")+"</th><th>"
                            +"TICK</th><th>"
                            +msg.getString("50_AVG")+"</th><th>"
                            +msg.getString("INVESTMENT_AVG")+"</th><th>"
                            +msg.getString("GAIN_LOSS")+"</th></tr>");
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        i = (Investment) it.next();
                        Float gain = new Float(i.getValue()*(i.getAverageTick()-i.getQuote().getLastestTick()));
                        out.println("<tr><td>"+i.getQuote().getQuote()+"</td>");
                        out.println("<td>"+i.getQuote().getName()+"</td>");
                        out.println("<td>"+i.getAmount()+"</td>");
                        out.println("<td>"+i.getQuote().getLastestTick()+"</td>");
                        out.println("<td>"+i.getQuote().getFiftydayMovingAverage()+"</td>");
                        out.println("<td>"+i.getAverageTick()+"</td>");
                        out.println("<td>"+gain+"</td>");
                        out.println("<td><a href=\"/xInvest/quote/index.jsp?quote="
                                +i.getQuote().getQuote()+"\">"+msg.getString("BUY")+"</a><br/>"
                                +"<a href=\"/xInvest/investment/sell.jsp?quote="
                                +i.getQuote().getQuote()+"\">"+msg.getString("SELL")+"</a></td></tr>");
                    }
                    out.println("</table>");
                    
                    session.getTransaction().commit();
                } catch (Exception e) {
					targetUrl = "/xInvest/message.jsp?msg=308";
                    e.printStackTrace();
				}
            break;

			case LISTONSALE:
				try {
                    session.beginTransaction();
                    
                    passive = (User) httpSession.getAttribute("user");                    
                    List list = Investment.findByPassive(passive);
                    
                    Locale currentLocale = request.getLocale();
                    ResourceBundle msg = ResourceBundle.getBundle("org.xinvest.bundles.message", currentLocale);
                    out.println("<table>");
                    out.println("<tr class=\"labelRow\"><th>"
                            +msg.getString("QUOTE")+"</th><th>"
                            +msg.getString("NAME")+"</th><th>"
                            +msg.getString("AMOUNT")+"</th><th>"
                            +"TICK</th><th>"
                            +msg.getString("50_AVG")+"</th><th>"
                            +msg.getString("PRICE")+"</th></tr>");
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        i = (Investment) it.next();
                        Float price = new Float(i.getValue()/i.getAmount());
                        out.println("<tr><td>"+i.getQuote().getQuote()+"</td>");
                        out.println("<td>"+i.getQuote().getName()+"</td>");
                        out.println("<td>"+i.getAmount()+"</td>");
                        out.println("<td>"+i.getQuote().getLastestTick()+"</td>");
                        out.println("<td>"+i.getQuote().getFiftydayMovingAverage()+"</td>");
                        out.println("<td>"+price+"</td>");
                        out.println("<td><a href=\"/xInvest/investment/investmentservlet?op=7&id="
                                +i.getId()+"\">"+msg.getString("BUY")+"</a><br/>"
                                +"<a href=\"/xInvest/investment/investmentservlet?quote="
                                +i.getQuote().getQuote()+"\">"+msg.getString("SELL")+"</a></td></tr>");
                    }
                    out.println("</table>");
                    
                    session.getTransaction().commit();
                } catch (Exception e) {
					targetUrl = "/xInvest/message.jsp?msg=309";
					e.printStackTrace();
				}
            break;

            case MARKET:
				try {
                    session.beginTransaction();
                    
                    List list = Investment.findAll();
                    
                    Locale currentLocale = request.getLocale();
                    ResourceBundle msg = ResourceBundle.getBundle("org.xinvest.bundles.message", currentLocale);
                    out.println("<table>");
                    out.println("<tr class=\"labelRow\"><th>"
                            +msg.getString("QUOTE")+"</th><th>"
                            +msg.getString("NAME")+"</th><th>"
                            +msg.getString("AMOUNT")+"</th><th>"
                            +"Tick</th><th>"
                            +msg.getString("50_AVG")+"</th><th>"
                            +msg.getString("PRICE")+"</th></tr>");
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        i = (Investment) it.next();
                        if (i.getPassive()!=null) {
                            Float price = new Float(i.getValue()/i.getAmount());
                            out.println("<tr><td>"+i.getQuote().getQuote()+"</td>");
                            out.println("<td>"+i.getQuote().getName()+"</td>");
                            out.println("<td>"+i.getAmount()+"</td>");
                            out.println("<td>"+i.getQuote().getLastestTick()+"</td>");
                            out.println("<td>"+i.getQuote().getFiftydayMovingAverage()+"</td>");
                            out.println("<td>"+price+"</td>");
                            out.println("<td><a href=\"/xInvest/investment/investmentservlet?op=7&id="
                                    +i.getId()+"\">"+msg.getString("BUY")+"</a></td></tr>");
                        }
                    }
                    out.println("</table>");
                    
                    session.getTransaction().commit();
                } catch (Exception e) {
					targetUrl = "/xInvest/message.jsp?msg=310";
				}
			break;
            
            case BANKPRICE:
                    session.beginTransaction();
                    out.println(Quote.find(request.getParameter("quote")).getDaysLow());
                    session.getTransaction().commit();
            break;
                
            default:
				targetUrl = "/xInvest/message.jsp?msg=404";
			break;
		}
		if (targetUrl != null) response.sendRedirect(targetUrl);
		return;
	}

	public void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		doGet(request,response);
	}

} /* close class */
