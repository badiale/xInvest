<%@ include file="/header.jsp"%>
<% String quote = request.getParameter("quote"); %>
<br/>

<jsp:include page="QuoteServlet">
    <jsp:param name="op" value="1"/>
    <jsp:param name="quote" value="<%=quote%>" />
</jsp:include>

<img src="/xInvest/quote/QuoteServlet?op=0&quote=<%= quote %>">
<br/>

<%@ include file="/footer.jsp"%>
