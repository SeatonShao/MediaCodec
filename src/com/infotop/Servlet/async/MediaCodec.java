package com.infotop.Servlet.async;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;

import com.infotop.common.HttpRequest;
import com.infotop.constants.Constants;
import com.infotop.dao.DaoFactory;
import com.infotop.dao.MediaDao;
import com.infotop.entity.Media;
import com.infotop.mapping.CodecMapper;
import com.infotop.mybatis.MybatisDao;
import com.infotop.service.HDFSService;

/**
 * @author Administrator
 *
 */
public class MediaCodec implements Runnable {

	private boolean copy = Constants.mediaConfig.getCopyOn().intValue() == 1;
	private static Logger log = LogManager.getLogger(MediaCodec.class.getName());
	private int sleepTime = Constants.mediaConfig.getFgSleep().intValue();

	public List<Media> find(Media media) {
		SqlSession session = new MybatisDao<Object>().getSession();
		CodecMapper userMapper = session.getMapper(CodecMapper.class);
		try {
			return userMapper.findMedia(media);
		} catch (Exception e) {
			log.error("media find err", MediaCodec.class, e);
			return null;
		} finally {
			log.info("session 关闭", media, MediaCodec.class);
			session.close();
		}
	}

	/**
	 * 查询临时文件信息
	 * 
	 * @param remainder
	 * @return
	 */
	public List<Media> findMediaTemp(int remainder) {
		SqlSession session = new MybatisDao<Object>().getSession();
		CodecMapper userMapper = session.getMapper(CodecMapper.class);
		try {
			return userMapper.findMediaTemp(remainder);
		} catch (Exception e) {
			log.error("media find", MediaCodec.class, e);
			return null;
		} finally {
			log.info("session 关闭");
			session.close();
		}
	}

	public Media findTempCodec(String virtualName) {
		SqlSession session = new MybatisDao<Object>().getSession();
		CodecMapper userMapper = session.getMapper(CodecMapper.class);
		try {
			return userMapper.findTempCodec(virtualName);
		} catch (Exception e) {
			log.error("media find", MediaCodec.class, e);
			return null;
		} finally {
			log.info("session 关闭");
			session.close();
		}
	}

	public boolean updateMediaTemp(@Param("remainder") Integer remainder, @Param("sort") String sort,
			@Param("virtualName") String virtualName) {
		SqlSession session = new MybatisDao<Object>().getSession();
		CodecMapper userMapper = session.getMapper(CodecMapper.class);
		try {
			boolean result = userMapper.updateMediaTemp(remainder, sort, virtualName);
			return result;
		} catch (Exception e) {
			log.error("media update temp err", MediaCodec.class, e);
			return false;
		} finally {
			log.info("session 关闭");
			session.close();
		}
	}

	public Media findOneTemp(int id) {
		SqlSession session = new MybatisDao<Object>().getSession();
		CodecMapper userMapper = session.getMapper(CodecMapper.class);
		try {
			return userMapper.findOneTemp(id);
		} catch (Exception e) {
			log.error("media find", MediaCodec.class, e);
			return null;
		} finally {
			log.info("session 关闭");
			session.close();
		}
	}

