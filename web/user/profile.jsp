<%@include file="/header.jsp"%>
<jsp:include page="/user/userservlet">
    <jsp:param name="op" value="0"/>
</jsp:include>
<%@page import="org.xinvest.beans.User"%>
<% User user = (User) session.getAttribute("user"); %>
<h1><%=msg.getString("PROFILE")%></h1>
<form action="userservlet" method="post" enctype="multipart/form-data">
<input type="hidden" name="op" value="4"/>
<img class="thumbs" src="/xInvest/user/pictures/<%=user.getEmail()%>"/>
<table>
<tr><td><%=msg.getString("NAME")%>:</td><td><input type="text" name="name" value="<%=user.getName()%>"/></td></tr>
<tr><td><%=msg.getString("PASSWORD")%>:</td><td><input type="password" name="pass"/></td></tr>
<tr><td><%=msg.getString("PICTURE")%>:</td><td><input type="file" name="picture"/></td></tr>
</table>
<input type="submit" value="<%=msg.getString("UPDATE")%>">
</form>
<br/>
<a href="javascript:history.go(-1)"/><%=msg.getString("BACK")%></a>
<%@include file="/footer.jsp"%>