/*
 * Copyright 2023 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.pmc;

import java.io.*;
import java.util.*;

import org.topicquests.os.asr.AuthorPojo;
import org.topicquests.os.asr.Environment;
import org.topicquests.os.asr.JSONDocumentObject;
import org.topicquests.os.asr.PublicationPojo;
import org.topicquests.os.asr.api.IAuthor;
import org.topicquests.os.asr.api.IPublication;
import org.topicquests.os.asr.file.FileHandler;
import org.topicquests.os.asr.util.UTF8FileUtil;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * @author park
 *
 */
public class PubMedFullReportPullParser {
	private Environment environment;
	private Set<String>unusedTags;
	private FileHandler host;
	private UTF8FileUtil fUtil;

	/**
	 * 
	 */
	public PubMedFullReportPullParser(Environment env, FileHandler h) {
		environment = env;
		unusedTags = new HashSet<String>();
		host = h;
		fUtil = new UTF8FileUtil();
	}
	
	public Set<String> getUnusedTags() {
		return unusedTags;
	}
	/**
	 * Returns an instance of {@link JSONDocumentObject}
	 * @param xmlFile TODO
	 * @return
	 */
	public IResult parseXML(File xmlFile) {
		//FIRST, clean up the text
		List<String> lines = null;
		try {
			lines = fUtil.getLines(xmlFile.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		Iterator<String>itr = lines.iterator();
		StringBuilder buf = new StringBuilder();
		while (itr.hasNext()) {
			buf.append(itr.next());
		}
		//&lt;  and &gt;
		String foo = buf.toString();
		String bar = cleanXML(foo); //cleanXML(buf.toString());// xmlString.replaceAll("&lt;", "<");
		// foo = foo.replaceAll("&gt;", ">"); //&gt;
		environment.logDebug("FOO\n"+bar);
		IResult result = new ResultPojo();
		//NOW, parse it
		try {
			InputStream ins = new ByteArrayInputStream(bar.getBytes("UTF-8"));
			BufferedInputStream bis = new BufferedInputStream(ins);
			buf = null;
			foo = null;
			parse(bis, result);
		} catch (Exception e) {
			e.printStackTrace();
			result.addErrorString(e.getMessage());
		}
//		environment.logDebug("PubMedReportPullParser+ "+result.hasError()+" "+result.getResultObject());
		return result;
	}

	/**
	 * Returns an instance of {@link JSONDocumentObject}
	 * @param xmlFile 
	 * @return
	 */
	public IResult parseXML(String xml) {
//		environment.logDebug("PubMedReportPullParser- "+foo.length());
		IResult result = new ResultPojo();
		//NOW, parse it
		try {
			InputStream ins = new ByteArrayInputStream(xml.getBytes());
			BufferedInputStream bis = new BufferedInputStream(ins);
			
			parse(bis, result);
		} catch (Exception e) {
			e.printStackTrace();
			result.addErrorString(e.getMessage());
		}
//		environment.logDebug("PubMedReportPullParser+ "+result.hasError()+" "+result.getResultObject());
		return result;
	}
	
	/**
	 * Remove anything inside "[" and "]" - heuristic
	 * @param p
	 * @return
	 */
	String cleanP(String p) {
		environment.logDebug("CP-1\n"+p);
		StringBuilder buf = new StringBuilder();
		int len = p.length();
		char c;
		boolean isLeft = false;
		for (int i=0;i<len;i++) {
			c = p.charAt(i);
			if (!isLeft && c == '[')
				isLeft = true;
			else if (isLeft && c == ']')
				isLeft = false;
			else if (!isLeft)
				buf.append(c);
			
		}
		environment.logDebug("CP-2:\n"+buf.toString());;
		return buf.toString().trim();
	}

	String removeXREFs(String inString) {
		StringBuilder buf = new StringBuilder();
		String x = inString;
		String left;
		int where = x.indexOf("<p>");
		int where2, where3;
		String para, px;
		while (where > -1) {
			System.out.println("A "+where);
			buf.append(x.substring(0, where));
			where2 = x.indexOf("</p>", where);
			//now have a <p></p> captured
			System.out.println("B "+where2);
			para = x.substring(where, where2+4);
			// x is what's left - if any
			x = x.substring(where2+4);
			System.out.println("X "+where+" "+where2+" "+x.length()+" "+para);
			// keep the clean para
			px = cleanP(para);
			environment.logDebug("PX:\n"+px);
			buf.append(px);
			//x = x.substring(para.length());
		//	environment.logDebug("RX: "+x);
			// anything left?
			where = x.indexOf("<p>");

		}
		buf.append(x);
		
		return buf.toString();
	}
	/**
	 * Clean up the xml input
	 * @param inString
	 * @return instance of {@link JSONDocumentObject}
	 */
	String cleanXML(String inString) {
		//if (true) return inString;
		String cs = inString;
		cs = cs.replaceAll("<italic>", " ");
		cs = cs.replaceAll("</italic>", " ");
		cs = cs.replaceAll("<sup>", "^");
		cs = cs.replaceAll("</sup>", " ");
		cs = removeXREFs(cs);
		//Unicode characters are a bitch
		/*		cs = cs.replaceAll("&#x03b1;", "α");
		cs = cs.replaceAll("&#x003b1;", "α");
		cs = cs.replaceAll("&#x03b2;", "β");
		cs = cs.replaceAll("&#x003b2;", "β");
		cs = cs.replaceAll("&#x03b3;", "γ");
		cs = cs.replaceAll("&#x003b3;", "γ");
		cs = cs.replaceAll("&#x03b4;", "δ");
		cs = cs.replaceAll("&#x003b4;", "δ");
		cs = cs.replaceAll("&#x03B5;", "ε");
		cs = cs.replaceAll("&#x003B5;", "ε");
		cs = cs.replaceAll("&#x003b4;", "δ");
		cs = cs.replaceAll("&#x003b4;", "δ");
		cs = cs.replaceAll("&#x003b4;", "δ");
		cs = cs.replaceAll("&#x003b4;", "δ");
		cs = cs.replaceAll("&#x003b4;", "δ"); */
		StringBuilder buf = new StringBuilder();
		int len = cs.length();
		int lll = (int)'l';
		int ggg = (int)'g';
		int ttt = (int)'t';
		int aaa = (int)'&';
				
		int c = 0, x,y,z;
		for (int i=0;i<len;i++) {
			c = cs.charAt(i);
			if (c == aaa) {
				x = cs.charAt(i+1);
				if (x == lll || x == ggg) {
					y = cs.charAt(i+2);
					if (y == ttt) {
						if (x == lll) 
							buf.append('<');
						else
							buf.append('>');
						i +=3;
						c=-1;
					}
				}
			}
			if (c > -1)
				buf.append((char)c);
		}
		return buf.toString();
	}
	
	void addContent(String content, StringBuilder buf) {
		buf = buf.append(content).append("\n");
	}
	
	void parse(InputStream ins, IResult result) {
	    try {
	         XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	         factory.setNamespaceAware(false);
	         XmlPullParser xpp = factory.newPullParser();

	         BufferedReader in = new BufferedReader(new InputStreamReader(ins));
	         xpp.setInput(in);
	         StringBuilder content = new StringBuilder();
	        //the working document
	         JSONDocumentObject theDocument = new JSONDocumentObject();
	         result.setResultObject(theDocument);
	         String temp = null;
	         String text = null;
	         String label = null;
	         String category = null;
	         String refType = null;
	         String lastName = null, firstName = null, initials = null, affiliation = null;
	         String grantId=null, agency=null, country = null;
	         String rid = null;
	         
	         String affID = null;
	         boolean isJournal = false;
	         boolean isValid = false;
	         boolean isAuthor = false;
	         boolean isRefType = false;
	         
	         boolean isAbstract = false;
	         boolean isBody = false;
	         boolean isKeywordGroup = false;
	         boolean isPMID = false;
	         boolean isPMCID = false;
	         boolean isDOI = false;
	         boolean isRefList = false; //helps differentiate authors
	         
	         IPublication thePub = null;
	         IAuthor theAuthor = null;
	         Map<String, IAuthor> _theAuth = null;
	         
	         boolean isGrant = true;
	         HashMap<String,String> props;
	         int eventType = xpp.getEventType();
	         boolean isStop = false;
	         while (!(isStop || eventType == XmlPullParser.END_DOCUMENT)) {
	        	 Thread.yield();
	            temp = xpp.getName();
	            if(eventType == XmlPullParser.START_DOCUMENT) {
	                System.out.println("PM Start document");
	            } else if(eventType == XmlPullParser.END_DOCUMENT) {
	                System.out.println("PM End document");
	                //TODO Temporary
	                
	            } else if(eventType == XmlPullParser.START_TAG) {
	                System.out.println("PM Start tag "+temp);
	                props = getAttributes(xpp);
	         //       environment.logDebug("PubMedReportPullParser props "+temp+" | "+props);
	                if (temp.equalsIgnoreCase("abstract")) {
	                	isAbstract = true;
	                } if (temp.equalsIgnoreCase("kwd-group")) {
	 	                	isKeywordGroup = true;
	                } else if (temp.equalsIgnoreCase("body")) {
	                	isBody = true;
	                } else if (temp.equalsIgnoreCase("article-id")) {
	                	String t = (String)props.get("pub-id-type");
	                	if (t.equals("pmid"))
	                		isPMID = true;
	                	else if (t.equals("pmc"))
	                		isPMCID = true;
	                	else if (t.equals("doi"))
	                		isDOI = true;
	                } else if (temp.equalsIgnoreCase("Grant")) {
	                	isGrant = true;
	                } else if (temp.equalsIgnoreCase("ref-list")) {
	                	isRefList = true;
	                } else if(temp.equalsIgnoreCase("AbstractText")) {
	                	// <AbstractText Label="BACKGROUND AND OBJECTIVES" NlmCategory="OBJECTIVE">
	                	label = (String)props.get("Label");
	                	category = (String)props.get("NlmCategory");
	                } else if (temp.equalsIgnoreCase("article-meta")) {
	                	thePub = new PublicationPojo();
	                } else if(temp.equalsIgnoreCase("CommentsCorrections")) {
	                	///////////////////////////////////
	                	//<CommentsCorrections RefType="CommentOn">
	                    //<RefSource>Blood. 2015 Jan 22;125(4):619-28</RefSource>
	                    //<PMID Version="1">25416276</PMID>
	                	//</CommentsCorrections>
	                	///////////////////////////////////
	                	refType = (String)props.get("RefType");
	                	isRefType = true;
	                } else if (temp.equalsIgnoreCase("contrib")) {
	                	if ("author".equals((String)props.get("contrib-type")))
	                		theAuthor = new AuthorPojo();
	                } else if (temp.equals("aff")) {
	                	affID = (String)props.get("id");
	                } else if (temp.equalsIgnoreCase("xref")) {
	                	rid = (String)props.get("rid");
	                	if (_theAuth == null) _theAuth = new HashMap<String, IAuthor>();
	                } else if (temp.equalsIgnoreCase("PubmedArticleSet")) {
	                	if (theDocument == null)
	                		isStop = true;
	                	//We must leave -- that's because the system builds
	                	// empty files with <PubmedArticleSet> and nothing else
	                	// if document is missing.
	                } 
//DescriptorName  (mesh heading)
//
	                
	            } else if(eventType == XmlPullParser.END_TAG) {
	                System.out.println("PM End tag "+temp+" // "+text);
	                if (temp.equalsIgnoreCase("journal-title")) {
	                	if (thePub != null)
	                		thePub.setTitle(text);
	                	//System.out.println(theDocument.toJSONString());
	                } else if (temp.equalsIgnoreCase("p")) {
	                	if (isAbstract)
	                		theDocument.addDocAbstract(text);
	                	else if (isBody) {
	                		environment.logDebug("P:\n"+text);
	                		if (text.startsWith("].")) {
	                			text = text.substring(2).trim();
	                		}
	                		if (text.length() > 2)
	                			theDocument.addContentParagraph(text);
	                		System.out.println("P: "+text);
	                	}
	                } else if (temp.equalsIgnoreCase("title")) {
	                	if (!isKeywordGroup)
	                		theDocument.addContentParagraph(text);
	                } else if (temp.equalsIgnoreCase("article-meta")) {
	                	theDocument.setPublication(thePub);
	                	//thePub = null;
	                } else if (temp.equalsIgnoreCase("body")) {
	                	//theDocument.setContent(content.toString(), "en");
	                	isBody = false;
	                } else if(temp.equalsIgnoreCase("abstract")) {
	                	theDocument.addDocAbstract(text);
	                	isAbstract = false;
	                } else if(temp.equalsIgnoreCase("title")) {
	                	this.addContent(text, content);
	                } else if(temp.equalsIgnoreCase("article-title")) {
	                	theDocument.setTitle(text);
	                } else if (temp.equalsIgnoreCase("issue")) {
	                	if (thePub != null)
	                		thePub.setPublicationNumber(text);
	                } else if (temp.equalsIgnoreCase("volume")) {
	                //	if (isJournal)
	                	if (thePub != null)
	                		thePub.setPubicationVolume(text);
	                } if (temp.equalsIgnoreCase("kwd-group")) {
 	                	isKeywordGroup = false;
	                } if (temp.equalsIgnoreCase("kwd")) {
	                	theDocument.addTag(text);
	                } else if (temp.equalsIgnoreCase("article-id")) {
	                	if (isPMID) {
	                		theDocument.setPMID(text);
	                		isPMID = false;
	                		System.out.println("SettingPMID "+text);
	                	} else if (isPMCID) {
	                		theDocument.setPMCID(text);
	                		isPMCID = false;
	                	} else if (isDOI) {
	                		thePub.setDOI(text);
	                		isDOI = false;
	                	}
	                //} else if (temp.equalsIgnoreCase("MedlinePgn")) {
	                //	theDocument.setPages(text);
	                } else if (temp.equalsIgnoreCase("Year")) {
	                	if (thePub != null)
	                		thePub.setPublicationYear(text);
	                } else if (temp.equalsIgnoreCase("PMID")) {
	                
	                } else if (temp.equalsIgnoreCase("Month")) {
	                	if (isJournal)
	                		thePub.setMonth(text);
	               // } else if (temp.equalsIgnoreCase("Title")) {
	               // 	if (isJournal)
	                		//theDocument.setPublicationTitle(text);
	                } else if (temp.equalsIgnoreCase("PublicationType")) {
	                	thePub.setPublicationType(makePublicationType(text));
	                } else if (temp.equalsIgnoreCase("NameOfSubstance")) {
	                	theDocument.addTag(text);
	                	//theDocument.addSubstance(text);
	                } else if (temp.equalsIgnoreCase("DescriptorName")) {
	                	theDocument.addTag(text);
	                } else if (temp.equalsIgnoreCase("QualifierName")) {
	                	theDocument.addTag(text);
	                } else if (temp.equalsIgnoreCase("Keyword ")) {
	                	theDocument.addTag(text);
	                } else if(temp.equalsIgnoreCase("CommentsCorrections")) {
	                	isRefType = false;
	                } else if(temp.equalsIgnoreCase("LastName")) { //TODO
	                	if (isAuthor)
	                		lastName = text;
	                } else if(temp.equalsIgnoreCase("ForeName")) { //TODO
	                	if (isAuthor)
	                		firstName = text;
	                } else if (temp.equalsIgnoreCase("MedlineTA")) {
	                	//theDocument.setPublisher(text);
	                } else if (temp.equalsIgnoreCase("Country")) {
	                	//if (!isGrant)
	                	//	theDocument.setPublisherLocation(text);
	                	//else
	                		country = text;
	                } else if (temp.equalsIgnoreCase("ISSNLinking")) {
	                	thePub.setISSN(text);
	                } else if (temp.equalsIgnoreCase("Affiliation")) {
	                	affiliation = text;
	                } else if (temp.equalsIgnoreCase("Initials")) {
	                	initials = text;
	                } else if (temp.equalsIgnoreCase("GrantID")) {
	                	grantId = text;
	                } else if (temp.equalsIgnoreCase("Agency")) {
	                	agency = text;
	                } else if (temp.equalsIgnoreCase("Grant")) {
	                	///////////////////////////////////////////
	                	//<Grant>
	                    //<GrantID>R01 AG034924</GrantID>
	                    //<Acronym>AG</Acronym>
	                    //<Agency>NIA NIH HHS</Agency>
	                    //<Country>United States</Country>
	                	//</Grant>
	                	//////////////////////////////////////////
	                	//theDocument.addGrant(grantId, agency, country);
	                	grantId = null;
	                	agency = null;
	                	country = null;
	                	isGrant = false;
	                } else if (temp.equalsIgnoreCase("CopyrightInformation")) {
	                	theDocument.setCopyright(text);
	                } else if (temp.equalsIgnoreCase("contrib")) {
	                	theDocument.addAuthor(theAuthor);
	                	theAuthor = null;
	                } else if (temp.equalsIgnoreCase("surname")) {
	                	theAuthor.setAuthorLastName(text);
	                } else if (temp.equalsIgnoreCase("given-names")) {
	                	theAuthor.setAuthorInitials(text);
	                } else if (temp.equalsIgnoreCase("xref")) {
	                	_theAuth.put(rid, theAuthor);
	                	//_theAuth = null;
	                	rid = null;
	                } else if (temp.equals("aff")) {
	                	System.out.println("AAA "+affID+" "+_theAuth);
	                	theAuthor = _theAuth.get(affID);
	                	System.out.println("AAA "+affID+" "+theAuthor);
	                	theAuthor.addAffiliationName(text);
	                } else if (temp.equalsIgnoreCase("Article")) {
	                	System.out.println("DID\n"+theDocument.getData().toString());
	                	//We are done here!
	                	if (host != null)
	                		host.parserCallback(theDocument);
	                } else if (temp.equalsIgnoreCase("ref-list")) {
	                	isRefList = false;
	                } else {
	                	unusedTags.add(temp);
	                }
	            } else if(eventType == XmlPullParser.TEXT) {
	                text = xpp.getText().trim();
	             } else if(eventType == XmlPullParser.CDSECT) {
	                text = xpp.getText().trim();
	            }
	            eventType = xpp.next();
	          }
	      } catch (Exception e) {
	      		environment.logError(e.getMessage(), e);
	      		result.addErrorString(e.getMessage());
	      } 		
	}
	
	/**
	 * @see http://www.nlm.nih.gov/mesh/pubtypes.html
	 * We don't model every one of them
	 * @param text
	 * @return
	 */
	String makePublicationType(String text) {
		String result = "JournalArticleType"; //default
		if (text.equalsIgnoreCase("Journal Article"))
			result = "JournalArticleType";
		else if (text.equalsIgnoreCase("Research Support, N.I.H., Extramural"))
			result = "RshSupExtramuralType";
		else if (text.equalsIgnoreCase("Research Support, N.I.H., Intramural"))
			result = "RshSupIntramuralType";
		else if (text.equalsIgnoreCase("Research Support, Non-U.S. Gov't"))
			result = "RshSupNonGovType";
		else if (text.equalsIgnoreCase("Research Support, U.S. Gov't, Non-P.H.S."))
			result = "RshSupGovNonPHSType";
		else if (text.equalsIgnoreCase("Research Support, U.S. Gov't, P.H.S."))
			result = "RshSupGovPHSType";
		else if (text.equalsIgnoreCase("Research Support, U.S. Government"))
			result = "RshSupGovType";
		else if (text.equalsIgnoreCase("Retracted Publication"))
			result = "RetractedPubType";
		else if (text.equalsIgnoreCase("Published Erratum"))
			result = "PublishedErratumType";
		else if (text.equalsIgnoreCase("Review"))
			result = "ReviewType";
		else if (text.equalsIgnoreCase("Scientific Integrity Review"))
			result = "SciIntegrityReviewType";
		else if (text.equalsIgnoreCase("Statistics"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Technical Report"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Practice Guideline"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Pharmacopoeias"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Personal Narratives"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Observational Study"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Multicenter Study"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Monograph"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Duplicate Publication"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Editorial"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Comparative Study"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Comment"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Clinical Trial"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Clinical Trial, Phase I"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Clinical Trial, Phase II"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Clinical Trial, Phase III"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Clinical Trial, Phase IV"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Randomized Controlled Trial"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Pragmatic Clinical Trial"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("English Abstract"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Meta-Analysis"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("News"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Letter"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("Newspaper Article"))
			result = ""; //TODO
		else if (text.equalsIgnoreCase("Case Reports"))
			result = ""; //TODO
		else if (text.equalsIgnoreCase("Interview"))
			result = ""; //TODO
		else if (text.equalsIgnoreCase("Historical Article"))
			result = "";//TODO
		else if (text.equalsIgnoreCase("In Vitro"))
			result = "";//TODO
		return result;
	}
	/**
	 * </p>@see http://www.ncbi.nlm.nih.gov/pubmed/23329350 for
	 * an abstract that has wild characters '0''9'</p>
	 * <p>@see http://www.ncbi.nlm.nih.gov/pubmed/23325918 uses:
	 * "Expt." for experiment</p>
	 * <p>Bad break:
	 * Trichophyton rubrum (T.
 rubrum) represents the most important agent of dermatophytosis in humans.
	 * </p>
	 * <p> (pâ€‰&lt;â€‰0.001),  bad sentence break here, and 0009 and &lg;
	 *   has UTF-8 characters</p>
	 * @param inString
	 * @return
	 * Note: grand possiblility of outOfBounds errors here
	 */
	String cleanText(String inString) {
		int lparen = (int)'(';
		String foo = inString;
		foo = foo.replace("Expt.", "Experiment"); //worked!
		StringBuilder buf = new StringBuilder();
		int len = foo.length();
		int c = 0;
		boolean blockNewLine = false;
		for (int i=0;i<len;i++) {
			c = foo.charAt(i);
			//case of 23329350 
			//TODO did not work
			if (blockNewLine) {
				if (c != 0x0D && c != 0x0A) {
					//we just passed the newline line feed we wanted to ignore
					blockNewLine = false;
					buf.append((char)c);
				}
			}
			if (c == 0 && foo.charAt(i+1) == 9)
				i++; // skip those
			else if (c == lparen) {
				//bad sentence 
				//TODO did not work
				////////////////////////////////
				// outof bounds at i+2 and i+3
				if (i+3 < foo.length() && foo.charAt(i+2)==(int)'.' &&
					foo.charAt(i+3)==0x0D) {
					blockNewLine = true;
				}
				buf.append((char)c);
			}
			else
				buf.append((char)c);
		}
		return buf.toString();
	}
	/**
     * does not return null if no attributes
     */
    HashMap<String,String> getAttributes(XmlPullParser p) {
      HashMap <String,String>result =  new HashMap<String,String>();;
      int count = p.getAttributeCount();
      if (count > 0) {
        String name = null;
        for (int i = 0; i < count; i++) {
          name = p.getAttributeName(i);
          result.put(name,p.getAttributeValue(i));
        }
      }
      return result;
    }		

}
