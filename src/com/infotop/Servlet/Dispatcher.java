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

import com.infotop.dao.DaoFactory;
import com.infotop.dao.MediaDao;
import com.infotop.entity.Media;

/**
 * 
 * Servlet implementation class Dispatcher
 */
@WebServlet("/Dispatcher")
public class Dispatcher extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Dispatcher() {
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
		String page = request.getParameter("page");
		String path = request.getParameter("path");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		request.setAttribute("page", page);
		if ("mediaList".equals(page)) {
			String play = request.getParameter("play");
			request.setAttribute("play", play);
			request.getRequestDispatcher("/WEB-INF/view/mediaList.jsp").forward(request, response);
		} else if ("biteMin".equals(page)) {
			request.getRequestDispatcher("/WEB-INF/view/source.jsp").forward(request, response);
		} else if ("config".equals(page)) {
			request.getRequestDispatcher("/WEB-INF/view/config.jsp").forward(request, response);
		}  else if ("codec".equals(page)) {
			request.getRequestDispatcher("/WEB-INF/view/mediaCodecList.jsp").forward(request, response);
		}  else if ("log".equals(page)) {
			request.getRequestDispatcher("/WEB-INF/view/log.jsp").forward(request, response);
		} else if ("configForm".equals(page)) {
			request.getRequestDispatcher("/WEB-INF/view/configForm.jsp").forward(request, response);
		} else if ("PlayList".equals(page)) {
			request.getRequestDispatcher("/WEB-INF/view/playList.jsp").forward(request, response);
		} else if ("error".equals(page)) {
			request.getRequestDispatcher("/WEB-INF/view/errList.jsp").forward(request, response);
		} else if (path != null && !"".equals(path)) {
			MediaDao mediaDao = DaoFactory.getMediaDao();
			Media media = new Media();
			media.setBiteMin("/" + path + "/biteMin/biteMin" + path + ".m3u8");
			List mediaList = mediaDao.findList(media);
			request.setAttribute("path", path);
			String play = request.getParameter("play");
			if (!"true".equals(play)) {
				if (mediaList != null && mediaList.size() > 0) {
					Map<String, String> msg = new HashMap<String, String>();
					ObjectMapper om = new ObjectMapper();
					msg.put("status", "2");
					String json = om.writeValueAsString(msg);
					out.println(json);
				}
				// request.getRequestDispatcher("/WEB-INF/view/play.jsp").forward(request,
				// response);
				else {
					Map<String, String> msg = new HashMap<String, String>();
					ObjectMapper om = new ObjectMapper();
					msg.put("status", "1");
					String json = om.writeValueAsString(msg);
					out.println(json);

				}
			} else {
				request.getRequestDispatcher("/WEB-INF/view/play.jsp").forward(request, response);
			}
		} else if("playvj".equals(page)) {
			request.getRequestDispatcher("/WEB-INF/view/playVJ.jsp").forward(request, response);
		}else {
			Map<String, String> msg = new HashMap<String, String>();
			ObjectMapper om = new ObjectMapper();
			msg.put("status", "1");
			String json = om.writeValueAsString(msg);
			out.println(json);
		}

		out.flush();
		out.close();
	}

}
