<!DOCTYPE html>
<html>
<head>
<%
 
    String basePath = request.getServletContext().getContextPath();
String path =  "1476069351474.flv" ;
long timestamp = 
System.currentTimeMillis()%1000000;
%>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="<%=basePath %>/ckplayer/ckplayer.js"
	charset="utf-8"></script>
</head>
<body>

	<div id="a1"></div>

	<script type="text/javascript">
    var flashvars={ 
        f:'f:/4666.flv' ,
        c:0 
    };
    var params={bgcolor:'#FFF',allowFullScreen:true,allowScriptAccess:'always',wmode:'transparent'};
    CKobject.embedSWF('<%=basePath %>/ckplayer/ckplayer.swf','a1','ckplayer_a1','600','400',flashvars,params);
</script>
</body>
</html>