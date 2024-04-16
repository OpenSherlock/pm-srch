/*
 * Copyright 2023 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr;

import com.google.gson.JsonObject;

import org.topicquests.os.asr.api.IAuthor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * @author jackpark
 *
 */
public class AuthorPojo implements IAuthor {
	private JsonObject data;
	private final String
		ID_FIELD		= "id",
		ALOC_FIELD		= "aloc",
		AT_FIELD		= "atf",
		AFULLN_FIELD	= "afulln",
		AFN_FIELD		= "afn", //JsonArray
		AMN_FIELD		= "amn",
		ALN_FIELD		= "almn",
		AIN_FIELD		= "ain",
		ANN_FIELD		= "ann",
		ASX_FIELD		= "asx",
		AD_FIELD		= "ad",
		AEM_FIELD		= "aem",
		AAF_FIELD		= "aaf", //should be a jsonarray
		AAFL_FIELD		= "aafl",
		DID_FIELD		= "did",
		DT_FIELD		= "dtf",
		PN_FIELD		= "pnf",
		PL_FIELD		= "plf",
		PBL_FIELD		= "pbl",
		PBN_FIELD		= "pbn";
		
		

	/**
	 * 
	 */
	public AuthorPojo() {
		data = new JsonObject();
	}


	public AuthorPojo(JsonObject d) {
		data = d;
	}
	
	String get(String key) {
		JsonElement je = data.get(key);
		if (je != null)  return je.getAsString();
		return null;
	}
	
	JsonArray getArray(String key) {
		JsonElement je = data.get(key);
		if (je != null) return je.getAsJsonArray();
		return null;
	}
	long getLong(String key) {
		JsonElement je = data.get(key);
		if (je != null)  return je.getAsLong();
		return -1;
	}

	@Override
	public void setId(long id) {
		data.addProperty(ID_FIELD, id);
	}

	@Override
	public long getId() {
		JsonElement je = data.get(ID_FIELD);
		if (je != null)  return je.getAsLong();
		return -1;
	}

	@Override
	public void setAuthorLocator(long locator) {
		data.addProperty(ALOC_FIELD, locator);
	}

	@Override
	public long getAuthorLocator() {
		JsonElement je = data.get(ALOC_FIELD);
		if (je != null)  return je.getAsLong();
		return -1;
	}

	@Override
	public void setAuthorTitle(String title) {
		data.addProperty(AT_FIELD, title);
	}

	@Override
	public String getAuthorTitle() {
		JsonElement je = data.get(AT_FIELD);
		if (je != null)  return je.getAsString();
		return null;
	}

	@Override
	public void setAuthorFullName(String fullName) {
		data.addProperty(AFN_FIELD, fullName);
	}

	@Override
	public String getAuthorFullName() {
		JsonElement je = data.get(AFN_FIELD);
		if (je != null)  return je.getAsString();
		return null;
	}

	@Override
	public void addAuthorFirstName(String firstName) {
		JsonArray ja = this.listAuthorFirstNames();
		if (ja == null) {
			ja = new JsonArray();
			data.add(AFN_FIELD, ja);
		}
		ja.add(firstName);
	}

	@Override
	public JsonArray listAuthorFirstNames() {
		JsonElement je = data.get(AFN_FIELD);
		if (je != null) return je.getAsJsonArray();
		return null;
	}

	@Override
	public void setAuthorMiddleName(String middleName) {
		data.addProperty(AMN_FIELD, middleName);
	}

	@Override
	public String getAuthorMiddleName() {
		JsonElement je = data.get(AMN_FIELD);
		if (je != null)  return je.getAsString();
		return null;
	}

	@Override
	public void setAuthorLastName(String lastName) {
		data.addProperty(ALN_FIELD, lastName);
	}

	@Override
	public String getAuthorLastName() {
		JsonElement je = data.get(ALN_FIELD);
		if (je != null)  return je.getAsString();
		return null;
	}

	@Override
	public void setAuthorSuffix(String t) {
		data.addProperty(ASX_FIELD, t);
	}

	@Override
	public String getAuthorSuffix() {
		JsonElement je = data.get(ASX_FIELD);
		if (je != null)  return je.getAsString();
		return null;
	}

	@Override
	public void setAuthorDegree(String deg) {
		data.addProperty(AD_FIELD, deg);
	}

	@Override
	public String getAuthorDegree() {
		JsonElement je = data.get(AD_FIELD);
		if (je != null)  return je.getAsString();
		return null;
	}

	@Override
	public void setAuthorInitials(String initials) {
		data.addProperty(AIN_FIELD, initials);
	}

	@Override
	public String getInitials() {
		JsonElement je = data.get(AIN_FIELD);
		if (je != null)  return je.getAsString();
		return null;
	}

	@Override
	public void setAuthorNickName(String name) {
		data.addProperty(ANN_FIELD, name);
	}

	@Override
	public String getAuthorNickName() {
		JsonElement je = data.get(ANN_FIELD);
		if (je != null)  return je.getAsString();
		return null;
	}

	@Override
	public void setAuthorEmail(String email) {
		data.addProperty(AEM_FIELD, email);
	}

	@Override
	public String getAuthorEmail() {
		JsonElement je = data.get(AEM_FIELD);
		if (je != null)  return je.getAsString();
		return null;
	}

	@Override
	public void addAffiliationName(String name) {
		JsonArray ja = listAffiliationNames();
		if (ja == null) {
			ja = new JsonArray();
			data.add(AAF_FIELD, ja);
		}
		ja.add(name);
	}

	@Override
	public JsonArray listAffiliationNames() {
		JsonElement je = data.get(AAF_FIELD);
		if (je != null) return je.getAsJsonArray();
		return null;
	}

	@Override
	public void setAffiliationLocator(long locator) {
		data.addProperty(AAFL_FIELD, locator);
	}

	@Override
	public long getAffiliationLocator() {
		JsonElement je = data.get(AAFL_FIELD);
		if (je != null) return je.getAsLong();
		return -1;
	}

	@Override
	public void setDocumentId(long id) {
		data.addProperty(DID_FIELD, id);
	}

	@Override
	public long getDocumentId() {
		JsonElement je = data.get(DID_FIELD);
		if (je != null) return je.getAsLong();
		return -1;
	}

	@Override
	public void setDocumentTitle(String title) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDocumentTitle() {
		JsonElement je = data.get(DT_FIELD);
		if (je != null)  return je.getAsString();
		return null;
	}

	@Override
	public void setPublisherName(String name) {
		data.addProperty(PN_FIELD, name);
	}

	@Override
	public String getPublisherName() {
		JsonElement je = data.get(PN_FIELD);
		if (je != null)  return je.getAsString();
		return null;
	}

	@Override
	public void setPublisherLocator(long locator) {
		data.addProperty(PL_FIELD, locator);
	}

	@Override
	public long getPublisherLocator() {
		JsonElement je = data.get(PL_FIELD);
		if (je != null)  return je.getAsLong();
		return -1;
	}

	@Override
	public void setPublicationName(String name) {
		data.addProperty(PBN_FIELD, name);
	}

	@Override
	public String getPublicationName() {
		JsonElement je = data.get(PBN_FIELD);
		if (je != null)  return je.getAsString();
		return null;
	}

	@Override
	public void setPublicationLocator(long locator) {
		data.addProperty(PBL_FIELD, locator);
	}

	@Override
	public long getPublicationLocator() {
		JsonElement je = data.get(PBL_FIELD);
		if (je != null)  return je.getAsLong();
		return -1;
	}

	@Override
	public JsonObject getData() {
		return data;
	}

}
