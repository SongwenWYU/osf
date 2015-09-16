package com.lvwang.osf.search;

import java.io.File;
import java.io.IOException;

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

import com.lvwang.osf.util.Property;

public class IndexHolder {

	private static String indexDir = "/indexDir";
	private static IndexWriter indexWriter;
	private static IndexReader indexReader;
	private static IndexSearcher indexSearcher;
	
	public static IndexWriter getIndexWriter() {
		if(indexWriter == null){
			synchronized (IndexHolder.class) {
				if(indexWriter == null){
					try {
						String classpath = IndexHolder.class.getClassLoader().getResource("").getPath();
						Directory dir = FSDirectory.open(new File(classpath+indexDir));
						Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_0);
						IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
						indexWriter = new IndexWriter(dir, iwc);
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
					IndexReader reader = null;
					try {
						String classpath = IndexHolder.class.getClassLoader().getResource("").getPath();
						reader = DirectoryReader.open(FSDirectory.open(new File(classpath+indexDir)));
					} catch (IOException e) {
						e.printStackTrace();
					}
					indexSearcher = new IndexSearcher(reader);
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
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
				
			return indexSearcher;
		}

	}
}





