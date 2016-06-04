package com.lvwang.osf.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lvwang.osf.model.Event;
import com.lvwang.osf.model.User;
import com.lvwang.osf.service.FeedService;
import com.lvwang.osf.util.Dic;
import com.lvwang.osf.web.RequestAttribute;

@Controller
@RequestMapping("/api/v1/timeline")
public class TimeLineAPI {

	
	@Autowired
	@Qualifier("feedService")
	private FeedService feedService;
	
	
	@ResponseBody
	@RequestMapping("/")
	public Map<String, Object> showHomePage(@RequestAttribute("uid") Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println("uid" + id);
		List<Event> feeds = feedService.getFeeds(id);
		map.put("feeds", feeds);		
		map.put("dic", new Dic());
		return map;
		
	}
	
	@ResponseBody
	@RequestMapping("/page/{num}")
	public Map<String, Object> nextPage(@PathVariable("num") String num_str, HttpSession session) {
		System.out.println(num_str);
		
		User user = (User)session.getAttribute("user");
		if(user == null) {
			return null;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		int num = Integer.parseInt(num_str);
		List<Event> feeds = feedService.getFeedsOfPage(user.getId(), num);
		map.put("feeds", feeds);
		map.put("dic", new Dic());
		return map;
	}
}
