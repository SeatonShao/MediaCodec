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
<meta charset="UTF-8">
<title>hls</title>
<script type="text/javascript" src="<%=basePath %>/ckplayer/ckplayer.js"
	charset="utf-8"></script>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/clappr/latest/clappr.min.js"></script>
</head>
<body>

 <div id="player"></div>
  <script>
    var player = new Clappr.Player({source: "<%=basePath %>/HLS?abc=<%=timestamp%>%26%26path=<%=path%>", parentId: "#player"});
  </script>
</body>
</html>