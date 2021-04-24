package com.obito.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.obito.seckill.exception.GlobalException;
import com.obito.seckill.mapper.OrderMapper;
import com.obito.seckill.pojo.Order;
import com.obito.seckill.pojo.SeckillGoods;
import com.obito.seckill.pojo.SeckillOrder;
import com.obito.seckill.pojo.User;
import com.obito.seckill.service.IOrderService;
import com.obito.seckill.service.ISeckillGoodsService;
import com.obito.seckill.service.ISeckillOrderService;
import com.obito.seckill.vo.GoodsVo;
import com.obito.seckill.vo.OrderDetailVo;
import com.obito.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kai
 * @since 2021-04-07
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private ISeckillGoodsService seckillGoodsService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private GoodsServiceImpl goodsService;

    @Autowired
    private RedisTemplate redisTemplate;
    /*
    * 秒杀
    * */
    @Transactional
    @Override
    public Order secKill(User user, GoodsVo goods) {
        //秒杀商品减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>()
                .eq("goods_id", goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        boolean result = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count = " + "stock_count - 1")
                .eq("goods_id", goods.getId())
                .gt("stock_count", 0));
        if (!result) {
            return null;
        }
        //更新成功的话
        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodId(goods.getId());
        order.setGoodName(goods.getGoodName());
        order.setGoodCount(1);
        order.setGoodPrice(goods.getSeckillPrice());
        order.setCreateDate(new Date());
        order.setStatus(0);
        orderMapper.insert(order);

        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodId(goods.getId());
        seckillOrderService.save(seckillOrder);
        //把秒杀订单存入redis
        redisTemplate.opsForValue().set("order:" + user.getId() + goods.getId(), seckillOrder);
        return order;
    }


    /*
    * 订单详情
    * */
    @Override
    public OrderDetailVo detail(Long orderId) {
        if (orderId == null) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_FOUND);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodId());
        OrderDetailVo detailVo = new OrderDetailVo();
        detailVo.setOrder(order);
        detailVo.setGoodsVo(goodsVo);
        return detailVo;
    }
}
