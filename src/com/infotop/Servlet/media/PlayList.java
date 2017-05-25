package com.infotop.Servlet.media;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.infotop.dao.DaoFactory;
import com.infotop.dao.MediaDao;
import com.infotop.entity.Media;
import com.infotop.service.HDFSService;
import com.infotop.service.MediaService;
import com.infotop.service.PlayListService;

/**
 * Servlet implementation class PlayList
 */
@WebServlet("/PlayList")
public class PlayList extends HttpServlet {
	private static Logger log = LogManager.getLogger(PlayList.class.getName());
	private static final long serialVersionUID = 1L;
	MediaService mediaService = new MediaService();
	PlayListService playListService = new PlayListService();
	ObjectMapper om = new ObjectMapper();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PlayList() {
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
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String method = request.getParameter("method");
		if (method != null && "list".equals(method)) {

			String mediaName = request.getParameter("mediaName");
			String sort = request.getParameter("sort");
			String rows = request.getParameter("rows");
			String page = request.getParameter("page");

			Map params = new HashMap();
			params.put("pageSize", Integer.parseInt(rows));
			params.put("pageIndex", (Integer.parseInt(page) - 1) * Integer.parseInt(rows));
			params.put("mediaName", mediaName);
			params.put("sort", sort);
			Map result = new HashMap();
			List list = mediaService.getPlayList(params);
			Integer count = mediaService.getPlayListCount(params);
			result.put("total", count);
			result.put("rows", list);
			String json = om.writeValueAsString(result);
			out.println(json);
			out.close();

		} else if (method != null && "delete".equals(method)) {
			long id = Long.parseLong(request.getParameter("id"));
			Map result = new HashMap();
			FileSystem fs = FileSystem.get(HDFSService.conf);
			MediaDao mediaDao = DaoFactory.getMediaDao();
			Media media = new Media();
			media.setId((int)id);
			media = mediaDao.find(media);
			String path = "/"+media.getMediaName();
			log.info(id+path);
			boolean bool = playListService.deleteMedia(id)&&fs.delete(new Path(path),true);
			if (bool) {
				result.put("success", true);
				result.put("message", "删除视频成功！");
			} else {
				result.put("success", false);
				result.put("message", "删除视频失败！");
			}
			String json = om.writeValueAsString(result);
			out.println(json);
			out.close();
		}
	}

}
