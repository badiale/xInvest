<%@ include file="/header.jsp"%>
<div id="userBox">
    <jsp:include page="userservlet">
        <jsp:param name="op" value="5"/>
    </jsp:include>
</div>
<%@ include file="/footer.jsp"%>
