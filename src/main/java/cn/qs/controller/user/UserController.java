package cn.qs.controller.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.qs.bean.user.User;
import cn.qs.service.user.UserService;
import cn.qs.utils.DefaultValue;
import cn.qs.utils.JSONResultUtil;
import cn.qs.utils.MD5Util;
import cn.qs.utils.ValidateCheck;

@Controller
@RequestMapping("user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@RequestMapping("/user-list")
	public String member_list() {
		return "user/user-list";
	}

	@RequestMapping("/user-add")
	public String member_add(String from, ModelMap map) {
		if ("register".equals(from)) {
			map.put("userType", "2");
		} else {
			map.put("userType", "1");
		}

		return "user/user-add";
	}

	@RequestMapping("addUser")
	@ResponseBody
	public JSONResultUtil addUser(User user) {
		if (user != null && "admin".equals(user.getUsername())) {
			return JSONResultUtil.error("您不能添加管理员用户");
		}

		User findUser = userService.findUserByUsername(user.getUsername());
		if (findUser != null) {
			return JSONResultUtil.error("用户已经存在");
		}

		user.setCreatetime(new Date());
		user.setPassword(MD5Util.md5(user.getPassword(), ""));// md5加密密码
		logger.info("user -> {}", user);
		userService.addUser(user);
		return JSONResultUtil.ok();
	}

	@RequestMapping("getUsers")
	@ResponseBody
	public PageInfo<User> getUsers(@RequestParam Map condition) {
		int pageNum = 1;
		if (ValidateCheck.isNotNull(MapUtils.getString(condition, "pageNum"))) { // 如果不为空的话改变当前页号
			pageNum = MapUtils.getInteger(condition, "pageNum");
		}
		int pageSize = DefaultValue.PAGE_SIZE;
		if (ValidateCheck.isNotNull(MapUtils.getString(condition, "pageSize"))) { // 如果不为空的话改变当前页大小
			pageSize = MapUtils.getInteger(condition, "pageSize");
		}

		// 开始分页
		PageHelper.startPage(pageNum, pageSize);
		List<User> users = new ArrayList<User>();
		try {
			users = userService.getUsers(condition);
		} catch (Exception e) {
			logger.error("getUsers error！", e);
		}
		PageInfo<User> pageInfo = new PageInfo<User>(users);

		return pageInfo;
	}

	@RequestMapping("deleteUser")
	@ResponseBody
	public JSONResultUtil deleteUser(int id) {
		userService.deleteUser(id);
		return JSONResultUtil.ok();
	}

	@RequestMapping("updateUser")
	public String updateUser(Integer id, String from, ModelMap map, HttpServletRequest request) {
		if ("personal".equals(from)) {
			User user = (User) request.getSession().getAttribute("user");
			id = user.getId();
		}

		User user = userService.getUser(id);
		map.addAttribute("user", user);
		return "user/updateUser";
	}

	@RequestMapping("doUpdateUser")
	@ResponseBody
	public JSONResultUtil doUpdateUser(User user) {
		logger.info("user -> {}", user);
		userService.updateUser(user);
		return JSONResultUtil.ok();
	}
}