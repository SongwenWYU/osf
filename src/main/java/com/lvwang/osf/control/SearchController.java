package com.lvwang.osf.control;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.lvwang.osf.model.Event;
import com.lvwang.osf.model.Tag;
import com.lvwang.osf.service.FeedService;
import com.lvwang.osf.service.TagService;
import com.lvwang.osf.util.Dic;

@Controller
@RequestMapping("/search")
public class SearchController {

	@Autowired
	@Qualifier("feedService")
	private FeedService feedService;
	
	@Autowired
	@Qualifier("tagService")
	private TagService tagService;
	
	@RequestMapping("/feed")
	public ModelAndView searchFeed(@RequestParam("term") String term) {
		System.out.println(term);
//		try {
//			//term = new String(term.getBytes("ISO-8859-1"),"UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		ModelAndView mav = new ModelAndView();
		mav.setViewName("search/feed");
		mav.addObject("feeds", feedService.getFeedsByTitleOrContentContains(term));
		mav.addObject("dic", new Dic());
		mav.addObject("term", term);
		return mav;
	}
		
	@RequestMapping("/tag")
	public ModelAndView searchTag(@RequestParam("term") String term) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("search/tag");
		List<Tag> tags = tagService.searchTag(term);
		mav.addObject("tags", tags);
		Map<Integer, List<Event>> feeds = new TreeMap<Integer, List<Event>>();
		for(Tag tag: tags){
			feeds.put(tag.getId(), feedService.getFeedsByTag(0, tag.getId(), 3));
		}
		mav.addObject("feeds", feeds);
		mav.addObject("term", term);
		mav.addObject("dic", new Dic());
		return mav;
	}

	@RequestMapping("/user")
	public ModelAndView searchUser(@RequestParam("term") String term) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("search");
		mav.addObject("feeds", feedService.getFeedsByTitleOrContentContains(term));
		mav.addObject("dic", new Dic());
		return mav;
	}
	
}
