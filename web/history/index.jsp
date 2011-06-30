<%@ include file="/header.jsp"%>
<jsp:include page="/user/userservlet">
    <jsp:param name="op" value="0"/>
</jsp:include>

<br/>
<img src="/xInvest/history/TickJurosServlet?op=0">
<br/>

<br/>
<%@ include file="/footer.jsp"%>
