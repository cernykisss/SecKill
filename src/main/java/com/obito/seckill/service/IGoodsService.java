package com.obito.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.obito.seckill.pojo.Goods;
import com.obito.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kai
 * @since 2021-04-07
 */
public interface IGoodsService extends IService<Goods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodId);
}
