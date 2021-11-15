package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface MenuMapper extends BaseMapper<Menu> {

	/**
	 * 根据用户id查询菜单列表
	 * @param id
	 * @return
	 */
	List<Menu> getMenusByAdminId(Integer id);

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
}
