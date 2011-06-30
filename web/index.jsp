<%@ include file="/header.jsp"%>
<div id="login">
    <h1>Login</h1>
    <form action="/xInvest/user/userservlet">
        <input type="hidden" name="op" value="1"/>
        <table>
            <tr><td><%=msg.getString("EMAIL")%>:</td><td><input type="text" name="email" style="width: 100%"></td></tr>
            <tr><td><%=msg.getString("PASSWORD")%>:</td><td><input type="password" name="pass" style="width: 100%"></td></tr>
        </table>
        <div id="submitLogin">
            <a href="/xInvest/user/register.jsp"><%=msg.getString("REGISTER")%></a>
            <input type="submit" value="<%=msg.getString("LOGIN")%>"/></td>
        </div>
    </form>
</div>
<%@ include file="/footer.jsp"%>
