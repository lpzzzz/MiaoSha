package cn.com.cqucc.service;


import cn.com.cqucc.domain.MiaoshaUser;
import cn.com.cqucc.result.CodeMsg;
import cn.com.cqucc.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

public interface MiaoShaUserService {

    public MiaoshaUser findById(Long id);

    String login(HttpServletResponse response,LoginVo loginVo);

    MiaoshaUser getByToken(HttpServletResponse response,String token);
}
