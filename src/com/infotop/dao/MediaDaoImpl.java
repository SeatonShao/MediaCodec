package com.infotop.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.infotop.entity.Media;
import com.infotop.mapping.MediaMapper;
import com.infotop.mybatis.MybatisDao;

public class MediaDaoImpl extends MybatisDao<Object> implements MediaDao {

	private Connection conn;
	private Statement stat;
	private ResultSet rs;
	String q = "0";

	public MediaDaoImpl() {
		super();
	}

	private static Logger log = LogManager.getLogger(MediaDaoImpl.class);

	public List<Media> findList(Media media) {
		SqlSession session = getSession();
		MediaMapper userMapper = session.getMapper(MediaMapper.class);
		try {
			return userMapper.findMedia(media);
		} catch (Exception e) {
			log.error("media find");
			return null;
		} finally {
			log.info("session 关闭");
			session.close();
		}
	}

	public Media find(Media media) {
		SqlSession session = getSession();
		MediaMapper userMapper = session.getMapper(MediaMapper.class);
		try {
			return userMapper.findMediaById(media);
		} catch (Exception e) {
			log.error("media find");
			return null;
		}finally {
			log.info("session 关闭");
			session.close();
		}
	}
	public Media findErr(Media media) {
		SqlSession session = getSession();
		MediaMapper userMapper = session.getMapper(MediaMapper.class);
		try {
			return userMapper.findErrMediaById(media);
		} catch (Exception e) {
			log.error("media find");
			return null;
		}finally {
			log.info("session 关闭");
			session.close();
		}
	}
	
	public boolean save(Media media) {
		SqlSession session = getSession();
		MediaMapper userMapper = session.getMapper(MediaMapper.class);
		try {
			return userMapper.addMedia(media);
		} catch (Exception e) {
			log.error("media find");
			e.printStackTrace();
			return false;
		} finally {
			log.info("session 关闭");
			session.close();
		}
	}
	public boolean updateStatus(Media media) {
		SqlSession session = getSession();
		MediaMapper userMapper = session.getMapper(MediaMapper.class);
		try {
			return userMapper.updateMediaStatus(media);
		} catch (Exception e) {
			log.error("media update");
			e.printStackTrace();
			return false;
		} finally {
			log.info("session 关闭");
			session.close();
		}
	}

	public boolean updateMedia(Media media) {
		SqlSession session = getSession();
		MediaMapper userMapper = session.getMapper(MediaMapper.class);
		try {
			return userMapper.updateMediaById(media);
		} catch (Exception e) {
			log.error("media update");
			e.printStackTrace();
			return false;
		}finally {
			log.info("session 关闭");
			session.close();
		}
	}
	
	public boolean updateMediaTemp(Media media) {
		SqlSession session = getSession();
		MediaMapper userMapper = session.getMapper(MediaMapper.class);
		try {
			return userMapper.updateMediaTempById(media);
		} catch (Exception e) {
			log.error("media update");
			e.printStackTrace();
			return false;
		}finally {
			log.info("session 关闭");
			session.close();
		}
	}
/**
 * 检测此路径下是否是文件
 * @param path
 * @return
 */
	private static boolean checkfile(String path) {
		File file = new File(path);
		if (!file.isFile()) {
			return false;
		}
		return true;
	}
/**
 * 检测目录是否存在
 * 不存在就新建目录
 * @param path
 */
	public void checkPath(String path) {
		File file = new File(path);
		// 如果文件夹不存在则创建
		if (!file.exists() && !file.isDirectory()) {
			log.info("//目录不存在");
			file.mkdir();
		} else {
			log.info("//目录存在");
		}
	}

	private int checkContentType(String inputPath) {
		String type = inputPath.substring(inputPath.lastIndexOf(".") + 1, inputPath.length()).toLowerCase();
		// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
		if (type.equals("avi")) {
			return 0;
		} else if (type.equals("mpg")) {
			return 0;
		} else if (type.equals("wmv")) {
			return 0;
		} else if (type.equals("3gp")) {
			return 0;
		} else if (type.equals("mov")) {
			return 0;
		} else if (type.equals("mp4")) {
			return 0;
		} else if (type.equals("asf")) {
			return 0;
		} else if (type.equals("asx")) {
			return 0;
		} else if (type.equals("flv")) {
			return 0;
		}
		// 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
		// 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
		else if (type.equals("wmv9")) {
			return 1;
		} else if (type.equals("rm")) {
			return 1;
		} else if (type.equals("rmvb")) {
			return 1;
		}
		return 9;
	}

