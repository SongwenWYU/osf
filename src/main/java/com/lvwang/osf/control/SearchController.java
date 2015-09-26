package com.lvwang.osf.control;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lvwang.osf.service.EventService;

@Controller
@RequestMapping("/search")
public class SearchController {

	@Autowired
	@Qualifier("eventService")
	private EventService eventService;
	
	@RequestMapping("/")
	public ModelAndView search(@RequestParam("term") String term) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("search");
		mav.addObject("data", eventService.getEventsByTitleOrContentContains(term));
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value="/event", method=RequestMethod.POST)
	public Map<String, Object> searchEvent(@RequestParam("term") String term) {
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("data", eventService.getEventsByTitleOrContentContains(term));
		return ret;
	}
	
	
}
