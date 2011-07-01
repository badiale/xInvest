<jsp:include page="/user/userservlet">
    <jsp:param name="op" value="0"/>
</jsp:include>
<%@ include file="/header.jsp"%>
<div id="userBox">
    <jsp:include page="userservlet">
        <jsp:param name="op" value="5"/>
    </jsp:include>
</div>
<br/>
<jsp:include page="/investment/investmentservlet">
    <jsp:param name="op" value="3"/>
</jsp:include>
<br/>
<jsp:include page="/investment/investmentservlet">
    <jsp:param name="op" value="4"/>
</jsp:include>
<br/>
<%@ include file="/footer.jsp"%>
