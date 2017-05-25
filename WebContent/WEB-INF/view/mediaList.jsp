<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<%
	String basePath = request.getServletContext().getContextPath();
String play = (String)request.getAttribute("play");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>播放列表</title>
<script type="text/javascript"
	src="<%=basePath%>/jquery/jquery-3.1.1.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
	$.post('<%=basePath%>/MediaList','',function(data){
			$.each(data,function(n,value){
				if(n%10==0){
					$("#media ").append("<tr><td><a href='<%=basePath%>/Dispatcher?path="+value.mediaName+"&play=<%=play%>'>"+value.name+value.contentType+"</a></td></tr>");
				}
				else{
					$("#media tr").children().css("border-bottom", "1px double red");
					$("#media tr").last().append("<td width=\"10%\"><a href='<%=basePath%>/Dispatcher?path="
											+ value.mediaName + "&play=<%=play%>'>" + value.name
											+ value.contentType + "</a></td>");
						}
					});
				}, 'json');
			});
</script>
</head>
<body>
	<table id="media" border="0">
	</table>
</body>
</html>