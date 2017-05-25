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

import org.codehaus.jackson.map.ObjectMapper;

import com.infotop.constants.Constants;
import com.infotop.service.MediaService;

/**
 * Servlet implementation class MediaCodecList
 */
@WebServlet("/MediaCodecList")
public class MediaCodecList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	MediaService mediaService = new MediaService();
	ObjectMapper om = new ObjectMapper();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MediaCodecList() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String method = request.getParameter("method");
		if (method != null && "list".equals(method)) {
			String mediaName = request.getParameter("mediaName");

			String rows = request.getParameter("rows");
			String page = request.getParameter("page");

			Map params = new HashMap();
			params.put("pageSize", Integer.parseInt(rows));
			params.put("pageIndex", (Integer.parseInt(page) - 1) * Integer.parseInt(rows));
			params.put("mediaName", mediaName);
			params.put("sort", null);
			Map result = new HashMap();
			List list = mediaService.getMediaCodecList(params);
			Integer count = mediaService.getMediaCodecCount(params);
			result.put("total", count);
			result.put("rows", list);
			String json = om.writeValueAsString(result);
			out.println(json);
			out.close();
		} else if (method != null && "delete".equals(method)) {
			long id = Long.parseLong(request.getParameter("id"));
			Map result = new HashMap();
			boolean bool = mediaService.deleteMedia(id);
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
		}else if (method != null && "weightUp".equals(method)) {
			long id = Long.parseLong(request.getParameter("id"));
			Map result = new HashMap();
			boolean bool = mediaService.updateMediaWeight(id);
			if (bool) {
				result.put("success", true);
				result.put("message", "升级转码id="+id+"视频优先级成功");
			} else {
				result.put("success", false);
				result.put("message", "升级转码id="+id+"视频优先级失败");
			}
			String json = om.writeValueAsString(result);
			out.println(json);
			out.close();
		}
	}

}
