package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.AdminRole;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hejian
 * @since 2021-09-02
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 查询角色下是否拥有用户
     *
     * @author lhr
     * @date 20:41 2020/7/23 0023
     */
    List<AdminRole> selectByRid(Integer rid);
}
