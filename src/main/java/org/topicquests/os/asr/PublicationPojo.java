/*
 * Copyright 2023 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr;

import org.topicquests.os.asr.api.IAuthor;
import org.topicquests.os.asr.api.ICitation;
import org.topicquests.os.asr.api.IGrant;
import org.topicquests.os.asr.api.IPublication;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author jackpark
 *
 */
public class PublicationPojo implements IPublication {
	private JsonObject data;
	private final String
		PT_FIELD		= "ptf",
		PN_FIELD		= "pnf",
		PL_FIELD		= "plf",
		PD_FIELD		= "pdf",
		PV_FIELD		= "pvf",
		PGS_FIELD		= "pgs",
		PPN_FIELD		= "ppn",
		PRL_FIELD		= "prl",
		PRN_FIELD		= "prn",
		PRLN_FIELD		= "prln",
		PY_FIELD		= "pyf",
		PTY_FIELD		= "pty",
		CR_FIELD		= "crf",
		DOI_FIELD		= "doi",
		ISBN_FIELD		= "isbn",
		ISSN_FIELD		= "issn",
		ISO_FIELD		= "iso",
		DL_FIELD		= "dlf",
		GR_FIELD		= "grf",
		MO_FIELD		= "mof",
		AU_FIELD		= "auf",
		CI_FIELD		= "cif";
	/**
	 * 
	 */
	public PublicationPojo() {
		data = new JsonObject();
	}

	public PublicationPojo(JsonObject d) {
		data = d;
	}
	@Override
	public void setTitle(String title) {
		data.addProperty(PT_FIELD, title);
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
	JsonObject getJson(String key) {
		JsonElement je = data.get(key);
		if (je != null)  return je.getAsJsonObject();
		return null;
	}
	
	@Override
	public String getTitle() {
		return get(PT_FIELD);
	}

	@Override
	public void setCopyright(String c) {
		data.addProperty(CR_FIELD, c);
	}

	@Override
	public String getCopyright() {
		return get(CR_FIELD);
	}

	@Override
	public void setPublicationName(String name) {
		data.addProperty(PN_FIELD, name);
	}

	@Override
	public String getPublicationName() {
		return get(PN_FIELD);
	}

	@Override
	public void setPublicationDate(String date) {
		data.addProperty(PD_FIELD, date);
	}

	@Override
	public String getPublicationDate() {
		return get(PD_FIELD);
	}

	@Override
	public void setPublicationYear(String year) {
		data.addProperty(PY_FIELD, year);

	}

	@Override
	public String getPublicationYear() {
		return get(PY_FIELD);
	}

	@Override
	public void setPubicationVolume(String volume) {
		data.addProperty(PV_FIELD, volume);
	}

	@Override
	public String getPublicationVolume() {
		return get(PV_FIELD);
	}

	@Override
	public void setPublicationNumber(String number) {
		data.addProperty(PPN_FIELD, number);
	}

	@Override
	public String getPublicationNumber() {
		return get(PPN_FIELD);
	}

	@Override
	public void setPublicationLocator(long locator) {
		data.addProperty(PL_FIELD, locator);
	}

	@Override
	public long getPublicationLocator() {
		return getLong(PL_FIELD);
	}

	@Override
	public void setPages(String pages) {
		data.addProperty(PGS_FIELD, pages);
	}

	@Override
	public String getPages() {
		return get(PGS_FIELD);
	}

	@Override
	public void setPublisherName(String name) {
		data.addProperty(PRN_FIELD, name);
	}

	@Override
	public String getPublisherName() {
		return get(PRN_FIELD);
	}

	@Override
	public void setPublisherLocator(long locator) {
		data.addProperty(PRL_FIELD, locator);
	}

	@Override
	public long getPublisherLocator() {
		return getLong(PRL_FIELD);
	}

	@Override
	public void setPublisherLocation(String location) {
		data.addProperty(PRLN_FIELD, location);
	}

	@Override
	public String getPublisherLocation() {
		return get(PRLN_FIELD);
	}

	@Override
	public void setDOI(String doi) {
		data.addProperty(DOI_FIELD, doi);
	}

	@Override
	public String getDOI() {
		return get(DOI_FIELD);
	}

	@Override
	public void setISSN(String issn) {
		data.addProperty(ISSN_FIELD, issn);
	}

	@Override
	public String getISSN() {
		return get(ISSN_FIELD);
	}

	@Override
	public void setISBN(String isbn) {
		data.addProperty(ISBN_FIELD, isbn);
	}

	@Override
	public String getISBN() {
		return get(ISBN_FIELD);
	}

	@Override
	public void setPublicationType(String type) {
		data.addProperty(PTY_FIELD, type);
	}

	@Override
	public String getPublicationType() {
		return get(PTY_FIELD);
	}

	@Override
	public void setISOAbbreviation(String abbrev) {
		data.addProperty(ISO_FIELD, abbrev);
	}

	@Override
	public String getISOAbbreviation() {
		return get(ISO_FIELD);
	}

	@Override
	public void setDocumentLocator(long locator) {
		data.addProperty(DL_FIELD, locator);
	}

	@Override
	public long getDocumentLocator() {
		return getLong(DL_FIELD);
	}

	@Override
	public void addGrant(IGrant g) {
		data.add(GR_FIELD, g.getData());
	}

	@Override
	public JsonArray listGrants() {
		return getArray(GR_FIELD);
	}

	@Override
	public void setMonth(String month) {
		data.addProperty(MO_FIELD, month);
	}

	@Override
	public String getMonth() {
		return get(MO_FIELD);
	}

	@Override
	public JsonObject getData() {
		return data;
	}

	@Override
	public void addAuthor(IAuthor author) {
		data.add(AU_FIELD, author.getData());
	}

	@Override
	public void setAuthors(JsonArray authors) {
		data.add(AU_FIELD, authors);
	}

	@Override
	public void removeAuthor(IAuthor author) {
		JsonArray ja = listAuthors();
		if (ja == null)
			return;
		ja.remove(author.getData());
		setAuthors(ja);
	}
	@Override
	public JsonArray listAuthors() {
		return getArray(AU_FIELD);
	}

	@Override
	public void addCitation(ICitation c) {
		data.add(CI_FIELD, c.getData());
	}

	@Override
	public JsonArray listCitations() {
		return getArray(CI_FIELD);
	}



}
