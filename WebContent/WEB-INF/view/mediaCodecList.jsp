<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>播放列表</title>
<link rel="stylesheet" type="text/css"
	href="${ctx}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/easyui/demo/demo.css">
<script type="text/javascript" src="${ctx}/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/common.js"></script>
<script type="text/javascript" src="${ctx}/js/extJquery.js"></script>
<script type="text/javascript" src="${ctx}/easyui/jquery.easyui.min.js"></script>
</head>
<body>
	<div
		data-options="region:'north',border:false,title:'查询条件',iconCls:'icon-find'"
		style="height: 65px; overflow: hidden;">

		视频名称: <input class="easyui-validatebox" type="text" name="mediaName"
			id="mediaName" data-options="required:true"> <a href="#"
			class="easyui-linkbutton" id="codec_searchBtn" iconCls="icon-search">查找</a>
		<a href="#" class="easyui-linkbutton" id="codec_clearBtn"
			iconCls="icon-clear">清空</a>

	</div>
	<div data-options="region:'center',border:false">
		<table id="codec_dg" style="display: none;"></table>
	</div>
	<div id="codec_list_toolbar" style="display: none;"></div>
	<script type="text/javascript">
		//DataGrid字段设置
		var codec_list_datagrid_columns = [ [ {
			field : 'id',
			title : '编号',
			width : 150,
			checkbox : true,
			align : 'center'
		}, {
			field : 'name',
			title : '视频名称',
			width : 100,
			align : 'center'

		}, {
			field : 'contentType',
			title : '类型',
			width : 100,
			align : 'center'
		}, {
			field : 'status',
			title : '状态',
			width : 100,
			align : 'center'
		}, {
			field : 'weight',
			title : '权重',
			width : 100,
			align : 'center'
		}, {
			field : 'ip',
			title : 'ip',
			width : 100,
			align : 'center'
		}, {
			field : 'mediaLength',
			title : '文件大小',
			width : 100,
			align : 'center',
			formatter : function(value, row, index) {
				var str = parseInt(row.mediaLength);
				if (str / (1024 * 1024) > 1) {
					return (str / (1024 * 1024)).toFixed(2) + "M";
				} else if (str / (1024) > 1) {
					return (str / 1024).toFixed(2) + "k";
				} else {
					return str;
				}
			}
		}, {
			field : 'action',
			title : '操作',
			width : 160,
			align : 'center',
			formatter : codec_list_Formatter
		} ] ];
		//定义相关的操作按钮
		function codec_list_Formatter(value, row, index) {

			var str = '';
			str += formatString(
					'<a onclick="deleteOne(\'{0}\',\'{1}\',\'codec_dg\');">删除</a>',
					row.id, '${ctx}/c');
			str += '&nbsp;';
			str += '&nbsp;';
			str += '&nbsp;';
			
			str += formatString(
					'<a onclick="update(\'{0}\',\'{1}\',\'codec_dg\',{method:\'weightUp\',id:\''
							+ row.id + '\'});">提高优先级</a>',
					'${ctx}/MediaCodecList', 'one');
			return str;
		}

		$(function() {
			/** 初始化DataGrid,加载数据 **/
			$('#codec_dg').datagrid({
				url : '${ctx}/MediaCodecList',
				queryParams : {
					method : 'list'
				},
				height : 600,
				rownumbers : true,
				remoteSort : false,
				singleSelect : true,
				nowrap : false,
				fitColumns : true,
				idField : 'id',
				pagination : true,

				pageList : [ 5, 10, 15, 20, 30, 40, 50 ],
				columns : codec_list_datagrid_columns,

			}).datagrid("getPager").pagination({
				beforePageText : '第', //页数文本框前显示的汉字    
				afterPageText : '页/{pages}页',
				displayMsg : '共{total}条记录',
				onBeforeRefresh : function() {
					return true;
				}
			});
			$("#codec_searchBtn").click(function() {
				$('#codec_dg').datagrid("reload", {
					method : 'list',
					mediaName : $("#mediaName").val()
				});
			});
			$("#codec_clearBtn").click(function() {
				$("#mediaName").val("");
				$('#codec_dg').datagrid("reload", {
					method : 'list'
				});
			});

		});
	</script>
</body>
</html>