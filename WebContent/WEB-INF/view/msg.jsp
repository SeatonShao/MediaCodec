<%@page  language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<html>
<head>
<%
	String to = (String) session.getAttribute("url");
	String msg = (String) session.getAttribute("msg");
	if (msg == null || msg.equals(""))
		msg = " ";
%>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" charset="utf-8" >
<title>提示信息</title>
<script type="text/javascript">
function go(){
    //window.open('http://218.57.135.42:8080/esms/jsp/service/<%=to%>.jsp','_self')
    window.location.href='<%=request.getServletContext().getContextPath()%><%=to%>';
	}
	setTimeout("go()", 10000)
</script>
</head>
<body class="body" style="overflow-x: hidden;">
	<table style="width: 100%;border: 0;">
		<tr>
			<td height="29" align="center"><span class="style7">提示信息！</span></td>
		</tr>
		<tr>
			<td width="58%" bgcolor="#FFFFFF">
				<p><%=msg%></p>
				<p>
					<a href="<%=to%>">如果你的浏览器不支持自动跳转请点击这里。</a>
				</p>
			</td>
		</tr>

	</table>
</body>
</html>
