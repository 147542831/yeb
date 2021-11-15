package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.MenuRole;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hejian
 * @since 2021-09-02
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {
    /**
     *
     * 根据角色id查询该角色是否别引用
     */
    List<MenuRole> selectByRid(Integer rid);


    /**
     * 删除角色
     *
     *
     *
     */
    boolean deleteByRid(Integer rid);

    /**
     * 添加权限
     *
     */
    Integer insertRole(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}
