package com.lvwang.osf.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lvwang.osf.model.User;
import com.lvwang.osf.service.UserService;
import com.lvwang.osf.util.Property;


@Controller
@RequestMapping("/api/v1/account")
public class AccountAPI {
	
	@Autowired
	private UserService userService;

	@ResponseBody
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public Map<String, Object> login(@RequestBody User user) {
		
		Map<String, Object> ret = userService.login(user.getUser_email(), user.getUser_pwd());
		String status = (String) ret.get("status");
		if(Property.SUCCESS_ACCOUNT_LOGIN.equals(status)) {
			ret.put("token", userService.newToken(user));	
		}
		return ret;		

	}
	
	@ResponseBody
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public Map<String, String> register(@RequestBody User user) {
		System.out.println("resister....");
		Map<String, String> map = new HashMap<String, String>();
		String status = userService.register(user.getUser_name(), user.getUser_email(), user.getUser_pwd(), user.getUser_cfm_pwd(), map);
		if(Property.SUCCESS_ACCOUNT_REG.equals(status)){
			userService.activateUser(user.getUser_email(), user.getUser_activationKey());
			map.put("api_key", "key_for_login_auth");
		} 
		map.put("status", status);
		return map;
	}
}
