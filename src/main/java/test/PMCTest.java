/*
 * Copyright 2024 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package test;

import java.io.File;

import org.topicquests.os.asr.Environment;
import org.topicquests.os.asr.JSONDocumentObject;
import org.topicquests.os.asr.pmc.PubMedFullReportPullParser;
import org.topicquests.support.api.IResult;

/**
 * 
 */
public class PMCTest {
	private Environment environment;
	private PubMedFullReportPullParser parser;
	private final String PATH = "data/pmc/article.xml";
	/**
	 * 
	 */
	public PMCTest() {
		environment = new Environment();
		parser = new PubMedFullReportPullParser(environment, null);
		File f = new File(PATH);
		IResult r = parser.parseXML(f);
		System.out.println("A "+r.getErrorString());
		Object o = r.getResultObject();
		if (o != null) {
			JSONDocumentObject jdo = (JSONDocumentObject)o;
			System.out.println("B\n"+jdo.getData().toString());
		}
		environment.shutDown();
		System.exit(0);
	}

}
