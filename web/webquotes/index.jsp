<%@ include file="/header.jsp"%>
<jsp:include page="/user/userservlet">
    <jsp:param name="op" value="0"/>
</jsp:include>
<h1><%=msg.getString("Cotacoes")%></h1>
<br/>
<jsp:include page="WebQuotesServlet">
    <jsp:param name="op" value="0"/>
</jsp:include>
<br/>

<%@ include file="/footer.jsp"%>