	/*
	 * 每天执行一次转码
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		while (true) {
			Media media = findTempCodec(Constants.VIRTUAL_NAME);
			int i = 1;
			if (media != null && i < 60) {
				try {
					i++;
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			} else {
				if (updateMediaTemp(Constants.mediaConfig.getRows().intValue(), Constants.mediaConfig.getSort(),
						Constants.VIRTUAL_NAME)) {
					media = findTempCodec(Constants.VIRTUAL_NAME);
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						log.error("修改temp表状态失败", MediaCodec.class, e);
					}
				}
				
					if (media == null) {
						Calendar calendar = Calendar.getInstance();
						while (sleepTime != calendar.get(Calendar.SECOND) % 10) {
							calendar = Calendar.getInstance();
							try {
								Thread.sleep(900);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						startCodec(media);
						System.out.println(
								"???????????????????????????????????????????????????????????????????????????????????");
						System.out.println(
								"???????????????????????????????????????????????????????????????????????????????????");
						System.out.println(
								"???????????????????????????????????????????????????????????????????????????????????");
						System.out.println(
								"???????????????????????????????????????????????????????????????????????????????????");
					}
				
			}
		}
	}

	public void startCodec(Media media) {
		MediaDao mediaDao = DaoFactory.getMediaDao();
		Map<String, String> keys = null;
		try {
			keys = codec(media);
			String param = "mediaUUid=" + media.getMediaName() + "&status=success";
			if (keys != null && !keys.isEmpty()) {
				param += "&fileName=" + keys.get("fileName") + "&duration=" + keys.get("duration");
			}

			HttpRequest.sendPost(media.getTitle(), param);
		} catch (Exception e) {
			mediaDao.saveErr(media);
			mediaDao.deleteMediaTemp(media.getId());
			String param = "mediaUUid=" + media.getMediaName() + "&status=failure";
			if (keys != null && !keys.isEmpty()) {
				param += "&fileName=" + keys.get("fileName") + "&duration=" + keys.get("duration").trim();
			}
			HttpRequest.sendPost(media.getTitle(), param);
			log.error("转码失败！", e);
			log.error("url", "http://" + Constants.mediaConfig.getRemoteIp() + ":"
					+ Constants.mediaConfig.getRemotePort() + Constants.mediaConfig.getRemoteStatus());
			log.error("param", param);
		}
	}

	/**
	 * 转码并保存到数据库，删除临时表数据，错误写入错误表
	 * 
	 * @param media
	 * @throws IOException
	 * @throws InterruptedException 
	 * @throws Exception
	 */
	public Map<String, String> codec(Media media) throws IOException, InterruptedException {
		MediaDao mediaDao = DaoFactory.getMediaDao();
		FileSystem fs = null;
		Path src = new Path(Constants.getSrcPath(media.getMediaName(), media.getContentType()));
		String localSrc = Constants.getLocalPath(media.getMediaName(), media.getContentType());
		Path local = new Path(localSrc);
		Map<String, String> keys = null;

		fs = FileSystem.get(HDFSService.conf);
		fs.copyToLocalFile(src, local);

		keys = mediaPro(Constants.getFFprobePath(), localSrc);

		if (!(0 == Constants.mediaConfig.getFgBiteMin().intValue())) {
			media2ts(media, "biteMin");
			if (1 == Constants.mediaConfig.getFgTs().intValue()) {
				hdfsTs2m3u8Auto(media, "biteMin");
			}
		}
		if (!(0 == Constants.mediaConfig.getFgBiteMid().intValue())) {
			media2ts(media, "biteMid");
			if (1 == Constants.mediaConfig.getFgTs().intValue()) {
				hdfsTs2m3u8Auto(media, "biteMid");
			}
		}
		if (!(0 == Constants.mediaConfig.getFgBiteMax().intValue())) {
			media2ts(media, "biteMax");
			if (1 == Constants.mediaConfig.getFgTs().intValue()) {
				hdfsTs2m3u8Auto(media, "biteMax");
			}
		}
		if (!(0 == Constants.mediaConfig.getFgBiteSup().intValue())) {
			media2ts(media, "biteSup");
			if (1 == Constants.mediaConfig.getFgTs().intValue()) {
				hdfsTs2m3u8Auto(media, "biteSup");
			}
		}
		getPic(media);

		new File(localSrc).delete();
		Constants constants = new Constants();
		media.setStatus("2");
		media.setMediaPath(Constants.getUploadPath(media.getName(), media.getMediaName(), media.getContentType()));
		media.setPicPath(constants.getHDFSPicPath(media.getMediaName()));
		media.setTitle("");
		media.setBiteMax("/" + media.getMediaName() + "/biteMax/" + "biteMax" + media.getMediaName() + ".m3u8");
		media.setBiteMid("/" + media.getMediaName() + "/biteMid/" + "biteMid" + media.getMediaName() + ".m3u8");
		media.setBiteMin("/" + media.getMediaName() + "/biteMin/" + "biteMin" + media.getMediaName() + ".m3u8");
		media.setBiteSup("/" + media.getMediaName() + "/biteSup/" + "biteSup" + media.getMediaName() + ".m3u8");
		mediaDao.updateMedia(media);
		log.info("更新视频信息");
		mediaDao.deleteMediaTemp(media.getId());
		log.info("删除视频临时信息");
		return keys;
	}

