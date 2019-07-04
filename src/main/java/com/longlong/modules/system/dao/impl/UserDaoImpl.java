package com.longlong.modules.system.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import com.longlong.modules.system.dao.UserDao;
import com.longlong.modules.system.vo.User;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * UserDaoImpl
 */

@Component
public class UserDaoImpl implements UserDao {

    @Resource
    private JdbcTemplate jt;

    @Override
    public User getUserByUsername(String username) {
        String sql = "select username,password from users where username = ?";
        List<User> list = jt.query(sql, new String[]{ username }, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        });

        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<String> queryRolesByUsername(String username) {
        String sql = "select role_name from user_roles where username = ?";
        List<String>  list = jt.query(sql,new String[]{username},new RowMapper<String>() {

            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("role_name");
            }
        });
        return list;
    }

    @Override
    public List<String> queryPermissionsByUsername(String username) {
        String sql = "select rp.permission from user_roles ur join roles_permissions rp on ur.role_name = rp.role_name where ur.username = ?";
        List<String> list = jt.query(sql,new String[]{username},new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("permission");
            }
        });
        return list;
    }

}