<jsp:include page="/user/userservlet">
    <jsp:param name="op" value="0"/>
</jsp:include>
<%@ include file="/header.jsp"%>
<h1><%=msg.getString("SELL")%> - <%=request.getParameter("quote")%></h1>
<FORM ACTION="/xInvest/investment/investmentservlet">
    <INPUT TYPE="hidden" NAME="op" VALUE="1">
    <INPUT TYPE="hidden" NAME="quote" VALUE="<%=request.getParameter("quote")%>">
	<table>
		<tr>
			<th><%= msg.getString("PRICE") %></th>
			<td><INPUT TYPE="text" NAME="price"></td>
		</tr>
		<tr>
			<th><%= msg.getString("QUANTITY") %></th>
			<td><INPUT TYPE="text" NAME="amount"></td>
		</tr>
		<tr>
			<td><INPUT TYPE="submit" VALUE="<%= msg.getString("SELL") %>"></td>
			<td><INPUT TYPE="reset" VALUE="<%= msg.getString("CANCEL") %>"></td>
		</tr>
	</table>
</FORM>
<%@ include file="/footer.jsp"%>
