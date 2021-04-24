package com.obito.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author kai
 * @since 2021-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long goodId;

    private Long deliveryAddr;

    private String goodName;

    private Integer goodCount;

    private BigDecimal goodPrice;

    /**
     * 1pc 2android 3ios
     */
    private Integer orderChannel;

    /**
     * 订单状态 0新建未支付 1已支付 2已发货 3已收货 4已退款 5已完成
     */
    private Integer status;

    private Date createDate;

    private Date payDate;


}
