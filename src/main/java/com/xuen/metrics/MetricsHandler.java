package com.xuen.metrics;

import com.google.common.collect.ImmutableMap;
import com.xuen.servlet.RequestHandler;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zheng.xu
 * @since 2017-07-11
 */
public class MetricsHandler implements RequestHandler{
    final String CONTENT_TYPE_TEXT = "text/plain;charset=UTF-8";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws IOException {
        handle0(Metrics.INSTANCE.holder, request, response);
    }

    private void handle0(MetricsHolder holder, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String format = request.getParameter("format");

        response.setContentType(CONTENT_TYPE_TEXT);
        response.setHeader("Content-Encoding", "gzip");

        GZIPOutputStream out = new GZIPOutputStream(response.getOutputStream());
        PrintWriter writer = new PrintWriter(out, true);

        pullValueOf(format);

        out.finish();
        out.flush();
        writer.close();
    }

    private static MetricsProcessor defaultReportor = new CerberusPullProcessorMetricsProcessor();;
    private static Map<String, MetricsProcessor> reportors = ImmutableMap.<String, MetricsProcessor> builder()
            .put("cerberus", defaultReportor).build();

    private MetricsProcessor pullValueOf(String format) {
        MetricsProcessor reportor = reportors.get(format);
        if (reportor == null){
            return defaultReportor;
        }
        return reportor;
    }
}
