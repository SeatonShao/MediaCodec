package com.infotop.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.infotop.constants.ConstantsMapper;
import com.infotop.constants.MediaConfig;
import com.infotop.mybatis.MybatisDao;
/**
 * 
 * @author Administrator
 *
 */
public class ConfigListService {
	private Logger log = LogManager.getLogger(ConfigListService.class);
	
	public MediaConfig find(MediaConfig mediaConfig) {
		SqlSession session = new MybatisDao<Object>().getSession();
		ConstantsMapper userMapper = session.getMapper(ConstantsMapper.class);
		try {
			return userMapper.find(mediaConfig);
		} catch (Exception e) {
			log.error("media find");
			return null;
		}finally {
			log.info("session 关闭");
			session.close();
		}
	}
/**
 * 修改系统配置
 * @param mediaConfig
 * @return
 */
	public boolean updateConfig(MediaConfig mediaConfig) {
		SqlSession session = new MybatisDao<Object>().getSession();
		ConstantsMapper userMapper = session.getMapper(ConstantsMapper.class);
		try {
			log.info("修改系统配置");
			return userMapper.updateConfig(mediaConfig);
		} catch (Exception e) {
			log.error("修改系统配置err");
			e.printStackTrace();
			return false;
		} finally{
			log.info("session 关闭");
			session.close();
		}
	}
/**
 * 查询多条系统配置信息
 * @param params
 * @return
 */
	public List getConfigList(Map params) {
		SqlSession session = new MybatisDao<Object>().getSession();
		ConstantsMapper userMapper = session.getMapper(ConstantsMapper.class);
		try {
			log.info("修改系统配置");
			return userMapper.findConfig(params);
		} catch (Exception e) {
			log.error("修改系统配置err");
			e.printStackTrace();
			return null;
		} finally{
			log.info("session 关闭");
			session.close();
		}
	}
/**
 * 查询系统配置数量
 * @param params
 * @return
 */
	public Integer getConfigListCount(Map params) {
		SqlSession session = new MybatisDao<Object>().getSession();
		ConstantsMapper userMapper = session.getMapper(ConstantsMapper.class);
		try {
			log.info("修改系统配置");
			return userMapper.findConfigCount(params);
		} catch (Exception e) {
			log.error("修改系统配置err");
			e.printStackTrace();
			return 0;
		} finally{
			log.info("session 关闭");
			session.close();
		}
	}
/**
 * 删除系统配置
 * @param id
 * @return
 */
	public boolean deleteConfig(int id) {
		SqlSession session = new MybatisDao<Object>().getSession();
		ConstantsMapper userMapper = session.getMapper(ConstantsMapper.class);
		try {
			if(userMapper.deleteConfig(id)){
				log.info("删除系统配置成功");
				return true;
			}else{
				log.info("删除系统配置失败");
				return false;
			}
			
		} catch (Exception e) {
			log.error("删除系统配置err");
			e.printStackTrace();
			return false;
		} finally{
			log.info("session 关闭");
			session.close();
		}
	}
	/**
	 * 
	 * @param id
	 * @return
	 */
public MediaConfig findById(String id) {
	
	MediaConfig mediaConfig = new MediaConfig();
	mediaConfig.setId(Long.parseLong(id));
	return this.find(mediaConfig);
}
		
}
