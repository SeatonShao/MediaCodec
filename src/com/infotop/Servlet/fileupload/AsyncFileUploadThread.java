package com.infotop.Servlet.fileupload;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.infotop.common.HttpRequest;
import com.infotop.constants.Constants;
import com.infotop.dao.DaoFactory;
import com.infotop.dao.MediaDao;
import com.infotop.entity.Media;
import com.infotop.service.HDFSService;

public class AsyncFileUploadThread implements Runnable {
	private AsyncContext asyncContext;
	private static Logger log = LogManager.getLogger(AsyncFileUploadThread.class.getName());

	
	public AsyncFileUploadThread(AsyncContext asyncContext) {
		this.asyncContext = asyncContext;
		asyncContext.addListener(new AsyncFileUploadListener() );
	}
	
	@Override
	public void run(){
		HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
		HttpServletRequest request = (HttpServletRequest)asyncContext.getRequest();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 转码工具路径
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 获取原文件类型
		Media media = new Media();
		
		List<FileItem> items = null;
		
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			log.error(e1);
			out.write("error");
			asyncContext.complete();
			return;
		}
		String targetuuid = "";
		String createdUserId = "";
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e1) {
			log.error("FileUploadException出错", AsyncFileUploadThread.class, e1);
			asyncContext.complete();
			return;
		}
		for (FileItem item : items) {
			if (item.isFormField()) {
				String key = item.getFieldName(); // 获取name属性的值
				String value = item.getString(); // 获取value属性的值
				if ("targetuuid".equals(key)) {
					targetuuid = value;
				}

				if ("createdUserId".equals(key)) {
					createdUserId = value;
				}
			} else {
				// 获取文件名
				String fileUrl = item.getName();
				media.setName(fileUrl.substring(0, fileUrl.lastIndexOf(".")));
				media.setContentType(fileUrl.substring(fileUrl.lastIndexOf(".")+1)); // 截取文件格式
				// 文件大小
				int length = (int) item.getSize();
				String mediaL = "";
				if (length / (1024 * 1024) > 1) {
					mediaL = length / (1024 * 1024) + "MB";
				} else if (length / 1024 > 1) {
					mediaL = length / 1024 + "kB";
				} else if (length / 1024 > 1) {
					mediaL = length / (1024 * 1024 * 1024) + "GB";
				}
				// 写入到hdfs
				InputStream is;
				try {
					// 随机文件名
					String serialName  = DigestUtils.md5Hex(item.getInputStream());
					media.setMediaName(serialName);
					is = item.getInputStream();
					FileSystem fs = FileSystem.get(HDFSService.conf);
					String path = Constants.getSrcPath(serialName, media.getContentType());
					// 写入原文件到hadoop
					FSDataOutputStream os = fs.create(new Path(path));
					IOUtils.copyBytes(is, os, HDFSService.conf);
				} catch (IOException e) {
					log.error("io出错", AsyncFileUploadThread.class, e);
					out.write("error");
					asyncContext.complete();
					return;
				}
				ObjectMapper om = new ObjectMapper();
				Map<String ,String > mediaMap = new HashMap<String ,String >();
				mediaMap.put("mediaName", media.getName()+media.getContentType());
				mediaMap.put("mediaUUid",media.getMediaName());
				String json;
				try {
					json = om.writeValueAsString(mediaMap);
					out.write(json);
					out.flush();
				} catch (IOException e) {
					log.error(e);
					out.write("error");
					asyncContext.complete();
					return;
				}
				
				media.setStatus("1");
				media.setTargetUuid(targetuuid);
				media.setMediaPath("");
				media.setMediaLength(length);
				media.setPicPath("");
				media.setTitle(Constants.findRemote("ymxt").getUrl());
				media.setCreateUser(createdUserId);
				// 写入到hdfs
				media.setBiteMax("");
				media.setBiteMid("");
				media.setBiteMin("");
				media.setBiteSup("");
				MediaDao mediaDao = DaoFactory.getMediaDao();
				mediaDao.save(media);
				mediaDao.saveTmp(media);
				String param = "targetuuid=" + targetuuid + "&filepath=" + media.getMediaName() + "&filename="
						+ media.getName() + "&ext=" + media.getContentType() + "&size="
						+ media.getMediaLength() + "&sizeSuf=" + mediaL + "&createdUserId=" + createdUserId;
				HttpRequest.sendPost("http://" + Constants.mediaConfig.getRemoteIp() + ":"
						+ Constants.mediaConfig.getRemotePort().intValue() + Constants.mediaConfig.getRemoteInfo(),
						param);
				log.info("url","http://" + Constants.mediaConfig.getRemoteIp() + ":"
						+ Constants.mediaConfig.getRemotePort() + Constants.mediaConfig.getRemoteInfo());
				log.info("param",param);
			}
		}
		asyncContext.complete();
	}

private  class AsyncFileUploadListener implements AsyncListener{

	@Override
	public void onComplete(AsyncEvent asyncEvent) throws IOException {
		PrintWriter out = asyncEvent.getSuppliedResponse().getWriter();
		//out.println("async succes: " + new Date());
		out.flush();
		/**
		 * 在这里关闭流(此处为重点)
		 * 
		 * @see -------------------------------------------------------------------------------------
		 * @see 总结:自打请求进入doGet()方法中开始,直到请求方收到响应为止
		 * @see 整个过程中的任意位置所获得的PrintWriter和ServletResponse对象都是相同的
		 * @see 说明:在5个步骤mark位置,均执行System.out.println(PrintWriter或ServletResponse对象);
		 * @see 我们会发现在五个mark位置的这俩对象的hashCode和内存地址都是相同的
		 * @see 并且:第一个mark处声明的PrintWriter对象不能在第二个mark处关闭,只能在第4个或第5个mark处关闭
		 * @see 否则异步支持就会失败
		 * @see 补充:第5个mark处的out.println()也会作为响应结果给请求方,这点要注意
		 * @see -------------------------------------------------------------------------------------
		 */
		out.close();
		System.out.println("第五步mark");
		return;
	}

	@Override
	public void onError(AsyncEvent arg0) throws IOException {
		
	}

	@Override
	public void onStartAsync(AsyncEvent arg0) throws IOException {
		arg0.getSuppliedResponse().getWriter().flush();
	}

	@Override
	public void onTimeout(AsyncEvent asyncEvent) throws IOException {
		System.out.println("AppAsyncListener onTimeout");  
        //we can send appropriate response to client  
        ServletResponse response = asyncEvent.getAsyncContext().getResponse();  
        PrintWriter out = response.getWriter(); 
        log.error("servlet 异步超时");
        out.write("TimeOut Error in Processing"); 
	}
	
}
}
