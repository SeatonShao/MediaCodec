package com.infotop.Servlet.media;

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

import com.infotop.Servlet.fileupload.AsyncFileUploadThread;
import com.infotop.service.HDFSService;

public class AsyncTSThread implements Runnable {

	private AsyncContext asyncContext;
	private static Logger log = LogManager.getLogger(AsyncFileUploadThread.class.getName());
	private class AsyncTSListener implements AsyncListener {
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
				OutputStream out = asyncEvent.getSuppliedResponse().getOutputStream();
				out.write(("async succes: " + new Date()).getBytes());
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
			}
		}

	public AsyncTSThread(AsyncContext asyncContext) {
		this.asyncContext = asyncContext;
		asyncContext.addListener(new AsyncTSListener());
	}

	@Override
	public void run() {
		HttpServletResponse resp = (HttpServletResponse) asyncContext.getResponse();
		HttpServletRequest req = (HttpServletRequest) asyncContext.getRequest();
		// 获取请求路径
		String fpath = req.getRequestURI();
		String name = fpath.substring(fpath.lastIndexOf("/"));
		if (fpath == null)
			return;
		if (fpath.contains("/biteMin")) {
			fpath = "/codec/" + fpath.substring(fpath.lastIndexOf("/biteMin") + 8, fpath.length() - 7)
					+ "/biteMin" + fpath.substring(fpath.lastIndexOf("/"));
		} else if (fpath.contains("/biteMid")) {
			fpath = "/codec/" + fpath.substring(fpath.lastIndexOf("/biteMid") + 8, fpath.length() - 7)
					+ "/biteMid" + fpath.substring(fpath.lastIndexOf("/"));
		} else if (fpath.contains("/biteMax")) {
			fpath = "/codec/" + fpath.substring(fpath.lastIndexOf("/biteMax") + 8, fpath.length() - 7)
					+ "/biteMax" + fpath.substring(fpath.lastIndexOf("/"));
		} else if (fpath.contains("/biteSup")) {
			fpath = "/codec/" + fpath.substring(fpath.lastIndexOf("/biteSup") + 8, fpath.length() - 7)
					+ "/biteSup" + fpath.substring(fpath.lastIndexOf("/"));
		} else if (fpath.contains("/source")) {
			fpath = fpath.substring(fpath.lastIndexOf("/source") + 7, fpath.lastIndexOf("/biteMin") + 40)
					+ fpath.substring(fpath.lastIndexOf("/"));
		}
		// 请求路径转换hadoop文件路径
		// fpath =
		// fpath.substring(fpath.lastIndexOf("/"),fpath.indexOf(".")-3)+fpath.substring(fpath.lastIndexOf("/"));
		// log.info(fpath);
		// hadoop访问路径
		/*
		 * try {
		 * req.getRequestDispatcher("http://"+Constants.getActiveAddress()+
		 * ":50070/webhdfs/v1"+fpath+"?op=OPEN").forward(req, resp); } catch
		 * (ServletException e) {
		 * 
		 * e.printStackTrace(); };
		 */
		FileSystem fs = null;
		FSDataInputStream in = null;
		Path path = new Path(fpath);
		try {
			fs = FileSystem.get(HDFSService.conf);
			// 打开请求文件
				if (!fs.exists(path))
					return;
				in = fs.open(path);
		} catch (IOException e) {
			log.error("文件打开出错",e);
		}
		String fileName = fpath.substring(fpath.lastIndexOf("/") + 1);
		Long fileLen = null;
		try {
			fileLen = fs.getFileStatus(path).getLen();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String range = req.getHeader("Range");
		String contentType = req.getHeader("Content-type");
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
				IOUtils.copyBytes(in, out, fileLen.intValue() / 256);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
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
				IOUtils.copyBytes(in, out, count / 128, false);
				out.flush();
			} catch (Exception e) {
				log.error("TS流媒体信息导出出错", e);
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		asyncContext.complete();
	}

}
