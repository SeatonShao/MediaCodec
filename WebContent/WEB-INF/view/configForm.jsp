<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/taglibs.jsp"%>
<script type="text/javascript">
	var config_form_inputform_id = 'config_form_inputForm';

$(function() {
		$('#' + config_form_inputform_id).form(
				{
					success : function(result) {
						result = $.parseJSON(result);
						if (result.success) {
							$("#win").window('close');
							$("#config_dg").datagrid('reload');
							$.messager.show({ // show error message
								title : '提示',
								msg : result.message
							});
						} else {
							$.messager.alert('错误', result.message, 'error');
						}
					}
				});
		$("#config_res").click(function(){
			$("#win").window('close');
		});
	});
</script>
<form id="config_form_inputForm" name="config_form_inputForm"
	action="${ctx}/ConfigList" method="post">
	<input type="hidden" name="id" id="id" />
	<input type="hidden" name="method" id="method" value="update" />
	<table class="content" style="width: 100%;">
		<tr>
			<td  class="biao_bt3">ip</td>
			<td><input type="text" name="ip" id="ip"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			<td class="biao_bt3">port</td>
			<td ><input type="text" name="port" id="port"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			<td class="biao_bt3">host</td>
			<td><input type="text" name="virtualName" id="virtualName"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			<td class="biao_bt3">转码时序</td>
			<td><input type="text" name="fgSleep" id="fgSleep"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
		</tr>
		<tr>
			<td class="biao_bt3">转码顺序</td>
			<td><input type="text" name="sort" id="sort"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			<td class="biao_bt3">起始转码序号</td>
			<td ><input type="text" name="rows" id="rows"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			<td class="biao_bt3">沂蒙学堂ip</td>
			<td><input type="text" name="remoteIp" id="remoteIp"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			<td class="biao_bt3">沂蒙学堂端口</td>
			<td><input type="text" name="remotePort" id="remotePort"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
		</tr>
		<tr>
			<td class="biao_bt3" >转码状态请求路径</td>
			<td colspan="3"><input type="text" name="remoteStatus" id="remoteStatus"
				class="easyui-validatebox" style="width:300px"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			<td class="biao_bt3"  >上传状态请求路径</td>
			<td colspan="3"><input type="text" name="remoteInfo" id="remoteInfo"
				class="easyui-validatebox"  style="width:300px"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			</tr>
		<tr>
		<td class="biao_bt3">默认播放码率</td>
			<td><input type="text" name="fgBt" id="fgBt"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			<td class="biao_bt3">默认线程</td>
			<td><input type="text" name="fgThread" id="fgThread"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
		  
		</tr>
		<tr>
			<td colspan="8" align="center" class="biao_bt3">比特率</td>
		</tr>
		<tr>
			<td class="biao_bt3">流畅</td>
			<td><input type="text" name="fgBiteMin" id="fgBiteMin"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			<td class="biao_bt3">普通</td>
			<td><input type="text" name="fgBiteMid" id="fgBiteMid"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			<td class="biao_bt3">高清</td>
			<td><input type="text" name="fgBiteMax" id="fgBiteMax"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			<td class="biao_bt3">超清</td>
			<td><input type="text" name="fgBiteSup" id="fgBiteSup"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
		</tr>
		<tr>
			<td colspan="8" class="biao_bt3" align="center">比特率缓存</td>
		</tr>
		<tr>
			<td class="biao_bt3">流畅</td>
			<td><input type="text" name="fgBminBuf" id="fgBminBuf"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			<td class="biao_bt3">普通</td>
			<td><input type="text" name="fgBmidBuf" id="fgBmidBuf"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			<td class="biao_bt3">高清</td>
			<td><input type="text" name="fgBmaxBuf" id="fgBmaxBuf"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			<td class="biao_bt3">超清</td>
			<td><input type="text" name="fgBsupBuf" id="fgBsupBuf"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
		</tr>
		
		<tr>
			<td class="biao_bt3">宽高</td>
			<td><input type="text" name="fgVf" id="fgVf"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			<td class="biao_bt3">切片</td>
			<td><input type="radio" name="fgTs" id="fgTs"
				 value="1" title="开" style="width: 20px"/>开<input type="radio" name="fgTs" id="fgTs"
				value="0"   title="关"  style="width: 20px"/>关</td>
			<td class="biao_bt3">切片时长(s)</td>
			<td><input type="text" name="fgHlsTime" id="fgHlsTime"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			<td class="biao_bt3">最大切片</td>
			<td><input type="text" name="fgHlsSize" id="fgHlsSize"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
		</tr>
		<tr>
			<td class="biao_bt3">视频编码开关</td>
			<td><input type="radio" name="fgCvOn" id="fgCvOn"
				 value="1" title="开" style="width: 20px"/>开<input type="radio" name="fgCvOn" id="fgCvOn"
				value="0"   title="关"  style="width: 20px"/>关</td>
			<td class="biao_bt3">视频编码</td>
			<td><input type="text" name="fgCv" id="fgCv"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
			<td class="biao_bt3">音频编码开关</td>
			<td><input type="radio" name="fgCaOn" id="fgCaOn" value="1" title="开" style="width: 20px"/>开<input type="radio" name="fgCaOn" id="fgCaOn" value="0"  style="width: 20px" title="关"/>关</td>
			<td class="biao_bt3">音频编码</td>
			<td><input type="text" name="fgCa" id="fgCa"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
		</tr>
		<tr>
			<td class="biao_bt3">视频转码开关</td>
			<td><input type="radio" name="codecOn" id="codecOn"
				 value="1" title="开" style="width: 20px"/>开<input type="radio" name="codecOn" id="codecOn"
				value="0"   title="关"  style="width: 20px"/>关</td>
			<td class="biao_bt3">ts绝对路径</td>
			<td><input type="text" name="fgBaseUrl" id="fgBaseUrl"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
				<td class="biao_bt3">master</td>
			<td><input type="text" name="master" id="master"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
				<td class="biao_bt3">帧频</td>
			<td><input type="text" name="r" id="r"
				class="easyui-validatebox"
				data-options="required:true,missingMessage:'不能为空.'" /></td>
		</tr>
		<tr>
		<td align="right" colspan="4"><input type="submit" name="config_sub" id="config_sub" value="确定"/></td>
			<td align="left" colspan="4"><input type="button" name="config_res" id="config_res" value="取消"/></td>
		</tr>
		
	</table>
</form>