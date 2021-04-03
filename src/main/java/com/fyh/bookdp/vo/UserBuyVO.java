package com.fyh.bookdp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserBuyVO {
    private Integer id;
    /**
     * 用户名
     */
    private String loginName;

    /**
     * 用户地址
     */
    private String userAddress;

    /**
     * 总金额
     */
    private Float cost;

    /**
     * 订单号
     */
    private String serialnumber;

    private Integer state;

    private Integer pay;


    private List<ProductListVO> productListVOS;




}
