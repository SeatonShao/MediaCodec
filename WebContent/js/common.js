/*
 * EasyUI 常用工具类，包含了增、删、改、查等常用的通用功能
 */


/**
 * 打开实体表单页面 包括：新增和修改
 * @param url
 *            表单初始化地址
 * @param listDatagrid
 *            表单提交后待刷新的表格
 * @param params
 *            其他参数如：title 弹出窗口标题，width 弹出窗口宽度，height弹出窗口高度
 */
function updateForm(url, formId, listDatagrid, params) {
	
	var opts = {
		width : 1000,
		height : 600,
		title : '信息',
		href : url,
		iconCls : 'icon-application_form_add',
		buttons : [
				{
					text : '保存',
					iconCls : 'icon-save',
					id : 'formSaveBtn',
					handler : function() {
						alert('helo');
						parent.$.modalDialog.openner_dataGrid =  listDatagrid ;
						var inputForm = parent.$.modalDialog.handler.find('#'
								+ formId);
						var isValid = inputForm.form('validate');
						if (isValid) {
							var file_upload = parent.$.modalDialog.handler
									.find('#file_upload');
							if (file_upload.length > 0) {
								var swfuploadify = file_upload
										.data('uploadify');
								if (swfuploadify.queueData.queueLength > 0) {
									$("#formSaveBtn").linkbutton('disable');
									$("#formCancelBtn").linkbutton('disable');
									file_upload.uploadify('upload', '*');
								} else {
									inputForm.submit();
								}
							} else {
								inputForm.submit();
							}
						}
					}
				}, {
					text : '取消',
					id : 'formCancelBtn',
					iconCls : 'icon-cross',
					handler : function() {
						parent.$.modalDialog.handler.dialog('close');
					}
				} ]
	};
	$.extend(opts, params);
	parent.$.modalDialog(opts);
}
/**
 * 打开实体表单页面 包括：新增和修改 wx
 * @param url
 *            表单初始化地址
 * @param listDatagrid
 *            表单提交后待刷新的表格
 * @param params
 *            其他参数如：title 弹出窗口标题，width 弹出窗口宽度，height弹出窗口高度
 */
function updateFormWx(url, formId, listDatagrid, params) {
	var opts = {
			width : 800,
			height : 360,
			title : '信息',
			href : url,
			iconCls : 'icon-application_form_add',
			buttons : [
			           {
			        	   text : '保存',
			        	   iconCls : 'icon-save',
			        	   id : 'formSaveBtn',
			        	   handler : function() {
			        		   parent.$.modalDialog.openner_dataGrid = listDatagrid;
			        		   var inputForm = parent.$.modalDialog.handler.find('#'
			        				   + formId);
			        		   var isValid = inputForm.form('validate');
			        		   if (isValid) {
			        			   var file_upload = parent.$.modalDialog.handler
			        			   .find('#file_upload');
			        			   if (file_upload.length > 0) {
			        				   var swfuploadify = file_upload
			        				   .data('uploadify');
			        				   if (swfuploadify.queueData.queueLength > 0) {
			        					   $("#formSaveBtn").linkbutton('disable');
			        					   $("#formCancelBtn").linkbutton('disable');
			        					   file_upload.uploadify('upload', '*');
			        				   } else {
			        					   inputForm.submit();
			        				   }
			        			   } else {
			        				   inputForm.submit();
			        			   }
			        		   }
			        	   }
			           }, {
			        	   text : '取消',
			        	   id : 'formCancelBtn',
			        	   iconCls : 'icon-cross',
			        	   handler : function() {
			        		   parent.$.modalDialog.handler.dialog('close');
			        	   }
			           } ]
	};
	$.extend(opts, params);
	parent.$.modalDialog(opts);
}
function takephoto(url){
	var opts = {
			width : 900,
			height : 700,
			title : '图像获取',
			href : url,
			iconCls : 'icon-application_form_add',
			buttons : [
					/* {
						text : '保存',
						iconCls : 'icon-save',
						id : 'formSaveBtn',
						handler : function() {
							checkpic();
							parent.$.childModalDialog.handler.dialog('close');
						}
					}, */
					{
						text : '关闭',
						id : 'formCancelBtn',
						iconCls : 'icon-cross',
						handler : function() {
							parent.$.childModalDialog.handler.dialog('close');
						}
					} ]
		};
		parent.$.childModalDialog(opts);
}
function applyForm(url, formId, listDatagrid, params) {
	var opts = {
		width : 600,
		height : 400,
		title : '信息',
		href : url,
		iconCls : 'icon-application_form_add',
		buttons : [
				{
					text : '提出申请',
					iconCls : 'icon-save',
					id : 'formSaveBtn',
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = listDatagrid;
						var inputForm = parent.$.modalDialog.handler.find('#'
								+ formId);
						var isValid = inputForm.form('validate');
						if (isValid) {
							var file_upload = parent.$.modalDialog.handler
									.find('#file_upload');
							if (file_upload.length > 0) {
								var swfuploadify = file_upload
										.data('uploadify');
								if (swfuploadify.queueData.queueLength > 0) {
									$("#formSaveBtn").linkbutton('disable');
									$("#formCancelBtn").linkbutton('disable');
									file_upload.uploadify('upload', '*');
								} else {
									inputForm.submit();
								}
							} else {
								inputForm.submit();
							}
						}
					}
				}, {
					text : '取消',
					id : 'formCancelBtn',
					iconCls : 'icon-cross',
					handler : function() {
						parent.$.modalDialog.handler.dialog('close');
					}
				} ]
	};
	$.extend(opts, params);
	parent.$.modalDialog(opts);
}
/**
 * 详细页面
 * 
 * @param url
 *            查看链接
 * @param params
 */
