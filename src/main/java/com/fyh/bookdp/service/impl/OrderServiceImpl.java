package com.fyh.bookdp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyh.bookdp.entity.*;
import com.fyh.bookdp.mapper.CartMapper;
import com.fyh.bookdp.mapper.OrderDetailMapper;
import com.fyh.bookdp.mapper.OrderMapper;
import com.fyh.bookdp.mapper.UserAddressMapper;
import com.fyh.bookdp.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fyh.bookdp.service.UserAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fyh
 * @since 2021-03-08
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {


    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private UserAddressService userAddressService;

    @Override
    public boolean save(Orders orders, User user,String address,String remark) {
        //判断是新地址还是新地址
        if (orders.getUserAddress().equals("newAddress")){
            //将其存入数据库
            UserAddress userAddress = new UserAddress();
            userAddress.setAddress(address);
            userAddress.setRemark(remark);
            userAddress.setIsdefault(1);
            userAddress.setUserId(user.getId());
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("user_id",user.getId());
            int size = userAddressService.list(wrapper).size();
            if (size != 0){
                wrapper = new QueryWrapper();
                wrapper.eq("isdefault",1);
                wrapper.eq("user_id",user.getId());
                //查询到旧的默认地址并将其改位0
                UserAddress oldDefault = userAddressMapper.selectOne(wrapper);
                oldDefault.setIsdefault(0);
                userAddressMapper.updateById(oldDefault);
            }else {

            }
            //存储新的地址信息,并将其修改为默认地址
            userAddressMapper.insert(userAddress);
            orders.setUserAddress(address);
        }

        //存储orders
        orders.setUserId(user.getId());
        orders.setLoginName(user.getLoginName());
        //生成随即编码作为订单编号
        String seriaNumber = null;
        try {
            StringBuffer result = new StringBuffer();
            for (int i = 0;i<32;i++){
                result.append(Integer.toHexString(new Random().nextInt(16)));
            }
            seriaNumber = result.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        orders.setSerialnumber(seriaNumber);
        orderMapper.insert(orders);
        //存储ordersDetal
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id",user.getId());
        List<Cart> cartList = cartMapper.selectList(wrapper);
        for (Cart cart : cartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setId(null);
            orderDetail.setOrderId(orders.getId());
            orderDetailMapper.insert(orderDetail);
        }

        //购物完成销毁当前的购物车的内容
        wrapper = new QueryWrapper();
        wrapper.eq("user_id",user.getId());
        cartMapper.delete(wrapper);
        return true;
    }
}
