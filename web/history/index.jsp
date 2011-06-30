<%@ include file="/header.jsp"%>
<jsp:include page="/user/userservlet">
    <jsp:param name="op" value="0"/>
</jsp:include>

<h1><%=msg.getString("HISTORY_PAGE")%><h1>

<br/>
<img src="/xInvest/history/TickJurosServlet?op=0">
<br/>

<br/>
<%@ include file="/footer.jsp"%>
