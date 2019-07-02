package com.longlong.modules.system.shiro.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.longlong.modules.system.dao.UserDao;
import com.longlong.modules.system.vo.User;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * CustomRealm
 */
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserDao userDao;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        
        //从数据库或者缓存中获取角色数据
        Set<String> roles = getRolesByUsername(username);
        Set<String> permissions = getPermissionsByUsername(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    /**
     * 数据库查询权限
     * @param username
     * @return
     */
    private Set<String> getPermissionsByUsername(String username) {
        Set<String> sets = new HashSet<>();
        sets.add("user:delete");
        sets.add("user:add");
        return sets;
    }

    /**
     * 数据库查询角色
     * 
     * @param username
     * @return
     */
    private Set<String> getRolesByUsername(String username) {
        List<String> list = userDao.queryRolesByUsername(username);
        Set<String> sets = new HashSet<>(list);
        return sets;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.从主体传过来的认证信息中，获取用户名
        String username = (String) token.getPrincipal();

        //2.通过用户名到数据库中获取凭证
        String password = getPasswordByUsername(username);
        if(password == null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username,password,"customRealm");
        //加盐(可以使用随机数，这里使用用户名)
        //authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("Mark"));
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(username));
        return authenticationInfo;
    }

    /**
     * 数据库查询凭证
     * @param username
     * @return
     */
    private String getPasswordByUsername(String username) {
        User user = userDao.getUserByUsername(username);
        if(user != null){
            return user.getPassword();
        }
        return null;
    }

    // public static void main(String[] args) {
    //     Md5Hash md5Hash = new Md5Hash("123456","Mark");
    //     System.out.println(md5Hash.toString());
    // }

}