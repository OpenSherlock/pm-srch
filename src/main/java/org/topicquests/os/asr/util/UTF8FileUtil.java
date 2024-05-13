/*
 * Copyright 2023 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.util;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author park
 * 
 * Read and Write UTF8 files
 */
public class UTF8FileUtil {

	/**
	 * 
	 */
	public UTF8FileUtil() {}

	/**
	 * Write a file at the given <code>filePath</code>
	 * @param filePath
	 * @param content
	 * @throws Exception
	 */
	public void writeFile(String filePath, String content) throws Exception {
		File f = new File(filePath);
		PrintWriter out = new PrintWriter(f, StandardCharsets.UTF_8.toString());
		out.write(content);
		out.flush();
		out.close();
	}
	
	/**
	 * List all the strings in the file at the given <code>filePath</code>
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public List<String> getLines(String filePath) throws Exception  {
		Path path = Paths.get(filePath);
		List<String> result = Files.readAllLines(path, StandardCharsets.UTF_8);
		return result;
	}

}
