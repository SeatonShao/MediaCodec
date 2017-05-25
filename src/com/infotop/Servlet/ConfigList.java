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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.codehaus.jackson.map.ObjectMapper;

import com.infotop.constants.MediaConfig;
import com.infotop.service.ConfigListService;

/**
 * Servlet implementation class ConfigList
 */
@WebServlet("/ConfigList")
public class ConfigList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ConfigListService configListService = new ConfigListService();
	private Logger log = LogManager.getLogger(ConfigList.class);
	ObjectMapper om = new ObjectMapper();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConfigList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String method = request.getParameter("method");
		if (method != null && "list".equals(method)) {

			String virtualName = request.getParameter("mediaName");
			String sort = request.getParameter("sort");
			String rows = request.getParameter("rows");
			String page = request.getParameter("page");

			Map params = new HashMap();
			params.put("pageSize", Integer.parseInt(rows));
			params.put("pageIndex", (Integer.parseInt(page) - 1) * Integer.parseInt(rows));
			params.put("virtualName", virtualName);
			params.put("sort", sort);
			Map result = new HashMap();
			List list = configListService.getConfigList(params);
			Integer count = configListService.getConfigListCount(params);
			result.put("total", count);
			result.put("rows", list);
			String json = om.writeValueAsString(result);
			out.println(json);
			out.close();

		} else if (method != null && "update".equals(method)) {
			Map conf = new HashMap();
			conf.put("id", request.getParameter("id"));
			conf.put("ip", request.getParameter("ip"));
			conf.put("port", request.getParameter("port"));
			conf.put("sort", request.getParameter("sort"));
			conf.put("rows", request.getParameter("rows"));
			conf.put("virtualName", request.getParameter("virtualName"));
			conf.put("fgCvOn", request.getParameter("fgCvOn"));
			conf.put("fgCv", request.getParameter("fgCv"));
			conf.put("fgCaOn", request.getParameter("fgCaOn"));
			conf.put("fgCa", request.getParameter("fgCa"));
			conf.put("fgTs", request.getParameter("fgTs"));
			conf.put("fgThread", request.getParameter("fgThread"));
			conf.put("fgBiteMin", request.getParameter("fgBiteMin"));
			conf.put("fgBiteMid", request.getParameter("fgBiteMid"));
			conf.put("fgBiteMax", request.getParameter("fgBiteMax"));
			conf.put("fgBiteSup", request.getParameter("fgBiteSup"));
			conf.put("fgBminBuf", request.getParameter("fgBminBuf"));
			conf.put("fgBmidBuf", request.getParameter("fgBmidBuf"));
			conf.put("fgBmaxBuf", request.getParameter("fgBmaxBuf"));
			conf.put("fgBsupBuf", request.getParameter("fgBsupBuf"));
			conf.put("fgBt", request.getParameter("fgBt"));
			conf.put("fgVf", request.getParameter("fgVf"));
			conf.put("fgSleep", request.getParameter("fgSleep"));
			conf.put("fgHlsTime", request.getParameter("fgHlsTime"));
			conf.put("fgHlsSize", request.getParameter("fgHlsSize"));
			conf.put("remoteIp", request.getParameter("remoteIp"));
			conf.put("remotePort", request.getParameter("remotePort"));
			conf.put("remoteStatus", request.getParameter("remoteStatus"));
			conf.put("remoteInfo", request.getParameter("remoteInfo"));
			Map result = new HashMap();
			MediaConfig config = om.convertValue(conf, MediaConfig.class);
			boolean bool = configListService.updateConfig(config);
			if (bool) {
				result.put("success", true);
				result.put("message", "更新配置成功！");
			} else {
				result.put("success", false);
				result.put("message", "更新配置失败！");
			}
			String json = om.writeValueAsString(result);
			out.println(json);
			out.close();
		} else if (method != null && "getConfig".equals(method)) {
			String id =  request.getParameter("id");
			MediaConfig mediaConfig = null;
			if(id!=null){
				mediaConfig =  configListService.findById(id);
			}
			Map result = new HashMap();
			if (mediaConfig!=null) {
				result.put("success", true);
				result.put("mediaConfig",mediaConfig);
				result.put("message", "获取配置成功！");
			} else {
				result.put("success", false);
				result.put("message", "获取配置失败！");
			}
			String json = om.writeValueAsString(result);
			log.info(json);
			out.println(json);
			out.close();
		}else if (method != null && "delete".equals(method)) {
			String  id = request.getParameter("id");
			Map result = new HashMap();
			boolean bool = configListService.deleteConfig(Integer.parseInt(id));
			if (bool) {
				result.put("success", true);
				result.put("message", "更新配置成功！");
			} else {
				result.put("success", false);
				result.put("message", "更新配置失败！");
			}
			String json = om.writeValueAsString(result);
			out.println(json);
			out.close();
		}
	}

}
