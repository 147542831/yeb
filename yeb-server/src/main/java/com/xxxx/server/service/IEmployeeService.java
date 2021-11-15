package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.*;
import com.xxxx.server.query.QueryEmployee;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hejian
 * @since 2021-09-02
 */
public interface IEmployeeService extends IService<Employee> {
    Map<String, Object> employeeList(QueryEmployee employee);

    RespBean getMaxWorkID();

    List<Position> initPositions();

    List<Nation> initNations();

    List<Joblevel> initJoblevels();

    List<PoliticsStatus> initPoliticsstatus();

    List<Department> initDeps();

    void deleteEmp(Integer id);

    void AddEmp(Employee employee);

    void UpdateEmp(Employee employee);

    List<Employee> export();

    /**
     * @description: 导入Excel文件
     * @param: multipartFile
     * @return:
     * @throws:
     * @author: hxxiapgy
     * @date: 2020/7/24 12:07
     */
    void importAll(MultipartFile multipartFile) throws Exception;

}