	/**
	 * 视频转码ts文件
	 * 
	 * @param ffmpegPath
	 *            转码工具的存放路径
	 * @param upFilePath
	 *            用于指定要切片转换格式的文件,要截图的视频源文件
	 * @param codcFilePath
	 *            格式转换后的的文件保存路径
	 * @param name
	 *            文件名
	 * @param mediaPicPath
	 *            截图保存路径
	 * @param bite
	 * @return
	 * @throws Exception
	 */
	public void media2ts(Media media, String bite) {
		Constants constants = new Constants(bite);
		FileSystem fs = null;
		try {
			fs = FileSystem.get(HDFSService.conf);
		} catch (IOException e1) {
			log.error(e1);
			return;
		}
		String localSrc = Constants.getLocalPath(media.getMediaName(), media.getContentType());
		String ffmpegPath = Constants.getFFmpegPath();
		Path hdfsPath = new Path(constants.getHdfsPath(media.getMediaName(), bite));
		String codecPath = Constants.getCodecPath(media.getMediaName(), bite);
		// 创建一个List集合来保存转换视频文件为flv格式的命令
		List<String> convert = new ArrayList<String>();
		convert.add(ffmpegPath); // 添加转换工具路径
		convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
		convert.add(localSrc); // 添加要转换格式的视频文件的路径
		// convert.add("-profile:v");
		// convert.add("main");
		convert.add("-threads"); // 多线程加速处理
		log.info(new JSONObject(Constants.mediaConfig).toString());
		convert.add(Constants.mediaConfig.getFgThread().intValue() + "");
		convert.add("-b:v"); // 设置基本码率
		log.info("-b:v" + constants.getBITE());
		convert.add(constants.getBITE() + "k");
		convert.add("-bufsize"); // 设置码率缓冲
		log.info("-bufsize" + constants.getBITE_BUF());
		convert.add(constants.getBITE_BUF() + "k");
		convert.add("-maxrate"); // 设置码率上限
		log.info("-maxrate");
		convert.add(constants.getBITE() + "k");
		convert.add("-minrate"); // 设置码率下限
		convert.add(constants.getBITE() + "k");
		convert.add("-vf"); // 与-s同等效果
		// 设置视频宽高 缩小速度提高，放大速度降低，设置等比缩放更快,
		// 600:600/a会出现高度不能被2整除错误,高度宽度设置-1会不行 -2测试中
		convert.add(Constants.mediaConfig.getFgVf());// constants.getSIZE()

		convert.add("-qmax");// 最差质量
		convert.add("30");//
		convert.add("-g");
		// 图片组，又为最大关键帧间隔
		convert.add(Constants.mediaConfig.getR().intValue() * 4 + "");
		convert.add("-keyint_min"); // // 最小关键帧间隔
		convert.add((Constants.mediaConfig.getR().intValue() * 4 - 10) + "");
		// convert.add("-bf"); // b帧策略 -1自动调节 默认 0
		// convert.add("-1");
		if (copy) {

		} else {
			convert.add("-r"); // 设置帧频
			convert.add(Constants.mediaConfig.getR().toString());//
			convert.add("-c:v"); // 设置视频编码
			if (Constants.mediaConfig.getFgCvOn().intValue() == 1) {
				convert.add(Constants.mediaConfig.getFgCv());
			} else {
				convert.add("copy");
			}
			convert.add("-pix_fmt"); // 设置yuv格式，ckplayer播放器下设置错误会显示不出图像
			convert.add("yuv420p");
			convert.add("-qcomp");// vbv buffer underflow
			convert.add("1");

			convert.add("-c:a"); // 设置音频编码
			if (Constants.mediaConfig.getFgCaOn().intValue() == 1) {
				convert.add(Constants.mediaConfig.getFgCa());
			} else {
				convert.add("copy");
			}
		}
		convert.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
		convert.add(codecPath);
		ProcessBuilder builder = new ProcessBuilder();
		try {
			builder.command(convert);
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream stderr = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				log.info(line);
			process.waitFor();
			stderr.close();
			isr.close();
			br.close();
			File file = new File(codecPath);
			if (file.exists()) {
				/*
				 * if (1 == Constants.mediaConfig.getFgTs().intValue()) { //
				 * 要切片不删除本地文件 fs.copyFromLocalFile(false, true, new
				 * Path(codecPath), hdfsPath); } else {
				 */
				// 不切片不删除本地文件
				fs.copyFromLocalFile(false, true, new Path(codecPath), hdfsPath);
				// }
			}
			// file.delete();
		} catch (Exception e) {
			log.error(e);
			return;
		}
	}

