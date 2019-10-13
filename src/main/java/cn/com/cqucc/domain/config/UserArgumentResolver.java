package cn.com.cqucc.domain.config;

import cn.com.cqucc.domain.MiaoshaUser;
import cn.com.cqucc.service.MiaoShaUserService;
import cn.com.cqucc.service.iml.MiaoShaUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    MiaoShaUserService userService;

    /**
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> parameterType = methodParameter.getParameterType(); // 获取参数类型
        return parameterType == MiaoshaUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        String parameterToken = request.getParameter(MiaoShaUserServiceImpl.COOK_NAME_TOKEN);
        String cookieToken = getCookieValue(request, MiaoShaUserServiceImpl.COOK_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(parameterToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(parameterToken) ? cookieToken : parameterToken;
        return userService.getByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookNameToken) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookNameToken)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
