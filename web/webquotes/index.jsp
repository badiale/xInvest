<%@ include file="/header.jsp"%>

<h1><%=msg.getString("Cotacoes")%></h1>
<br/>
<jsp:include page="WebQuotesServlet">
    <jsp:param name="op" value="0"/>
</jsp:include>
<br/>

<%@ include file="/footer.jsp"%>
