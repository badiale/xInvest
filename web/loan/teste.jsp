<%@ include file="/header.jsp"%>

<jsp:include page="/user/userservlet">
    <jsp:param name="op" value="0"/>
</jsp:include>

<img style="text-align:center" src="/xInvest/loan/loanservlet?op=4&id=19">

<%@ include file="/footer.jsp"%>
