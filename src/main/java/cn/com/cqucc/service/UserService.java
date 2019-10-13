package cn.com.cqucc.service;

import cn.com.cqucc.domain.User;

public interface UserService  {

    public User findById(Integer id);

    public boolean save();
}
