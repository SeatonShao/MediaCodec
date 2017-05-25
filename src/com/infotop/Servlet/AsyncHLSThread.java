package com.infotop.Servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infotop.Servlet.async.MediaCodec;
import com.infotop.Servlet.fileupload.AsyncFileUploadThread;
import com.infotop.constants.Constants;
import com.infotop.service.HDFSService;

public class AsyncHLSThread implements Runnable {

	private AsyncContext asyncContext;
	private static Logger log = LogManager.getLogger(AsyncFileUploadThread.class.getName());

	private class AsyncHLSListener implements AsyncListener {
		@Override
		public void onTimeout(AsyncEvent arg0) throws IOException {

		}
		@Override
		public void onStartAsync(AsyncEvent arg0) throws IOException {

		}
		@Override
		public void onError(AsyncEvent arg0) throws IOException {

		}
		@Override
		public void onComplete(AsyncEvent asyncEvent) throws IOException {
			OutputStream out = asyncEvent.getAsyncContext().getResponse().getOutputStream();
			log.info("异步完成");
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
			return;
		}
	}

	public AsyncHLSThread(AsyncContext asyncContext) {
		this.asyncContext = asyncContext;
		asyncContext.addListener(new AsyncHLSListener());
	}

	@Override
	public void run() {
		HttpServletResponse resp = (HttpServletResponse) asyncContext.getResponse();
		HttpServletRequest req = (HttpServletRequest) asyncContext.getRequest();
		String path = req.getParameter("path");
		String bite = req.getParameter("bite");
		bite = bite==null?"biteMin":bite;
		String fpath = Constants.getPlayPath(bite, path);

		FileSystem fs = null;
		FSDataInputStream in = null;
		try {
			fs = FileSystem.get(HDFSService.conf);
			Path p = new Path(fpath);
			Path pSource = new Path(Constants.getHdfsPath(path, bite));
			/*
			 * //判断是否有转码文件 if (!fs.exists(pSource)) { FileStatus[] fsts =
			 * fs.listStatus(new Path("/"+fpath)); for(FileStatus fst :fsts ){
			 * if(fst.isFile()){ //如果没转码现在转 Media media = new Media();
			 * media.setMediaName(fpath); String name = fst.getPath().getName();
			 * media.setContentType(name.substring(name.indexOf("."))); Path src
			 * = new Path(Constants.getSrcPath(media.getMediaName(),
			 * media.getContentType())); String localSrc =
			 * Constants.getLocalPath(media.getMediaName()); Path local = new
			 * Path(localSrc); fs.copyToLocalFile(src, local); media2ts(media,
			 * Constants.mediaConfig.getFgBt()); } } }
			 */
			if (!fs.exists(p)) {
				MediaCodec.hdfsTs2m3u8(path, bite, 0);
			}
			in = fs.open(p);
		} catch (Exception e) {
			log.error("m3u8视频文件打开出错", e);
			asyncContext.complete();
		}
		String fileName = fpath.substring(fpath.lastIndexOf("/") + 1);
		Long fileLen = null;
		try {
			fileLen = fs.getFileStatus(new Path(fpath)).getLen();
		} catch (IllegalArgumentException | IOException e1) {
			log.error("文件获取大小失败", e1);
			asyncContext.complete();
		}
		String range = req.getHeader("Range");
		String contentType = req.getHeader("Content-type");
		// log.info(contentType);
		// resp.setHeader("Content-type", "rtmp/flv");
		resp.setHeader("Content-type", "video/ogg");
		// 文件名
		resp.addHeader("Content-Disposition", "attachment; filename=" + fileName);
		// 文件大小
		resp.addHeader("Content-Length", fileLen.intValue() + "");
		// 接受范围bytes
		resp.setHeader("Accept-Ranges", "bytes");

		if (range == null) {
			resp.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			resp.setContentType("application/octet-stream");
			resp.setContentLength(fileLen.intValue());

			try {
				OutputStream out = resp.getOutputStream();
				IOUtils.copyBytes(in, out, fileLen.intValue() / 20);
				out.flush();
			} catch (IOException e) {
				log.error("文件传输出错", e);
				asyncContext.complete();
			}
		} else {
			long start = Integer.valueOf(range.substring(range.indexOf("=") + 1, range.indexOf("-")));
			long count = fileLen - start;
			long end;
			if (range.endsWith("-"))
				end = fileLen - 1;
			else
				end = Integer.valueOf(range.substring(range.indexOf("-") + 1));
			String ContentRange = "bytes " + String.valueOf(start) + "-" + end + "/" + String.valueOf(fileLen);
			// 设置 206状态表示可以断点下载
			resp.setStatus(206);
			resp.setContentType("video/ogg");
			// 设置下载的范围
			resp.setHeader("Content-Range", ContentRange);
			try {
				in.seek(start);
				OutputStream out = resp.getOutputStream();
				IOUtils.copyBytes(in, out, count, false);
				in.close();
				out.flush();
			} catch (Exception e) {
				log.error("文件上传出错", e);
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				asyncContext.complete();
			}
		}
		
		in = null;
		asyncContext.complete();
	}

}
