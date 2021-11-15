package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.Role;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoubin
 */
public interface RoleMapper extends BaseMapper<Role> {
	/**
	 * 根据用户id查询角色列表
	 * @param adminId
	 * @return
	 */
	List<Role> getRoles(Integer adminId);

	/**
	 * 判断角色唯一
	 *
	 */
	List<Role> selectByName(String roleName);

	/**
	 * 判断角色唯一
	 *
	 *
	 */
	List<Role> selectByNameZh(String roleNameZh);

	/**
	 * 直接删除角色
	 * @param rid
	 * @return
	 */
	boolean deleteRoleByRid(Integer rid);
}
