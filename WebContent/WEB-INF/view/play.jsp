<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>
	<!DOCTYPE html>
<html>
<head>
<%
 
    String basePath = request.getServletContext().getContextPath();
//String path = StringUtil.mediaPathEncode("1476758893565/1476758893565.m3u8");
String path =  (String)request.getAttribute("path");
//"biteMin/ed6b28b8-f418-411b-b33f-19f4260aa61e/biteMined6b28b8-f418-411b-b33f-19f4260aa61e.m3u8";
long timestamp = System.currentTimeMillis()%1000000;
System.out.println(timestamp+path);
%>
<meta charset="UTF-8">
<title>hls</title>
<script type="text/javascript" src="<%=basePath %>/ckplayer/ckplayer.js"
	charset="utf-8"></script>
</head>
<body>
	<div id="a1"></div>
	<script type="text/javascript">
function getOsSrc()  
{  
    var OsObject = "";  
   if(navigator.userAgent.indexOf("MSIE")>0) {
        return '<%=basePath %>/HLS?abc=<%=timestamp%>%26%26path=<%=path%>';
        
   }  
   if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){  
        return '<%=basePath %>/HLS?abc=<%=timestamp%>&&path=<%=path%>'; 
   }  
   if(isSafari=navigator.userAgent.indexOf("Chrome")>0) {  
        return '<%=basePath %>/HLS?abc=<%=timestamp%>%26%26path=<%=path%>';
   }   
   
}  
var src = '<%=basePath %>/HLS?abc=<%=timestamp%>%26%26path=<%=path%>';
var flashvars={
		f:'<%=basePath %>/ckplayer/m3u8.swf',
		a:getOsSrc(),
		s:4,
		c:0,
		lv:0,
		i:'<%=basePath %>/Picture%3fpath=<%=path%>'
};
	///var params={bgcolor:'#FFF',allowFullScreen:true,allowScriptAccess:'always',wmode:'transparent'};
	var video=[getOsSrc()];
	CKobject.embed('<%=basePath %>/ckplayer/ckplayer.swf','a1','ckplayer_a1','600','400',false,flashvars,video);
</script>
</body>
</html>