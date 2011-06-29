<%@ include file="/header.jsp"%>

<div id="login">
    <h1>Login</h1>
    <form action="#">
        <table>
            <tr><td>E-mail:</td><td><input type="text" name="email" style="width: 100%"></td></tr>
            <tr><td>Password:</td><td><input type="password" name="password" style="width: 100%"></td></tr>
        </table>
        <div id="submitLogin">
            <a href="user/register.jsp">Register</a>
            <input type="submit" value="Login"/></td>
        </div>
    </form>
</div>
<%@ include file="/footer.jsp"%>
