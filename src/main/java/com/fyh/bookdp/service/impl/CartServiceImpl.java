package com.fyh.bookdp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyh.bookdp.entity.Cart;
import com.fyh.bookdp.entity.Product;
import com.fyh.bookdp.enums.ResultEnum;
import com.fyh.bookdp.exception.MyException;
import com.fyh.bookdp.mapper.CartMapper;
import com.fyh.bookdp.mapper.ProductMapper;
import com.fyh.bookdp.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fyh.bookdp.vo.CartVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fyh
 * @since 2021-03-08
 */
@Service
@Slf4j
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public boolean save(Cart entity) {
        //减少库存的数量
        Product product = productMapper.selectById(entity.getProductId());
        Integer stock = product.getStock() - entity.getQuantity();

        //防止坏蛋使用bug买东西
        if (stock < 0){
            log.error("[添加购物车]库存不足！！！stock={}",stock);
            throw new MyException(ResultEnum.STOCK_ERROR);
        }

        product.setStock(stock);
        productMapper.updateById(product);
        if (cartMapper.insert(entity) == 1){
            return true;
        }
        return false;
    }

    @Override
    public List<CartVO> findAllCartVOByUserId(Integer id) {
        List<CartVO> cartVOList = new ArrayList<>();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id",id);
        List<Cart> cartList = cartMapper.selectList(wrapper);
        for (Cart cart : cartList) {
            CartVO cartVO = new CartVO();
            Product product = productMapper.selectById(cart.getProductId());
            BeanUtils.copyProperties(product,cartVO);
            BeanUtils.copyProperties(cart,cartVO);
            cartVOList.add(cartVO);
        }
        return cartVOList;
    }
}
