package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Admin;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hejian
 * @since 2021-09-02
 */
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * @description: 根据用户id，修改用户的enable
     * @param: paramAdmin
     * @return: {@link int}
     * @throws:
     * @author: hxxiapgy
     * @date: 2020/7/18 20:42
     */
    int updateAdminEnabledById(Map<String, Object> map);

    /**
     * @description: 根据id修改用户的头像
     * @param: admin
     * @return:
     * @throws:
     * @author: hxxiapgy
     * @date: 2020/7/20 14:13
     */
    int updateUserFaceById(@Param("admin") Admin admin);

    /**
     * @description: 根据用户id修改密码
     * @param: adminId
     * @param: pass
     * @return:
     * @throws:
     * @author: hxxiapgy
     * @date: 2020/7/20 21:17
     */
    int updatePasswordById(Integer adminId, String pass);

    /**
     * @description: 修改用户信息
     * @param: admin
     * @return:
     * @throws:
     * @author: hxxiapgy
     * @date: 2020/7/20 22:11
     */
    int updateAdminInfo(@Param("admin") Admin admin);
}