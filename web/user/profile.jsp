<%@include file="/header.jsp"%>
<jsp:include page="/user/userservlet">
    <jsp:param name="op" value="0"/>
</jsp:include>
<%@page import="org.xinvest.beans.User"%>
<% User user = (User) session.getAttribute("user"); %>
<h1>Profile</h1>
<form action="userservlet" method="post" enctype="multipart/form-data">
<input type="hidden" name="op" value="4"/>
<table>
<tr><td>Name:</td><td><input type="text" name="name" value="<%=user.getName()%>"/></td></tr>
<tr><td>Password:</td><td><input type="password" name="pass"/></td></tr>
<tr><td>Picture:</td><td><input type="file" name="picture"/></td></tr>
</table>
<img class="thumbs" src="/xInvest/user/pictures/<%=user.getEmail()%>"/>
<input type="submit" value="Alterar">
</form>
<br/>
<a href="javascript:history.go(-1)"/><%=msg.getString("BACK")%></a>
<%@include file="/footer.jsp"%>