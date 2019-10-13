package cn.com.cqucc.service.iml;

import cn.com.cqucc.dao.UserDao;
import cn.com.cqucc.domain.User;
import cn.com.cqucc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    /**
     * 用于测试事务 需要加上一个注解 @Transactional
     *  SQLIntegrityConstraintViolationException: 主键冲突
     * @return
     */
    @Override
    @Transactional
    public boolean save() {
        User u1 = new User();
        u1.setId(2);
        u1.setName("222");
        userDao.save(u1);
        User u2 = new User();
        u2.setId(3);
        u2.setName("111");
        userDao.save(u2);
        return true;
    }
}
