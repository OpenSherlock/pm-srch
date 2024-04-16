/*
 * Copyright 2018 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr;


import org.topicquests.os.asr.api.IAuthor;
import org.topicquests.os.asr.api.IPublication;
import org.topicquests.os.asr.api.ITQCoreOntology;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;



/**
 * @author park
 */
public class JSONDocumentObject {
	public static final String
		_PMID				= "pmid", //
		_PMCID				= "pmcid",
		_PUBMED_PREFIX 		= "PubMed",
		_TITLE				= "title",
		_CITATIONS			= "citations",
		_COPYRIGHT			= "cpyright",
		_ABSTRACT			= "abstract", // now a list
		_CONTENT			= "content",
		_LANGUAGE			= "language",
		_URL				= "url",
		_AUTHORS			= "authors",
		_PUBLICATION 		= "publication",
		_ISO_ABBREV			= "isoA",
		_EDITORS			= "editors",
		_CLUSTER_LOCATOR	= "clusterLocator",
		_CLUSTER_TITLE		= "clusterTitle",
		_CLUSTER_DATA_LIST	= "clusterDataList",
		_TAG_LIST			= "tagList",
		_SUBSTANCE_LIST		= "substList",
		_TFIDF_MAP			= "tfidfMap",
		_DOC_TYPE			= "doctyp",
		_CHEM_NAME			= "chemn",
		_SYNONYMS			= "syns";
	
	private JsonObject data;
	
	public JSONDocumentObject(JsonObject jo) {
		data = jo;
	}
	