	/**
	 * 视频ts文件切片
	 * 
	 * @param ffmpegPath
	 *            转码工具的存放路径
	 * @param filePath
	 *            格式转换后的的文件保存路径
	 * @param name
	 *            文件名
	 * @param fileType
	 *            文件后缀
	 * @return
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws Exception
	 */
	public static boolean hdfsTs2m3u8Auto(Media media, String type) throws IOException, InterruptedException {

		return hdfsTs2m3u8(media.getMediaName(), type, 1);
	}

	/**
	 * 视频ts文件切片
	 * 
	 * @param ffmpegPath
	 *            转码工具的存放路径
	 * @param filePath
	 *            格式转换后的的文件保存路径
	 * @param name
	 *            文件名
	 * @param fileType
	 *            文件后缀
	 * @param auto
	 *            1自动切片
	 * @return
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws Exception
	 */
	public static boolean hdfsTs2m3u8(String mediaName, String type, int auto) throws IOException, InterruptedException {
		FileSystem fs = null;
		
			fs = FileSystem.get(HDFSService.conf);
		
		String m3u8Path = Constants.getM3u8Path(mediaName, type);
		String path = Constants.getHdfsPath(mediaName, type);// "/" + mediaName
																// + "/" + type
																// + "/" + type
																// + mediaName +
																// ".mp4";
		File local = new File(m3u8Path + "/" + type + mediaName + ".mp4");
		if (!local.exists()) {
			try {
				fs.copyToLocalFile(new Path(path), new Path(m3u8Path));
			} catch (IOException e2) {
				log.error(e2);
				return false;
			}
		}
		String inputFile = Constants.getLocalTSPath(mediaName, type);
		// 创建一个List集合来保存转换视频文件为flv格式的命令
		List<String> convert = new ArrayList<String>();
		convert.add(Constants.getFFmpegPath()); // 添加转换工具路径
		convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
		convert.add(inputFile); // 添加要转换格式的视频文件的路径

		convert.add("-c:v");
		convert.add("copy");
		convert.add("-c:a");
		convert.add("copy");
		convert.add("-f");
		convert.add("hls");
		// convert.add("-hls_base_url");
		// convert.add(Constants.mediaConfig.getFgBaseUrl());
		// convert.add("-hls_key_info_file");
		// convert.add(Constants.CLASSES_PATH + "/key");
		convert.add("-hls_list_size");
		convert.add("" + Constants.mediaConfig.getFgHlsSize().intValue());
		convert.add("-hls_time");
		convert.add("" + Constants.mediaConfig.getFgHlsTime().intValue());
		// convert.add("-hls_flags");
		// convert.add("split_by_time");//官网说可能会更糟，但可以切得更均匀，不按关键帧来切而是按切片时间长度，导致切入点黑屏因为没有关键帧p帧会无法参照
		convert.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
		convert.add("-hls_segment_filename"); // ffmpeg in.nut
		// -hls_segment_filename
		// 'file%03d.ts' out.m3u8
		// This example will produce the playlist, out.m3u8, and segment files:
		// file000.ts, file001.ts, file002.ts, etc.
		convert.add(m3u8Path + "/" + type + mediaName + "%04d.ts");
		convert.add(m3u8Path + "/" + type + mediaName + ".m3u8");
		ProcessBuilder builder = new ProcessBuilder();
		
			builder.command(convert);
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream stderr = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				log.info(line);
			process.waitFor();
			stderr.close();
			isr.close();
			br.close();

			File root = new File(m3u8Path);
			File[] files = root.listFiles();
			for (File file : files) {
				log.info(file.getAbsolutePath());
				if (file.isDirectory()) {
					file.delete();
				} else {
					String name = file.getName();
					if (name.endsWith(".ts") || name.endsWith(".m3u8")) {
						fs.copyFromLocalFile(true, true, new Path(file.getPath()),
								new Path(Constants.getHDFSM3u8Path(mediaName, type) + name));
						log.info("copy " + name + " from: " + file.getPath() + " to "
								+ Constants.getHDFSM3u8Path(mediaName, type));
					} 
				}
			}
			
			return true;
	}

