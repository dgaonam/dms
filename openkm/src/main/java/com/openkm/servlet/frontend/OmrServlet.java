/**
 * OpenKM, Open Document Management System (http://www.openkm.com)
 * Copyright (c) 2006-2013 Paco Avila & Josep Llort
 * 
 * No bytes were intentionally harmed during the development of this application.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.openkm.servlet.frontend;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openkm.api.OKMDocument;
import com.openkm.api.OKMPropertyGroup;
import com.openkm.api.OKMRepository;
import com.openkm.automation.AutomationException;
import com.openkm.bean.PropertyGroup;
import com.openkm.core.AccessDeniedException;
import com.openkm.core.DatabaseException;
import com.openkm.core.LockException;
import com.openkm.core.NoSuchGroupException;
import com.openkm.core.NoSuchPropertyException;
import com.openkm.core.ParseException;
import com.openkm.core.PathNotFoundException;
import com.openkm.core.RepositoryException;
import com.openkm.dao.OmrDAO;
import com.openkm.dao.bean.Omr;
import com.openkm.extension.core.ExtensionException;
import com.openkm.frontend.client.OKMException;
import com.openkm.frontend.client.bean.GWTOmr;
import com.openkm.frontend.client.constants.service.ErrorCode;
import com.openkm.frontend.client.service.OKMOmrService;
import com.openkm.util.FileUtils;
import com.openkm.util.GWTUtil;
import com.openkm.util.OMRException;
import com.openkm.util.OMRUtils;

/**
 * OMR service
 */
public class OmrServlet extends OKMRemoteServiceServlet implements OKMOmrService {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(OmrServlet.class);

	@Override
	public List<GWTOmr> getAllOmr() throws OKMException {
		List<GWTOmr> omrList = new ArrayList<GWTOmr>();
		try {
			for (Omr omr : OmrDAO.getInstance().findAllActive()) {
				omrList.add(GWTUtil.copy(omr));
			}
		} catch (DatabaseException e) {
			log.error(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMOmrService, ErrorCode.CAUSE_Database),e.getMessage());
		}
		return omrList;
	}

	@Override
	public void process(long omId, String uuid) throws OKMException {
		InputStream is = null;
		File fileToProcess = null;
		List<String> groups = new ArrayList<String>();
		try {
			String docPath = OKMRepository.getInstance().getNodePath(null, uuid);
			// create tmp content file
			fileToProcess = FileUtils.createTempFile();
			is = OKMDocument.getInstance().getContent(null, docPath, false);
			FileUtils.copy(is, fileToProcess);
			is.close();
			// process 
			Map<String, String> results = OMRUtils.process(fileToProcess, omId); 
			// capture involved groups from properties
			for (String key : results.keySet()) {
				if (key.contains(":")) {
					String grpName = key.substring(0, key.indexOf("."));
					grpName = grpName.replace("okp", "okg"); // convert to okg ( group name always start with okg )
					if (!groups.contains(grpName)) {
						groups.add(grpName);
					}
				}
			}
			// Add missing groups
			for (PropertyGroup registeredGroup : OKMPropertyGroup.getInstance().getGroups(null, docPath)) {
				if (groups.contains(registeredGroup.getName())) {
					groups.remove(registeredGroup.getName());
				}
			}
			// Add properties
			for (String grpName : groups) {
				OKMPropertyGroup.getInstance().addGroup(null, docPath, grpName);
				String propertyBeginning = grpName.replace("okg", "okp"); // convert to okp ( property format )
				Map<String, String> properties = new HashMap<String, String>();
				for (String key : results.keySet()) {
					if (key.startsWith(propertyBeginning)) {
						properties.put(key, results.get(key));
					}
				}
				OKMPropertyGroup.getInstance().setPropertiesSimple(null, docPath, grpName, properties);
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMOmrService, ErrorCode.CAUSE_IO),e.getMessage());
		} catch (PathNotFoundException e) {
			log.error(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMOmrService, ErrorCode.CAUSE_PathNotFound),e.getMessage());
		} catch (AccessDeniedException e) {
			log.error(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMOmrService, ErrorCode.CAUSE_AccessDenied),e.getMessage());
		} catch (RepositoryException e) {
			log.error(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMOmrService, ErrorCode.CAUSE_Repository),e.getMessage());
		} catch (DatabaseException e) {
			log.error(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMOmrService, ErrorCode.CAUSE_Database),e.getMessage());
		} catch (OMRException e) {
			log.error(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMOmrService, ErrorCode.CAUSE_Omr),e.getMessage());
		} catch (NoSuchGroupException e) {
			log.error(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMOmrService, ErrorCode.CAUSE_NoSuchGroup),e.getMessage());
		} catch (LockException e) {
			log.error(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMOmrService, ErrorCode.CAUSE_Lock),e.getMessage());
		} catch (ExtensionException e) {
			log.error(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMOmrService, ErrorCode.CAUSE_Extension),e.getMessage());
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMOmrService, ErrorCode.CAUSE_Parse),e.getMessage());
		} catch (NoSuchPropertyException e) {
			log.error(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMOmrService, ErrorCode.CAUSE_NoSuchProperty),e.getMessage());
		} catch (AutomationException e) {
			log.error(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMOmrService, ErrorCode.CAUSE_Automation),e.getMessage());
		}
	}
}