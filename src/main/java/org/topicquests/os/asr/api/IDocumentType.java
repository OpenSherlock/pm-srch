/*
 * Copyright 2024 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.api;

/**
 * @author jackpark
 */
public interface IDocumentType {
	public static final String
		PUBMD		= "pubmed",
		PMC			= "pmc",
		UMLS		= "umls",
		DBPEDIA		= "dbped",
		WIKIDATA	= "wiki",
		XRIV		= "xriv",
		REPORT		= "rept",
		BLOG		= "blog";
}
