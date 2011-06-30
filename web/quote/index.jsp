<%@ include file="/header.jsp"%>
<jsp:include page="/user/userservlet">
    <jsp:param name="op" value="0"/>
</jsp:include>
<% String quote = request.getParameter("quote"); %>

<br/>
<jsp:include page="QuoteServlet">
    <jsp:param name="op" value="1"/>
    <jsp:param name="quote" value="<%=quote%>" />
</jsp:include>
<br/>

<img src="/xInvest/quote/QuoteServlet?op=0&quote=<%= quote %>">
<br/>

<%@ include file="/footer.jsp"%>
