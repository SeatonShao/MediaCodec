<%@page import="java.math.BigDecimal"%>
<%@page import="com.infotop.common.HdfsStatus"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/taglibs.jsp"%>
<%
	request.setCharacterEncoding("utf-8");
	BigDecimal dfsRemain = new BigDecimal(HdfsStatus.getDFSRemaining()).divide(new BigDecimal(1024 * 1024 * 1024), 2,
		BigDecimal.ROUND_HALF_UP);
	BigDecimal datanode = new BigDecimal(HdfsStatus.getDataNodeCapacity()).divide(new BigDecimal(1024 * 1024 * 1024), 2,BigDecimal.ROUND_HALF_UP);
%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配置列表</title>
<link rel="stylesheet" type="text/css"
	href="${ctx}/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/easyui/demo/demo.css">
	<style type="text/css">
input{width:100px }
.biao_bt3{background:#deeef6;text-align:center;width:15%;color:#004c7c; height:39px;width:100px}
</style>
<script type="text/javascript" src="${ctx}/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/common.js"></script>
<script type="text/javascript" src="${ctx}/js/extJquery.js"></script>
<script type="text/javascript" src="${ctx}/easyui/jquery.easyui.min.js"></script>
</head>
<body>
	<div
		data-options="region:'north',border:false,title:'查询条件',iconCls:'icon-find'"
		style="height: 65px; overflow: hidden;">
		tomcat名称: <input class="easyui-validatebox" type="text"
			name="mediaName" id="mediaName" data-options="required:true">
		<a href="#" class="easyui-linkbutton" id="config_searchBtn"
			iconCls="icon-search">查找</a> <a href="#" class="easyui-linkbutton"
			id="config_clearBtn" iconCls="icon-clear">清空</a>

	</div>
	<div data-options="region:'center',border:false">
		<table id="config_dg" style="display: none;"></table>
	</div>
	<div id="config_tb" style="height: auto">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-add',plain:true" onclick="append()">添加</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-remove',plain:true" onclick="removeit()">删除</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-save',plain:true" onclick="accept()">保存修改</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-undo',plain:true" onclick="reject()">撤销</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-search',plain:true"
			onclick="getChanges()">当前增减条数</a>
	</div>
	<div id="win"></div>
	<script type="text/javascript">
	var config_list_datagrid;
		var editIndex = undefined;
		//DataGrid字段设置
		var config_list_datagrid_columns = [ [ {
			field : 'id',
			title : '编号',
			width : 150,
			checkbox : true,
			align : 'center'
		}, {
			field : 'virtualName',
			title : 'tomcat',
			editor : 'textbox',
			width : 100,
			align : 'center'

		}, {
			field : 'host',
			title : 'host',
			width : 100,
			align : 'center',
			formatter : function(value, row, index) {
				return row.ip + ':' + row.port;
			}
		}, {
			field : 'rows',
			title : '起始转码序号',
			editor : 'textbox',
			width : 100,
			align : 'center'
		}, {
			field : 'fgSleep',
			title : '转码时序',
			editor : 'textbox',
			width : 100,
			align : 'center'
		}, {
			field : 'sort',
			title : '转码排序',
			width : 100,
			editor : 'textbox',
			align : 'center'
		}, {
			field : 'fgBiteMin',
			title : '流畅',
			width : 100,
			editor : 'numberbox',
			align : 'center'
		}, {
			field : 'fgBiteMid',
			title : '标清',
			width : 100,
			editor : 'numberbox',
			align : 'center'
		}, {
			field : 'fgBiteMax',
			title : '高清',
			width : 100,
			editor : 'numberbox',
			align : 'center'
		}, {
			field : 'fgBiteSup',
			title : '超清',
			width : 100,
			editor :  'numberbox',
			align : 'center'
		}, {
			field : 'fgBt',
			title : '比特率',
			width : 100,
			editor : 'textbox',
			align : 'center'
		}, {
			field : 'fgThread',
			title : '线程',
			width : 100,
			editor : 'numberbox',
			align : 'center'
		}, {
			field : 'codec_on',
			title : '转码',
			width : 100,
			editor : 'textbox',
			align : 'center',
                        formatter:function(value, row, index){
                            if(value=='1'){
                                return '已开启<input type="button" onclick="codec(\''+row.id+'\',\'0\');" value="点击关闭"/>';
                            }else{
                                return '已关闭<input type="button" onclick="codec(\''+row.id+'\',\'0\');" value="点击开启"/>';
                            }
                        }
		}, {
			field : 'action',
			title : '操作',
			width : 160,
			align : 'center',
			formatter : config_list_Formatter
		} ] ];
		function config_list_Formatter(value, row, index) {
			var str = '';
			str += '<a onclick="openWindow(\''+row.id+'\');">修改</a>';
			str += '&nbsp;';
			return str;
		}
		
		function endEditing() {
			if (editIndex == undefined) {
				return true
			}
			if ($('#config_dg').datagrid('validateRow', editIndex)) {
				var ed = $('#config_dg').datagrid('getEditor', {
					index : editIndex
				});
				$('#config_dg').datagrid('endEdit', editIndex);
				editIndex = undefined;
				return true;
			} else {
				return false;
			}
		}
		function onClickRow(index) {
			if (editIndex != index) {
				if (endEditing()) {
					$('#config_dg').datagrid('selectRow', index).datagrid(
							'beginEdit', index);
					editIndex = index;

				} else {
					$('#config_dg').datagrid('selectRow', editIndex);
				}
			}
		}
		function append() {
			endEditing();
			$("#win").window('refresh');
			$("#win").window('open');
			
		}
		function openWindow(index){
			editIndex =index;
			$("#win").window('refresh');
			$("#win").window('open');
		}
		function removeit() {
			if (editIndex == undefined) {
				return
			}
			$('#config_dg').datagrid('cancelEdit', editIndex).datagrid(
					'deleteRow', editIndex);
			deleteOne(editIndex, '${ctx}/ConfigList', 'config_dg');
			editIndex = undefined;
		}
		function accept() {
			if (endEditing()) {
				$('#config_dg').datagrid('acceptChanges');
			}
		}
		function reject() {
			$('#config_dg').datagrid('rejectChanges');
			editIndex = undefined;
		}
		function getChanges() {
			var rows = $('#config_dg').datagrid('getChanges');
			alert(rows.length + ' rows are changed!');
		}
                function codec(value, on){
                   
                    $.post('${ctx}/ConfigList',{method:'codec',id:on},function(data){
                        
                        
                    },'json');
                        
                }   
		$(function() {
			/** 初始化DataGrid,加载数据 **/
			config_list_datagrid = $('#config_dg').datagrid({
				url : '${ctx}/ConfigList',
				queryParams : {
					method : 'list'
				},
				title : '配置信息,节点最小剩余:'+<%=dfsRemain%>+"G"+"，总剩余："+<%=datanode%>+"",
				iconCls : 'icon-edit',
				toolbar : '#config_tb',
				rownumbers : true,
				remoteSort : false,
				singleSelect : true,
				nowrap : false,
				fitColumns : true,
				idField : 'id',
				pagination : true,
				onDblClickRow : onClickRow,
				pageList : [ 5, 10, 15, 20, 30, 40, 50 ],
				columns : config_list_datagrid_columns,
				onAfterEdit : function(rowIndex, rowData, changes) {
					rowData.method = 'update';
					//endEdit该方法触发此事件
					update("${ctx}/ConfigList", "one", "config_dg", rowData);
					console.info(rowData);
					editRow = undefined;
				}
			}).datagrid("getPager").pagination({
				beforePageText : '第', //页数文本框前显示的汉字    
				afterPageText : '页/{pages}页',
				displayMsg : '共{total}条记录',
				onBeforeRefresh : function() {
					return true;
				}
			});
			$("#config_searchBtn").click(function() {
				$('#config_dg').datagrid("reload", {
					method : 'list',
					mediaName : $("#mediaName").val()
				});
			});
			$("#config_clearBtn").click(function() {
				$("#mediaName").val("");
				$('#config_dg').datagrid("reload", {
					method : 'list'
				});
			});
                       
			$('#win').window({
				title:'配置信息',
			    width:800,
			    height:600,
			    modal:true,
			    closed:true,
			    href:'${ctx}/Dispatcher?page=configForm',
			    onLoad:function(){
			    	if (editIndex == undefined) {
			    		return true;
					}else{
						$.post('${ctx}/ConfigList',{method:'getConfig',id:editIndex},function(data){
							if (data.success) {
								console.info(data);
								for(var item in data.mediaConfig){
									$("#"+item).val(data.mediaConfig[item]);
									if($("#"+item).attr("type")=="radio"&&1==data.mediaConfig[item]){
										
										$("#"+item+"[value='1']").attr("checked","checked");
									}
								}
							} else {
								$.messager.alert('错误', data.message, 'error');
							}
						},'json');
					}
			    }
			}) ;
		});
	</script>
</body>
</html>