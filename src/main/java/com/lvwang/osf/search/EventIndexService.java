package com.lvwang.osf.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.springframework.stereotype.Service;

import com.lvwang.osf.model.Event;

@Service("eventIndexService")
public class EventIndexService implements IndexService<Event>{

	public void add(Event event) {
		Document doc = new Document();
		doc.add(new LongField("id", event.getId(), Field.Store.YES));
		doc.add(new TextField("title", event.getTitle(), Field.Store.NO));
		doc.add(new TextField("content", event.getContent(), Field.Store.NO));
		
		try {
			IndexHolder.getIndexWriter().addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Event> findByTitleOrContent(String searchTerm) {
		List<Event> events = new ArrayList<Event>();
		Analyzer analyzer=new StandardAnalyzer();
		QueryParser parser = new MultiFieldQueryParser(new String[]{"title", "content"}, analyzer);
		Query query;
		try {
			query = parser.parse(searchTerm);
			IndexSearcher searcher = IndexHolder.getIndexSearcher();
			TopDocs docs = searcher.search(query, 10);
			ScoreDoc[] sds =docs.scoreDocs;
			for(ScoreDoc sd: sds){
				Document doc = searcher.doc(sd.doc);
				Event event = new Event();
				event.setId(Integer.valueOf(doc.get("id")));
				event.setTitle(doc.get("title"));
				event.setContent(doc.get("content"));
				events.add(event);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return events;
		
	}
	
}
