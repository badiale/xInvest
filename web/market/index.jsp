<%@ include file="/header.jsp"%>
<h1><%=msg.getString("MARKET")%><h1>

<H2><%= msg.getString("SELL") %></H2>
<FORM ACTION="acao_vender">
	<p aling="center">
	<table width="300">
		<tr>
			<th><%= msg.getString("QUOTE") %></th>
			<td><INPUT TYPE="text" NAME="quote"></td>
		</tr>
		<tr>
			<th><%= msg.getString("PRICE") %></th>
			<td><INPUT TYPE="text" NAME="price"</td>
		</tr>
		<tr>
			<th><%= msg.getString("QUANTITY") %></th>
			<td><INPUT TYPE="text" NAME="quantity"></td>
		</tr>
		<tr>
			<td><INPUT TYPE="submit" VALUE="<%= msg.getString("SELL") %>"></td>
			<td><INPUT TYPE="reset" VALUE="<%= msg.getString("CANCEL") %>"></td>
		</tr>
	</table>
	</p>
</FORM>

<H2><%= msg.getString("BUY") %></H2>
<table width=100>
	<tr>
		<th><%= msg.getString("EMAIL") %></th>
		<th><%= msg.getString("QUOTE")%></th>
		<th><%= msg.getString("PRICE")%></th>
		<th><%= msg.getString("QUANTITY")%></th>
		<th></th>
	</tr>
	<tr>
		<td>jose@email.com</td>
		<td>YHOO</td>
		<td>20</td>
		<td>1</td>
		<td><A HREF="acao_de_comprar?id=">Buy</A></td>
	</tr>
</table>

<%@ include file="/footer.jsp"%>