function view(url, params) {
	var opts = {
		width : 900,
		height : 600,
		title : '详细信息',
		href : url,
		iconCls : 'icon-application_form_magnify',
		buttons : [ {
			text : '关闭',
			iconCls : 'icon-cross',
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	};
	$.extend(opts, params);
	parent.$.modalDialog(opts);
}
/**
 * 详细页面
 * 
 * @param url wx
 *            查看链接
 * @param params
 */
function view2(url, params) {
	var opts = {
			width : 800,
			height : 360,
			title : '详细信息',
			href : url,
			iconCls : 'icon-application_form_magnify',
			buttons : [ {
				text : '关闭',
				iconCls : 'icon-cross',
				handler : function() {
					parent.$.modalDialog.handler.dialog('close');
				}
			} ]
	};
	$.extend(opts, params);
	parent.$.modalDialog(opts);
}

/**
 * 详细页面   用于第二个窗口弹出
 * 
 * @param url
 *            查看链接
 * @param params
 */
function viewChild(url, params) {
	var opts = {
		width : 900,
		height : 600,
		title : '详细信息',
		href : url,
		iconCls : 'icon-application_form_magnify',
		buttons : [ {
			text : '关闭',
			iconCls : 'icon-cross',
			handler : function() {
				parent.$.childModalDialog.handler.dialog('close');
			}
		} ]
	};
	$.extend(opts, params);
	parent.$.childModalDialog(opts);
}

/**
 * 删除单条记录
 * @param id
 * @param deleteUrl
 * @param listDatagrid
 */
function deleteOne(id, deleteUrl, listDatagrid) {
	parent.$.messager.confirm('确认', '是否确定删除该记录?', function(r) {
		if (r) {
			$.post(deleteUrl, {
				id: id,
				method:'delete'
			}, function(data) {
				
				if (data) {
					
					$("#"+listDatagrid).datagrid('reload'); // reload the user
					// data
					$.messager.show({ // show error message
						title : '提示',
						msg : data.message
					});
				} else {
					$.messager.alert('错误', data.message, 'error');
				}
			}, 'JSON');
		}
	});
}

/**
 * 解除单条记录结对
 * @param id
 * @param deleteUrl
 * @param listDatagrid
 */
function deletePairOne(id, deleteUrl, listDatagrid) {
	parent.$.messager.confirm('确认', '是否确定解除该结对?', function(r) {
		if (r) {
			$.post(deleteUrl, {
				ids : id
			}, function(data) {
				if (data.success) {
					listDatagrid.datagrid('reload'); // reload the user
					// data
					$.messager.show({ // show error message
						title : '提示',
						msg : data.message
					});
				} else {
					$.messager.alert('错误', data.message, 'error');
				}
			}, 'JSON');
		}
	});
}
/**
 * 整村推进、产业发展解除单条记录结对
 * @param id
 * @param deleteUrl
 * @param listDatagrid
 */
function deletePairOneByxmzj(id, deleteUrl, listDatagrid,mainDatagrid) {
	parent.$.messager.confirm('确认', '是否确定解除该结对?', function(r) {
		if (r) {
			$.post(deleteUrl, {
				ids : id
			}, function(data) {
				if (data.success) {
					listDatagrid.datagrid('reload');
					mainDatagrid.datagrid('reload');
					// reload the user
					// data
					$.messager.show({ // show error message
						title : '提示',
						msg : data.message
					});
				} else {
					$.messager.alert('错误', data.message, 'error');
				}
			}, 'JSON');
		}
	});
}
/**
 * 设为已办事项（代办员档案管理系统待办事项模块）
 * @param id
 * @param isCompleteUrl
 * @param listDatagrid
 */
function isComplete(id, isCompleteUrl, listDatagrid) {
	parent.$.messager.confirm('确认', '是否确定此事项已办理完成?', function(r) {
		if (r) {
			$.post(isCompleteUrl, {
				id : id
			}, function(data) {
				if (data.success) {
					listDatagrid.datagrid('reload'); // reload the user
					// data
					$.messager.show({ // show error message
						title : '提示',
						msg : data.message
					});
				} else {
					$.messager.alert('错误', data.message, 'error');
				}
			}, 'JSON');
		}
	});
}



/**
 * 批量删除
 * @param deleteUrl
 * @param listDatagrid
 */
function deleteBatch(deleteUrl, listDatagrid) {
	var rows = listDatagrid.datagrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		parent.$.messager.confirm('确认', '您是否要删除当前选中的项目？', function(r) {
			if (r) {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				for (var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				var idParam = decodeURIComponent($.param({
					ids : ids
				}, true));
				$.post(deleteUrl, idParam, function(data) {
					if (data.success) {
						listDatagrid.datagrid('reload');
						listDatagrid.datagrid('uncheckAll').datagrid(
								'unselectAll').datagrid('clearSelections');
						$.messager.show({ // messager信息提示
							title : '提示',
							msg : data.message
						});
					} else {
						$.messager.alert('错误', data.message, 'error');
					}
					parent.$.messager.progress('close');
				}, 'JSON');

			}
		});
	} else {
		$.messager.show({
			title : '提示',
			msg : '请勾选要删除的记录！'
		});
	}
}

/**
 * 批量解除结对
 * @param deleteUrl
 * @param listDatagrid
 */
function deletePairBatch(deleteUrl, listDatagrid) {
	var rows = listDatagrid.datagrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		parent.$.messager.confirm('确认', '您是否要解除当前选中的结对项目？', function(r) {
			if (r) {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				for (var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				var idParam = decodeURIComponent($.param({
					ids : ids
				}, true));
				$.post(deleteUrl, idParam, function(data) {
					if (data.success) {
						listDatagrid.datagrid('reload');
						listDatagrid.datagrid('uncheckAll').datagrid(
								'unselectAll').datagrid('clearSelections');
						$.messager.show({ // messager信息提示
							title : '提示',
							msg : data.message
						});
					} else {
						$.messager.alert('错误', data.message, 'error');
					}
					parent.$.messager.progress('close');
				}, 'JSON');

			}
		});
	} else {
		$.messager.show({
			title : '提示',
			msg : '请勾选要解除结对的记录！'
		});
	}
}
/**
 * 整村推进、产业发展批量解除结对
 * @param deleteUrl
 * @param listDatagrid
 */
function deletePairBatchByxmzj(deleteUrl, listDatagrid,mainDatagrid) {
	var rows = listDatagrid.datagrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		parent.$.messager.confirm('确认', '您是否要解除当前选中的结对项目？', function(r) {
			if (r) {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				for (var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				var idParam = decodeURIComponent($.param({
					ids : ids
				}, true));
				$.post(deleteUrl, idParam, function(data) {
					if (data.success) {
						listDatagrid.datagrid('reload');
						mainDatagrid.datagrid('reload');
						listDatagrid.datagrid('uncheckAll').datagrid(
								'unselectAll').datagrid('clearSelections');
						$.messager.show({ // messager信息提示
							title : '提示',
							msg : data.message
						});
					} else {
						$.messager.alert('错误', data.message, 'error');
					}
					parent.$.messager.progress('close');
				}, 'JSON');

			}
		});
	} else {
		$.messager.show({
			title : '提示',
			msg : '请勾选要解除结对的记录！'
		});
	}
}
/**
 * 批量审核
 * @param auditBatchUrl
 * @param listDatagrid
 */
function auditBatch(auditBatchUrl, listDatagrid) {
	var rows = listDatagrid.datagrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		parent.$.messager.confirm('确认', '您是否要审核当前选中的项目？', function(r) {
			if (r) {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				for (var i = 0; i < rows.length; i++) {
					if(rows[i].auditStatus!='1'){//只加入待审核的记录
						ids.push(rows[i].id);
					}
				}
				var idParam = decodeURIComponent($.param({
					ids : ids
				}, true));
				$.post(auditBatchUrl, idParam, function(data) {
					if (data.success) {
						listDatagrid.datagrid('reload');
						listDatagrid.datagrid('uncheckAll').datagrid(
								'unselectAll').datagrid('clearSelections');
						$.messager.show({ // messager信息提示
							title : '提示',
							msg : data.message
						});
					} else {
						$.messager.alert('错误', data.message, 'error');
					}
					parent.$.messager.progress('close');
				}, 'JSON');

			}
		});
	} else {
		$.messager.show({
			title : '提示',
			msg : '请勾选要审核的记录！'
		});
	}
}

/**
 * 批量启用/禁用
 * @param deleteUrl
 * @param listDatagrid
 */
function enableBatch(ableUrl, listDatagrid, optionStr) {
	var rows = listDatagrid.datagrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		parent.$.messager.confirm('确认', '您是否要'+optionStr+'当前选中的项目？', function(r) {
			if (r) {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				for (var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				var idParam = decodeURIComponent($.param({
					ids : ids
				}, true));
				$.post(ableUrl, idParam, function(data) {
					if (data.success) {
						listDatagrid.datagrid('reload');
						listDatagrid.datagrid('uncheckAll').datagrid(
								'unselectAll').datagrid('clearSelections');
						$.messager.show({ // messager信息提示
							title : '提示',
							msg : data.message
						});
					} else {
						$.messager.alert('错误', data.message, 'error');
					}
					parent.$.messager.progress('close');
				}, 'JSON');

			}
		});
	} else {
		$.messager.show({
			title : '提示',
			msg : '请勾选要'+optionStr+'的记录！'
		});
	}
}

/**
 * TreeGrid 删除单条记录
 * @param id
 * @param deleteUrl
 * @param listDatagrid
 */
function treegridDeleteOne(id, deleteUrl, listDatagrid) {
	parent.$.messager.confirm('确认', '是否确定删除该记录?', function(r) {
		if (r) {
			$.post(deleteUrl, {
				ids : id
			}, function(data) {
				if (data.success) {
					listDatagrid.treegrid('reload'); // reload the user data
					$.messager.show({ // show error message
						title : '提示',
						msg : data.message
					});
				} else {
					$.messager.alert('错误', data.message, 'error');
				}
			}, 'JSON');
		}
	});
}

/**
 * 更新修改操作，包含：上报，作废等直接更改信息的操作等。可根据不同需要进行扩展
 * @param url
 * @param type
 * @param listDatagrid
 * @param params
 */
function update(url,type,listDatagrid,params) {
	switch (type) {
	case 'one'://单条上报
		parent.$.messager.confirm('确认', '是否确定修改?', function(r) {
			if (r) {
				$.post(url, params, function(data) {
					if (data.success) {
						$("#"+listDatagrid).datagrid('reload'); // reload the user
						$.messager.show({ // show error message
							title : '提示',
							msg : data.message
						});
					} else {
						$.messager.alert('错误', data.message, 'error');
					}
				}, 'JSON');
			}
		});
		break;
	case 'batch'://批量上报
		var rows = listDatagrid.datagrid('getChecked');
		var ids = [];
		for (var i = 0; i < rows.length; i++) {
			ids.push(rows[i].id);
		}
		var idParam = decodeURIComponent($.param({
			id : ids
		}, true));
		$.extend(idParam, params);
		parent.$.messager.confirm('确认', '您是否要上报当前选中的记录？', function(r) {
			if (r) {
				$.post(reportUrl,idParam, function(data) {
					if (data.success) {
						listDatagrid.datagrid('reload');
						listDatagrid.datagrid('uncheckAll').datagrid(
								'unselectAll').datagrid('clearSelections');
						$.messager.show({ // messager信息提示
							title : '提示',
							msg : data.message
						});
					} else {
						$.messager.alert('错误', data.message, 'error');
					}
					parent.$.messager.progress('close');
				});
			}
		});
		break;
	case 'initialPwd':
		break;
	}

}
/**
 * datagrid 表格字段 formatter 开启关闭
 */
var onOff = function(value, row, index){
	if(value==1){
		return '开启';
	}else{
		return '关闭';
	}
}
/**
 * 查询表单按钮绑定
 * @param searchBtn：查询按钮ID
 * @param clearBtn：
 *            清空按钮ID
 * @param searchForm：查询Form
 *            ID
 * @return
 */
function bindSearchBtn(searchBtn, clearBtn, searchForm, listDatagrid) {
	searchBtn = $('#' + searchBtn).linkbutton({
		iconCls : 'icon-search'
	}).click(
			function() {
				listDatagrid.datagrid('load', infotop.serializeObject($('#'
						+ searchForm)));
			});
	clearBtn = $('#' + clearBtn).linkbutton({
		iconCls : 'icon-no'
	}).click(function() {
		$('#' + searchForm).form('clear');
	});
}

var isReportedFormatter = function(value, row, index) {
	return value == 1 ? '已上报' : '未上报';
};

var isLockFormatter = function(value, row, index) {
	return value == 1 ? '已锁定' : '未锁定';
};

var sexFormatter = function(value, row, index) {
	return value == 1 ? '男' : '女';
};

var isTrueFormatter = function(value, row, index) {
	return value == 1 ? '是' : '否';
};

function newTabView(type, params) {
	parent.$.messager.progress({
		title : '提示',
		text : '数据处理中，请稍后....'
	});
	var index_tabs = top.$('#index_tabs').tabs();
	var opts;
	if (type == '' || type == 'href') {
		opts = {
			title : params.title,
			closable : true,
			iconCls : params.iconCls,
			href : params.url,
			border : false,
			fit : true
		};
	} else {
		var iframe = '<iframe src="'
				+ params.url
				+ '" frameborder="0" style="border:0;width:100%;height:98%;"></iframe>';
		opts = {
			title : params.title,
			closable : true,
			iconCls : params.iconCls,
			content : iframe,
			border : false,
			fit : true
		};
	}
	if (index_tabs.tabs('exists', opts.title)) {
		index_tabs.tabs('select', opts.title);
		parent.$.messager.progress('close');
	} else {
		index_tabs.tabs('add', opts);
	}
}

/**
 * 
 * @param userType	用户类型
 * @param city		市区控件id
 * @param county	县区控件id
 * @param town		乡镇街道控件id
 * @param village	行政村控件id
 */
function showArea(userType,city,county,town,village){
	
	var $city = $("#" + city);
	var $county = $("#" + county);
	var $town = $("#" + town);
	var $village = $("#" + village);
	
	//$city.val("");
	$county.val("");
	//$county.empty();
	//$county.append("<option value=\"\">--请选择县区--</option>");
	
	$town.val("");
	$town.empty();
	$town.append("<option value=\"\">--请选择乡镇--</option>");
	
	$village.val("");
	$village.empty();
	$village.append("<option value=\"\">--请选择行政村--</option>");
	if(userType == '' || userType == null) {
		$city.hide();
		$county.hide();
		$town.hide();
		$village.hide();
	} else if(userType == '0') {
		$city.hide();
		$county.hide();
		$town.hide();
		$village.hide();
		
	} else if(userType == '1'){
		$city.show();
		$county.show();
		$town.show();
		$village.show();
	} else if(userType == '2'){
		$city.show();
		$county.show();
		$town.show();
		$village.show();
	} else if(userType == '3'){
		$city.show();
		$county.show();
		$town.show();
		$village.show();
	}
}


function changeCity(ctx,code,county,town,village) {//选择市区动态加载县区
	var ss = Math.random();
	$("#" + town).empty();
	$("#" + town).append("<option value=\"\">--请选择乡镇--</option>");
	$("#" + village).empty();
	$("#" + village).append("<option value=\"\">--请选择行政村--</option>");
	
	var option = "<option value=\"\">--请选择县区--</option>";
	if(code == '' || code == null || code == undefined){
		 option = "<option value=\"\">--请选择县区--</option>";
	} else {
		
		$.ajax({
			type:'GET',
			cache:'false',
			url: ctx + '/division/getSonByParentCode/' + ss,
			data:{
				code : code
			},
			async:false,
			success : function(msg){
				if(msg == '') {
					option = "<option value=\"\">--请选择县区--</option>";
				} else {
					if(typeof JSON == 'undefined') {
						$('head').append($("<script type='text/javascript' src='${ctx}/static/js/json2.js'"));
					}
					var data = JSON.parse(msg);
					$.each(data,function(k,v){
						option += "<option value=\"" + v['code'] + "\">"
						 + v['name']
						+ "</option>";
					});
				}
			},
			error : function(msg,textStatus,e) {
				option = "<option value=\"\">--请选择县区--</option>";
			}
		});
	}
	$("#" + county).empty();
	$("#" + county).append(option);
}


function changeCountyNoDetail(ctx,code,town,village) {//选择县区动态加载乡镇
	var ss = Math.random();
	if(village=='' || village==null || village==undefined){
		$("#" + village).empty();
		$("#" + village).append("<option value=\"\">--请选择行政村--</option>");
	}
	
	var option = "<option value=\"\">--请选择乡镇--</option>";
	if(code == '' || code == null || code == undefined){
		 option = "<option value=\"\">--请选择乡镇--</option>";
	} else {
		
		$.ajax({
			type:'GET',
			cache:'false',
			url: ctx + '/division/getSonByParentCode/' + ss,
			data:{
				code : code
			},
			async:false,
			success : function(msg){
				if(msg == '') {
					option = "<option value=\"\">--请选择乡镇--</option>";
				} else {
					if(typeof JSON == 'undefined') {
						$('head').append($("<script type='text/javascript' src='${ctx}/static/js/json2.js'"));
					}
					var data = JSON.parse(msg);
					$.each(data,function(k,v){
						option += "<option value=\"" + v['code'] + "\">"
						 + v['name']
						+ "</option>";
					});
				}
			},
			error : function(msg,textStatus,e) {
				option = "<option value=\"\">--请选择乡镇--</option>";
			}
		});
	}
	$("#" + town).empty();
	$("#" + town).append(option);
}
function changeTownNoDetail(ctx,code,village) {//选择乡镇动态加载行政村
	var ss = Math.random();
	
	var option = "<option value=\"\">--请选择行政村--</option>";
	if(code == '' || code == null || code == undefined){
		 option = "<option value=\"\">--请选择行政村--</option>";
	} else {
		
		$.ajax({
			type:'GET',
			cache:'false',
			url: ctx + '/division/getSonByParentCode/' + ss,
			data:{
				code : code
			},
			async:false,
			success : function(msg){
				if(msg == '') {
					option = "<option value=\"\">--请选择行政村--</option>";
				} else {
					if(typeof JSON == 'undefined') {
						$('head').append($("<script type='text/javascript' src='${ctx}/static/js/json2.js'"));
					}
					var data = JSON.parse(msg);
					$.each(data,function(k,v){
						option += "<option value=\"" + v['code'] + "\">"
						 + v['name']
						+ "</option>";
					});
				}
				
			},
			error : function(msg,textStatus,e) {
				option = "<option value=\"\">--请选择行政村--</option>";
			}
		});
	}
	$("#" + village).empty();
	$("#" + village).append(option);
}

/**
 * 所属单位-县区选择下拉
 * @param ctx
 * @param code
 * @param town
 * @param village
 */
function changeCountyUnit(ctx,code,town,village) {//选择县区动态加载乡镇
	var ss = Math.random();
	if(village=='' || village==null || village==undefined){
		$("#" + village).empty();
		$("#" + village).append("<option value=\"\">--请选择单位--</option>");
	}
	
	var option = "<option value=\"\">--请选择乡镇--</option>";
	if(code == '' || code == null || code == undefined){
		 option = "<option value=\"\">--请选择乡镇--</option>";
	} else {
		
		$.ajax({
			type:'GET',
			cache:'false',
			url: ctx + '/division/getSonAllByParentCode/' + ss,
			data:{
				code : code
			},
			async:false,
			success : function(msg){
				if(msg == '') {
					option = "<option value=\"\">--请选择乡镇--</option>";
				} else {
					if(typeof JSON == 'undefined') {
						$('head').append($("<script type='text/javascript' src='${ctx}/static/js/json2.js'"));
					}
					var data = JSON.parse(msg);
					$.each(data,function(k,v){
						option += "<option value=\"" + v['code'] + "\">"
						 + v['name']
						+ "</option>";
					});
				}
			},
			error : function(msg,textStatus,e) {
				option = "<option value=\"\">--请选择乡镇--</option>";
			}
		});
	}
	$("#" + town).empty();
	$("#" + town).append(option);
}

/**
 * 所属单位-乡镇选择下拉
 * @param ctx
 * @param code
 * @param village
 */
function changeTownUnit(ctx,code,village) {//选择乡镇动态加载行政村
	var ss = Math.random();
	
	var option = "<option value=\"\">--请选择单位--</option>";
	if(code == '' || code == null || code == undefined){
		 option = "<option value=\"\">--请选择单位--</option>";
	} else {
		
		$.ajax({
			type:'GET',
			cache:'false',
			url: ctx + '/division/getSonAllByParentCode/' + ss,
			data:{
				code : code
			},
			async:false,
			success : function(msg){
				if(msg == '') {
					option = "<option value=\"\">--请选择单位--</option>";
				} else {
					if(typeof JSON == 'undefined') {
						$('head').append($("<script type='text/javascript' src='${ctx}/static/js/json2.js'"));
					}
					var data = JSON.parse(msg);
					$.each(data,function(k,v){
						option += "<option value=\"" + v['code'] + "\">"
						 + v['name']
						+ "</option>";
					});
				}
				
			},
			error : function(msg,textStatus,e) {
				option = "<option value=\"\">--请选择单位--</option>";
			}
		});
	}
	$("#" + village).empty();
	$("#" + village).append(option);
}

/**
 * @Author lyj
 * @param ctx
 * @param code
 * @param village
 * @param towncode 用来选中
 */

function changeCounty(ctx,code,street,village,towncode) {//选择县区动态加载乡镇
	var ss = Math.random();
	$("#" + village).empty();
	$("#" + village).append("<option value=\"\">--请选择村--</option>");
	
	var option = "<option value=\"\">--请选择乡镇--</option>";
	if(code == '' || code == null || code == undefined){
		 option = "<option value=\"\">--请选择乡镇--</option>";
	} else {
		
		$.ajax({
			type:'GET',
			cache:'false',
			url: ctx + '/division/getSonByParentCode/' + ss,
			data:{
				code:code
			},
			async:false,
			success : function(msg){
				if(msg == '') {
					option = "<option value=\"\">--请选择乡镇--</option>";
				} else {
					if(typeof JSON == 'undefined') {
						$('head').append($("<script type='text/javascript' src='${ctx}/static/js/json2.js'"));
					}
					var data = JSON.parse(msg);
					$.each(data,function(k,v){
						var redStyleStr = '\"style=\"color:red\"';
						if(v['isPoverty'] == 1) {
							option += "<option value=\"" + v['code'] + redStyleStr + "\">"
							 + v['name'] +"(村:"+v['villageAmount']+",户:"+v['familyAmount']+")"
							+ "</option>";
						}else {
							option += "<option value=\"" + v['code'] + "\">"
							 + v['name'] +"(村:"+v['villageAmount']+",户:"+v['familyAmount']+")"
							+ "</option>";
						}
							
					});
				}
			},
			error : function(msg,textStatus,e) {
				option = "<option value=\"\">--请选择乡镇--</option>";
			}
		});
	}
	$("#" + street).empty();
	$("#" + street).append(option);
	$("#" + street).val(towncode);
	
}
/**
 * @autor lyj
 * @param ctx
 * @param code
 * @param village
 * @param villagecode 选中的村
 */

function changeTown(ctx,code,village,villagecode) {//选择乡镇动态加载村
	var ss = Math.random();
	
	var option = "<option value=\"\">--请选择村--</option>";
	if(code == '' || code == null || code == undefined){
		 option = "<option value=\"\">--请选择村--</option>";
	} else {
		
		$.ajax({
			type:'GET',
			cache:'false',
			url: ctx + '/division/getSonByParentCode/' + ss,
			data:{
				code : code
			},
			async:false,
			success : function(msg){
				if(msg == '') {
					option = "<option value=\"\">--请选择村--</option>";
				} else {
					if(typeof JSON == 'undefined') {
						$('head').append($("<script type='text/javascript' src='${ctx}/static/js/json2.js'"));
					}
					var data = JSON.parse(msg);
					$.each(data,function(k,v){
						var redStyleStr = '\"style=\"color:red\"';
						var blueStyleStr = '\"style=\"color:blue\"';
						if(v['isPoverty'] == 1) {
							option += "<option value=\"" + v['code'] + redStyleStr + "\">"
							 + v['name'] +"("+v['familyAmount']+"户)"
							+ "</option>";
						} else if(v['isPoverty'] == 2) {
							option += "<option value=\"" + v['code'] + blueStyleStr + "\">"
							 + v['name'] +"("+v['familyAmount']+"户)"
							+ "</option>";
						}else {
							option += "<option value=\"" + v['code'] + "\">"
							 + v['name'] +"("+v['familyAmount']+"户)"
							+ "</option>";
						}
					});
				}
				
			},
			error : function(msg,textStatus,e) {
				option = "<option value=\"\">--请选择村--</option>";
			}
		});
	}
	$("#" + village).empty();
	$("#" + village).append(option);
	$("#" + village).val(villagecode);
}

/**
 * 市级直属单位选择
 * @param ctx
 * @param code
 * @param department
 */
function changeCityUnit(ctx,code,department) {//选择县区动态加载所属单位
	var ss = Math.random();
	
	var option = "<option value=\"\">--请选择部门--</option>";
	if(code == '' || code == null || code == undefined){
		 option = "<option value=\"\">--请选择部门--</option>";
	} else {
		
		$.ajax({
			type:'GET',
			cache:'false',
			url: ctx + '/division/getSonUnitByParentCode/' + ss,
			data:{
				code : code
			},
			async:false,
			success : function(msg){
				if(msg == '') {
					option = "<option value=\"\">--请选择部门--</option>";
				} else {
					if(typeof JSON == 'undefined') {
						$('head').append($("<script type='text/javascript' src='${ctx}/static/js/json2.js'"));
					}
					var data = JSON.parse(msg);
					$.each(data,function(k,v){
						option += "<option value=\"" + v['code'] + "\">"
						 + v['name']
						+ "</option>";
					});
				}
			},
			error : function(msg,textStatus,e) {
				option = "<option value=\"\">--请选择部门--</option>";
			}
		});
	}
	$("#" + department).empty();
	$("#" + department).append(option);
}

function popWin(theURL, winName, theW, theH, showAsModal) {
	theTop = (window.screen.height - theH) / 2;
	theLeft = (window.screen.width - theW) / 2;
	var features = "toolbar=0,scrollbars=yes,location=no,left=" + theLeft + ",top="
			+ theTop + ",width=" + theW + ",height=" + theH;
//	var showDig = "dialogWidth:" + theW + ";dialogHeight:" + theH+"px;dialogLeft:"+ theLeft + ";dialogTop:"
//			+ theTop + ";center:yes;help:no;resizable:no;status:no";

	window.SubWin = window.open(theURL, winName, features);
//	window.SubWin = showModelessDialog(theURL, winName, showDig);
	window.SubWin.focus();

	if (showAsModal) {
		window.CtrlsDisabled = new Array();
		DisableCtrls("INPUT;SELECT;TEXTAREA;BUTTON");
	}

	function DisableCtrls(tagNameStr) {
		var arrTags = tagNameStr.split(";");
		for ( var i = 0; i < arrTags.length; i++) {
			var arrEle = document.getElementsByTagName(arrTags[i]);
			PushToCtrlsDisabled(arrEle);
		}

		for ( var i = 0; i < window.CtrlsDisabled.length; i++) {
			window.CtrlsDisabled[i].disabled = true;
			window.CtrlsDisabled[i].readOnly = true;
		}
	}

	function PushToCtrlsDisabled(arrEle) {
		for ( var i = 0; i < arrEle.length; i++) {
			if (!arrEle[i].disabled) {
				window.CtrlsDisabled.push(arrEle[i]);
			}
		}
	}

	window.onfocus = function() {
		if (window.SubWin && showAsModal) {
			if (window.SubWin.closed == true
					|| typeof (window.SubWin.closed) == "undefined") {
				for ( var i = 0; i < window.CtrlsDisabled.length; i++) {
					window.CtrlsDisabled[i].disabled = false;
					window.CtrlsDisabled[i].readOnly = false;
				}
			} else {
				window.SubWin.focus();
			}
		}
	}
}
function   isInteger(str)   {   
	  if   (/[^\d]+$/.test(str)){   
	  return   false;   
	  }   
	  return   true;   
	  }   
function   isValidDate(iY,   iM,   iD)   {  
	   if   (iY.length==2)   
	          {   
	              var   D   =   new   Date("19"+iY+"/"+iM+"/"+iD);   
	              var   B   =   D.getYear()==iY&&(D.getMonth()+1)==iM&&D.getDate()==iD;   
	          }   
	          else   
	          {   
	              var   D   =   new   Date(iY+"/"+iM+"/"+iD);   
	              var   B   =   D.getFullYear()==iY&&(D.getMonth()+1)==iM&&D.getDate()==iD;   
	          }   
	          if   (!B)   {alert("输入的身份证号里出生日期不对！");   return   false;} 
	          
	             
	         /* var   a=new   Date(iY+"/"+iM+"/"+iD);  alert(a); 
	          var   y=a.getYear();   alert(y);
	          var   m=a.getMonth()+1;   alert(m);
	          var   d=a.getDate();   alert(d);
	          if   (y!=iY   ||   m!=iM   ||   d!=iD)   
	          {   
	                  window.alert   ('身份证号码内日期错误！');   
	                  return   false;   
	          }   */
	  return   true   
	  }   
	    
//身份证验证
function   isChinaIDCard(StrNo,birthday){   
  StrNo   =   StrNo.toString();
  if(StrNo==""){
  	alert("身份证不能为空!");
  	return false;
  }   
  if   (StrNo.length==18)   
  {   
            var   a,b,c   
            if   (!isInteger(StrNo.substr(0,17)))   {return   false}   
            a=parseInt(StrNo.substr(0,1))*7+parseInt(StrNo.substr(1,1))*9+parseInt(StrNo.substr(2,1))*10;   
            a=a+parseInt(StrNo.substr(3,1))*5+parseInt(StrNo.substr(4,1))*8+parseInt(StrNo.substr(5,1))*4;   
            a=a+parseInt(StrNo.substr(6,1))*2+parseInt(StrNo.substr(7,1))*1+parseInt(StrNo.substr(8,1))*6;     
            a=a+parseInt(StrNo.substr(9,1))*3+parseInt(StrNo.substr(10,1))*7+parseInt(StrNo.substr(11,1))*9;     
            a=a+parseInt(StrNo.substr(12,1))*10+parseInt(StrNo.substr(13,1))*5+parseInt(StrNo.substr(14,1))*8;     
            a=a+parseInt(StrNo.substr(15,1))*4+parseInt(StrNo.substr(16,1))*2;   
            b=a%11;   
    
            if   (b==2)   //最后一位为校验位   
            {   
            c=StrNo.substr(17,1).toUpperCase();   //转为大写X   
            }   
            else   
            {   
            c=parseInt(StrNo.substr(17,1));   
            }   
    
            switch(b)   
            {   
            case   0:   if   (   c!=1   )   {alert("身份证好号码校验位错:最后一位应该为:1");return   false;}break;   
            case   1:   if   (   c!=0   )   {alert("身份证好号码校验位错:最后一位应该为:0");return   false;}break;   
            case   2:   if   (   c!="X")   {alert("身份证好号码校验位错:最后一位应该为:X");return   false;}break;   
            case   3:   if   (   c!=9   )   {alert("身份证好号码校验位错:最后一位应该为:9");return   false;}break;   
            case   4:   if   (   c!=8   )   {alert("身份证好号码校验位错:最后一位应该为:8");return   false;}break;   
            case   5:   if   (   c!=7   )   {alert("身份证好号码校验位错:最后一位应该为:7");return   false;}break;   
            case   6:   if   (   c!=6   )   {alert("身份证好号码校验位错:最后一位应该为:6");return   false;}break;   
            case   7:   if   (   c!=5   )   {alert("身份证好号码校验位错:最后一位应该为:5");return   false;}break;   
            case   8:   if   (   c!=4   )   {alert("身份证好号码校验位错:最后一位应该为:4");return   false;}break;   
            case   9:   if   (   c!=3   )   {alert("身份证好号码校验位错:最后一位应该为:3");return   false;}break;   
            case   10:   if   (   c!=2   ){alert("身份证好号码校验位错:最后一位应该为:2");return   false}   
            }   
            }   
  else   //15位身份证号   
            {   
            if   (!isInteger(StrNo))   {alert("身份证号码错误,前15位不能含有英文字母！");return   false}     
            }   
    
  switch(StrNo.length){   
  case   15:     
                  if   (isValidDate(StrNo.substr(6,2),StrNo.substr(8,2),StrNo.substr(10,2)))   
                          {
                          if(birthday!=null&&birthday!=""){
                             document.getElementsByName(birthday)[0].value="19"+(StrNo.substr(6,2))+"-"+(StrNo.substr(8,2))+"-"+(StrNo.substr(10,2));
                          }
                          return   true;}   
                  else   
                          {return   false;}   
  case   18:     
                  if   (isValidDate(StrNo.substr(6,4),StrNo.substr(10,2),StrNo.substr(12,2)))   
                          { if(birthday!=null&&birthday!=""){
                           	document.getElementsByName(birthday)[0].value=(StrNo.substr(6,4))+"-"+(StrNo.substr(10,2))+"-"+(StrNo.substr(12,2));
                          }
                          return   true;}   
                  else   
                          {return   false;}   
  }   
  alert("输入的身份证号码必须为15位或者18位！");   
  return   false;
  } 

function timestampToDate(timestamp){
    var date = new Date(timestamp);
	Y = date.getFullYear() + '-';
	M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	D = (date.getDate() < 10 ? '0'+date.getDate() : date.getDate());
	return (Y+M+D);
}

function changeSubParameters(ctx,category,value,paramterTwoLev) {//选择乡镇动态加载行政村
	var ss = Math.random();
	
	var option = "<option value=\"\">--请选择--</option>";
	if(value == '' || value == null || value == undefined){
		 option = "<option value=\"\">--请选择--</option>";
	} else {
		
		$.ajax({
			type:'GET',
			cache:'false',
			url: ctx + '/parameter/getSonByParentCode/' + ss,
			data:{
				category : category,
				value : value
			},
			async:false,
			success : function(msg){
				if(msg == '') {
					option = "<option value=\"\">--请选择--</option>";
				} else {
					if(typeof JSON == 'undefined') {
						$('head').append($("<script type='text/javascript' src='${ctx}/static/js/json2.js'"));
					}
					var data = JSON.parse(msg);
					$.each(data,function(k,v){
						option += "<option value=\"" + v['value'] + "\">"
						 + v['name']
						+ "</option>";
					});
				}
				
			},
			error : function(msg,textStatus,e) {
				option = "<option value=\"\">--请选择--</option>";
			}
		});
	}
	$("#" + paramterTwoLev).empty();
	$("#" + paramterTwoLev).append(option);
}


function updatePairForm(url, listDatagrid, params) {
	var opts = {
		width : 1000,
		height : 600,
		title : '信息',
		href : url,
		iconCls : 'icon-application_form_add',
		buttons : [
				 {
					text : '取消',
					id : 'formCancelBtn',
					iconCls : 'icon-cross',
					handler : function() {
						parent.$.modalDialog.handler.dialog('close');
					}
				} ]
	};
	$.extend(opts, params);
	parent.$.modalDialog(opts);
}