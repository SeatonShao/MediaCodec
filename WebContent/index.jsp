<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8"
	charset="utf-8">
<%
	String basePath = request.getServletContext().getContextPath();
%>
<title>文件上传</title>

</head>
<body>
	<table>
		<tr id="up">
			<td><a href="<%=basePath%>/Dispatcher?page=biteMin">上传文件</a></td>
		</tr>

		<tr>
		
			<td><a href="<%=basePath%>/Dispatcher?page=mediaList&play=true">视频列表(播放)</a></td>
			<%--<td><a href="<%=basePath%>/Dispatcher?page=play">视频</a></td>
			 <td><a href="<%=basePath%>/Dispatcher?page=playvj">视频列表vj(播放)</a></td>
			 --%><td><a href="<%=basePath%>/Dispatcher?page=mediaList&play=false">视频列表(返回状态)</a></td>
			
<%-- 			<td><a href="<%=basePath%>/Dispatcher?page=video2">video2视频列表</a></td> --%>
			<td><a href="<%=basePath%>/Dispatcher?page=codec">编码列表</a></td>
			<td><a href="<%=basePath%>/Dispatcher?page=PlayList">视频列表</a></td>
			<td><a href="<%=basePath%>/Dispatcher?page=config">配置列表</a></td>
		<%-- 	<td><a href="<%=basePath%>/Dispatcher?page=log">日志列表</a></td>
		 --%></tr>
	</table>
</body>
</html>