	public void getPic(Media media) throws IOException {
		FileSystem fs = FileSystem.get(HDFSService.conf);
		Constants constants = new Constants();
		String ffmpegPath = Constants.getFFmpegPath();
		Path hdfsPicPath = new Path(constants.getHDFSPicPath(media.getMediaName()));
		if (fs.exists(hdfsPicPath)) {
			return;
		}
		String localSrc = Constants.getLocalPath(media.getMediaName(), media.getContentType());
		// String uploadPath = constants.getUploadPath(media.getName(),
		// media.getMediaName(), media.getContentType());
		String pic = constants.getPicPath(media.getMediaName());
		// 创建一个List集合来保存从视频中截取图片的命令
		List<String> cutpic = new ArrayList<String>();
		cutpic.add(ffmpegPath);
		cutpic.add("-i");
		cutpic.add(localSrc); // 同上（指定的文件即可以是转换为flv格式之前的文件，也可以是转换的flv文件）
		cutpic.add("-y");
		cutpic.add("-f");
		cutpic.add("image2");
		cutpic.add("-pix_fmt"); // 设置yuv格式，ckplayer播放器下设置错误会显示不出图像
		cutpic.add("yuv420p");
		cutpic.add("-ss"); // 添加参数＂-ss＂，该参数指定截取的起始时间
		cutpic.add("1"); // 添加起始时间为第17秒
		cutpic.add("-t"); // 添加参数＂-t＂，该参数指定持续时间
		cutpic.add("0.001"); // 添加持续时间为1毫秒
		cutpic.add("-vf"); // 添加参数＂-s＂，该参数指定截取的图片大小
		cutpic.add(Constants.mediaConfig.getFgVf()); // 添加截取的图片大小为350*240
		cutpic.add(pic); // 添加截取的图片的保存路径
		ProcessBuilder builder = new ProcessBuilder();
		try {
			// process.destroy();
			builder.command(cutpic);
			builder.redirectErrorStream(true);
			// 如果此属性为 true，则任何由通过此对象的 start() 方法启动的后续子进程生成的错误输出都将与标准输出合并，
			// 因此两者均可使用 Process.getInputStream() 方法读取。这使得关联错误消息和相应的输出变得更容易
			Process process = builder.start();

			InputStream stderr = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				log.info(line);
			process.waitFor();

			fs.copyFromLocalFile(true, true, new Path(pic), hdfsPicPath);
			
			File root = new File(Constants.getM3u8Path(media.getMediaName(), media.getContentType()));
			File[] files = root.listFiles();
			for (File file : files) {
				log.info(file.getAbsolutePath());
				file.delete();
			}
			root.delete();
			stderr.close();
			isr.close();
			br.close();
		} catch (Exception e) {
			log.error(e);
			return;
		}
	}

