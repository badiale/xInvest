<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@page import="org.xcommerce.beans.Cliente"%>
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
            <div id="body-top"></div>
            <div id="content">
                