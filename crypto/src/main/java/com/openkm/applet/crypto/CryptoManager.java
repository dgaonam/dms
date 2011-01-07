/**
 *  OpenKM, Open Document Management System (http://www.openkm.com)
 *  Copyright (c) 2006-2010  Paco Avila & Josep Llort
 *
 *  No bytes were intentionally harmed during the development of this application.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.openkm.applet.crypto;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.apache.http.client.ClientProtocolException;

/**
 * CryptoManager
 * 
 * @author jllort
 *
 */
public class CryptoManager {
	private static Logger log = Logger.getLogger(CryptoManager.class.getName());
	private String token;
	private String path;
	private String url;
	private String uuid;
	private String action;
	private CryptoByPassword criptoByPassword;

	/**
	 * CryptoManager
	 * 
	 * @param token
	 * @param win
	 */
	public CryptoManager(String token, String path, String url, String action, String uuid) {
		this.token = token;
		this.path = path;
		this.url = url;
		this.action = action;
		this.uuid = uuid;
		criptoByPassword = new CryptoByPassword();
	}
	
	/**
	 * getAction
	 * 
	 * @return
	 */
	public String getAction() {
		return action;
	}
	
	/**
	 * encrypt
	 * 
	 * @param data
	 * @param passWord
	 * @return
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] data, String passWord) throws Exception
	{    
		try {
			return criptoByPassword.encrypt(data, passWord.toCharArray());
		} catch (Exception e) {
			return null;
		}
    }
	
	/**
	 * decrypt
	 * 
	 * @param data
	 * @param passWord
	 * @return
	 * @throws Exception
	 */
	public byte[] decrypt(byte[] data, String passWord) throws Exception
    {
		return criptoByPassword.decrypt(data, passWord.toCharArray());
    }
	
	/**
	 * upload
	 * 
	 * @param tmpFile
	 * @param fileName
	 * @param parentComponent
	 */
	public void upload(File tmpFile, String fileName, Component parentComponent) {
		log.log(Level.INFO, "**** UPLOAD DOCUMENT ****");
		try {
			String response = Util.createDocument(token, path, fileName, url, tmpFile);
			if (!response.startsWith("OKM_OK")) {
				log.log(Level.SEVERE, "Error: " + response);
				ErrorCode.displayError(response, path+"/"+fileName);
			}
		} catch (IOException e) {
			log.log(Level.SEVERE, "IOException: " + e.getMessage(), e);
			JOptionPane.showMessageDialog(parentComponent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (Throwable e) { // Catch java.lang.OutOfMemeoryException
			log.log(Level.SEVERE, "Throwable: " + e.getMessage(), e);
			JOptionPane.showMessageDialog(parentComponent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		log.log(Level.INFO, "**** DOCUMENT UPLOADED ****");
	}
	
	/**
	 * download
	 * 
	 * @param parentComponent
	 * @return
	 */
	public File download(Component parentComponent) {
		try {
			return Util.downloadDocument(token, uuid, url);
		} catch (ClientProtocolException e) {
			log.log(Level.SEVERE, "IOException: " + e.getMessage(), e);
			JOptionPane.showMessageDialog(parentComponent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			log.log(Level.SEVERE, "IOException: " + e.getMessage(), e);
			JOptionPane.showMessageDialog(parentComponent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	/**
	 * getFileName
	 * 
	 * @return
	 */
	public String getFileName() {
		return path.substring(path.lastIndexOf("/"));
	}
}