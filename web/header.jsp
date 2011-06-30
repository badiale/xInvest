<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
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
            if (request.getSession().getAttribute("user")!=null) {
                out.println("<div id=\"menu-user\"><table><tr>");
                out.println("<td><a href=\"/xInvest/user\">Home</a></td>");
                out.println("<td><a href=\"/xInvest/user/profile.jsp\">Profile</a></td>");
                out.println("<td><a href=\"/xInvest/user\">Histórico</a></td>");
                out.println("<td><a href=\"/xInvest/user\">Cotacões</a></td>");
                out.println("<td><a href=\"/xInvest/user\">Mercado</a></td>");
                out.println("<td><a href=\"/xInvest/user\">Empréstimo</a></td>");
                out.println("<td><a href=\"/xInvest/user/userservlet?op=2\">Logout</a></td>");
                out.println("</tr></table></div>");
            }
%>                    
            <div id="body-top"></div>
            <div id="content">
                