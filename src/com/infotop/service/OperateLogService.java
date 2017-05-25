package com.infotop.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest; 
import org.codehaus.jackson.map.ObjectMapper; 
import com.infotop.dao.OperateLogDao;
import com.infotop.entity.OperateLog; 
 
public class OperateLogService {
	
	private static OperateLogDao operateLogDao ;
	
	public enum LOG_TYPE {INFO,ERR,WARN};
	/**
	 * 保存一个operateLog，如果保存成功返回该对象的id，否则返回null
	 * @param entity
	 * @return 保存成功的对象的Id
	 */
	public boolean save(OperateLog entity){
		return operateLogDao.save(entity);
	}
	/**
	 * 
	 * @param user
	 * @param name
	 * @param logType
	 * @param operation
	 * @param content
	 * @param requset
	 */
	public static void saveLog(String user,String name,LOG_TYPE logType, String operation, Map content, HttpServletRequest requset){
		
		ObjectMapper om = new ObjectMapper();
		try{
			OperateLog entity = new OperateLog();
			entity.setOperateUser(user);
			entity.setOperateName(name);
			entity.setOperateIp(requset.getRemoteAddr());
			entity.setLogType(logType);
			entity.setOperation(operation);
			String json = (content != null ?   om.writeValueAsString(content) : "{}");
			entity.setContent(json);
			operateLogDao.save(entity);
			}catch (Exception e) {
				 e.printStackTrace();
			}
	}
	public static void saveLog(LOG_TYPE logType, String operation, Map content, HttpServletRequest requset){
			saveLog("", "", logType, operation, content, requset);
	}
	
	 
	
	/**
	 * 根据类型得到operateLog
	 * @param type
	 * @return
	 */
	public List<OperateLog> findLog(Map type) {
		return operateLogDao.findLog(type);
	}
	/**
	 * 根据类型得到operateLog
	 * @param type
	 * @return
	 */
	public Integer findLogCount(Map type) {
		return operateLogDao.findLogCount(type);
	}
	/**
	 * 删除一个operateLog
	 * @param id
	 */
	public static boolean delete(Long id){
		return  operateLogDao.delete(id);
	}

	public static void saveLog(String name,LOG_TYPE logType, String operation, Object obj, String ip) {
		ObjectMapper om = new ObjectMapper();
		try{
			OperateLog entity = new OperateLog();
			entity.setOperateUser("");
			entity.setOperateName(name);
			entity.setOperateIp(ip);
			entity.setLogType(logType);
			entity.setOperation(operation);
			String json = (obj != null ?   om.writeValueAsString(obj) : "{}");
			entity.setContent(json);
			operateLogDao.save(entity);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
	}
	
}
