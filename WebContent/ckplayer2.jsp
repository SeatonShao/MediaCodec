<!DOCTYPE html>
<html>
<head>
<%
    String basePath = request.getServletContext().getContextPath();
%>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="<%=basePath %>/ckplayer/ckplayer.js"
	charset="utf-8"></script>
</head>
<body>
	<div id="a1"></div>
	<script type="text/javascript" src="<%=basePath %>/ckplayer/ckplayer.js"
		charset="utf-8"></script>
	<script type="text/javascript">
    var flashvars={
    	//f:'http://192.168.10.231:50070/webhdfs/v1/5fc5f3937bd04e8ca5a3d32a520f4d83/biteMin/biteMin5fc5f3937bd04e8ca5a3d32a520f4d830000.ts?op=OPEN',
        f:'<%=basePath%>/4666.flv',
        c:0,
        quality:'low',
        p:2
    };
    var params={bgcolor:'#FFF',allowFullScreen:true,allowScriptAccess:'always',wmode:'transparent'};
    var video=['4666.flv'];
    CKobject.embedSWF('<%=basePath%>/ckplayer/ckplayer.swf','a1','ckplayer_a','600','400',flashvars,params);
</script>
</body>
</html>