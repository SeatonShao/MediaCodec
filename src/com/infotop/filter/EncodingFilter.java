package com.infotop.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletResponse;
@MultipartConfig
@WebFilter(asyncSupported=true,urlPatterns="/*",initParams={@WebInitParam(name="encoding",value="utf-8")})
public class EncodingFilter implements Filter {
    protected String encoding = null;
    protected FilterConfig filterConfig = null;

    public void destroy() {
        this.encoding = null;
        this.filterConfig = null;

    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
    	  HttpServletResponse httpResponse = (HttpServletResponse) response;
    	
    	  if (encoding != null) {
              request.setCharacterEncoding(encoding);
              //response.setContentType("Access-Control-Allow-Origin", "*");
          }
    	  httpResponse.addHeader("Access-Control-Allow-Origin", "*");
        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding");
    }

    protected String selectEncoding(ServletRequest request) {

        return (this.encoding);

    }
}
