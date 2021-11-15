package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxxx.server.pojo.Employee;
import com.xxxx.server.query.QueryEmployee;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hejian
 * @since 2021-09-02
 */
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 获取帐套与员工的级联总数
     */
    public Integer contCommon(Integer id);

    public Integer contAll();

    public ArrayList<Employee> selectAll();

    /**
     * 查询该职位下的总人数
     *
     * @param id
     * @return
     */
    @Select("SELECT COUNT(1) FROM t_employee WHERE posId = #{id}")
    int selectcount(Integer id);


    Employee selectByName(String name);

    String selectMaxWorkId();

    IPage<Employee> selectPagePlus(@Param("page") Page page, @Param("queryEmployee") QueryEmployee queryEmployee);

    int selectCountEmp();


    int insertBatch(List<Employee> employeeList);
}
