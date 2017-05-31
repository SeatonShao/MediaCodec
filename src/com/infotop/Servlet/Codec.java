package com.infotop.Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;

import com.infotop.constants.Constants;
import com.infotop.thrift.resources.common.MediaService;
import com.infotop.util.ThriftConfig;
import com.infotop.thrift.MediaHandler;
import com.google.gson.JsonObject;
import com.infotop.Servlet.async.MediaCodec;

/**
 * 异步转码 Servlet implementation class Codec
 */
@WebServlet(urlPatterns = "/Codec", loadOnStartup = 2)
public class Codec extends HttpServlet {

	private static final long serialVersionUID = 1L;
	Thread codecThread = null;
	Thread thriftThread = null;
	TServer server = null;
	private Logger log = LogManager.getLogger(Codec.class.getName());

	@Override
	public void init() throws ServletException {
		log.info(Constants.VIRTUAL_NAME.toLowerCase() + ":"+ Constants.mediaConfig.getMaster());
		if (Constants.VIRTUAL_NAME.toLowerCase().equals(Constants.mediaConfig.getMaster())) {
			MediaCodec codec = new MediaCodec();
			codecThread = new Thread(codec);
			codecThread.start();
			log.info("start codec");
		}
		thriftThread = new Thread() {
			public void run() {
				try {
					// 关联处理器与 Hello 服务的实现
					TProcessor processor = new MediaService.Processor<MediaService.Iface>(new MediaHandler());
					// 传输通道 - 非阻塞方式
					TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(Integer.parseInt(ThriftConfig.getProperty("mediaServer.port")));
					// 设置协议工厂为 TBinaryProtocol.Factory

					// 异步IO，需要使用TFramedTransport，它将分块缓存读取。
					TThreadedSelectorServer.Args tArgs = new TThreadedSelectorServer.Args(serverTransport);
					tArgs.processor(processor);
					tArgs.transportFactory(new TFramedTransport.Factory());
					// 使用高密度二进制协议
					tArgs.protocolFactory(new TCompactProtocol.Factory());
					// tArgs.protocolFactory(new TBinaryProtocol.Factory());
					// 使用多线程非阻塞式IO，服务端和客户端需要指定TFramedTransport数据传输的方式
					server = new TThreadedSelectorServer(tArgs);

					System.out.println("Starting the TThreadedSelectorServer server...");
					server.serve();
				} catch (TTransportException e) {
					log.error("通过ServletContextListener启动thrift资源服务异常", e);
				} catch (Exception e) {
					log.error("通过ServletContextListener启动thrift资源服务异常", e);
				}
			}
		};
		thriftThread.start();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		String on = request.getParameter("on");
		if ("1".equals(on)) {
			if (codecThread == null) {
				MediaCodec codec = new MediaCodec();
				codecThread = new Thread(codec);
				codecThread.start();
			} else {
				codecThread.notify();
			}
			resp.getOutputStream().print(true);
		} else if("0".equals(on)) {
			try {
				codecThread.wait();
				resp.getOutputStream().print(true);
			} catch (InterruptedException e) {
				log.error("转码线程等待出错！", Codec.class, e);
				resp.getOutputStream().print(false);
			}
		}else if("check".equals(on)) {
			if(codecThread!=null&&codecThread.getState().equals(Thread.State.RUNNABLE)){
				resp.getOutputStream().print(true);
			}else{
				resp.getOutputStream().print(false);
			}
		}
		resp.getOutputStream().close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	public void destroy() {
		super.destroy();

		if (codecThread != null)
			codecThread.interrupt();
		if (server != null)
			server.stop();
		if (thriftThread != null)
			thriftThread.interrupt();
	}

}
