<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>
	<!DOCTYPE html>
<html>
<head>
<%
String basePath = request.getServletContext().getContextPath();
String pic = (String)request.getAttribute("pic");
//String path = StringUtil.mediaPathEncode("1476758893565/1476758893565.m3u8");
String path = (String)request.getAttribute("path");//"biteMin/ed6b28b8-f418-411b-b33f-19f4260aa61e/biteMined6b28b8-f418-411b-b33f-19f4260aa61e.m3u8";
long timestamp = System.currentTimeMillis()%1000000;
System.out.println(timestamp);
%>
<title>Insert title here</title>
</head>
<body>
	<!--HLSPlayer代码开始-->
<div class="video" id="HLSPlayer" >
<script type="text/javascript" >
<!--
/*
* HLSPlayer参数应用=======================
* @param {Object} vID        ID
* @param {Object} vWidth     播放器宽度设置
* @param {Object} vHeight    播放器宽度设置
* @param {Object} vPlayer    播放器文件
* @param {Object} vHLSset    HLS配置
* @param {Object} vPic       视频缩略图
* @param {Object} vCssurl    移动端CSS应用文件
* HLSPlayer参数应用=======================
* 提示1：本实例请在IIS/Apache等网站环境下测试
* 提示2：本实例仅支持hls(m3u8)/不支持flv,mp4,f4v
*/
var vID        = "";
var vWidth     = 600;                //播放器宽度设置
var vHeight    = 400;                   //播放器宽度设置
var vPlayer    = "<%=basePath %>/HLSPlayer/HLSPlayer.swf?v=1.5"; //播放器文件
var vHLSset    = "<%=basePath %>/HLSPlayer/HLS.swf";             //HLS配置
var vPic       = "<%=basePath %>/Picture%3fpath=<%=pic%>";    //视频缩略图
var vCssurl    = "<%=basePath %>/HLSPlayer/images/mini.css";     //移动端CSS应用文件

//HLS(m3u8)地址,适配PC,安卓,iOS,WP
var vHLSurl    = "<%=basePath %>/HLS?fpath=<%=path%>";
//-->
</script>
<script type="text/javascript" src="<%=basePath %>/HLSPlayer/js/hls.min.js?rand=3396fsa778"></script>
</div>
<!--HLSPlayer代码结束-->
</body>
</html>