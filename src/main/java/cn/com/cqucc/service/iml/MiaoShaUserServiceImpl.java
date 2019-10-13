package cn.com.cqucc.service.iml;

import cn.com.cqucc.dao.MiaoShaUserDao;
import cn.com.cqucc.domain.MiaoshaUser;
import cn.com.cqucc.exception.GlobalException;
import cn.com.cqucc.redis.MiaoShaUserKey;
import cn.com.cqucc.redis.RedisService;
import cn.com.cqucc.result.CodeMsg;
import cn.com.cqucc.service.MiaoShaUserService;
import cn.com.cqucc.utils.MD5Util;
import cn.com.cqucc.utils.UUIDUtil;
import cn.com.cqucc.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoShaUserServiceImpl implements MiaoShaUserService {


    public static final String COOK_NAME_TOKEN = "token";

    @Autowired
    private MiaoShaUserDao miaoShaUserDao;

    @Autowired
    private RedisService redisService; // 将个人信息存入到缓存中

    @Override
    public MiaoshaUser findById(Long id) {
        //取缓存
        MiaoshaUser user = redisService.get(MiaoShaUserKey.getById, "" + id, MiaoshaUser.class);

        if (user != null) {
            return user;
        }

        //如果缓存中没有这个对象 就查询 数据库
        user = miaoShaUserDao.findById(id);
        if (user != null) {
            redisService.set(MiaoShaUserKey.getById, "" + id, user);
        }
        return user;
    }

    public boolean updatePassword(String token, long id, String formPass) {
        //取user
        MiaoshaUser user = findById(id);
        if(user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser(); // 新建一个一个对象去更新。 提升速度
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        miaoShaUserDao.update(toBeUpdate);
        //处理缓存
        redisService.delete(MiaoShaUserKey.getById, ""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoShaUserKey.token, token, user);
        return true;
    }

    @Override
    public String login(HttpServletResponse response, LoginVo loginVo) {


        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String formPassWord = loginVo.getPassword();
        String mobile = loginVo.getMobile();
        // 判断手机号是否存在
        MiaoshaUser miaoshaUser = findById(Long.parseLong(mobile));

        if (miaoshaUser == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        // 验证密码
        String dbPassword = miaoshaUser.getPassword();
        String saltDB = miaoshaUser.getSalt();
        String passCal = MD5Util.formPassToDBPass(formPassWord, saltDB);
        if (!passCal.equals(dbPassword)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();

        addCookie(response, token, miaoshaUser);
        return token; // 登录成功
    }

    @Override
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {

        //参数验证
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser miaoshaUser = redisService.get(MiaoShaUserKey.token, token, MiaoshaUser.class);

        // 延长有效期 ： 重新将缓存中的Cookie清除重新写入
        if (miaoshaUser != null) {
            addCookie(response, token, miaoshaUser);
        }

        return miaoshaUser;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser miaoshaUser) {
        // 生成Cookie
        redisService.set(MiaoShaUserKey.token, token, miaoshaUser);
        Cookie cookie = new Cookie(COOK_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoShaUserKey.token.expireSeconds()); // 设置为它的有效期
        cookie.setPath("/");
        response.addCookie(cookie);
    }


}
