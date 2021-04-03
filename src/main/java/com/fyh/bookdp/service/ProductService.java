package com.fyh.bookdp.service;

import com.fyh.bookdp.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fyh
 * @since 2021-03-08
 */
public interface ProductService extends IService<Product> {
    public List<Product> findByCategoryId(String type,Integer categoryId);
}
