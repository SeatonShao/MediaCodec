package com.infotop.mybatis;


import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MybatisDao<E>   {
	private Logger log = LogManager.getLogger(MybatisDao.class.getName());
	/**
	 * 
	 */
	private SqlSessionFactory sessionFactory = MybatisUtil.getInstance();
	// 创建能执行映射文件中sql的sqlSession session自动提交true
	
	/*protected E add(E e){
		try {
			return session.getMapper(e.getClass()).add(e);
		} catch (Exception e) {
			log.error("media find");
			e.printStackTrace();
			return false;
		} finally {
			session.close();
		}
		return e;
		
	}*/
	public SqlSession getSession() {
		log.info("session 开启");
		return sessionFactory.openSession(true);
	}
	
}
