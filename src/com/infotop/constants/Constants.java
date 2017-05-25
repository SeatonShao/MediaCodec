package com.infotop.constants;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
//Import log4j classes.
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.HAUtil;

import com.infotop.entity.Remote;
import com.infotop.mybatis.MybatisDao;
import com.infotop.service.HDFSService;

/**
 * urlPatterns必须有，否则loadOnStartup无效
 *
 * @author Administrator
 *
 */
@WebServlet(loadOnStartup = 1, urlPatterns = "/Constants", asyncSupported = true)
public class Constants extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(Constants.class.getName());
	public static MediaConfig mediaConfig;
	public static Remote remote;
	public static Properties service;
	public static Properties REDIS;
	public static String OS_NAME = System.getProperty("os.name").toLowerCase();
	public static String IP;
	public static String CLASSES_PATH;
	public static String WEB_INF_PATH;
	public static String PROJECT_PATH;
	public static String VIRTUAL_NAME;
	private ServletContext application;
	String type;// 码率
	// 异步servlet3.0超时时间
	private int BITE;
	private int BITE_BUF;
	// private final static String resource = "conf.properties";
	private final static String redisSrc = "redis.properties";
	static {
		try {
			CLASSES_PATH = Constants.class.getClassLoader().getResource("").getPath();
			if (CLASSES_PATH.contains(":"))
				CLASSES_PATH = CLASSES_PATH.substring(1);
			WEB_INF_PATH = CLASSES_PATH.substring(0, CLASSES_PATH.lastIndexOf("/classes"));
			log.info(CLASSES_PATH);
			log.info(WEB_INF_PATH);
			IP = InetAddress.getLocalHost().getHostAddress();
			service = Resources.getResourceAsProperties("service.properties");
			REDIS = Resources.getResourceAsProperties(redisSrc);
			// log.info(getActiveAddress());
		} catch (IOException e) {
			log.error("静态代码块加载失败", e);
		}
	}

	public Constants() {
		super();
	}

	public Constants(String bite) {
		super();
		switch (bite) {
		case "biteMin": {
			BITE_BUF = Constants.mediaConfig.getFgBminBuf();
			BITE = Constants.mediaConfig.getFgBiteMin();
			break;
		}
		case "biteMid": {
			BITE_BUF = Constants.mediaConfig.getFgBmidBuf();
			BITE = Constants.mediaConfig.getFgBiteMid();
			break;
		}
		case "biteMax": {
			BITE_BUF = Constants.mediaConfig.getFgBmaxBuf();
			BITE = Constants.mediaConfig.getFgBiteMax();
			break;
		}
		case "biteSup": {
			BITE_BUF = Constants.mediaConfig.getFgBsupBuf();
			BITE = Constants.mediaConfig.getFgBiteSup();
			break;
		}
		}
	}

	/**
	 * 从数据库中读取参数表，保存在mediaConfig里
	 *
	 * @throws ServletException
	 *             if an error occure
	 */
	public void init() throws ServletException {

		// 获取WebApplicationContext
		application = this.getServletContext();
		PROJECT_PATH = application.getRealPath("");
		VIRTUAL_NAME = application.getVirtualServerName();
		mediaConfig = findProperties(VIRTUAL_NAME.toLowerCase());
		
	}

	public static Remote findRemote(String source) {
		SqlSession session = new MybatisDao<Object>().getSession();
		ConstantsMapper userMapper = session.getMapper(ConstantsMapper.class);
		try {
			return userMapper.findRemote(source);
		} catch (Exception e) {
			return null;
		} finally {
			log.info("session 关闭");
			session.close();
		}
	}

	private MediaConfig findProperties(String virtualName) {
		SqlSession session = new MybatisDao<Object>().getSession();
		ConstantsMapper userMapper = session.getMapper(ConstantsMapper.class);
		try {
			log.info("获取系统配置:" + virtualName);
			return userMapper.findPropertiesByName(virtualName);
		} catch (Exception e) {
			log.error("获取系统配置失败", e);
			return null;
		} finally {
			log.info("session 关闭");
			session.close();
		}
	}

	
	
	/**
	 *
	 * @param ip
	 * @param port
	 * @return
	 */
	private MediaConfig findProperties(String ip, int port) {
		SqlSession session = new MybatisDao<Object>().getSession();
		ConstantsMapper userMapper = session.getMapper(ConstantsMapper.class);
		try {
			return userMapper.findProperties(ip, port);
		} catch (Exception e) {
			log.error("获取系统配置失败", e);
			return null;
		} finally {
			log.info("获取系统配置成功");
			log.info("session 关闭");
			session.close();
		}
	}

	/**
	 *
	 * @return
	 */
	public static String getFFmpegPath() {
		String ffmpegPath;
		// 判断操作系统
		// 获取配置的转换工具（ffmpeg.exe）的存放路径
		if (OS_NAME.startsWith("win")) {
			ffmpegPath = WEB_INF_PATH + "/tools/ffmpeg.exe";
		} else {
			ffmpegPath = WEB_INF_PATH + "/tools/ffmpeg";
		}
		return ffmpegPath;
	}

	public static String getPlayPath(String bite,String path){
		return "/codec/" +path + "/" +bite + "/" + bite + path
		+ ".m3u8";
	}
	/**
	 *
	 * @return
	 */
	public static String getFFprobePath() {
		String ffprobePath;
		// 判断操作系统
		// 获取配置的转换工具（ffmpeg.exe）的存放路径
		if (OS_NAME.startsWith("win")) {
			ffprobePath = WEB_INF_PATH + "/tools/ffprobe.exe";
		} else {
			ffprobePath = WEB_INF_PATH + "/tools/ffprobe";
		}
		return ffprobePath;
	}

	/**
	 * 获取活动的结点ip
	 *
	 * @return
	 */
	public static String getActiveAddress() {
		FileSystem fs = null;
		try {
			fs = FileSystem.get(HDFSService.conf);
			InetSocketAddress active = HAUtil.getAddressOfActive(fs);
			return active.getAddress().getHostAddress();
		} catch (IOException e) {
			log.error("获取活动namenode失败", e);
			return null;
		}
	}

	/**
	 * webhdfs 文件路径
	 *
	 * @param name
	 * @param mediaName
	 * @param type
	 * @return
	 */
	public static String getUploadPath(String name, String mediaName, String type) {
		return "http://" + getActiveAddress() + ":50070/webhdfs/v1/" + mediaName + "/" + mediaName + type + "?op=OPEN";
	}

	/**
	 * 源文件在hdfs上的路径/mediaName/@mediaName+@type
	 *
	 * @param mediaName
	 * @param type
	 *            文件类型
	 * @return
	 */
	public static String getSrcPath(String mediaName, String type) {
		return "/" + type + "/" + mediaName +"."+ type;
	}

	/**
	 * 源文件在本地保存的路径（包含文件名）
	 *
	 * @param mediaName
	 * @param bite
	 *            码率
	 * @return
	 */
	public static String getLocalPath(String mediaName,String contentType) {
		String path = WEB_INF_PATH + "/videos/" + mediaName + "/" + mediaName + "."+contentType;
		checkPath(path.substring(0, path.lastIndexOf("/")));
		return path;
	}

	/**
	 * webhdfs获取ts文件路径
	 *
	 * @param mediaName
	 * @param bite
	 * @return
	 */
	public static String getTSPath(String mediaName, String bite) {

		return "http://" + getActiveAddress() + ":50070/webhdfs/v1/" + mediaName + "/" + bite + "/" + bite + mediaName
				+ ".mp4?op=OPEN";
	}

	/**
	 * 获取下载到本地的要切ts的文件路径（包含文件名）
	 *
	 * @param mediaName
	 * @param bite
	 * @return
	 */
	public static String getLocalTSPath(String mediaName, String bite) {

		String path = WEB_INF_PATH + "/videos/" + mediaName + "/" + bite + mediaName + ".mp4";
		checkPath(path.substring(0, path.lastIndexOf("/")));
		return path;
	}

	/**
	 * 本地保存转码文件路径（包含文件名）
	 *
	 * @param bite
	 * @param name
	 * @return
	 */
	public static String getCodecPath( String mediaName, String bite) {

		String path = WEB_INF_PATH + "/videos/" + mediaName + "/" + bite + mediaName + ".mp4";
		checkPath(path.substring(0, path.lastIndexOf("/")));
		return path;
	}

	/**
	 * 本地保存切片文件路径
	 *
	 * @param bite
	 * @param name
	 * @return
	 */
	public static String getM3u8Path(String mediaName, String bite) {

		String path = WEB_INF_PATH + "/videos/" + mediaName + "/";
		checkPath(path);
		return path;
	}

	/**
	 * hdfs保存m3u8路径
	 *
	 * @param bite
	 * @param name
	 * @return
	 */
	public static String getHDFSM3u8Path(String mediaName, String bite) {

		return "/codec/" + mediaName + "/" + bite + "/";
	}

	/**
	 * hdfs保存转码文件路径（包含文件名）
	 *
	 * @param bite
	 * @param name
	 * @return
	 */
	public static String getHdfsPath(String mediaName, String bite) {
		String path = "/codec/" + mediaName + "/" + bite + mediaName + ".mp4";
		return path;
	}

	/**
	 * hdfs保存目录
	 *
	 * @param bite
	 * @param name
	 * @return
	 */
	public static String getHdfsDir(String mediaName, String bite) {
		String path = "/" + mediaName + "/" + bite + "/";
		return path;
	}

	/**
	 * 本地保存截取图片的路径
	 *
	 * @param mediaName
	 * @return
	 */
	public String getPicPath(String mediaName) {
		String pic = WEB_INF_PATH + "/pic/" + mediaName + ".jpg";
		checkPath(pic.substring(0, pic.lastIndexOf("/")));
		return pic;
	}

	/**
	 * hdfs保存截取图片的路径
	 *
	 * @param mediaName
	 * @return
	 */
	public String getHDFSPicPath(String mediaName) {

		return "/pic/" + mediaName + "/" + mediaName + ".jpg";
	}

	public int getBITE() {
		return BITE;
	}

	public void setBITE(int bITE) {
		BITE = bITE;
	}

	public int getBITE_BUF() {
		return BITE_BUF;
	}

	public void setBITE_BUF(int bITE_BUF) {
		BITE_BUF = bITE_BUF;
	}

	public static void checkPath(String path) {
		if (path.contains("/")) {
			checkPath(path.substring(0, path.lastIndexOf("/")));
		}
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}
	}

}
