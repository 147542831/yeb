package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 通过用户id查询菜单列表
     * @return
     */
    List<Menu> getMenusByAdminId();

    /**
     * 根据角色获取菜单列表
     * @return
     */
    List<Menu> getMenusWithRole();

    /**
     * 根据角色id获取菜单列表
     * @return
     */
    List<Integer> getMenusWithRoleByid(Integer id);

    /**
     * 所有菜单显示
     *
     *
     */
    List<Menu> getAllMenus(Integer rid);

    /**
     *
     * 根据角色id查询拥有的权限
     */
    List<Integer> getMidsByRid(Integer rid);

    /**
     * @param rid  角色id
     * @param mids 关联表中mids
     * @return com.xxxx.yeb.pojo.RespBean
     *
     *
     */
    boolean updateMenuRole(Integer rid, Integer[] mids);
}
