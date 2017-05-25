package com.infotop.Servlet.media;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.infotop.dao.DaoFactory;
import com.infotop.dao.MediaDao;
import com.infotop.entity.Media;

/**
 * 
 * 查询已保存的视频信息转换并返回json字符串
 * 
 * 
 * Servlet implementation class MediaList
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/MediaList" })
public class MediaList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MediaList() {
		super();
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MediaDao mediaDao = DaoFactory.getMediaDao();
		response.setContentType("application/json");
		List<Media> list = mediaDao.findList(new Media());
		request.setAttribute("media", list);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(list);
		out.println(json);
		out.close();
	}

}
