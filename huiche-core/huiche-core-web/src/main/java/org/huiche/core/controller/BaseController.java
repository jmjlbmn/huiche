package org.huiche.core.controller;

import org.huiche.core.api.base.Api;
import org.huiche.core.response.SimpleView;
import org.huiche.core.util.StringUtil;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Maning
 */
public class BaseController implements Api {
    protected ModelAndView view(String page) {
        return new ModelAndView(page);
    }

    protected ModelAndView jump(String url) {
        return jump(url, null);
    }

    protected ModelAndView jump(String url, String msg) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("url", url);
        if (StringUtil.isNotEmpty(msg)) {
            mv.addObject("msg", msg);
        }
        mv.setViewName("jump");
        return mv;
    }

    protected ModelAndView redirect(String url) {
        return SimpleView.redirect(url);
    }

    protected ModelAndView forward(String url) {
        return SimpleView.forward(url);
    }


}
