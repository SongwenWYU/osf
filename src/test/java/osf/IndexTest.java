package osf;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lvwang.osf.model.Event;
import com.lvwang.osf.search.EventIndexService;
import com.lvwang.osf.search.IndexHolder;
import com.lvwang.osf.service.PostService;

public class IndexTest {

	@Test
	public void test() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring/app-config.xml");
		PostService postService = (PostService)context.getBean("postService");
		EventIndexService eventIndexService = (EventIndexService)context.getBean("eventIndexService");
//		Event event = new Event();
//		event.setId(1);
//		event.setTitle("hello titile");
//		event.setContent("hello content");
//		eventIndexService.add(event);
		
		List<Event> events = eventIndexService.findByTitleOrContent("hello");
		if(events != null && events.size() !=0) {
			for(Event e : events) {
				System.out.println(e.getId());
			}
		}
		try {
			IndexHolder.getIndexWriter().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print("index ");
		//fail("Not yet implemented");
	}

}
