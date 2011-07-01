<jsp:include page="/user/userservlet">
    <jsp:param name="op" value="0"/>
</jsp:include>
<%@ include file="/header.jsp"%>
<h1><%=msg.getString("MARKET")%></h1>
<jsp:include page="investmentservlet">
    <jsp:param name="op" value="5"/>
</jsp:include>
<br/>
<%@ include file="/footer.jsp"%>
