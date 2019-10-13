package cn.com.cqucc.dao;

import cn.com.cqucc.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    public User findById(Integer id);

    /**
     * 用于测试事务控制
     * @param user
     */
    @Insert("insert into user(id,name) values (#{id},#{name})")
    public void save(User user);
}
