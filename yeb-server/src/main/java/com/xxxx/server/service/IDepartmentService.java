package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Department;
import com.xxxx.server.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hejian
 * @since 2021-09-02
 */
public interface IDepartmentService extends IService<Department> {

    /**
     * 查询部门树结构
     *
     * @return
     */
    List<Department> queryAllModules();

    /**
     * 添加子部门
     */
    RespBean addDepartment(Department department);

    /**
     * 删除子部门
     */
    void deleteDepartment(Integer id);
}
