package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hejian
 * @since 2021-09-02
 */
public interface IRoleService extends IService<Role> {


    /**
     *
     * 查询角色列表
     */
    List<Role> roleList();


    /**
     * 添加角色
     *
     */
    RespBean insertRole(String roleName, String roleNameZh);

    /**
     *
     *
     * 根据角色id删除角色记录
     */
    RespBean deleteRoleByRid(Integer rid);
}
