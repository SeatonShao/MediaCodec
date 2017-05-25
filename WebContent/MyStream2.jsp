<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
<%
 
    String basePath = request.getServletContext().getContextPath();
//String path = StringUtil.mediaPathEncode("1476758893565/1476758893565.m3u8");
String path = (String)request.getAttribute("path");//"biteMin/ed6b28b8-f418-411b-b33f-19f4260aa61e/biteMined6b28b8-f418-411b-b33f-19f4260aa61e.m3u8";
long timestamp = System.currentTimeMillis()%1000000;
System.out.println(timestamp);
%>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<title>Insert title here</title>
<link href="<%=basePath %>/css/video-js.css" rel="stylesheet">
<script src="<%=basePath %>/js/video.js"></script>
<script>
     videojs.options.flash.swf = "<%=basePath %>/video-js.swf"; 
  </script>
</head>
<body>
	<video id="really-cool-video" class="video-js vjs-default-skin"
		controls preload="auto" width="640" height="264" poster="<%=basePath %>/Picture?fpath=<%=path%>"
		data-setup='{}'>
		<source src="<%=basePath %>/HLS?abc=<%=timestamp%>%26%26fpath=<%=path%>" type="video/mp4">
		<p class="vjs-no-js">
			To view this video please enable JavaScript, and consider upgrading
			to a web browser that <a
				href="http://videojs.com/html5-video-support/" target="_blank">supports
				HTML5 video</a>
		</p>
	</video>
</body>
</html>