package com.fyh.bookdp.service;

import com.fyh.bookdp.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fyh.bookdp.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fyh
 * @since 2021-03-08
 */
public interface OrderService extends IService<Orders> {
    public boolean save(Orders orders, User user,String address,String remark);
}
