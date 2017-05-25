package com.infotop.service;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.infotop.mapping.MediaMapper;
import com.infotop.mybatis.MybatisDao;

public class PlayListService {
	private Logger log = LogManager.getLogger(PlayListService.class);

	public boolean deleteMedia(long id) {
		SqlSession session = new MybatisDao<Object>().getSession();
		MediaMapper userMapper = session.getMapper(MediaMapper.class);
		try {
			return userMapper.deleteMediaById(id);
		} catch (Exception e) {
			log.error("media find");
			e.printStackTrace();
			return false;
		} finally{
			log.info("删除id="+id+"视频");
			log.info("session 关闭");
			session.close();
		}
	}
}
