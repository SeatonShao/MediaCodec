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

/**
 * 文件下载请求处理 Servlet implementation class DownLoad
 */
@WebServlet(urlPatterns = "/DownLoad", asyncSupported = true)
public class DownLoad extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(DownLoad.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownLoad() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * log.info(request.getSession().getAttribute("token")+":"+request.
		 * getParameter("key"));
		 * if(Long.parseLong((String)request.getSession().getAttribute("token"))
		 * +Long.parseLong(request.getParameter("key"))==1000000){
		 * doPost(request, response); }else{ doPost(request, response); }
		 */
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		AsyncContext asyncCtx = request.startAsync();
		// asyncCtx.setTimeout(9000);
		asyncCtx.start(new AsyncDownloadThread(asyncCtx));
	}

}
