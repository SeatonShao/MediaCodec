<%@page  language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<html>
<head>
<%
    String basePath = request.getServletContext().getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html" charset="utf-8">
<title>文件上传</title>
<script src="/jquery/jquery-3.1.1.js"></script>

</head>
<body>

	<form action="<%=basePath %>/FileUpload?bite=biteMin" method="post"
		enctype="multipart/form-data">
		<table>
			<tr>
				<td>上传文件:</td>
				<td><input type="file" id="file" name="file" /></td>
				<td><input type="submit" value="submit"></td>
			</tr>
		</table>
	</form>
</body>
</html>