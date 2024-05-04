/*
 * Copyright 2020 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.*;

import org.topicquests.os.asr.enigines.PMCEngine;
import org.topicquests.os.asr.enigines.PubMedEngine;
import org.topicquests.os.asr.file.FileHandler;
import org.topicquests.research.carrot2.Accountant;
import org.topicquests.research.carrot2.HarvestTimer;
import org.topicquests.research.carrot2.TCPListener;
import org.topicquests.research.carrot2.file.FileManager;
import org.topicquests.research.carrot2.pubmed.query.BatchQueryFileHandler;
import org.topicquests.research.carrot2.pubmed.query.QueryEngine;
import org.topicquests.research.carrot2.search.VagabondThread;
import org.topicquests.support.RootEnvironment;

import net.minidev.json.JSONObject;

/**
 * @author park
 *
 */
public class Environment extends RootEnvironment {
	private static Environment instance;
	//NOT thread safe
	private StringBuilder buf;
	private QueryEngine engine;
	private BatchQueryFileHandler batcher;
	private PubMedEngine parserThread;
	private PMCEngine pmcEngine;
	private Accountant accountant;
	private FileManager fileManager;
	private VagabondThread vagabondThread;
	private JSONObject queryInstrumentation;
	private JSONObject nlpInstrumentation;
	private Set<String> keywordInstrumentation;
	private final String STATS_PATH;
	private TCPListener listener;
	private RedisClient redis;
	private FileHandler filer;

	/**
	 * 
	 */
	public Environment() {
		super("config-props.xml", "logger.properties");
		logDebug("Environment ");
		buf = new StringBuilder();
		listener = new TCPListener(this);
		redis = new RedisClient(this);

		accountant = new Accountant(this);
		fileManager = new FileManager(this);
		parserThread = new PubMedEngine(this);
		engine = new QueryEngine(this);
		pmcEngine = new PMCEngine(this);
		STATS_PATH = getStringProperty("StatsPath");
		logDebug("Environment- "+engine);
		System.out.println("E1");
		batcher = new BatchQueryFileHandler(this);
		String schemaName = getStringProperty("DatabaseSchema");
		System.out.println("E2 "+schemaName);
		logDebug("Environment-1 "+schemaName);
		System.out.println("E4");
		System.out.println("E5");
		vagabondThread = new VagabondThread(this);
		System.out.println("E6");
		logDebug("Environment-2 "+parserThread);
		queryInstrumentation = new JSONObject();
		nlpInstrumentation  = new JSONObject();
		keywordInstrumentation = new HashSet<String>();
		instance = this;
		filer = new FileHandler(this);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			
			@Override
			public void run() {
				shutDown();
			}
		});
		logDebug("Environment booted");
		System.out.println("E7");
	}
	
	public FileHandler getFileHandler() {
		return filer;
	}
	public PMCEngine getPMCEngine() {
		return pmcEngine;
	}
	public RedisClient getRedis() {
		return redis;
	}
	
	public PubMedEngine getParserThread() {
		return parserThread;
	}
	/////////////////////
	// DSL
	/////////////////////
	/**
	 * Called from PubMedDocumentSource when it has
	 * a new PubMed Doc for processing
	 * @param pmid
	 * @param xml
	 */
	public void addPubMedAbstract(String pmid, String xml) {
		System.out.println("Env.add "+pmid);
		if (!accountant.seenBefore(pmid))
			parserThread.addDoc(xml);
		fileManager.persistAbstract(pmid, xml);
	}
	
	/**
	 * Callback from PubMedEngine when its
	 * queue runs empty
	 */
	public void queueEmpty() {
		accountant.bump();
		//fileManager.bump();
	}
	
	/////////////////////
	// Support
	/////////////////////
	
	public void armTimer() {
		HarvestTimer t = new HarvestTimer(this);
	}

	
	public void startHarvest() {
		batcher.startHarvest();
		System.out.println("Starting Harvest");		
	}
	public void pauseHarvest() {
		batcher.pauseHarvest();
		//saver will keep going till it has flushed all files
		System.out.println("Pausing Harvest");		
	}
	
	public VagabondThread getVagabondThread() {
		return vagabondThread;
	}
	
	
	public FileManager getFileManager() {
		return fileManager;
	}
	public Accountant getAccountant() {
		return accountant;
	}
	
	public static Environment getInstance() {
		return instance;
	}

	public BatchQueryFileHandler getBatchFileHandler() {
		return batcher;
	}
	public QueryEngine getQueryEngine() {
		return engine;
	}

	/**
	 * Utility to convert a Carrot2 <code>query</code> to a file name
	 * @param path
	 * @param query
	 * @return
	 */
	public String queryToFileName(String path, String query) {
		String x = query.replaceAll(" ", "_");
		buf.setLength(0);
		buf = buf.append(path).append(x).append(".xml");
		return buf.toString();
	}
	
	//////////////////////////
	// Instrumentation
	/////////////////////////
	
	public void queryTiming(String query, long delta) {
		queryInstrumentation.put(query, new Long(delta));
	}
	
	public void nlpTiming(String pmid, long delta) {
		nlpInstrumentation.put(pmid, new Long(delta));
	}
	
	public void keywords(List<String> keywords) {
		if (keywords != null && !keywords.isEmpty())
			keywordInstrumentation.addAll(keywords);
	}
	
	void saveInstrumentation() {
		PrintWriter out;
		FileOutputStream fos;
		//query
		try
        {
			fos = new FileOutputStream(new File(STATS_PATH+"Query.json"));
			out = new PrintWriter(fos);
			out.write(queryInstrumentation.toJSONString());
			out.flush();
			out.close();
        } catch (Exception e) {
        	logError(e.getMessage(), e);
        	e.printStackTrace();
        }
		//nlp
		try
        {
			fos = new FileOutputStream(new File(STATS_PATH+"NLP.json"));
			out = new PrintWriter(fos);
			out.write(nlpInstrumentation.toJSONString());
			out.flush();
			out.close();
        } catch (Exception e) {
        	logError(e.getMessage(), e);
        	e.printStackTrace();
        }
		//keywords
		try
        {
            fos = new FileOutputStream(STATS_PATH+"MyKeywords");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(keywordInstrumentation);
            oos.close();
            fos.close();
        } 
        catch (IOException ioe) 
        {
        	logError(ioe.getMessage(), ioe);
            ioe.printStackTrace();
        }
		//TODO
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Environment();
	}

	@Override
	public void shutDown() {
		logDebug("Environment.shuttingDown");
		System.out.println("Environment.shutDown");
		saveInstrumentation();
		parserThread.shutDown();
		pmcEngine.shutDown();
	}

}