	/**
	 * 视频转码
	 * 
	 * @param ffmpegPath
	 *            转码工具的存放路径
	 * @param upFilePath
	 *            用于指定要转换格式的文件,要截图的视频源文件
	 * @param codcFilePath
	 *            格式转换后的的文件保存路径
	 * @param mediaPicPath
	 *            截图保存路径
	 * @return
	 * @throws Exception
	 */
	public boolean executeCodecs(String ffmpegPath, String upFilePath, String codcFilePath, String mediaPicPath)
			throws Exception {
		if (!checkfile(upFilePath)) {
			log.info(upFilePath + " is not file");
			return false;
		}
		// 创建一个List集合来保存转换视频文件为flv格式的命令
		List<String> convert = new ArrayList<String>();
		// windows
		convert.add(ffmpegPath); // 添加转换工具路径
		// linux
		// convert.add("ffmpeg");
		convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
		convert.add(upFilePath); // 添加要转换格式的视频文件的路径
		convert.add("-qscale"); // 指定转换的质量
		convert.add("4");// 4>6
		convert.add("-ab"); // 设置音频码率
		convert.add("64k");
		convert.add("-ac"); // 设置声道数
		convert.add("2");
		convert.add("-ar"); // 设置声音的采样频率
		convert.add("22050");
		convert.add("-r"); // 设置帧频
		convert.add("24");
		convert.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
		convert.add(codcFilePath);

		// 创建一个List集合来保存从视频中截取图片的命令
		List<String> cutpic = new ArrayList<String>();
		// windows
		cutpic.add(ffmpegPath); // 添加转换工具路径
		// linux
		// cutpic.add("ffmpeg");
		cutpic.add("-ss"); // 添加参数＂-ss＂，该参数指定截取的起始时间-ss作为输入选项的话要放在-i之前，当做输出选项的话放在输出文件之前。
		cutpic.add("1"); // 添加起始时间为第1秒
		cutpic.add("-i");
		cutpic.add(codcFilePath); // 同上（指定的文件即可以是转换为flv格式之前的文件，也可以是转换的flv文件）
		cutpic.add("-y");
		// cutpic.add("-f");
		// cutpic.add("image2");

		cutpic.add("-t"); // 添加参数＂-t＂，该参数指定持续时间
		cutpic.add("0.001"); // 添加持续时间为1毫秒
		// cutpic.add("-s"); // 添加参数＂-s＂，该参数指定截取的图片大小
		// cutpic.add("200*280"); // 添加截取的图片大小为350*240
		cutpic.add(mediaPicPath); // 添加截取的图片的保存路径

		boolean mark = true;
		ProcessBuilder builder = new ProcessBuilder();
		try {
			builder.command(convert);
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream stderr = process.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				log.info(line);
			stderr = process.getInputStream();
			isr = new InputStreamReader(stderr);
			br = new BufferedReader(isr);
			while ((line = br.readLine()) != null)
				log.info(line);
			process.waitFor();
			// process.destroy();
			builder.command(cutpic);
			builder.redirectErrorStream(true);
			// 如果此属性为 true，则任何由通过此对象的 start() 方法启动的后续子进程生成的错误输出都将与标准输出合并，
			// 因此两者均可使用 Process.getInputStream() 方法读取。这使得关联错误消息和相应的输出变得更容易
			process = builder.start();
			stderr = process.getErrorStream();
			isr = new InputStreamReader(stderr);
			br = new BufferedReader(isr);
			while ((line = br.readLine()) != null)
				log.info(line);
			stderr = process.getInputStream();
			isr = new InputStreamReader(stderr);
			br = new BufferedReader(isr);
			while ((line = br.readLine()) != null)
				log.info(line);
			process.waitFor();
			br.close();
		} catch (Exception e) {
			mark = false;
			log.error(e);
		}
		return mark;
	}

	@Override
	public void sqlClose() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				log.error(e);
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		if (stat != null) {
			try {
				stat.close();
			} catch (SQLException e) {
				log.error(e);
				try {
					stat.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}

		}
	}

	@Override
	public boolean deleteMediaTemp(int id) {
		SqlSession session = getSession();
		MediaMapper userMapper = session.getMapper(MediaMapper.class);
		try {
			return userMapper.deleteMediaTemp(id);
		} catch (Exception e) {
			log.error("media update");
			return false;
		} finally {
			log.info("session 关闭");
			session.close();
		}
	}

	@Override
	public void saveErr(Media media) {
		SqlSession session = getSession();
		MediaMapper userMapper = session.getMapper(MediaMapper.class);
		try {
			 userMapper.addMediaErr(media);
		} catch (Exception e) {
			log.error("media update");
		} finally {
			log.info("session 关闭");
			session.close();
		}
	}

	@Override
	public void saveTmp(Media media) {
		SqlSession session = getSession();
		MediaMapper userMapper = session.getMapper(MediaMapper.class);
		try {
			 userMapper.addMediaTemp(media);
		} catch (Exception e) {
			log.error("media update");
		} finally {
			log.info("session 关闭");
			session.close();
		}
		
	}

   

}

class PrintStream extends Thread {
	java.io.InputStream __is = null;

	public PrintStream(java.io.InputStream is) {
		__is = is;
	}

	public void run() {
		try {
			while (this != null) {
				int _ch = __is.read();
				if (_ch != -1)
					System.out.print((char) _ch);
				else
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}