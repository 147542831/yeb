package com.xxxx.server.controller;


import com.xxxx.server.pojo.Joblevel;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IJoblevelService;
import com.xxxx.server.service.IMenuRoleService;
import com.xxxx.server.service.IMenuService;
import com.xxxx.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hejian
 * @since 2021-09-02
 */
@RestController
@RequestMapping("/system/basic/permiss")
public class RoleController {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMenuRoleService iMenuRoleService;
    @Autowired
    private IMenuService menuService;

    /**
     *
     * 查询角色列表
     */
    @ApiOperation(value = "显示角色列表")
    @GetMapping
    public List<Role> roleList() {
        return roleService.roleList();
    }

    /**
     * 所有角色权限显示
     *
     *
     */
    @ApiOperation(value = "查询所有角色")
    @GetMapping("/menus")
    @ResponseBody
    public List<Menu> menuList(Integer rid) {
        return menuService.getAllMenus(rid);
    }

    /**
     *
     * 根据角色id查询拥有的权限
     */
    @ApiOperation(value = "角色权限回显")
    @GetMapping("/mid/{rid}")
    @ResponseBody
    public List<Integer> getMidsByRids(@PathVariable Integer rid) {
        return menuService.getMidsByRid(rid);
    }

    /**
     * 添加角色
     *
     */
    @PostMapping("/role")
    @ApiOperation(value = "添加角色")
    public RespBean addRole(@RequestBody Role role) {
        //将添加结果存入公共返回对象
        return roleService.insertRole(role.getName(), role.getNameZh());
    }

    /**
     *
     *
     * 根据角色id删除角色记录
     */
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/role/{rid}")
    @ResponseBody
    public RespBean deleteRole(@PathVariable Integer rid) {
        // 进行参数的非空校验
        if (null == rid || rid == 0) {
            return RespBean.error("角色不存在请刷新后重试");
        }
        return roleService.deleteRoleByRid(rid);
    }


    /**
     * @param rid  角色id
     * @param mids 关联表中mids
     * @return com.xxxx.yeb.pojo.RespBean
     *
     *
     */
    @ApiOperation(value = "角色授权")
    @PutMapping("/")
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        // 更新成功
        if (menuService.updateMenuRole(rid, mids)) {
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
}
