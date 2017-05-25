package com.infotop.Servlet.media;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.AsyncContext;
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

import com.infotop.redis.common.JedisUtils;
import com.infotop.service.HDFSService;

/**
 * ts文件请求处理
 * Servlet implementation class TS
 */
@WebServlet(urlPatterns = {"*.ts"}, asyncSupported = true)
public class TS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(TS.class);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TS() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*log.info(request.getSession().getAttribute("token")+":"+request.getParameter("key"));
		if(Long.parseLong((String)request.getSession().getAttribute("token"))+Long.parseLong(request.getParameter("key"))==1000000){
			doPost(request, response);
		}else{
			doPost(request, response);
		}*/
		/*try {
			preview(request, response);
		} catch (ClassNotFoundException e) {
			log.error("ts",e);
		}*/
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AsyncContext asyncCtx = request.startAsync();  
        //asyncCtx.setTimeout(9000);  
        asyncCtx.start(new AsyncTSThread(asyncCtx)); 
	}

	public void preview(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
		 //获取请求路径
		String fpath = req.getRequestURI();
		String name = fpath.substring(fpath.lastIndexOf("/"));
		if (fpath == null)
			return;
		if(fpath.contains("/biteMin")){
			fpath = "/"+fpath.substring(fpath.lastIndexOf("/biteMin")+8,fpath.lastIndexOf("/biteMin")+40)+"/biteMin"+fpath.substring(fpath.lastIndexOf("/"));
		}else if(fpath.contains("/biteMid")){
			fpath = "/"+fpath.substring(fpath.lastIndexOf("/biteMid")+8,fpath.lastIndexOf("/biteMin")+40)+"/biteMid"+fpath.substring(fpath.lastIndexOf("/"));
		}else if(fpath.contains("/biteMax")){
			fpath = "/"+fpath.substring(fpath.lastIndexOf("/biteMax")+8,fpath.lastIndexOf("/biteMin")+40)+"/biteMax"+fpath.substring(fpath.lastIndexOf("/"));
		}else if(fpath.contains("/biteSup")){
			fpath = "/"+fpath.substring(fpath.lastIndexOf("/biteSup")+8,fpath.lastIndexOf("/biteMin")+40)+"/biteSup"+fpath.substring(fpath.lastIndexOf("/"));
		}else if(fpath.contains("/source")){
			fpath = fpath.substring(fpath.lastIndexOf("/source")+7,fpath.lastIndexOf("/biteMin")+40)+fpath.substring(fpath.lastIndexOf("/"));
		}
		//请求路径转换hadoop文件路径
		//fpath = fpath.substring(fpath.lastIndexOf("/"),fpath.indexOf(".")-3)+fpath.substring(fpath.lastIndexOf("/"));
		//log.info(fpath);
		//hadoop访问路径
		/*try {
			req.getRequestDispatcher("http://"+Constants.getActiveAddress()+":50070/webhdfs/v1"+fpath+"?op=OPEN").forward(req, resp);
		} catch (ServletException e) {
			
			e.printStackTrace();
		};*/
		FileSystem fs = null;
		FSDataInputStream in = null;
		 Path path = new Path(fpath);
		try {
			fs = FileSystem.get(HDFSService.conf);
			//打开请求文件
			
			JedisUtils jedisUtils = new JedisUtils();
			if(false&&jedisUtils.exists(name)){
				in = (FSDataInputStream) org.apache.commons.io.IOUtils.toInputStream(jedisUtils.get(name));
			}else{
				if(!fs.exists(path))
					return;
				in = fs.open(path);
				//jedisUtils.set(name, org.apache.commons.io.IOUtils.toString(in), 0);
				//in = fs.open(path);
			}
		} catch (IOException e) {
			log.error(e);
		} 
		String fileName = fpath.substring(fpath.lastIndexOf("/") + 1);
		final Long fileLen = fs.getFileStatus(path).getLen();
		String range = req.getHeader("Range");
		String contentType = req.getHeader("Content-type");
		resp.setHeader("Content-type", "video/ogg");
		// 文件名
		resp.addHeader("Content-Disposition", "attachment; filename=" + fileName);
		// 文件大小
		resp.addHeader("Content-Length", fileLen.intValue() + "");
		//接受范围bytes
		resp.setHeader("Accept-Ranges", "bytes");
		OutputStream out = resp.getOutputStream();
		if (range == null) {
			
			resp.setHeader("Content-Disposition", "attachment; filename=" +   fileName);
			resp.setContentType("application/octet-stream");
			resp.setContentLength(fileLen.intValue());
			IOUtils.copyBytes(in, out, fileLen.intValue() / 256);
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
				IOUtils.copyBytes(in, out, count/128, false);
			} catch (Exception e) {
				log.error("TS流媒体信息导出出错", e);
			}
		}
		out.flush();
		in.close();
		in = null;
	}
}
