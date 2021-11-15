package com.xxxx.server.controller;


import com.xxxx.server.pojo.Joblevel;
import com.xxxx.server.pojo.Position;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IJoblevelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器(职称操作)
 * </p>
 *
 * @author hejian
 * @since 2021-09-02
 */
@RestController
@RequestMapping("/system/basic/joblevel")
public class JoblevelController {
    @Autowired
    private IJoblevelService joblevelService;

    @ApiOperation(value ="获取所有职称信息")
    @GetMapping("/")
    public List<Joblevel> getAllJoblevel(){
        return joblevelService.list();
    }

    @ApiOperation(value = "添加职称信息")
    @PostMapping("/")
    public RespBean addJoblevel(@RequestBody Joblevel joblevel){
        joblevel.setCreateDate(LocalDateTime.now());
        if (joblevelService.save(joblevel)){
            return RespBean.success("添加成功！");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation(value = "更新职称信息")
    @PutMapping("/")
    public RespBean updateJoblevel(@RequestBody Joblevel joblevel){
        if (joblevelService.updateById(joblevel)){
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @ApiOperation(value = "删除职称信息")
    @DeleteMapping("/{id}")
    public RespBean deleteJoblevel(@PathVariable Integer id){
        if (joblevelService.removeById(id)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "批量删除职称信息")
    @DeleteMapping("/")
    public RespBean deleteJoblevelById(Integer[] ids){
        if (joblevelService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("删除成功！");
        }
        return  RespBean.error("删除失败！");
    }


}
