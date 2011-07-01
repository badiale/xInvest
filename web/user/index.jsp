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
<h2><%=msg.getString("MY_INVESTMENTS")%></h2>
<jsp:include page="/investment/investmentservlet">
    <jsp:param name="op" value="3"/>
</jsp:include>
<br/>
<h2><%=msg.getString("MY_SELLINGS")%></h2>
<jsp:include page="/investment/investmentservlet">
    <jsp:param name="op" value="4"/>
</jsp:include>
<br/>
<%@ include file="/footer.jsp"%>
