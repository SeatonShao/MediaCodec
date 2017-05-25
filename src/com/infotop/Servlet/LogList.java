package com.infotop.Servlet;

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
 
import com.infotop.service.OperateLogService;
import com.infotop.service.PlayListService;

/**
 * Servlet implementation class PlayList
 */
@WebServlet("/LogList")
public class LogList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	OperateLogService  logService = new OperateLogService();
	PlayListService playListService = new PlayListService();
	ObjectMapper om = new ObjectMapper();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LogList() {
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
			String sort = request.getParameter("sort");
			String rows = request.getParameter("rows");
			String page = request.getParameter("page");

			Map params = new HashMap();
			params.put("pageSize", Integer.parseInt(rows));
			params.put("pageIndex", (Integer.parseInt(page) - 1) * Integer.parseInt(rows));
			params.put("mediaName", mediaName);
			params.put("sort", sort);
			Map result = new HashMap();
			List list = logService.findLog(params);
			Integer count = logService.findLogCount(params);
			result.put("total", count);
			result.put("rows", list);
			String json = om.writeValueAsString(result);
			out.println(json);
			out.close();

		} else if (method != null && "delete".equals(method)) {
			long id = Long.parseLong(request.getParameter("id"));
			Map result = new HashMap();
			boolean bool = logService.delete(id);
			if (bool) {
				result.put("success", true);
				result.put("message", "删除日志成功！");
			} else {
				result.put("success", false);
				result.put("message", "删除日志失败！");
			}
			String json = om.writeValueAsString(result);
			out.println(json);
			out.close();
		}
	}

}
