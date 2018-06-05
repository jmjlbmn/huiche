package org.huiche.web.controller;

import org.huiche.core.util.StringUtil;
import org.huiche.web.api.Api;
import org.huiche.web.response.SimpleView;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 基础传统控制器
 *
 * @author Maning
 */
public class BaseController implements Api {
    protected ModelAndView view(@Nonnull String page) {
        return new ModelAndView(page);
    }

    protected ModelAndView jump(@Nonnull String url) {
        return jump(url, null);
    }

    protected ModelAndView jump(@Nonnull String url, @Nullable String msg) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("url", url);
        if (StringUtil.isNotEmpty(msg)) {
            mv.addObject("msg", msg);
        }
        mv.setViewName("jump");
        return mv;
    }

    protected ModelAndView redirect(@Nonnull String url) {
        return SimpleView.redirect(url);
    }

    protected ModelAndView forward(@Nonnull String url) {
        return SimpleView.forward(url);
    }


}
