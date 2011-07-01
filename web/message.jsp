
<%@ include file="/header.jsp"%>
<div id="message"><p class="<%
    int msgcode = Integer.parseInt(request.getParameter("msg"));

    switch (msgcode) {
	// Gerais do sistema
	case 0: out.println("error_msg\">"+msg.getString("LOGIN_ERROR")); break;
	// Operacoes de transaction
	case 100: out.println("error_msg\">"+msg.getString("LOAN_ERROR")); break;
	case 101: out.println("error_msg\">"+msg.getString("LOAN_USER_ERROR")); break;
	case 102: out.println("error_msg\">"+msg.getString("LOAN_CREATE_ERROR")); break;
	case 103: out.println("error_msg\">"+msg.getString("LOAN_PAY_ERROR")); break;
	case 105: out.println("success_msg\">"+msg.getString("LOAN_SUCCESS")); break;
	case 106: out.println("success_msg\">"+msg.getString("LOAN_CREATE_SUCCESS")); break;
	// Operacoes de user
    case 200: out.println("error_msg\">"+msg.getString("USER_FORM_ERROR")); break;
    case 201: out.println("error_msg\">"+msg.getString("USER_CONFIRM_ERROR")); break;
    case 202: out.println("success_msg\">"+msg.getString("USER_REGISTER_SUCCESS")); break;
    case 203: out.println("error_msg\">"+msg.getString("USER_PICTURE_ERROR")); break;
    case 204: out.println("error_msg\">"+msg.getString("USER_REGISTER_ERROR")); break;
    case 205: out.println("success_msg\">"+msg.getString("USER_MODIFY_SUCCESS")); break;
    case 206: out.println("error_msg\">"+msg.getString("USER_MODIFY_ERROR")); break;
	
	// Erro inesperado
	default: out.println("error_msg\">"+msg.getString("UNEXPECTED_ERROR")); break;
    }
    // Template error message: out.println("error_msg\">"+msg.getString("")); break;
    // Template success message: out.println("success_msg\">"+msg.getString("")); break;

%></p></div>
<br/>
<a href="javascript:history.go(-1)"/><%=msg.getString("BACK")%></a>
<%@ include file="/footer.jsp"%>
