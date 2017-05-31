 <%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8"
	charset="utf-8">
<head>
<%
	String basePath = request.getServletContext().getContextPath();
	String bite = (String) request.getAttribute("page");
%>
<script type="text/javascript"
	src="<%=basePath%>/jquery/jquery-3.1.1.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	var i =1;
	$("#add").click(function(){
		if(i==1)
 		$("#up").after("<tr id='up"+i+"'><td>上传文件:<input type='file' id='file"+i+"' name='file"+i+"' /></td> </tr>");
		else
			$("#up"+(i-1)).after("<tr id='up"+i+"'><td>上传文件:<input type='file' id='file"+i+"' name='file"+i+"' /></td> </tr>");	
	i++;
	});
});
</script>
<title>文件上传</title>
</head>
<body>
	<form action="<%=basePath%>/FileUpload" method="post"
		enctype="multipart/form-data">
		<table>
			<tr id="up">
				<td>上传文件:<input type="file" id="file" name="file" /></td>
			</tr>
			<tr>
				<td><input type="button" id="add" value="add"></td>
			</tr>
			<tr>
				<td><input type="submit" value="submit"></td>
			</tr>
		</table>
	</form>
</body>
</html>