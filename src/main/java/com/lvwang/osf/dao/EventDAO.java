package com.lvwang.osf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.lvwang.osf.model.Event;


public interface EventDAO {
	
	String TABLE = "osf_events";
	
	@Insert("insert into " + TABLE + 
			" values(null,#{object_type},#{object_id},#{ts},"
			+ "#{user_id},#{user_name},#{user_avatar},#{like_count},"
			+ "#{share_count},#{comment_count},#{title},#{summary},"
			+ "#{content},#{tags},#{following_user_id},"
			+ "#{following_user_name},#{follower_user_id},"
			+ "#{follower_user_name}")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id", flushCache=true)
	int save(Event event);
	
	@Select("select * from " + TABLE + " limit #{start},#{step}")
	List<Event> getEvents(@Param("start")int start, @Param("step")int step);
	
	
	List<Event> getEventsWithIDs(List<Integer> event_ids);
	List<Event> getEventsWithRelations(Map<Integer, List<Integer>> relations);
	List<Event> getEventsOfUser(int user_id, int count);
	List<Event> getEventsHasPhoto(int start, int step);
	void delete(int id);
	void delete(int object_type, int object_id);
	Event getEvent(int object_type, int object_id);
}