	public static void getHdfsPic(Media media, String contentType) throws IOException {
		FileSystem fs = FileSystem.get(HDFSService.conf);
		Constants constants = new Constants();
		String ffmpegPath = Constants.getFFmpegPath();
		Path hdfsPicPath = new Path(constants.getHDFSPicPath(media.getMediaName()));
		if (fs.exists(hdfsPicPath)) {
			return;
		}
		String localSrc = Constants.getUploadPath(null, media.getMediaName(), contentType);
		// String uploadPath = constants.getUploadPath(media.getName(),
		// media.getMediaName(), media.getContentType());
		String pic = constants.getPicPath(media.getMediaName());
		// 创建一个List集合来保存从视频中截取图片的命令
		List<String> cutpic = new ArrayList<String>();
		cutpic.add(ffmpegPath);
		cutpic.add("-i");
		cutpic.add(localSrc); // 同上（指定的文件即可以是转换为flv格式之前的文件，也可以是转换的flv文件）
		cutpic.add("-y");
		cutpic.add("-f");
		cutpic.add("image2");
		cutpic.add("-pix_fmt"); // 设置yuv格式，ckplayer播放器下设置错误会显示不出图像
		cutpic.add("yuv420p");
		cutpic.add("-ss"); // 添加参数＂-ss＂，该参数指定截取的起始时间
		cutpic.add("1"); // 添加起始时间为第17秒
		cutpic.add("-t"); // 添加参数＂-t＂，该参数指定持续时间
		cutpic.add("0.001"); // 添加持续时间为1毫秒
		cutpic.add("-vf"); // 添加参数＂-s＂，该参数指定截取的图片大小
		cutpic.add(Constants.mediaConfig.getFgVf()); // 添加截取的图片大小为350*240
		cutpic.add(pic); // 添加截取的图片的保存路径
		ProcessBuilder builder = new ProcessBuilder();
		try {
			// process.destroy();
			builder.command(cutpic);
			builder.redirectErrorStream(true);
			// 如果此属性为 true，则任何由通过此对象的 start() 方法启动的后续子进程生成的错误输出都将与标准输出合并，
			// 因此两者均可使用 Process.getInputStream() 方法读取。这使得关联错误消息和相应的输出变得更容易
			Process process = builder.start();

			InputStream stderr = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				log.info(line);
			process.waitFor();
			stderr.close();
			isr.close();
			br.close();

			fs.copyFromLocalFile(new Path(pic), hdfsPicPath);
			File file = new File(pic);
			file.delete();
		} catch (Exception e) {
			log.info(e);
			e.printStackTrace();
		}
	}

	public static Map<String, String> mediaPro(String ffprobePath, String upFilePath) {

		// 创建一个List集合来保存转换视频文件为flv格式的命令
		List<String> convert = new ArrayList<String>();
		convert.add(ffprobePath); // 添加转换工具路径
		convert.add(upFilePath); // 添加要转换格式的视频文件的路径
		Map<String, String> keys = new HashMap<String, String>();
		ProcessBuilder builder = new ProcessBuilder();
		try {
			builder.command(convert);
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream stderr = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				log.info(line);
				getDuration(line, keys);
			}

			process.waitFor();
			stderr.close();
			isr.close();
			br.close();
		} catch (Exception e) {
			log.error(e);
		}
		return keys;
	}

	/**
	 * 获取视频信息 时长 视频名
	 * 
	 * @param line
	 */
	private static void getDuration(String line, Map keys) {
		if (line.contains("Duration:")) {
			String duration = line.substring(line.indexOf("Duration:") + 9, line.indexOf(","));
			log.info(duration);
			keys.put("duration", duration);
		}
		if (line.contains("Input #0")) {
			String fileName = line.substring(line.lastIndexOf("/") + 1, line.lastIndexOf(":") - 1);
			log.info(fileName);
			keys.put("fileName", fileName);
		}
	}

}
