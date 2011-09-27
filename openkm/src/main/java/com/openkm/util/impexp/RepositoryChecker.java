/**
 *  OpenKM, Open Document Management System (http://www.openkm.com)
 *  Copyright (c) 2006-2011  Paco Avila & Josep Llort
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

package com.openkm.util.impexp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openkm.bean.Document;
import com.openkm.bean.Folder;
import com.openkm.bean.Mail;
import com.openkm.bean.Version;
import com.openkm.core.AccessDeniedException;
import com.openkm.core.Config;
import com.openkm.core.DatabaseException;
import com.openkm.core.PathNotFoundException;
import com.openkm.core.RepositoryException;
import com.openkm.module.DocumentModule;
import com.openkm.module.FolderModule;
import com.openkm.module.MailModule;
import com.openkm.module.ModuleManager;

public class RepositoryChecker {
	private static Logger log = LoggerFactory.getLogger(RepositoryChecker.class);
	private RepositoryChecker() {}

	/**
	 * Performs a recursive repository document check
	 */
	public static ImpExpStats checkDocuments(String token, String fldPath, boolean versions, Writer out, 
			InfoDecorator deco) throws PathNotFoundException, AccessDeniedException, RepositoryException,
			IOException, DatabaseException {
		log.debug("checkDocuments({}, {}, {}, {}, {})", new Object[] { token, fldPath, versions, out, deco });
		ImpExpStats stats;
		
		try {
			stats = checkDocumentsHelper(token, fldPath, versions, out, deco);
		} catch (PathNotFoundException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (AccessDeniedException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (RepositoryException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (DatabaseException e) {
			log.error(e.getMessage(), e);
			throw e;
		}

		log.debug("checkDocuments: {}", stats);
		return stats;
	}

	/**
	 * Performs a recursive repository document check
	 */
	private static ImpExpStats checkDocumentsHelper(String token, String fldPath, boolean versions, Writer out, 
			InfoDecorator deco) throws FileNotFoundException, PathNotFoundException, AccessDeniedException,
			RepositoryException, IOException, DatabaseException {
		log.debug("checkDocumentsHelper({}, {}, {}, {}, {})", new Object[] { token, fldPath, versions, out, deco });
		ImpExpStats stats = new ImpExpStats();
		File fsPath = new File(Config.NULL_DEVICE);
		DocumentModule dm = ModuleManager.getDocumentModule();
		FolderModule fm = ModuleManager.getFolderModule();
		MailModule mm = ModuleManager.getMailModule();
		
		// Iterate through document childs
		for (Document docChild : dm.getChilds(token, fldPath)) {
			try {
				FileOutputStream fos = new FileOutputStream(fsPath);
				InputStream is = dm.getContent(token, docChild.getPath(), false);
				IOUtils.copy(is, fos);
				is.close();
				
				if (versions) { // Check version history
					for (Version ver : dm.getVersionHistory(token, docChild.getPath())) {
						is = dm.getContentByVersion(token, docChild.getPath(), ver.getName());
						IOUtils.copy(is, fos);
						is.close();
					}
				}
				
				fos.close();
				out.write(deco.print(docChild.getPath(), docChild.getActualVersion().getSize(), null));
				out.flush();
				
				// Stats
				stats.setSize(stats.getSize() + docChild.getActualVersion().getSize());
				stats.setDocuments(stats.getDocuments() + 1);
			} catch (RepositoryException e) {
				log.error(e.getMessage());
				out.write(deco.print(docChild.getPath(), docChild.getActualVersion().getSize(), e.getMessage()));
				out.flush();
			}
		}
		
		// Iterate through mail childs
		for (Mail mailChild : mm.getChilds(token, fldPath)) {
			ImpExpStats tmp = checkDocumentsHelper(token, mailChild.getPath(), versions, out, deco);
			
			// Stats
			stats.setSize(stats.getSize() + tmp.getSize());
			stats.setDocuments(stats.getDocuments() + tmp.getDocuments());
			stats.setFolders(stats.getFolders() + tmp.getFolders() + 1);
			stats.setOk(stats.isOk() && tmp.isOk());
		}
		
		// Iterate through folder childs
		for (Folder fldChild : fm.getChilds(token, fldPath)) {
			ImpExpStats tmp = checkDocumentsHelper(token, fldChild.getPath(), versions, out, deco);
			
			// Stats
			stats.setSize(stats.getSize() + tmp.getSize());
			stats.setDocuments(stats.getDocuments() + tmp.getDocuments());
			stats.setFolders(stats.getFolders() + tmp.getFolders() + 1);
			stats.setOk(stats.isOk() && tmp.isOk());
		}
		
		log.debug("checkDocumentsHelper: {}", stats);
		return stats;
	}
}
