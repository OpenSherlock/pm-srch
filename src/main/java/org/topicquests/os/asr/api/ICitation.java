/*
 * Copyright 2012, TopicQuests
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.topicquests.os.asr.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


/**
 * @author Park
 * @see {@link IBiblioLegend}
 */
public interface ICitation {
	
	/**
	 * 
	 * @param title
	 * 
	 */
	void setDocumentTitle(String title);
	
	/**
	 * 
	 * @return 
	 */
	String getDocumentTitle();
	
		
	/**
	 * A citation may have many authors
	 * @param name
	 */
	void addAuthorName(String name);
	
	JsonArray listAuthorNames();
	
	void setPublicationType(String type);
	
	String getPublicationType();
	
	void setPublisherName(String name);
	
	String getPublisherNamee();
	
	void setDOI(String doi);
	
	String getDOI();
	
	void setISBN(String isbn);
	
	String getISBN();
	
	void setISSN(String issn);
	
	String getISSSN();
	
	/**
	 * Has its own field; the paper's title uses INode label field
	 * @param journalTitle
	 */
	void setJournalTitle(String journalTitle);
	
	/**
	 * 
	 * @return
	 */
	String getJournalTitle();
	
	void setPublicationDate(String dateString);
	
	/**
	 *  
	 * @return can return <code>null</code> if not set
	 */
	String getPublicationDate();
	
	void setJournalVolume(String vol);
	
	String getJournalVolume();
	
	void setJournalNumber(String num);
	
	String getJournalNumber();
	
	void setPages(String pages);
	
	String getPages();
	
	JsonObject getData();
	
	
}
