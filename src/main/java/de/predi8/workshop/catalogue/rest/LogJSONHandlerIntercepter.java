package de.predi8.workshop.catalogue.rest;

import de.predi8.workshop.catalogue.event.ShopListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import static net.logstash.logback.marker.Markers.append;
import static net.logstash.logback.marker.Markers.appendEntries;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class LogJSONHandlerIntercepter extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(ShopListener.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        Map<String,Object> entries = new HashMap();
        entries.put("method",request.getMethod());
        entries.put("path", request.getServletPath());

        log.info(appendEntries(entries),"");

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        log.info(append("status_code", response.getStatus()),null);
    }
}
