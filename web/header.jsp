<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*,org.xinvest.beans.User" %>
<%
	Locale currentLocale = request.getLocale();
	ResourceBundle msg = ResourceBundle.getBundle("org.xinvest.bundles.message", currentLocale);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/xInvest/style.css" />
        <title>xInvest</title>
    </head>
    <body>
        <div id="wrapper">
			<div id="header"></div>
<%
			User USER = (User) request.getSession().getAttribute("user");
            if (USER!=null) {
                out.println("<div id=\"menu-user\"><table><tr>");
                out.println("<td>"+msg.getString("HELLO")+ " " +USER.getName() + " | </a></td>");
                out.println("<td><a href=\"/xInvest/user\">"+msg.getString("HOME")+"</a></td>");
                out.println("<td><a href=\"/xInvest/user/profile.jsp\">"+msg.getString("PROFILE")+"</a></td>");
                out.println("<td><a href=\"/xInvest/history\">"+msg.getString("HISTORY")+"</a></td>");
                out.println("<td><a href=\"/xInvest/webquotes\">"+msg.getString("Cotacoes")+"</a></td>");
                out.println("<td><a href=\"/xInvest/market\">"+msg.getString("MARKET")+"</a></td>");
                out.println("<td><a href=\"/xInvest/loan\">"+msg.getString("LOANS")+"</a></td>");
                out.println("<td><a href=\"/xInvest/user/userservlet?op=2\">"+msg.getString("LOGOUT")+"</a></td>");
                out.println("</tr></table></div>");
            }
%>                    
            <div id="body-top"></div>
            <div id="content">
                
