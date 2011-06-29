<%@include file="/header.jsp"%>
<h1>Register</h1>
<form action="#" method="post" enctype="multipart/form-data">
    <input type="hidden" name="op" value="1">
    <table>
	<tr><td>Name:</td><td><input class="loginInputText" type="text" name="nome"></td></tr>
	<tr><td>E-mail:</td><td><input class="loginInputText" type="text" name="email"></td></tr>
	<tr><td>Password:</td><td><input class="loginInputText" type="password" name="senha"></td></tr>
	<tr><td>Re-type password:</td><td><input class="loginInputText" type="password" name="senha"></td></tr>
	<tr><td>Picture:</td><td><input type="file" name="picture"/></td></tr>
    </table>
    <input type="submit" value="Submit">
</form>
<br/>
<a href="javascript:history.go(-1)"/>BACK</a>
<%@include file="/footer.jsp"%>