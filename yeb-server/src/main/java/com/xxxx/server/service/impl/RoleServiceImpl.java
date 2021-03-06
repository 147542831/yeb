package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.mapper.*;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hejian
 * @since 2021-09-02
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Autowired
    private MenuRoleMapper menuRoleMapper;

    /**
     *
     * 查询角色列表
     */
    @Override
    public List<Role> roleList() {
        return roleMapper.selectList(new QueryWrapper<>());
    }



    /**
     * 添加角色
     *
     */
    @Override
    public RespBean insertRole(String roleName, String roleNameZh) {
        //判断roleName,roleNameZh非空
        if (null == roleName || roleName.isEmpty() || null == roleNameZh || roleNameZh.isEmpty()) {
            return RespBean.error("角色名不能为空");
        }
        //角色唯一性判断
        if (roleMapper.selectByName(roleName).size() != 0) {
            return RespBean.error("角色英文名已存在，请确认后重新添加");
        }
        if (roleMapper.selectByNameZh(roleNameZh).size() !=0) {
            return RespBean.error("角色中文名已存在，请确认后重新添加");
        }
        Role role = new Role();
        role.setName("ROLE_" + roleName.trim());
        role.setNameZh(roleNameZh.trim());
        // 健壮性判断
        if (roleMapper.insert(role) < 1) {
            return RespBean.error("角色添加失败，请刷新后重试");
        }
        return RespBean.success("角色添加成功");
    }

    /**
     *
     *
     * 根据角色id删除角色记录
     */
    @Override
    public RespBean deleteRoleByRid(Integer rid) {
        // 查询角色下有无用户;返回对象集合
        if (adminRoleMapper.selectByRid(rid).size() > 0) {
            return RespBean.error("角色下存在用户无法删除，请到操作员管理取消角色下所有用户再删除");
        }
        // 查询角色下是否有权限，返回集合；集合为空代表没有权限，角色直接删除；有权限，先删权限再删角色
        if (menuRoleMapper.selectByRid(rid).size() == 0) {
            // 没有权限直接删除角色
            if (roleMapper.deleteRoleByRid(rid)) {
                return RespBean.success("角色删除成功");
            }
            return RespBean.error("角色删除失败，请刷新后重试!!!!!!!!!!!");
        } else {
            // 判断删除权限是否成功，返回Boolean类型，为真则删除成功；然后删除角色
            if (menuRoleMapper.deleteByRid(rid)) {
                // 判断角色是否删除成功，返回Boolean值，
                if (roleMapper.deleteRoleByRid(rid)) {
                    return RespBean.success("角色删除成功");
                }
                return RespBean.error("角色删除失败，请刷新后重试!!!!!!!!!!!");
            }
            return RespBean.error("角色删除失败，请刷新后重试!!!!!!!!!!!");
        }
    }


}