	/**
	 * @param userId
	 * @throws Exception
	 */
	public JSONDocumentObject(String userId) throws Exception {
		if (userId == null)
			throw new Exception("JSONDocumentObject missing userId");
		data = new JsonObject();
		data.addProperty(ITQCoreOntology.CREATOR_ID_PROPERTY, userId);
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
	JsonObject getObj(String key) {
		JsonElement je = data.get(key);
		if (je != null) return je.getAsJsonObject();
		return null;
	}

	public void setDocType(String t) {
		data.addProperty(_DOC_TYPE, t);
	}
	public String getDocType() {
		return get(_DOC_TYPE);
	}

	public String getCreatorId() {
		return get(ITQCoreOntology.CREATOR_ID_PROPERTY);
	}
	/**
	 * For pubmed docs
	 * @param pmid
	 */
	public void setPMID(String pmid) {
		data.addProperty(_PMID, pmid);
		//setLocator(_PUBMED_PREFIX+pmid);
	}
	
	/**
	 * For pubmed docs with a PMC document
	 * @return
	 */
	public String getPMID() {
		
		return get(_PMID);
	}
	
	public void setPMCID(String pmid) {
		data.addProperty(_PMCID, pmid);
	}
	
	public String getPMCID() {
		return get(_PMCID);
	}
	/**
	 * Set the document's topic <em>locator</em>
	 * @param locator
	 */
	public void setLocator(long locator) {
		data.addProperty(ITQCoreOntology.LOCATOR_PROPERTY, locator);
	}
	/**
	 * Return the document's topic <em>locator</em>
	 * @return can return <code>null</code>
	 */
	public long getLocator() {
		return getLong(ITQCoreOntology.LOCATOR_PROPERTY);
	}
	
	public void setPublicationISOAbbreviation(String a) {
		data.addProperty(_ISO_ABBREV, a);
	}
	
	public String getPublicationISOAbbreviation() {
		return data.get(_ISO_ABBREV).getAsString();
	}
	
	public void setCopyright(String copyright) {
		data.addProperty(_COPYRIGHT, copyright);
	}
	
	public String getCopyright() {
		return get(_COPYRIGHT);
	}
	
	public void addChemName(String name) {
		data.addProperty(_CHEM_NAME, name);
	}
	
	public JsonArray listChemNames() {
		return getArray(_CHEM_NAME);
	}
	
	public void addSynonym(String name) {
		data.addProperty(_SYNONYMS, name);
	}
	
	public JsonArray listSynonyms() {
		return getArray(_SYNONYMS);
	}

	public void setTagList(JsonArray tags) {
		data.add(_TAG_LIST, tags);
	}
	public void addTag(String t) {
		JsonArray l = listTags();
		if (l == null)
			l = new JsonArray();
		if (!l.contains(new JsonPrimitive(t))) {
			l.add(t);
			data.add(_TAG_LIST,l);
		}
	}
/*
	public void addKeyword(String t) {
		List<String>l = (List<String>)data.get(_KEYWORD_LIST);
		if (l == null)
			l = new ArrayList<String>();
		if (!l.contains(t)) {
			l.add(t);
			data.addProperty(_KEYWORD_LIST,l);
		}
	}
	public List<String> listKeywords() {
		return (List<String>)data.get(_KEYWORD_LIST);
	}
*/
	public void addSubstance(String t) {
		
		JsonArray l = listSubstances();
		if (l == null)
			l = new JsonArray();
		if (!l.contains(new JsonPrimitive(t))) {
			l.add(t);
			data.add(_SUBSTANCE_LIST,l);
		}
	}
	public JsonArray listSubstances() {
		return getArray(_SUBSTANCE_LIST);
	}

	/**
	 * Can return <code>null</code>
	 * @return
	 */
	public JsonArray listTags() {
		return getArray(_TAG_LIST);
	}
	/**
	 * Return userId
	 * @return
	 */
	public String getUserId() {
		return get(ITQCoreOntology.CREATOR_ID_PROPERTY);
	}
	
	/**
	 * Metadata for the {@link IDocument}
	 * @param title
	 */
	public void setClusterTitle(String title) {
		data.addProperty(_CLUSTER_TITLE, title);
	}
	
	public String getClusterTitle() {
		return get(_CLUSTER_TITLE);
	}
	
	/**
	 * Metadata for the {@link IDocument}
	 * @param title
	 */
	//public void setClusterQuery(String title) {
	//	data.addProperty(_CLUSTER_QUERY, title);
	//}
	
/*	public void addClusterData(String clusterLocator, String query, String clusterPhrase, String clusterScore) {
		Map<String,String>m = new HashMap<String,String>();
		m.addProperty(_CLUSTER_LOCATOR, clusterLocator);
		m.addProperty(IHarvestingOntology.CLUSTER_QUERY, query);
		m.addProperty(_CLUSTER_TITLE, clusterPhrase);
		m.addProperty(IHarvestingOntology.CLUSTER_WEIGHT, clusterScore);
		List<Map<String,String>>l = (List<Map<String,String>>)data.get(_CLUSTER_DATA_LIST);
		if (l == null)
			l = new ArrayList<Map<String,String>>();
		l.add(m);
		data.addProperty(_CLUSTER_DATA_LIST, l);
	}
	
	public String getClusterQuery() {
		return data.get(IHarvestingOntology.CLUSTER_QUERY);
	}
	
	public String getClusterWeight() {
		return data.getAsString(IHarvestingOntology.CLUSTER_WEIGHT);
	} */
	/**
	 * The full text to harvest
	 * @param content
	 * @param language defaults to "en"
	 */
	public void setContent(String content, String language) {
		data.addProperty(_CONTENT, content);
		if (language != null)
			data.addProperty(_LANGUAGE, language);
		else
			data.addProperty(_LANGUAGE, "en");

	}
	
	public void setTitle(String title) {
		data.addProperty(_TITLE, title);
	}
	
	public String getTitle() {
		return get(_TITLE);
	}
	/**
	 * Text of a document's abstract
	 * @param abs
	 */
	//public void setAbstract(String abs) {
	//	data.addProperty(_ABSTRACT, abs);
	//}
	
	/**
	 * Some documents have just one abstract paragraph, others several.
	 * @param a
	 */
	public void addDocAbstract(String a) {
		JsonArray ab = this.listAbstract();
		if (ab == null)
			ab = new JsonArray();
		if (!ab.contains(new JsonPrimitive(a)))
			ab.add(a);
		data.add(_ABSTRACT, ab);
	}

	/**
	 * Can return {@code null}
	 * @return
	 */
	public JsonArray listAbstract() {
		return getArray(_ABSTRACT);
	}
	/*public String getAbstract() {
		return data.get(_ABSTRACT);
	}*/
	
	public String getLanguage() {
		return data.get(_LANGUAGE).getAsString();
	}
	
	public void setLanguage(String lang) {
		String x = lang;
		//heuristic punt
		if (x.equals("eng"))
			x = "en";
		data.addProperty(_LANGUAGE, x);
	}
	/**
	public void setTFIDFData(SortedMap<Double,String>d) {
		JsonObject jo = new JsonObject();
		data.add(_TFIDF_MAP, jo);
	}
	
	public SortedMap<Double,String> getTFIDFData(Comparator c) {
		JsonObject jo = data.get(_TFIDF_MAP).getAsJsonObject();
		if (jo == null)
			return null;
		SortedMap<Double,String>result = new TreeMap(c);
		Iterator<String>itr = jo.keySet().iterator();
		String d;
		while (itr.hasNext()) {
			d = itr.next();
			result.addProperty(new Double(d),(String)jo.get(d));
		}
		return result;
	}
	*/
	/**
	 * Return the content
	 * @return
	 */
	public String getContent() {
		return get(_CONTENT);
	}
	
	public void setURL(String url) {
		data.addProperty(_URL, url);
	}
	
	/**
	 * Return url or empty string
	 * @return
	 */
	public String getURL() {
		String result = get(_URL);
		if (result == null)
			result = "";
		return result;
	}
	
	public void setPublication(String title, String publicationName,
			String volume, String number, String pages, String date, String year,
			String publisherName, String publisherLocation,
			String doi, String issn, String publicationType, String isoAbbreviation) {
		IPublication p = new PublicationPojo();
		if (title != null && !title.equals(""))
			p.setTitle(title);
		if (publicationName != null && !publicationName.equals(""))
			p.setPublicationName(publicationName);
		if (volume != null && !volume.equals(""))
			p.setPubicationVolume(volume);
		if (number != null && !number.equals(""))
			p.setPublicationNumber(number);
		if (pages != null && !pages.equals(""))
			p.setPages(pages);
		if (date != null && !date.equals(""))
			p.setPublicationDate(date);
		if (year != null && !year.equals(""))
			p.setPublicationYear(year);
		if (publisherName != null && !publisherName.equals(""))
			p.setPublisherName(publisherName);
		if (publisherLocation != null && !publisherLocation.equals(""))
			p.setPublisherLocation(publisherLocation);
		if (doi != null && !doi.equals(""))
			p.setDOI(doi);
		if (issn != null && !issn.equals(""))
			p.setISSN(issn);
		if (isoAbbreviation != null && !isoAbbreviation.equals(""))
			p.setISOAbbreviation(isoAbbreviation);
		setPublication(p);
	}
	
	public void setPublication(IPublication p) {
		data.add(_PUBLICATION, p.getData());
	}
	
	public IPublication getPublication() {
		JsonObject p = getObj(_PUBLICATION);
		if (p != null) {
			return new PublicationPojo(p);
		}
		return null;
	}

	public String toJSONString() {
		return data.getAsString();
	}
	
	/**
	 * Citations I make of other documents typically PMIDs
	 * @param type .e.g. doi, pubmed
	 * @param value
	 */
	public void addCitation(String type, String value) {
		JsonObject jo = new JsonObject();
		jo.addProperty("type", type);
		jo.addProperty("value", value);
		JsonArray l = listCitations();
		if (l == null)
			l = new JsonArray();
		if (!l.contains(jo))
			l.add(jo);
		data.add(_CITATIONS,l);
	}
	
	public JsonArray listCitations() {
		return getArray(_CITATIONS);
	}
	
	/**
	 * 
	 * @param title
	 * @param initials
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param suffix e.g. jr, sr, II, III, ...
	 * @param degree e.g. M.D., PhD, ..
	 * @param fullName
	 * @param authorLocator
	 * @param publicationName
	 * @param publicationLocator
	 * @param publisherName
	 * @param publisherLocator
	 * @param affiliationName
	 * @param affiliationLocator
	 * @param funderName
	 * @param funderLocator
	 * @param fundingContractId
	 */
	public IAuthor addAuthor(String title, String initials, String firstName, String middleName, String lastName,
						  String suffix, String degree, String fullName, long authorLocator, 
						  String publicationName, long publicationLocator, 
						  String publisherName, long publisherLocator, 
						  String affiliationName, long affiliationLocator) {
		IAuthor a = new AuthorPojo();
		if (title != null && !title.equals(""))
			a.setAuthorTitle(title);
		if (initials != null && !initials.equals(""))
			a.setAuthorInitials(initials);
		if (firstName != null && !firstName.equals(""))
			a.addAuthorFirstName(firstName);
		if (middleName != null && !middleName.equals(""))
			a.setAuthorMiddleName(middleName);
		if (lastName != null && !lastName.equals(""))
			a.setAuthorLastName(lastName);
		if (suffix != null && !suffix.equals(""))
			a.setAuthorSuffix(suffix);
		if (degree != null && !degree.equals(""))
			a.setAuthorDegree(degree);
		if (fullName != null && !fullName.equals(""))
			a.setAuthorFullName(fullName);
		if (authorLocator >-1)
			a.setAuthorLocator(authorLocator);
		if (publicationName != null && !publicationName.equals(""))
			a.setPublicationName(publicationName);
		if (publicationLocator > -1)
			a.setPublicationLocator(publicationLocator);
		if (publisherName != null && !publisherName.equals(""))
			a.setPublisherName(publisherName);
		if (publisherLocator > -1)
			a.setPublisherLocator(publisherLocator);
		if (affiliationName != null && !affiliationName.equals(""))
			a.addAffiliationName(affiliationName);
		if (affiliationLocator  > -1)
			a.setAffiliationLocator(affiliationLocator);
		this.addAuthor(a);
		return a;
	}	
	
	public void updateAuthor(IAuthor author) {
		
	}
	public void addAuthor(IAuthor author) {
		JsonObject ja = author.getData();
		JsonArray a = this.listAuthors();
		if (a == null)
			a = new JsonArray();
		if (!a.contains(ja))
			a.add(ja);
		this.setAuthorList(a);
	}

	public void setAuthorList(JsonArray authors) {
		data.add(_AUTHORS, authors);
	}
	/**
	 * List authors as JSON strings
	 * @return can return <code>null</code>
	 */
	public JsonArray listAuthors() {
		return getArray(_AUTHORS);
	}	

	@Override
	public String toString() {
		return data.getAsString();
	}
	
	public JsonObject getData() {
		return data;
	}
	
}
