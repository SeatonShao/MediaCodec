<!DOCTYPE html>
<html>
<head>
<%
    String basePath = request.getServletContext().getContextPath();
%>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="<%=basePath %>/css/video-js.min.css" rel="stylesheet">
<script src="<%=basePath %>/js/video.min.js"></script>
</head>
<body>
	<video id="really-cool-video" class="video-js vjs-default-skin"
		controls preload="auto" width="640" height="264"
		poster="really-cool-video-poster.jpg" data-setup='{}'>
		<source src="f:4666.flv/"
			type="video/flv">
		<p class="vjs-no-js">
			To view this video please enable JavaScript, and consider upgrading
			to a web browser that <a
				href="http://videojs.com/html5-video-support/" target="_blank">supports
				HTML5 video</a>
		</p>
	</video>
</body>
</html>