/**
 * 
 */
package org.topicquests.os.asr;

import org.topicquests.os.asr.api.ICitation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 
 */
public class CitationPojo implements ICitation {
	private JsonObject data;
	private final String
		DT_FIELD		= "dtf",
		AN_FIELD		= "an",
		PT_FIELD		= "pt",
		PN_FIELD		= "pn",
		DOI_FIELD		= "doi",
		ISBN_FIELD		= "isbn",
		ISSN_FIELD		= "issn",
		JT_FIELD		= "jtf",
		PD_FIELD		= "pdf",
		JV_FIELD		= "jvf",
		JN_FIELD		= "jnf",
		PGS_FIELD		= "pgs";
	/**
	 * 
	 */
	public CitationPojo() {
		data = new JsonObject();
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
	public void setDocumentTitle(String title) {
		data.addProperty(DT_FIELD, title);
	}

	@Override
	public String getDocumentTitle() {
		JsonElement je = data.get(DT_FIELD);
		if (je != null)  return je.getAsString();
		return null;	}

	@Override
	public void addAuthorName(String name) {
		JsonElement je = data.get(AN_FIELD);
		JsonArray ja=null;
		if (je == null) {
			ja = new JsonArray();
			data.add(AN_FIELD, ja);
		}
		ja.add(name);
	}

	@Override
	public JsonArray listAuthorNames() {
		JsonElement je = data.get(AN_FIELD);
		if (je != null) return je.getAsJsonArray();
		return null;
	}

	@Override
	public void setPublicationType(String type) {
		data.addProperty(PT_FIELD, type);
	}

	@Override
	public String getPublicationType() {
		JsonElement je = data.get(AN_FIELD);
		if (je != null) return je.getAsString();
		return null;
	}

	@Override
	public void setPublisherName(String name) {
		data.addProperty(PN_FIELD, name);
	}

	@Override
	public String getPublisherNamee() {
		JsonElement je = data.get(PN_FIELD);
		if (je != null) return je.getAsString();
		return null;
	}

	@Override
	public void setDOI(String doi) {
		data.addProperty(DOI_FIELD, doi);
	}

	@Override
	public String getDOI() {
		JsonElement je = data.get(DOI_FIELD);
		if (je != null) return je.getAsString();
		return null;
	}

	@Override
	public void setISBN(String isbn) {
		data.addProperty(ISBN_FIELD, isbn);
	}

	@Override
	public String getISBN() {
		JsonElement je = data.get(ISBN_FIELD);
		if (je != null) return je.getAsString();
		return null;
	}

	@Override
	public void setISSN(String issn) {
		data.addProperty(ISSN_FIELD, issn);
	}

	@Override
	public String getISSSN() {
		JsonElement je = data.get(ISSN_FIELD);
		if (je != null) return je.getAsString();
		return null;
	}

	@Override
	public void setJournalTitle(String journalTitle) {
		data.addProperty(JT_FIELD, journalTitle);
	}

	@Override
	public String getJournalTitle() {
		JsonElement je = data.get(JT_FIELD);
		if (je != null) return je.getAsString();
		return null;
	}

	@Override
	public void setPublicationDate(String dateString) {
		data.addProperty(PD_FIELD, dateString);
	}

	@Override
	public String getPublicationDate() {
		JsonElement je = data.get(PD_FIELD);
		if (je != null) return je.getAsString();
		return null;
	}

	@Override
	public void setJournalVolume(String vol) {
		data.addProperty(JV_FIELD, vol);
	}

	@Override
	public String getJournalVolume() {
		JsonElement je = data.get(JV_FIELD);
		if (je != null) return je.getAsString();
		return null;
	}

	@Override
	public void setJournalNumber(String num) {
		data.addProperty(JN_FIELD, num);
	}

	@Override
	public String getJournalNumber() {
		JsonElement je = data.get(JN_FIELD);
		if (je != null) return je.getAsString();
		return null;
	}

	@Override
	public void setPages(String pages) {
		data.addProperty(PGS_FIELD, pages);
	}

	@Override
	public String getPages() {
		JsonElement je = data.get(PGS_FIELD);
		if (je != null) return je.getAsString();
		return null;
	}

	@Override
	public JsonObject getData() {
		return data;
	}

}
