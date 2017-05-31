package com.infotop.service;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.infotop.mapping.MediaMapper;
import com.infotop.mybatis.MybatisDao;

public class ErrListService {
	private Logger log = LogManager.getLogger(ErrListService.class);

	public boolean redoMedia(long id) {
		SqlSession session = new MybatisDao<Object>().getSession(false);
		MediaMapper userMapper = session.getMapper(MediaMapper.class);
		try {
			userMapper.addMediaById(id);
			userMapper.deleteErrMediaById(id);
			
			session.commit();
			return true;
		} catch (Exception e) {
			log.error("media find");
			e.printStackTrace();
			session.rollback();
			return false;
		} finally{
			log.info("删除id="+id+"视频");
			log.info("session 关闭");
			session.close();
		}
	}
}
