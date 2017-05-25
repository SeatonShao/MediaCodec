<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
<%
 
String basePath = request.getServletContext().getContextPath();
String pic = "037fb825b93f41d887db8647bb8cfe07";//(String)request.getAttribute("pic");
//String path = StringUtil.mediaPathEncode("1476758893565/1476758893565.m3u8");
String path = (String)request.getAttribute("path");//"biteMin/ed6b28b8-f418-411b-b33f-19f4260aa61e/biteMined6b28b8-f418-411b-b33f-19f4260aa61e.m3u8";
long timestamp = System.currentTimeMillis()%1000000;
System.out.println(timestamp);
%>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Cache-Control" content="no-store"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<title>Insert title here</title>
<link href="<%=basePath %>/js/video-js.css" rel="stylesheet">
<script src="<%=basePath %>/js/video.js"></script>

<script src="<%=basePath %>/js/ie8/videojs-ie8.min.js"></script>
<script src="<%=basePath %>/js/videojs-contrib-hls.min.js"></script>
<script src="<%=basePath %>/js/videojs-contrib-media-sources.min.js"></script>
</head>
<body>
	<video id="really-cool-video" class="video-js vjs-default-skin  vjs-big-play-centered"
		controls preload="auto" width="720" height="540" poster="<%=basePath %>/Picture?path=<%=path%>"
		data-setup='{}'>
		<source src="<%=basePath %>/biteMin<%=path %>.m3u8" type="application/x-mpegURL" >
		<p class="vjs-no-js">
			To view this video please enable JavaScript, and consider upgrading
			to a web browser that <a
				href="http://videojs.com/html5-video-support/" target="_blank">supports
				HTML5 video</a>
		</p>
	</video>
	<script>
	 var options = {
		        width: 720,
		        height:540,
		        hls: {
				    withCredentials: true
				  }
		    }
	 var player = videojs('really-cool-video',options);
	 player.on(['loadstart', 'play', 'playing', 'firstplay', 'pause', 'ended', 'adplay', 'adplaying', 'adfirstplay', 'adpause', 'adended', 'contentplay', 'contentplaying', 'contentfirstplay', 'contentpause', 'contentended', 'contentupdate'], function(e) {
	        console.warn('VIDEOJS player event: ',  e.type);
	 });
	 
	 player.play();
</script>
</body>
</html>