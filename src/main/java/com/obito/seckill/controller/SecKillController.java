package com.obito.seckill.controller;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.obito.seckill.pojo.Order;
import com.obito.seckill.pojo.SeckillOrder;
import com.obito.seckill.pojo.User;
import com.obito.seckill.service.IGoodsService;
import com.obito.seckill.service.IOrderService;
import com.obito.seckill.service.ISeckillOrderService;
import com.obito.seckill.vo.GoodsVo;
import com.obito.seckill.vo.RespBean;
import com.obito.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/secKill")
public class SecKillController {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;
    /*
    * 秒杀
    * */
    @RequestMapping(value = "/doSecKill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSecKill(User user, Model model, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROE);
        }
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if (goods.getStockCount() < 1) {
            //页面静态化以后不用model
//            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //判断是否重复抢购 通过数据库获取
//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryChainWrapper<SeckillOrder>(seckillOrderService.getBaseMapper())
//                .eq("user_id", user.getId())
//                .eq("goods_id", goodsId));

        //通过redis过去订单
        Order seckillOrder = (Order) redisTemplate.opsForValue().get("order:" + user.getId() + goods.getId());
        if (seckillOrder != null) {
//            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        Order order = orderService.secKill(user, goods);
        return RespBean.success(order);
    }
}
