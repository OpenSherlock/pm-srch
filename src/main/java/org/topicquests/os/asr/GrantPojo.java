/*
 * Copyright 2023 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr;


import org.topicquests.os.asr.api.IGrant;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author jackpark
 *
 */
public class GrantPojo implements IGrant {
	private JsonObject data;
	private String 
		ID_FIELD	= "idf",
		AG_FIELD	= "agf",
		CY_FIELD	= "cyf";
	/**
	 * 
	 */
	public GrantPojo() {
		data = new JsonObject();
	}

	public GrantPojo(JsonObject d) {
		data = d;
	}

	String get(String key) {
		JsonElement je = data.get(key);
		if (je != null)  return je.getAsString();
		return null;
	}
	

	@Override
	public void setGrantId(String id) {
		data.addProperty(ID_FIELD, id);
	}

	@Override
	public String getGrantId() {
		return get(ID_FIELD);
	}

	@Override
	public void setAgency(String agency) {
		data.addProperty(AG_FIELD, agency);
	}

	@Override
	public String getAgency() {
		return get(AG_FIELD);
	}

	@Override
	public void setCountry(String country) {
		data.addProperty(CY_FIELD, country);
	}

	@Override
	public String getCountry() {
		return get(CY_FIELD);
	}

	@Override
	public JsonObject getData() {
		return data;
	}

}
