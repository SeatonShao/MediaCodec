package com.infotop.entity;
import java.util.Date;

import com.infotop.service.OperateLogService.LOG_TYPE;

public class OperateLog  {

	private String operateUser;
	private String operateName;
	private Date operateTime;
	private String operateIp;
	private LOG_TYPE logType;
	private String operation;
	private String content;
	
	public String getOperateUser() {
		return operateUser;
	}
	public void setOperateUser(String operateUser) {
		this.operateUser = operateUser;
	}
	public String getOperateName() {
		return operateName;
	}
	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	public String getOperateIp() {
		return operateIp;
	}
	public void setOperateIp(String operateIp) {
		this.operateIp = operateIp;
	}
	public LOG_TYPE getLogType() {
		return logType;
	}
	public void setLogType(LOG_TYPE logType) {
		this.logType = logType;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
