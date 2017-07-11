package com.xuen.servlet;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zheng.xu
 * @since 2017-07-11
 */
public interface RequestHandler {

    void handle(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException;
}
