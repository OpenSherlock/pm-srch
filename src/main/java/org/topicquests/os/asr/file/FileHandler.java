/**
 * 
 */
//package org.topicquests.harvester.pubmed;
package org.topicquests.os.asr.file;
import java.io.*;
import java.nio.charset.StandardCharsets;

import org.topicquests.os.asr.Environment;
import org.topicquests.os.asr.JSONDocumentObject;
/**
 * @author park
 *
 */
public class FileHandler {
	private Environment environment;

	/**
	 * @param enc
	 */
	public FileHandler(Environment env) {
		environment = env;
	}

	
	/**
	 * Recursively walk down a given <code>directory</code> sending
	 * file paths to the reader.
	 * @param directory
	 * @param isPMID  - might be PMC
	 */
	public void processDirectory(File directory, boolean isPMID) {
		File [] files = directory.listFiles();
		File f;
		int len = files.length;
		for (int i=0;i<len;i++) {
			f = files[i];
			if (f.isDirectory())
				processDirectory(f, isPMID); // recurse
			else if (isPMID)
				processPubMedFile(f);
			else
				processPMCFile(f);
		}
	}
	
	void processPubMedFile(File xml) {
		//TODO
	}
	
	void processPMCFile(File xml) {
		
	}
	
	/**
	 * <p>Callbackfrom {@link PubMedFullReportPullParser}</p>
	 * <p>The parser, when it knows PMID, will check here.
	 * If that PMID has been saved, parser returns <code>null</code></p>
	 * @param jo can be <code>null</code>
	 */
	public void parserCallback(JSONDocumentObject jo) {
		if (jo == null)
			return;
		/*try {
			String pmid = jo.getPMID();
			System.out.println("GotPMID "+pmid);
			StringBuilder buf = new StringBuilder(environment.getOutputPath()).append(pmid).append(".json");
			File f = new File(buf.toString());
			PrintWriter out = new PrintWriter(f, StandardCharsets.UTF_8.toString());
	    	out.print(jo.toJSONString());
	    	out.flush();
	    	out.close();
		} catch (Exception e) {
			//TODO
			e.printStackTrace();
		}*/
	}
}
