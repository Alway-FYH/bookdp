package com.fyh.bookdp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductListVO {
    /**
     * 名称
     */
    private String name;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 消费
     */
    private Float cost;


}
