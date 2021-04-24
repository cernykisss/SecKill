package com.obito.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.obito.seckill.pojo.Goods;
import com.obito.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kai
 * @since 2021-04-07
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /*
    * 获取商品列表
    * */
    List<GoodsVo> findGoodsVo();

    /*
    * 获取商品详情
    * */
    GoodsVo findGoodsByGoodId(Long goodId);
}
