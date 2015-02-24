package com.site.commons.lib.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class RequestInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI();
    	
		long gnb = 100;
		if ( uri.indexOf("/home") != -1 ) {
			gnb = 100;
		}
		else if ( uri.indexOf("/three") != -1 ) {
			gnb = 200;
		}
		else if ( uri.indexOf("/two1") != -1 ) {
			gnb = 300;
		}
		else if ( uri.indexOf("/two2") != -1 ) {
			gnb = 400;
		}
		else if ( uri.indexOf("/one") != -1 ) {
			gnb = 500;
		}
		   
		request.setAttribute("gnb", gnb);

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {/**/}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {/**/}
}
