package com.infotop.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.HAUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infotop.constants.Constants;
import com.infotop.service.HDFSService;

public class FFMPEGTest {
	private static Logger log = LogManager.getLogger(FFMPEGTest.class.getName());
	
	private static String ffmpegPath = "d:/ffmpeg/bin/ffmpeg.exe";
	public static void pushStream(String path){
		// 创建一个List集合来保存转换视频文件为flv格式的命令
				List<String> convert = new ArrayList<String>();
				convert.add(ffmpegPath); // 添加转换工具路径
				convert.add("-re");
				convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
				convert.add("http://192.168.10.232:50070/webhdfs/v1"+path +"?op=OPEN"); // 添加要转换格式的视频文件的路径
				convert.add("-c:v");
				convert.add("h264");
				convert.add("-c:a");
				convert.add("aac");
				convert.add("-b:v"); // 设置基本码率
				convert.add(512 + "k");
				convert.add("-bufsize"); // 设置码率缓冲，缓存越小码率越小
				convert.add(512 + "k");
				convert.add("-maxrate");
				// 设置码率上限
				convert.add(512 + "k");
				convert.add("-minrate"); // 设置码率下限
				convert.add(512 + "k");
				convert.add("-pix_fmt");
				// 设置yuv格式，ckplayer播放器下设置错误会显示不出图像
				convert.add("yuv420p");
				convert.add("-y"); 
				/*convert.add("-qcomp");// 0-1，1最大的减少警告，提高质量， 指定视频量化器压缩系数，默认0.6
				// vbv buffer underflow
				convert.add("1"); //
				convert.add("-vf");// 与-s同等效果
				// 设置视频宽高 缩小速度提高，放大速度降低，设置等比缩放更快,
				// 600:600/a会出现高度不能被2整除错误,高度设置-1会自动转为被2除整数，宽度不行
				convert.add("scale=-2:720");// constants.getSIZE()
				convert.add("-r");// 设置帧频,输出流信息显示修改，测试为帧频修改,不影响时长
				convert.add("15");//
				convert.add("-c:v");
				convert.add("h264");
				convert.add("-c:a");
				convert.add("aac");
				convert.add("-b:v"); // 设置基本码率
				convert.add(512 + "k");
				convert.add("-bufsize"); // 设置码率缓冲，缓存越小码率越小
				convert.add(256 + "k");
				convert.add("-maxrate");
				// 设置码率上限
				convert.add(512 + "k");
				convert.add("-minrate"); // 设置码率下限
				convert.add(512 + "k");
				convert.add("-pix_fmt");
				// 设置yuv格式，ckplayer播放器下设置错误会显示不出图像
				convert.add("yuv420p");*/
				// -hls_segment_filename
				// 'file%03d.ts' out.m3u8
				// This example will produce the playlist, out.m3u8, and segment files:
				// file000.ts, file001.ts, file002.ts, etc.
				convert.add("D:/video"+path.substring(0,path.indexOf("."))+".mp4" );

				boolean mark = true;
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
				} catch (Exception e) {
					mark = false;
					log.info(e);
					e.printStackTrace();
				}
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
	 * @return
	 * @throws Exception
	 */
	public static boolean media2M3u8(String ffmpegPath, String upFilePath, String codcPath) throws Exception {
		
		// 创建一个List集合来保存转换视频文件为flv格式的命令
		List<String> convert = new ArrayList<String>();
		convert.add(ffmpegPath); // 添加转换工具路径
		//convert.add("-re");
		convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
		convert.add(upFilePath); // 添加要转换格式的视频文件的路径
		convert.add("-c:v");
		convert.add("copy");
		convert.add("-c:a");
		convert.add("copy");
		convert.add("-f");
		convert.add("hls");
        //convert.add("-hls_key_info_file");
        //convert.add("d:/ffmpeg/bin/key");
		convert.add("-hls_allow_cache");
		convert.add("1");
        convert.add("-hls_base_url");
        convert.add("http://192.168.10.231:50070/webhdfs/v1/");

		convert.add("-hls_list_size");
		convert.add("2500");
		convert.add("-hls_time");
		convert.add("4");
		//convert.add("-hls_flags");
		//convert.add("split_by_time");//官网说可能会更糟，但可以切得更均匀，不按关键帧来切而是按切片时间长度
		convert.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
		convert.add("-hls_segment_filename"); // ffmpeg in.nut
		// -hls_segment_filename
		// 'file%03d.ts' out.m3u8
		// This example will produce the playlist, out.m3u8, and segment files:
		// file000.ts, file001.ts, file002.ts, etc.
		convert.add("D:/flazr/home/apps/vod/" + codcPath + "%03d.ts");
		convert.add("D:/flazr/home/apps/vod/" + codcPath+ ".m3u8");

		boolean mark = true;
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
		} catch (Exception e) {
			mark = false;
			log.info(e);
			e.printStackTrace();
		}
		return mark;
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
	 * @return
	 * @throws Exception
	 */
	public static boolean segment(String ffmpegPath, String upFilePath, String codcPath) throws Exception {
		
		// 创建一个List集合来保存转换视频文件为flv格式的命令
		List<String> convert = new ArrayList<String>();
		convert.add(ffmpegPath); // 添加转换工具路径
		//convert.add("-re");
		convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
		convert.add(upFilePath); // 添加要转换格式的视频文件的路径
		convert.add("-c:v");
		convert.add("copy");
		convert.add("-c:a");
		convert.add("copy");
		convert.add("-map");
		convert.add("0");
        convert.add("-f");
        convert.add("segment");
//		convert.add("-hls_allow_cache");
//		convert.add("1");
//        convert.add("-hls_base_url");
//        convert.add("http://192.168.10.231:50070/webhdfs/v1/");

		convert.add("-segment_list_size");
		convert.add("2500");
		convert.add("-segment_time");
		convert.add("10");
		//convert.add("-hls_flags");
		//convert.add("-segment_list");//官网说可能会更糟，但可以切得更均匀，不按关键帧来切而是按切片时间长度
		convert.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
		//convert.add("-segment_list_flags"); // ffmpeg in.nut
		//convert.add("+live");
		// -hls_segment_filename
		// 'file%03d.ts' out.m3u8
		// This example will produce the playlist, out.m3u8, and segment files:
		// file000.ts, file001.ts, file002.ts, etc.
		convert.add("-segment_list_type");
		convert.add("m3u8");
		convert.add("-segment_list");
		convert.add("f:/vod/" + codcPath+ ".m3u8");
		convert.add("f:/vod/" + codcPath + "%03d.ts");
		boolean mark = true;
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
		} catch (Exception e) {
			mark = false;
			log.info(e);
			e.printStackTrace();
		}
		return mark;
	}
