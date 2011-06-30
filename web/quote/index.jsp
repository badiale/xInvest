<%@ include file="/header.jsp"%>
<% String quote = request.getParameter("quote"); %>
<br/>
<jsp:include page="QuoteServlet">
    <jsp:param name="op" value="0"/>
    <jsp:param name="quote" value="<%=quote%>" />
</jsp:include>
<br/>

<%@ include file="/footer.jsp"%>
