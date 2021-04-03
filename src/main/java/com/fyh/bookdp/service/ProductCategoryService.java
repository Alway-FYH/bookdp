package com.fyh.bookdp.service;

import com.fyh.bookdp.entity.ProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fyh.bookdp.vo.ProductCategoryVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fyh
 * @since 2021-03-08
 */
public interface ProductCategoryService extends IService<ProductCategory> {
    public List<ProductCategoryVO> getAllProductCategoryVO();
}
