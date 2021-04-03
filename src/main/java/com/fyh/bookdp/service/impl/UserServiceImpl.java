package com.fyh.bookdp.service.impl;

import com.fyh.bookdp.entity.User;
import com.fyh.bookdp.mapper.UserMapper;
import com.fyh.bookdp.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fyh
 * @since 2021-03-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
