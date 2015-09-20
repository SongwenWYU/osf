package com.lvwang.osf.search;

import java.io.File;
import java.io.IOException;

import javax.annotation.PreDestroy;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


public class IndexHolder {

	private static String indexDir = "/indexDir";
	private static IndexWriter indexWriter;
	private static IndexReader indexReader;
	private static IndexSearcher indexSearcher;
	
	static{
		String classpath = IndexHolder.class.getClassLoader().getResource("").getPath();
		try {
			Directory dir = FSDirectory.open(new File(classpath+indexDir));
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_4, analyzer);
			indexWriter = new IndexWriter(dir, iwc);
			indexWriter.commit();
			
			indexReader = DirectoryReader.open(FSDirectory.open(new File(classpath+indexDir)));
			indexSearcher = new IndexSearcher(indexReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static IndexWriter getIndexWriter() {
		if(indexWriter == null){
			synchronized (IndexHolder.class) {
				if(indexWriter == null){
					try {
						String classpath = IndexHolder.class.getClassLoader().getResource("").getPath();
						Directory dir = FSDirectory.open(new File(classpath+indexDir));
						Analyzer analyzer = new StandardAnalyzer();
						IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_4, analyzer);
						indexWriter = new IndexWriter(dir, iwc);
						indexWriter.commit();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
			return indexWriter;
		} else {
			return indexWriter;
		}
	}
	
	public static IndexSearcher getIndexSearcher() {
		if(indexSearcher == null){
			synchronized (IndexHolder.class) {
				if(indexSearcher == null){
					try {
						indexWriter.commit();
						String classpath = IndexHolder.class.getClassLoader().getResource("").getPath();
						indexReader = DirectoryReader.open(FSDirectory.open(new File(classpath+indexDir)));
					} catch (IOException e) {
						e.printStackTrace();
					}
					indexSearcher = new IndexSearcher(indexReader);
				}
			}
			return indexSearcher;
		} else {
			IndexReader newReader = null;
			try {
				newReader = DirectoryReader.openIfChanged((DirectoryReader)indexReader, indexWriter, false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if(newReader != null) {
				synchronized (IndexHolder.class) {
					if(indexReader != null) {
						try {
							indexReader.close();
							indexReader = newReader;
							indexSearcher = new IndexSearcher(indexReader);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
				
			return indexSearcher;
		}

	}
	
	@PreDestroy
	private void destroy(){
		System.out.println("index writer and reader closing.......");
		try {
			indexWriter.close();
			indexReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}





