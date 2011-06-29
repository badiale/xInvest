<%@include file="/header.jsp"%>
<h1>Register</h1>
<form action="userservlet" method="post" enctype="multipart/form-data">
    <input type="hidden" name="op" value="3">
    <table>
	<tr><td>Name:</td><td><input type="text" name="nome"></td></tr>
	<tr><td>E-mail:</td><td><input type="text" name="email"></td></tr>
	<tr><td>Password:</td><td><input type="password" name="senha"></td></tr>
	<tr><td>Re-type password:</td><td><input type="password" name="senha"></td></tr>
	<tr><td>Picture:</td><td><input type="file" name="picture"/></td></tr>
    </table>
    <input type="submit" value="Submit">
</form>
<br/>
<a href="javascript:history.go(-1)"/>BACK</a>
<%@include file="/footer.jsp"%>