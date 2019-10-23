package org.huiche.web.response;


import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 简单的常用视图
 *
 * @author Maning
 */
public class SimpleView implements View {
    private final String content;
    private final String contentType;

    public SimpleView(@Nonnull String content, @Nonnull String contentType) {
        this.content = content;
        this.contentType = contentType;
    }

    public static ModelAndView js(@Nonnull String content) {
        return new ModelAndView(new SimpleView(content, "text/javascript"));
    }

    public static ModelAndView json(@Nonnull String content) {
        return new ModelAndView(new SimpleView(content, MediaType.APPLICATION_JSON_VALUE));
    }

    public static ModelAndView css(@Nonnull String content) {
        return new ModelAndView(new SimpleView(content, "text/css"));
    }

    public static ModelAndView html(@Nonnull String content) {
        return new ModelAndView(new SimpleView(content, MediaType.TEXT_HTML_VALUE));
    }

    public static ModelAndView forward(@Nonnull String url) {
        return new ModelAndView("forward:" + url);
    }

    public static ModelAndView redirect(@Nonnull String url) {
        return new ModelAndView(new RedirectView(url));
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void render(@Nullable Map<String, ?> model, @Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(contentType);
        response.getWriter().write(content);
        response.getWriter().flush();
    }
}