<%@include file="/header.jsp"%>
<h1><%=msg.getString("REDISTER")%></h1>
<form action="userservlet" method="post" enctype="multipart/form-data">
    <input type="hidden" name="op" value="3">
    <table>
	<tr><td><%=msg.getString("NAME")%>:</td><td><input type="text" name="name"></td></tr>
	<tr><td><%=msg.getString("EMAIL")%>:</td><td><input type="text" name="email"></td></tr>
	<tr><td><%=msg.getString("PASSWORD")%>:</td><td><input type="password" name="pass"></td></tr>
	<tr><td><%=msg.getString("PICTURE")%>:</td><td><input type="file" name="picture"/></td></tr>
    </table>
    <input type="submit" value="<%=msg.getString("SUBMIT")%>">
</form>
<br/>
<a href="javascript:history.go(-1)"/><%=msg.getString("BACK")%></a>
<%@include file="/footer.jsp"%>