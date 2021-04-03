package com.fyh.bookdp.service;

import com.fyh.bookdp.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fyh.bookdp.vo.CartVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fyh
 * @since 2021-03-08
 */
public interface CartService extends IService<Cart> {
    public List<CartVO> findAllCartVOByUserId(Integer id);
}
