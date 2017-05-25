package com.infotop.Servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.infotop.Servlet.async.MediaCodec;
import com.infotop.constants.Constants;
import com.infotop.entity.Media;
import com.infotop.service.HDFSService;

/**
 * Servlet implementation class Picture
 */
@WebServlet(urlPatterns = { "/Img" })
public class Img extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(Img.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Img() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 获取请求路径
		// String fpath = req.getRequestURI();
		String fpath = req.getParameter("path");
		FileSystem fs = null;
		FSDataInputStream in = null;
		// 图片文件路径/pic/视频uuid/视频uuid.jpg
		Path path = new Path(fpath);

		try {
			fs = FileSystem.get(HDFSService.conf);

			if (fs.exists(path))
				in = fs.open(path);
			else {
				FileStatus[] fsts = fs.listStatus(new Path("/" + fpath));
				for (FileStatus fst : fsts) {
					if (fst.isFile()) {
						Media media = new Media();
						media.setMediaName(fpath);
						String name = fst.getPath().getName();
						MediaCodec.getHdfsPic(media, name.substring(name.indexOf(".")));

					}
				}

				in = fs.open(path);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		// 文件长度
		final Long fileLen = fs.getFileStatus(path).getLen();
		String range = req.getHeader("Range");
		String contentType = req.getHeader("Content-type");
		// 文件名
		resp.addHeader("Content-Disposition", "attachment; filename=" + fpath.substring(1));
		// 文件大小
		resp.addHeader("Content-Length", fileLen.intValue() + "");
		// 接受范围bytes
		resp.setHeader("Accept-Ranges", "bytes");
		OutputStream out = resp.getOutputStream();
		if (range == null) {
			String fileName = fpath.substring(1) + ".jpg";
			resp.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			resp.setContentType("application/octet-stream");
			resp.setContentLength(fileLen.intValue());
			IOUtils.copyBytes(in, out, fileLen.intValue());
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
			// 设置下载的范围
			resp.setHeader("Content-Range", ContentRange);
			in.seek(start);
			try {
				IOUtils.copyBytes(in, out, count / 16, false);
			} catch (Exception e) {
				log.error("图片信息出错", e);
			}
		}
		in.close();
		in = null;
		out.close();
		out = null;
	}

}
