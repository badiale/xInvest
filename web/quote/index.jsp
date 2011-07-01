<%@ include file="/header.jsp"%>
<jsp:include page="/user/userservlet">
    <jsp:param name="op" value="0"/>
</jsp:include>
<% String quote = request.getParameter("quote"); %>

<br/>
<jsp:include page="QuoteServlet">
    <jsp:param name="op" value="1"/>
    <jsp:param name="quote" value="<%=quote%>" />
</jsp:include>
<br/>

<img style="text-align:center" src="/xInvest/quote/QuoteServlet?op=0&quote=<%= quote %>">
<br/>

<h2><%=msg.getString("BUY")%></h2>
<form action="/xInvest/investment/investmentservlet">
    <input type="hidden" name="op" value="0"/>
    <input type="hidden" name="quote" value="<%=quote%>"/>
    <table style="width:200px">
        <tr><td><%=msg.getString("QUANTITY")%></td>
        <td><input type="text" name="amount"/></td></tr>
    </table>
    <input type="submit" value="<%=msg.getString("BUY")%>"/>
</form>
<%@ include file="/footer.jsp"%>
