/*
 * Copyright 2020 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.research.carrot2.pubmed.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.io.*;

import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.source.SearchEngineResponse;
//import org.carrot2.core.ProcessingResult;
import org.carrot2.core.attribute.CommonAttributesDescriptor;
import org.topicquests.os.asr.Environment;
import org.topicquests.research.carrot2.Accountant;
import org.topicquests.research.carrot2.impl.PubMedDocumentSource;
import org.topicquests.research.carrot2.pubmed.ParserThread;


/**
 * @author park
 * based on Carrot2 example: ClusteringDataFromPubMed
 */
public class QueryEngine {
	private Environment environment;
	private final String outputPath;
	//direect path to search without clustering
	private PubMedDocumentSource srch;
	private ParserThread xmlThread;
	// by dropping Controller, avoid clustering
	//private Controller controller;
	private StringBuilder buf;
	private Accountant accountant;
	private final int HOW_MANY, COUNT;
	
	private List<String> queries;

	private boolean isRunning = false;
	private boolean isWaiting = false;

	private Worker worker;

	/**
	 * 
	 */
	public QueryEngine(Environment env) {
		environment = env;
		srch = new PubMedDocumentSource();
		xmlThread = environment.getParserThread();
		outputPath = environment.getStringProperty("ClusterDataPath");
		accountant = environment.getAccountant();
		buf = new StringBuilder();
		String x =  environment.getStringProperty("MAX_COUNT");
		HOW_MANY = Integer.parseInt(x);
		x = environment.getStringProperty("COUNT");
		COUNT = Integer.parseInt(x);
		queries = new ArrayList<String>();
		worker = new Worker();
		worker.start();

	}
	public void startHarvest() {
		synchronized(queries) {
			isRunning = true;
			queries.notify();
			System.out.println("Harvester Started");
		}
	}
	
	public void pauseHarvest() {
		synchronized(queries) {
			isRunning = false;
			queries.notify();
		}
	}
	public void addQuery(String query) {
		synchronized(queries) {
			queries.add(query);
			queries.notify();
		}
	}

	public void runQuery(String query){
		environment.logDebug("QE "+query);
		if (query.equals(""))
			return;
		environment.getAccountant().newQuery(query);
		//controller = ControllerFactory.createSimple();
//		Map<String, Object> attributes = new HashMap<String, Object>();
		int total = COUNT;
		int start = 0;
		long count = 0;
		long howMany = 0;
//		int i = 0;
        while (true) {
        	
    		try {
    			List<String> result = srch.startSearch(query, total, start);
	        	if (result == null) // try one more time
	        		result = srch.startSearch(query, total, start);
//	        	i++;
	        	//now process hits
	        	//////////////////////
	        	// Convert to IDocument
	        	// Ship abstract to ElasticSearch
	        	// Ship IDpcument to asr-v-document or asr-v-ingest
	        	//////////////////////
	        	count = result.size();
	        	Iterator<String> itr = result.iterator();
	        	String xml;
	        	while(itr.hasNext()) {
	        		xml = itr.next().trim();
	        		xmlThread.addDoc(xml);
	        	}
	        	
	        	
	        	//update start
	        	start += count;
	        	howMany += (long)count;
	        	System.out.println(count+" "+howMany);
	        	if (count < total)
	        		break;
	        	if (howMany >= HOW_MANY)
	        		break;
	        	synchronized(buf) {
		        	try {
		        		buf.wait(4000); // 4 second delay
		        	} catch (Exception e) {}
	        	}
    		} catch (Exception x) {
	        	environment.logError(x.getMessage(), x);
	        	x.printStackTrace();
	        	break;
	        }
    		
        }
		environment.getAccountant().endQuery(query);

	}
	
/*	private ProcessingResult runProcess(Map<String, Object> attributes, String query, File f) {
		System.out.println(attributes);
		ProcessingResult result = null;
		try {
			result =  controller.process(attributes,
	            PubMedDocumentSource.class, LingoClusteringAlgorithm.class);
		} catch (Exception e) {
			System.out.println("Second Try");
        	try {
        		buf.wait(30000); // 30 second delay
        	} catch (Exception x) {}
        	result =  controller.process(attributes,
    	            PubMedDocumentSource.class, LingoClusteringAlgorithm.class);
		}
		if (result != null) {
			//don't save cluster results if there are no clusters -- that can happen!
			//See example below of a bad cluster document
			if (result.getDocuments().size() > 0) {
				try {
		        	
		        	FileOutputStream fos = new FileOutputStream(f);
		        	result.serialize(fos, true, true);
		        	fos.flush();
		        	fos.close();
		        } catch (Exception e) {
		        	e.printStackTrace();
		        } 
			}
		}
		System.gc();
        return result;
	}*/
	
	public class Worker extends Thread {
		
		public Worker() {};
		
		public void run() {
			String theQuery = null;
			while (isRunning) {
				synchronized(queries) {
					if (queries.isEmpty()) {
						Thread.yield();
						try {
							queries.wait();
						} catch (Exception e) {}
					} else {
						theQuery = queries.remove(0);
					}
				}
				if (isRunning && theQuery != null) {
					runQuery(theQuery);
					theQuery = null;
				}
			}
		}
	}
	
}
	


/** Example of a cluster file with no data
<searchresult>
   <query>acrylamide desensitizes</query>
   <attribute key="MultilingualClustering.languageCounts">
      <value>
         <wrapper class="org.carrot2.util.simplexml.MapSimpleXmlWrapper">
            <map/>
         </wrapper>
      </value>
   </attribute>
   <attribute key="processing-time-algorithm">
      <value type="java.lang.Long" value="0"/>
   </attribute>
   <attribute key="start">
      <value type="java.lang.Integer" value="0"/>
   </attribute>
   <attribute key="SearchEngineBase.compressed">
      <value type="java.lang.Boolean" value="false"/>
   </attribute>
   <attribute key="SearchEngineStats.queries">
      <value type="java.lang.Integer" value="0"/>
   </attribute>
   <attribute key="processing-time-source">
      <value type="java.lang.Long" value="428"/>
   </attribute>
   <attribute key="SearchEngineStats.pageRequests">
      <value type="java.lang.Integer" value="0"/>
   </attribute>
   <attribute key="MultilingualClustering.majorityLanguage">
      <value value=""/>
   </attribute>
   <attribute key="processing-time-total">
      <value type="java.lang.Long" value="428"/>
   </attribute>
   <attribute key="results">
      <value type="java.lang.Integer" value="150"/>
   </attribute>
   <attribute key="results-total">
      <value type="java.lang.Long" value="0"/>
   </attribute>
</searchresult>
 */
