package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Department;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hejian
 * @since 2021-09-02
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    List<Department> queryAllModules();

    int addOrUpdateDepartment(String name);

    String queryDepartMent(String name);

    Department selectDeparymentById(Integer id);

    int selectIdByName(String name);
}
