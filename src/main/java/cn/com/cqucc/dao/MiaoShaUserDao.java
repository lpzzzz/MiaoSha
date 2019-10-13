package cn.com.cqucc.dao;

import cn.com.cqucc.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MiaoShaUserDao {

    @Select("select * from miaosha_user where id = #{id}")
    public MiaoshaUser findById(Long id);

    @Update("update miaosha_user set password = #{password} where id = #{id}")
    public void update(MiaoshaUser toBeUpdate);

}
