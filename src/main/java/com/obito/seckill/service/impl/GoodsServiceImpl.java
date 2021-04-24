package com.obito.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.obito.seckill.mapper.GoodsMapper;
import com.obito.seckill.pojo.Goods;
import com.obito.seckill.service.IGoodsService;
import com.obito.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kai
 * @since 2021-04-07
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    /*
    * 获取商品列表
    * */
    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }

    /*
    *获取商品详情
    *  */
    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodId) {
        return goodsMapper.findGoodsByGoodId(goodId);
    }
}
