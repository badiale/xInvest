<%@ include file="/header.jsp"%>
<% String quote = request.getParameter("quote"); %>
<br/>
<img src="/xInvest/quote/QuoteServlet?op=0">
<jsp:include page="QuoteServlet">
    <jsp:param name="op" value="1"/>
    <jsp:param name="quote" value="<%=quote%>" />
</jsp:include>
<br/>

<%@ include file="/footer.jsp"%>
