<%@ include file="/header.jsp"%>

<jsp:include page="/user/userservlet">
    <jsp:param name="op" value="0"/>
</jsp:include>

<jsp:include page="loanservlet">
    <jsp:param name="op" value="0"/>
</jsp:include>

<%@ include file="/footer.jsp"%>
