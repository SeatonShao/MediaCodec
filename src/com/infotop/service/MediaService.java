package com.infotop.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.infotop.mapping.CodecMapper;
import com.infotop.mapping.MediaMapper;
import com.infotop.mybatis.MybatisDao;

public class MediaService {
	private Logger log = LogManager.getLogger(MediaService.class.getName());

	public boolean deleteMedia(long id) {
		SqlSession session = new MybatisDao<Object>().getSession();
		MediaMapper userMapper = session.getMapper(MediaMapper.class);
		try {
			return userMapper.deleteMediaTempById(id);
		} catch (Exception e) {
			log.error("删除转码id="+id+"视频失败");
			return false;
		} finally{
			log.info("删除转码id="+id+"视频");
			log.info("session 关闭");
			session.close();
		}
	}

	public List getMediaCodecList(Map  params) {
		SqlSession session = new MybatisDao<Object>().getSession();
		CodecMapper userMapper = session.getMapper(CodecMapper.class);
		try {
			return userMapper.findMediaCodec(params);
		} catch (Exception e) {
			log.error("media find");
			return null;
		} finally{
			log.info("session 关闭");
			session.close();
		}
	}

	public Integer getMediaCodecCount(Map  params) {
		SqlSession session = new MybatisDao<Object>().getSession();
		CodecMapper userMapper = session.getMapper(CodecMapper.class);
		try {
			return userMapper.findMediaCodecCount( params);
		} catch (Exception e) {
			log.error("media find");
			return 0;
		} finally{
			log.info("session 关闭");
			session.close();
		}
	}

	public List getPlayList(Map  params) {
		SqlSession session = new MybatisDao<Object>().getSession();
		CodecMapper userMapper = session.getMapper(CodecMapper.class);
		try {
			return userMapper.findPlayList(params);
		} catch (Exception e) {
			log.error("media find",e);
			return null;
		} finally{
			log.info("session 关闭");
			session.close();
		}
	}
	
	public List getErrList(Map  params) {
		SqlSession session = new MybatisDao<Object>().getSession();
		CodecMapper userMapper = session.getMapper(CodecMapper.class);
		try {
			return userMapper.findErrList(params);
		} catch (Exception e) {
			log.error("media find",e);
			return null;
		} finally{
			log.info("session 关闭");
			session.close();
		}
	}
	public Integer getPlayListCount(Map  params) {
		SqlSession session = new MybatisDao<Object>().getSession();
		CodecMapper userMapper = session.getMapper(CodecMapper.class);
		try {
			return userMapper.findPlayListCount( params);
		} catch (Exception e) {
			log.error("media find",e);
			return 0;
		} finally{
			log.info("session 关闭");
			session.close();
		}
	}
	public boolean updateMediaWeight(long id) {
		SqlSession session = new MybatisDao<Object>().getSession();
		MediaMapper userMapper = session.getMapper(MediaMapper.class);
		try {
			return userMapper.updateMediaWeightById(id);
		} catch (Exception e) {
			log.error("升级转码id="+id+"视频优先级失败",e);
			return false;
		} finally{
			log.info("升级转码id="+id+"视频优先级");
			log.info("session 关闭");
			session.close();
		}
	}

	public Integer getErrListCount(Map params) {
		SqlSession session = new MybatisDao<Object>().getSession();
		CodecMapper userMapper = session.getMapper(CodecMapper.class);
		try {
			return userMapper.findErrListCount( params);
		} catch (Exception e) {
			log.error("media find",e);
			return 0;
		} finally{
			log.info("session 关闭");
			session.close();
		}
	}
}
