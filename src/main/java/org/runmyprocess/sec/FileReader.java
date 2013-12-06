package org.runmyprocess.sec;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;

import org.runmyprocess.sec.Config;
import org.runmyprocess.sec.ProtocolInterface;
import org.runmyprocess.sec.Response;
import org.runmyprocess.sec.SECErrorManager;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Malcolm Haslam <mhaslam@runmyprocess.com>
 *
 * Copyright (C) 2013 Fujitsu RunMyProcess
 *
 * This file is part of RunMyProcess SEC.
 *
 * RunMyProcess SEC is free software: you can redistribute it and/or modify
 * it under the terms of the Apache License Version 2.0 (the "License");
 *
 *   You may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import org.runmyprocess.json.JSONObject;

public class FileReader implements ProtocolInterface {

	private Response response = new Response();

	public FileReader() {
		// TODO Auto-generated constructor stub
	}

    /**
     * Error manager
     * @param error  error message
     * @return error jsonObject
     */
	private JSONObject FileReaderError(String error){
		response.setStatus(500);//sets the return status to internal server error
		JSONObject errorObject = new JSONObject();
		errorObject.put("error", error.toString());
		return errorObject;
	}

    /**
     * Reads the loaded file and returns it in jsonObject format
     * @param file the file to read
     * @return the read file and name in jsonObject format
     * @throws Exception
     */
	@SuppressWarnings("resource")
	private static JSONObject loadFile(File file) throws Exception {
		InputStream is = new FileInputStream(file);
		 
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
		// File is too large
		}
		byte[] bytes = new byte[(int)length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}
		 
		if (offset < bytes.length) {
			throw new Exception("Could not completely read file "+file.getName());
		}
		is.close();
		
		JSONObject fileInfo = new JSONObject();
		byte[] encoded = Base64.encodeBase64(bytes);
		String encodedString = new String(encoded);
		
		fileInfo.put("fileName", file.getName());
		fileInfo.put("file",encodedString);
		
		return fileInfo;
	}

    /**
     * receives the object with the path to look for the file and read the configuration file
     * @param jsonObject
     * @param configPath
     */
	  @Override
	public void accept(JSONObject jsonObject,String configPath) {
		try {
			
			final String path = jsonObject.getString("path");//Gets the path sent 

		      System.out.println("Searching for fileReader config file ...");
			  Config config = new Config("configFiles"+File.separator+"fileReader.config",true);//finds and reads the config file
		      System.out.println("fileReader config file found");
		      String basePath = config.getProperty("basePath");//sets the base path to look for the file
			 
			try{ 
				System.out.println("Looking for file...");
				File file = new File(basePath+path);//gets the file 
				JSONObject fileInfo = loadFile(file);//loads the file and returns an object with the name and base64 string
				System.out.println("File Read!");
				
				response.setStatus(200);//sets the return status to 200
				JSONObject resp = new JSONObject();
				resp.put("file", fileInfo);//sends the info inside a file object
				response.setData(resp);
				
			} catch (Exception e) {
				response.setData(this.FileReaderError(e.getMessage()));
	        	SECErrorManager errorManager = new SECErrorManager();
	        	errorManager.logError(e.getMessage(), Level.SEVERE);
				e.printStackTrace();
	        	throw e;
			}		
		} catch (Exception e) {
			response.setData(this.FileReaderError(e.getMessage()));
        	SECErrorManager errorManager = new SECErrorManager();
        	errorManager.logError(e.getMessage(), Level.SEVERE);
			e.printStackTrace();
		}
	}
	
	@Override
	public Response getResponse() {
		return response;
	}
	
}
