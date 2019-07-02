package com.longlong.modules.system.dao;

import java.util.List;

import com.longlong.modules.system.vo.User;

/**
 * UserDao
 */
public interface UserDao {

	User getUserByUsername(String username);

    List<String> queryRolesByUsername(String username);
}