package com.infotop.dao;

import java.util.List;
import java.util.Map;

import com.infotop.entity.OperateLog;

public interface OperateLogDao  {

	List<OperateLog> findLog(Map param);
	Integer findLogCount(Map param);
	boolean save(OperateLog entity);
	boolean delete(Long id);
}