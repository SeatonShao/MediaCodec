package com.infotop.Servlet.media;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.infotop.service.HDFSService;

/**
 * 传入文件名播放视频源文件
 * Servlet implementation class Play
 */
@WebServlet( urlPatterns = { "/Play" })
public class Play extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(Play.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Play() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String fpath = req.getParameter("path");
		fpath = "/" + fpath + "/" + fpath + ".mp4";
		FileSystem fs = null;
		FSDataInputStream in = null;
		try {
			fs = FileSystem.get(HDFSService.conf);
			Path p = new Path(fpath);
			in = fs.open(p);
		} catch (IOException e) {
			log.error("文件系统打开出错", e);
		}
		String fileName = fpath.substring(fpath.lastIndexOf("/") + 1);
		final Long fileLen = fs.getFileStatus(new Path(fpath)).getLen();
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
		OutputStream out = resp.getOutputStream();
		if (range == null) {
			resp.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			resp.setContentType("application/octet-stream");
			resp.setContentLength(fileLen.intValue());
			IOUtils.copyBytes(in, out, HDFSService.conf);
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
			in.seek(start);
			try {
				IOUtils.copyBytes(in, out, count, true);
			} catch (Exception e) {
				log.error("文件上传出错", e);
			}
		}
	}
}
