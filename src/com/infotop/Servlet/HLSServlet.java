package com.infotop.Servlet;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.infotop.constants.Constants;

/**
 * 视频点播
 * 
 * @author Administrator
 *
 */
@WebServlet(urlPatterns = { "/HLS" }, asyncSupported = true)
public class HLSServlet extends HttpServlet {

	/**
	 * 通过ffmpeg推送hadoop文件到直播服务器 ffmpeg -i
	 * http://192.168.10.231:50070/webhdfs/v1/1476069490041.flv?op=OPEN -c copy
	 * -f flv rtmp://localhost/vod/shipin
	 */
	private static final long serialVersionUID = 1755900638112169516L;
	private static Logger log = LogManager.getLogger(HLSServlet.class);

	@Override		
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AsyncContext asyncCtx = req.startAsync();
										asyncCtx.setTimeout(90000);
		asyncCtx.start(new AsyncHLSThread(asyncCtx));
	}

}