public static boolean mediaPro(String ffprobePath, String upFilePath) throws Exception {
		
		// 创建一个List集合来保存转换视频文件为flv格式的命令
		List<String> convert = new ArrayList<String>();
		convert.add(ffprobePath); // 添加转换工具路径
		convert.add(upFilePath); // 添加要转换格式的视频文件的路径
		
		boolean mark = true;
		ProcessBuilder builder = new ProcessBuilder();
		try {
			builder.command(convert);
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream stderr = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null){
				log.info(line);
				getDuration(line);
			}
				
			process.waitFor();
			stderr.close();
			isr.close();
			br.close();
		} catch (Exception e) {
			mark = false;
			log.info(e);
			e.printStackTrace();
		}
		return mark;
	}
/**
 * 获取视频信息 时长 视频名 
 * @param line
 */
	private static void getDuration(String line) {
	if(line.contains("Duration:")){
		log.info(line.substring(line.indexOf("Duration:")+9,line.indexOf(",")));
	}
	if(line.contains("Input #0")){
		log.info(line.substring(line.lastIndexOf("/")+1,line.lastIndexOf(":")-1));
	}
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
	 * @return
	 * @throws Exception
	 */
	public static boolean media2ts(String ffmpegPath, String upFilePath, String codcPath) throws Exception {
		//Constants constants = new Constants("biteMin");
		// 创建一个List集合来保存转换视频文件为flv格式的命令
		List<String> convert = new ArrayList<String>();
		convert.add(ffmpegPath); // 添加转换工具路径
		convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
		convert.add(upFilePath); // 添加要转换格式的视频文件的路径
		//convert.add("-bsf"); convert.add("h264_mp4toannexb");
		convert.add("-threads"); // 多线程加速处理
		convert.add("2");
		//convert.add("-profile:v");
		//convert.add("main");
		//convert.add("-rc_strategy"); // 设置基本码率
		//convert.add("2");
		convert.add("-g"); // 图片组，又为最大关键帧间隔
		convert.add("60");
		convert.add("-bf"); // b帧策略 -1自动调节 默认 0 
		convert.add("-1");
		convert.add("-keyint_min"); // 最小关键帧间隔
		convert.add("50");
		convert.add("-b:v"); // 设置基本码率
		convert.add(512 + "k");
		convert.add("-bufsize"); // 设置码率缓冲，缓存越小码率越小
		convert.add(512 + "k");
		convert.add("-maxrate");
		// 设置码率上限
		convert.add(512 + "k");
		convert.add("-minrate"); // 设置码率下限
		convert.add(512 + "k");
		//convert.add("-qmax");// 最差质量
		//convert.add("50");//
		// convert.add("-level");
		// convert.add("31");//
		//convert.add("-qdiff");/// 指定固定量化器因子允许的最大偏差,越大越好用默认4速度略有影响反比例影响q值
		//convert.add("2");
		//convert.add("-qblur");/// 指定量化器模糊系数,越大使得码率在时间上分配的越平均，大了q(质量)反而高，速度慢
		//convert.add("0");
		convert.add("-qcomp");// 0-1，1最大的减少警告，提高质量， 指定视频量化器压缩系数，默认0.6
		// vbv buffer underflow
		convert.add("1"); //
		convert.add("-vf");// 与-s同等效果
		// 设置视频宽高 缩小速度提高，放大速度降低，设置等比缩放更快,
		// 600:600/a会出现高度不能被2整除错误,高度设置-1会自动转为被2除整数，宽度不行
		convert.add("scale=-2:768");// constants.getSIZE()
		convert.add("-r");// 设置帧频,输出流信息显示修改，测试为帧频修改,不影响时长
		convert.add("15");//
		convert.add("-c:v");// 设置视频编码
		convert.add("h264");
		
		convert.add("-pix_fmt");
		// 设置yuv格式，ckplayer播放器下设置错误会显示不出图像
		convert.add("yuv420p");
		convert.add("-c:a"); // 设置音频编码
		//convert.add("copy");
		// 设置音频编码
		convert.add("aac");
		
		/*
		convert.add("-ar");
		// 设置音频采样率
		convert.add("44100");
		convert.add("-ac");
		// 设置声道数影像码率
		convert.add("3");*/
		convert.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
		convert.add(codcPath);
		boolean mark = true;
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
		} catch (Exception e) {
			mark = false;
			log.info(e);
			e.printStackTrace();
		}
		return mark;
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
	 * @return
	 * @throws Exception
	 */
	public static boolean media2flv(String ffmpegPath, String upFilePath, String codcPath) throws Exception {
		//Constants constants = new Constants("biteMin");
		// 创建一个List集合来保存转换视频文件为flv格式的命令
		List<String> convert = new ArrayList<String>();
		convert.add(ffmpegPath); // 添加转换工具路径
		convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
		convert.add(upFilePath); // 添加要转换格式的视频文件的路径
		//convert.add("-bsf"); convert.add("h264_mp4toannexb");
		convert.add("-threads"); // 多线程加速处理
		convert.add("4");
		//convert.add("-profile:v");
		//convert.add("main");
		//convert.add("-preset"); // 设置基本码率
		//convert.add(  "slow");
		convert.add("-b:v"); // 设置基本码率
		convert.add(512 + "k");
		convert.add("-bufsize"); // 设置码率缓冲，缓存越小码率越小
		convert.add(256 + "k");
		convert.add("-maxrate");
		// 设置码率上限
		convert.add(512 + "k");
		convert.add("-minrate"); // 设置码率下限
		convert.add(512 + "k");
		//convert.add("-qmax");// 最差质量
		//convert.add("50");//
		// convert.add("-level");
		// convert.add("31");//
		//convert.add("-qdiff");/// 指定固定量化器因子允许的最大偏差,越大越好用默认4速度略有影响反比例影响q值
		//convert.add("2");
		//convert.add("-qblur");/// 指定量化器模糊系数,越大使得码率在时间上分配的越平均，大了q(质量)反而高，速度慢
		//convert.add("0");
		convert.add("-qcomp");// 0-1，1最大的减少警告，提高质量， 指定视频量化器压缩系数，默认0.6
		// vbv buffer underflow
		convert.add("1"); //
		convert.add("-vf");// 与-s同等效果
		// 设置视频宽高 缩小速度提高，放大速度降低，设置等比缩放更快,
		// 600:600/a会出现高度不能被2整除错误,高度设置-1会自动转为被2除整数，宽度不行
		convert.add("scale=-2:720");// constants.getSIZE()
		convert.add("-r");// 设置帧频,输出流信息显示修改，测试为帧频修改,不影响时长
		convert.add("15");//
		convert.add("-c:v");// 设置视频编码
		convert.add("h264");
		
		convert.add("-pix_fmt");
		// 设置yuv格式，ckplayer播放器下设置错误会显示不出图像
		convert.add("yuv420p");
		convert.add("-c:a"); // 设置音频编码
		//convert.add("copy");
		// 设置音频编码
		convert.add("aac");
		
		/*
		convert.add("-ar");
		// 设置音频采样率
		convert.add("44100");
		convert.add("-ac");
		// 设置声道数影像码率
		convert.add("3");*/
		convert.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
		convert.add(codcPath);
		boolean mark = true;
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
		} catch (Exception e) {
			mark = false;
			log.info(e);
			e.printStackTrace();
		}
		return mark;
	}
	public static void main(String[] args) {
		try {
			//String m3u8Path = getM3u8Path("5912e157-0b6c-4f20-9ac0-a331cfe3481c", "minb");
			//String inputFile =  getTSPath("5912e157-0b6c-4f20-9ac0-a331cfe3481c", "minb");
			// media2M3u8("d:/ffmpeg/bin/ffmpeg.exe","f:/4666.mp4", "46667");
			 //pushStream("/4666.flv");
			media2ts("d:/ffmpeg/bin/ffmpeg.exe", "f:/46.mp4", "f:/4666.mp4");
			segment("d:/ffmpeg/bin/ffmpeg.exe", "f:/4666.mp4", "4666");
			//mediaPro("d:/ffmpeg/bin/ffprobe.exe",  getUploadPath("","1095346ccc974c899de3205ec2f8afb5",".mp4"));//"f:/108.mp4"
			//media2flv("d:/ffmpeg/bin/ffmpeg.exe", "f:/46.mp4", "f:/4666.flv");
			//log.info(FFMPEGTest.class.getClassLoader().getSystemResource("/"));
		} catch (Exception e) {
			e.printStackTrace();
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
            log.error("获取活动namenode失败",e);
            return null;
        }
    }
	/**
     * Get host IP address
     *
     * @return IP Address
     */
    private static InetAddress getAddress() {
        try {
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    return addresses.nextElement();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
	private static String getTSPath(String mediaName, String type) {
		return "http://" + Constants.getActiveAddress() + ":50070/webhdfs/v1/" + mediaName + "/" + type + "/" + mediaName
				+ ".mp4?op=OPEN"; 
	}
	
	private static  String getM3u8Path(String mediaName, String bite) {
		String path = null;
		if (System.getProperties().getProperty("os.name").toLowerCase().startsWith("win")) {
			path = "d:/file/" + mediaName + "/" + bite;
			log.info(path);
		} else {
			path = "/root" + "/file/" + mediaName + "/" + bite;
			log.info(path);
		}
		checkPath(path);
		return path;
	}

	private static void checkPath(String path) {
		if (path.contains("/")) {
			checkPath(path.substring(0, path.lastIndexOf("/")));
		}
		File f = new File(path);
		if (!f.exists())
			f.mkdir();
	}
}
