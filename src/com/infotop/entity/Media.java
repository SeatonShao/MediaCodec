package com.infotop.entity;

public class Media {
	private int id;
	private String title;
	private String name;
	private String mediaName;
	private String mediaPath;
	private int mediaLength;
	private String picPath;
	private String contentType;
	private String biteMin;
	private String biteMid;
	private String biteMax;
	private String biteSup;
	private String status;
	private String createTime;
	private String createUser;
	private String targetUuid;
	private String virtualName;
	
	public String getTargetUuid() {
		return targetUuid;
	}
	public void setTargetUuid(String targetUuid) {
		this.targetUuid = targetUuid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMediaName() {
		return mediaName;
	}
	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}
	public String getMediaPath() {
		return mediaPath;
	}
	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getBiteMin() {
		return biteMin;
	}
	public void setBiteMin(String biteMin) {
		this.biteMin = biteMin;
	}
	public String getBiteMid() {
		return biteMid;
	}
	public void setBiteMid(String biteMid) {
		this.biteMid = biteMid;
	}
	public String getBiteMax() {
		return biteMax;
	}
	public void setBiteMax(String biteMax) {
		this.biteMax = biteMax;
	}
	
	
	public int getMediaLength() {
		return mediaLength;
	}
	public void setMediaLength(int mediaLength) {
		this.mediaLength = mediaLength;
	}
	public String getBiteSup() {
		return biteSup;
	}
	public void setBiteSup(String biteSup) {
		this.biteSup = biteSup;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getVirtualName() {
		return virtualName;
	}
	public void setVirtualName(String virtualName) {
		this.virtualName = virtualName;
	}
	
}
