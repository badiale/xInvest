<jsp:include page="/user/userservlet">
    <jsp:param name="op" value="0"/>
</jsp:include>
<%@page import="org.xinvest.beans.Quote"%>
<%@ include file="/header.jsp"%>
<% String quote = request.getParameter("quote"); %>
<h1><%=msg.getString("SELL")%> - <%=quote%><h1>

        <h2><a href="/xInvest/market/sell.jsp?quote=<%=quote%>"><%=msg.getString("SELL_MARKET")%></a></h2>
        
        <h2><%=msg.getString("SELL_BANK")%></h2>
        <p><%=msg.getString("PRICE")%>:
        <jsp:include page="investmentservlet">
            <jsp:param name="op" value="6"/>
            <jsp:param name="quote" value="<%=quote%>"/>
        </jsp:include>
        </p>
        <form action="investmentservlet">
            <input type="hidden" name="op" value="2"/>
            <input type="hidden" name="quote" value="<%=quote%>"/>
            <%=msg.getString("QUANTITY")%>: <input type="text" name="amount"/>
            <input type="submit" value="<%=msg.getString("SELL")%>"/>
        </form>
        
<%@ include file="/footer.jsp"%>
