package de.predi8.workshop.catalogue.web;

import de.predi8.workshop.catalogue.event.ShopListener;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
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

    private final Logger log = LoggerFactory.getLogger(LogJSONHandlerIntercepter.class);

    private static final Counter requests = Counter.build()
            .name("requests_total").help("Total requests.").register();

    private static final Histogram latency = Histogram.build()
            .name("requests_latency_seconds").help("Request latency in seconds.").register();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        //Histogram.Timer timer = latency.startTimer();

        requests.inc();

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
