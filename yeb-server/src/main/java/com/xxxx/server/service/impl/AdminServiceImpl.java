package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.config.security.component.JwtTokenUtil;
import com.xxxx.server.enums.EnabledChangeEnum;
import com.xxxx.server.mapper.AdminMapper;
import com.xxxx.server.mapper.AdminRoleMapper;
import com.xxxx.server.mapper.RoleMapper;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.AdminRole;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IAdminService;
import com.xxxx.server.utils.AssertUtil;
import com.xxxx.server.utils.PhoneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hejian
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

	@Autowired
	private AdminMapper adminMapper;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Value("${jwt.tokenHead}")
	private String tokenHead;
	@Autowired
	private RoleMapper roleMapper;
	@Resource
	private AdminRoleMapper adminRoleMapper;

	private static final Integer DEFAULT_ROLE_ID = 9;


	/**
	 * 登录之后返回token
	 * @param username
	 * @param password
	 * @param code
	 * @param request
	 * @return
	 */
	@Override
	public RespBean login(String username, String password, String code, HttpServletRequest request) {
		String captcha = (String) request.getSession().getAttribute("captcha");
		if (StringUtils.isEmpty(code)||!captcha.equalsIgnoreCase(code)){
			return RespBean.error("验证码输入错误，请重新输入！");
		}
		//登录
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		if (null==userDetails||!passwordEncoder.matches(password,userDetails.getPassword())){
			return RespBean.error("用户名或密码不正确");
		}
		if (!userDetails.isEnabled()){
			return RespBean.error("账号被禁用，请联系管理员！");
		}
		//更新security登录用户对象
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails
				,null,userDetails. getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		//生成token
		String token = jwtTokenUtil.generateToken(userDetails);
		Map<String,String> tokenMap = new HashMap<>();
		tokenMap.put("token",token);
		tokenMap.put("tokenHead",tokenHead);
		return RespBean.success("登录成功",tokenMap);
	}

	/**
	 * 根据用户名获取用户
	 * @param username
	 * @return
	 */
	@Override
	public Admin getAdminByUserName(String username) {
		return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username));
	}


	/**
	 * 根据用户名id查询角色列表
	 * @param adminId
	 * @return
	 */
	@Override
	public List<Role> getRoles(Integer adminId) {
		return roleMapper.getRoles(adminId);
	}
	/**
	 * @description: 查询操作员列表
	 * @param: keywords
	 * @return: {@link List<Admin>}
	 * @throws:
	 * @author: hxxiapgy
	 * @date: 2020/7/18 14:06
	 */
	@Override
	public List<Admin> selectAdminList(String keywords) {

		// ‘’为没有查询条件
		String none = "''";
		// 设置查询条件
		QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();

		// 如果传递keywords，条件搜索
		if (StringUtils.isNotBlank(keywords) && !none.equals(keywords)) {
			queryWrapper.like("name", keywords);
		}
		// 查询操作员列表
		List<Admin> adminList = adminMapper.selectList(queryWrapper);
		// 查询角色
		adminList.forEach(admin -> {
			// 设置角色信息
			admin.setRoles(getRoles(admin.getId()));
			// 将密码设为null
			admin.setPassword(null);
		});
		return adminList;
	}

	/**
	 * @description: 查询所有角色列表
	 * @param:
	 * @return: {@link List< Role>}
	 * @throws:
	 * @author: hxxiapgy
	 * @date: 2020/7/18 17:36
	 */
	@Override
	public List<Role> selectAllRoleList() {
		return roleMapper.selectList(new QueryWrapper<Role>());
	}

	/**
	 * @description: 修改用户角色信息
	 * @param: adminId
	 * @param: rids
	 * @return:
	 * @throws:
	 * @author: hxxiapgy
	 * @date: 2020/7/18 18:01
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateAdminRole(Integer adminId, String rids) {

		// 判断用户是否被禁用
		Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("id", adminId));
		AssertUtil.isTrue(!admin.getEnabled(), "该用户已被禁用，请启用后在设置角色！");

		// 创建sql条件对象
		QueryWrapper<AdminRole> queryWrapper = new QueryWrapper<>();

		// 1.参数校验
		AssertUtil.isTrue(null == adminId || 0 == adminId, "用户不合法！");

		// 2.根据用户id删除原有的角色数据
		deleteAdminRoleByAdminId(adminId);
		// 清空queryWrapper中的条件
		queryWrapper.clear();

		// 3.判断是否有角色数据绑定
		if (StringUtils.isNotBlank(rids)) {

			// 将rids转为数组
			String[] ridStrs = rids.split(",");

			// 添加标识
			int insCount = 0;
			// 为该用户绑定新的角色数据
			AdminRole adminRole = new AdminRole();
			adminRole.setAdminId(adminId);
			for (String rid : ridStrs) {
				adminRole.setRid(Integer.parseInt(rid));
				adminRoleMapper.insert(adminRole);
				insCount++;
			}
			AssertUtil.isTrue(insCount < ridStrs.length, "用户角色绑定失败！");
		} else {
			// 没有权限绑定默认权限
			AdminRole adminRole = new AdminRole();
			adminRole.setAdminId(adminId);
			adminRole.setRid(DEFAULT_ROLE_ID);
		}
	}

	/**
	 * @description: 根据用户id删除相关的角色信息
	 * @param: adminId
	 * @return:
	 * @throws:
	 * @author: hxxiapgy
	 * @date: 2020/7/18 21:09
	 */
	private void deleteAdminRoleByAdminId(Integer adminId) {

		// 创建sql条件对象
		QueryWrapper<AdminRole> queryWrapper = new QueryWrapper<>();
		// 根据用户id查询该用户具有的角色数量
		Integer count = adminRoleMapper.selectCount(queryWrapper.eq("adminId", adminId));
		if (count > 0) {
			// 删除原有角色数据
			int delRows = adminRoleMapper.delete(queryWrapper);
			AssertUtil.isTrue(delRows < count, "用户信息重置失败！");
		}

	}

	/**
	 * @description: 禁用用户
	 * @param: admin
	 * @return:
	 * @throws:
	 * @author: hxxiapgy
	 * @date: 2020/7/18 19:37
	 */
	@Override
	public EnabledChangeEnum enabledChange(Map<String, Object> admin) {

		AssertUtil.isTrue(null == admin, "请选择要禁用的用户！");

		Integer adminId = (Integer) admin.get("id");
		boolean enabled = (boolean) admin.get("enabled");

		// 创建参数对象
		Map<String, Object> map = new HashMap<>();
		map.put("id", adminId);
		map.put("enabled", enabled ? 1 : 0);


		// 根据id查询用户信息
		Admin temp = adminMapper.selectOne(new QueryWrapper<Admin>().eq("id", adminId));

		if (enabled) {
			// 启用用户
			if (temp.getEnabled()) {
				// 用户已经被启用
				return EnabledChangeEnum.ENABLED;
			} else {
				// 修改用户的enabled
				AssertUtil.isTrue(adminMapper.updateAdminEnabledById(map) < 0, "用户启用失败！");
				return EnabledChangeEnum.ENABLE;
			}
		} else if (!enabled) {
			// 禁用用户
			if (!temp.getEnabled()) {
				// 用户已经被禁用
				return EnabledChangeEnum.EABLED;
			} else {
				// 修改用户的enabled
				AssertUtil.isTrue(adminMapper.updateAdminEnabledById(map) < 0, "用户禁用失败！");
				return EnabledChangeEnum.DISABLE;
			}
		} else {
			return null;
		}
	}

	/**
	 * @description: 根据用户id删除用户
	 * @param: id
	 * @return:
	 * @throws:
	 * @author: hxxiapgy
	 * @date: 2020/7/18 21:04
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteAdminById(Integer id) {

		// 判断是否存在要删除的用户
		AssertUtil.isTrue(null == id, "请选择要删除的用户！");

		// 删除用户绑定的角色信息
		deleteAdminRoleByAdminId(id);

		// 删除用户
		AssertUtil.isTrue(adminMapper.deleteById(id) < 0, "用户删除失败！");

	}

	/**
	 * @Author lhr
	 * @Date 20:41 2020/7/22 0022
	 * 查询角色下是否存在用户
	 */
	@Override
	public List<AdminRole> selectByrid(Integer rid) {
		return adminRoleMapper.selectByRid(rid);
	}

	/**
	 * @description: 修改用户密码
	 * @param: ruleForm
	 * @return:
	 * @throws:
	 * @author: hxxiapgy
	 * @date: 2020/7/20 20:57
	 */
	@Override
	public void updatePassword(Map<String, Object> ruleForm) {

		// 参数不能为空
		AssertUtil.isTrue(null == ruleForm, "参数异常");

		// 获取用户输入数据
		Integer adminId = (Integer) ruleForm.get("adminId");
		// 加密密码
		String oldPass = (String) ruleForm.get("oldPass");
		String pass = (String) ruleForm.get("pass");
		String checkPass = (String) ruleForm.get("checkPass");

		// 参数校验
		checkParamForPassword(adminId, oldPass, pass, checkPass);

		// 修改密码
		AssertUtil.isTrue(
				adminMapper.updatePasswordById(adminId, passwordEncoder.encode(pass)) != 1
				, "密码修改失败!");


	}

	/**
	 * @param adminStr
	 * @description: 修改用户信息
	 * @param: admin
	 * @return:
	 * @throws:
	 * @author: hxxiapgy
	 * @date: 2020/7/20 21:59
	 */
	@Override
	public void updateAdminInfo(String adminStr) {

		// 参数校验
		AssertUtil.isTrue(StringUtils.isBlank(adminStr), "更新的数据存在异常！");


		// 转为admin对象
		Admin admin = JSON.parseObject(adminStr, Admin.class);

		AssertUtil.isTrue(null == admin || null == admin.getId(), "用户不存在！");
		AssertUtil.isTrue(null == adminMapper.
						selectOne(new QueryWrapper<Admin>().eq("id", admin.getId()))
				, "用户不存在");
		// 校验手机号格式
		AssertUtil.isTrue(!PhoneUtil.isMobile(admin.getPhone()), "手机号格式不合法！");
		// 更新
		int row = adminMapper.updateAdminInfo(admin);

		AssertUtil.isTrue(row < 0, "用户信息更失败！");

	}

	/**
	 * 校验修改密码参数
	 *
	 * @param: ruleForm
	 * @return:
	 * @author: hxxiapgy
	 */
	private void checkParamForPassword(Integer adminId, String oldPass, String pass, String checkPass) {

		AssertUtil.isTrue(null == adminId, "无法重新用户信息");
		AssertUtil.isTrue(StringUtils.isBlank(oldPass), "原密码不能为空");
		AssertUtil.isTrue(StringUtils.isBlank(pass), "新密码不能为空！");
		AssertUtil.isTrue(StringUtils.isBlank(checkPass), "确认密码不能为空！");
		AssertUtil.isTrue(!pass.equals(checkPass), "确认密码不一致！");
		AssertUtil.isTrue(oldPass.equals(pass), "新密码不能为原密码相同！");

		String p = passwordEncoder.encode("123");

		// 查询用户
		Admin temp = adminMapper.selectOne(new QueryWrapper<Admin>().eq("id", adminId));
		AssertUtil.isTrue(null == temp, "用户不存在！");
		AssertUtil.isTrue(!passwordEncoder.matches(oldPass, temp.getPassword()), "密码不正确！");

	}

}
