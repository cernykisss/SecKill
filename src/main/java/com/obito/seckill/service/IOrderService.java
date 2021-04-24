package com.obito.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.obito.seckill.pojo.Order;
import com.obito.seckill.pojo.User;
import com.obito.seckill.vo.GoodsVo;
import com.obito.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kai
 * @since 2021-04-07
 */
public interface IOrderService extends IService<Order> {


    /*
    * 创建订单
    * */
    Order secKill(User user, GoodsVo goods);

    OrderDetailVo detail(Long orderId);
}
