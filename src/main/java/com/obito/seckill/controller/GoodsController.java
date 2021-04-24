package com.obito.seckill.controller;

import com.obito.seckill.pojo.User;
import com.obito.seckill.service.IGoodsService;
import com.obito.seckill.service.IUserService;
import com.obito.seckill.vo.DetailVo;
import com.obito.seckill.vo.GoodsVo;
import com.obito.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IGoodsService goodsService;

    /*
    * 把商品列表页缓存到redis
    * */
    @Autowired
    private RedisTemplate redisTemplate;

    //手动渲染依赖
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    /*
    * 跳转商品列表页面
    * */
    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    //使用自定义参数直接接受user
    public String toList(Model model, User user, HttpServletResponse response, HttpServletRequest request) {
//        if (ticket.isEmpty()) {
//            return "login";
//        }
        //通过session找用户信息
//        User user = (User) session.getAttribute(ticket);
        //通过redis获取user
//        User user = userService.getUserByCookie(ticket, request, response);
//        if (user == null) return "login";
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //redis中获取页面
        String html = (String) valueOperations.get("goodsList");
        if (!html.isEmpty()) {
            //不为空 直接返回页面
            return html;
        } else {
            //为空 手动渲染
            model.addAttribute("user", user);
            model.addAttribute("goodsList", goodsService.findGoodsVo());
            WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
            html = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
            if (!html.isEmpty()) {
                //存入redis key value expiretime = 1min
                valueOperations.set("goodsList", html, 1, TimeUnit.MINUTES);
            }
        }
        return html;
//        return "goodsList";
    }

    /*
    * 跳转商品详情页
    * 把html整个存入redis
    * */
//    @RequestMapping(value = "/toDetail/{goodId}", produces = "text/html;charset=utf-8")
//    @ResponseBody
//    public String toDetail(Model model, User user, @PathVariable Long goodId, HttpServletRequest request, HttpServletResponse response) {
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        //redis中获取html
//        String html = (String) valueOperations.get("goodsDetail" + goodId);
//        if (!html.isEmpty()) {
//            //不为空直接返回页面
//            return html;
//        }
//        //为空 重新获取 手动渲染
//        model.addAttribute("user", user);
//        GoodsVo goodVo = goodsService.findGoodsVoByGoodsId(goodId);
//        Date startDate = goodVo.getStartDate();
//        Date endDate = goodVo.getEndDate();
//        Date currentDate = new Date();
//        int secKillStatus = 0;
//        int remainSeconds = 0;
//        //判断秒杀状态
//        if (currentDate.before(startDate)) {
//            //未开始
//            remainSeconds = ((int) ((startDate.getTime() - currentDate.getTime()) / 1000));
//            secKillStatus = 0;
//        } else if (currentDate.after(startDate)) {
//            //已结束
//            secKillStatus = 2;
//            remainSeconds = -1;
//        } else {
//            //秒杀中
//            secKillStatus = 1;
//        }
//        model.addAttribute("remainSeconds", remainSeconds);
//        model.addAttribute("secKillStatus", secKillStatus);
//        model.addAttribute("goods", goodVo);
//        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
//        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", webContext);
//        if (!html.isEmpty()) {
//            valueOperations.set("goodsDetail" + goodId, html, 1, TimeUnit.MINUTES);
//        }
//        return html;
//    }

    @RequestMapping(value = "/toDetail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(Model model, User user, @PathVariable Long goodId) {
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        int secKillStatus = 0;
        int remainSeconds = 0;
        if (nowDate.before(startDate)) {
            remainSeconds = (int) (startDate.getTime() - nowDate.getTime()) / 1000;
        } else if (nowDate.after(endDate)) {
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            secKillStatus = 1;
            remainSeconds = 0;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSeckKillStatus(secKillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVo);
    }

    public String toList(HttpSession session, Model model, @CookieValue("userTicket") String ticket,
                         HttpServletRequest request, HttpServletResponse response) {
        if (ticket.isEmpty()) {
            return "login";
        }
        //通过session找用户信息
//        User user = (User) session.getAttribute(ticket);
        //
        User user = userService.getUserByCookie(ticket, request, response);
        if (user == null) return "login";
        model.addAttribute("user", user);
        return "goodslist";
    }
}
