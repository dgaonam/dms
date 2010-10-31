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

package com.openkm.frontend.server.extension;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openkm.core.AccessDeniedException;
import com.openkm.core.DatabaseException;
import com.openkm.core.PathNotFoundException;
import com.openkm.core.RepositoryException;
import com.openkm.dao.bean.extension.Staple;
import com.openkm.dao.bean.extension.StapleGroup;
import com.openkm.dao.extension.StapleGroupDAO;
import com.openkm.frontend.client.OKMException;
import com.openkm.frontend.client.bean.extension.GWTStapleGroup;
import com.openkm.frontend.client.config.ErrorCode;
import com.openkm.frontend.client.service.extension.OKMStaplingService;
import com.openkm.frontend.server.OKMRemoteServiceServlet;
import com.openkm.frontend.server.Util;

/**
 * Servlet Class
 * 
 * @web.servlet              name="OKMStaplingServlet"
 *                           display-name="Directory tree service"
 *                           description="Directory tree service"
 * @web.servlet-mapping      url-pattern="/OKMStaplingServlet"
 * @web.servlet-init-param   name="A parameter"
 *                           value="A value"
 */
public class OKMStaplingServlet extends OKMRemoteServiceServlet implements OKMStaplingService {
	private static Logger log = LoggerFactory.getLogger(OKMStaplingServlet.class);
	private static final long serialVersionUID = 395857404418870245L;
	
	@Override
	public String create(String username, String uuid, String type, String uuid2, String type2) throws OKMException {
		StapleGroup stapleGroup = new StapleGroup();
		stapleGroup.setUsername(username);
		try {
			// Creating stapling group
			int id = StapleGroupDAO.create(stapleGroup);
			
			// Adding stapling elements
			stapleGroup = StapleGroupDAO.findByPk(id);
			Staple staple = new Staple();
			staple.setUuid(uuid);
			staple.setType(type);
			stapleGroup.getStaples().add(staple); // Added first
			staple = new Staple();
			staple.setUuid(uuid2);
			staple.setType(type2);
			stapleGroup.getStaples().add(staple); // Added second
			StapleGroupDAO.update(stapleGroup); 	// Updating
			return String.valueOf(id);
		} catch (DatabaseException e) {
			log.warn(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMStaplingService, ErrorCode.CAUSE_DatabaseException), e.getMessage());
		}
	}
	
	@Override
	public void add(String id, String uuid, String type) throws OKMException {
		try {
			StapleGroup stapleGroup = StapleGroupDAO.findByPk(Integer.valueOf(id));
			boolean found = false;
			for (Staple st : stapleGroup.getStaples()) {
				if (st.getUuid().equals(uuid)) {
					found = true;
					break;
				}
			}
			// Only we add if document not exists
			if (!found) {
				Staple staple = new Staple();
				staple.setUuid(uuid);
				staple.setType(type);
				stapleGroup.getStaples().add(staple); // Added first
				StapleGroupDAO.update(stapleGroup); 	// Updating
			}
		} catch (NumberFormatException e) {
			log.warn(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMStaplingService, ErrorCode.CAUSE_NumberFormatException), e.getMessage());
		} catch (DatabaseException e) {
			log.warn(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMStaplingService, ErrorCode.CAUSE_DatabaseException), e.getMessage());
		}
	}
	
	@Override
	public List<GWTStapleGroup> getAll(String uuid) throws OKMException {
		List<GWTStapleGroup> stapList = new ArrayList<GWTStapleGroup>();
		try {
			for (StapleGroup sg : StapleGroupDAO.findAll(uuid)) {
				stapList.add(Util.copy(sg));
			}
		} catch (DatabaseException e) {
			log.warn(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMStaplingService, ErrorCode.CAUSE_DatabaseException), e.getMessage());
		} catch (RepositoryException e) {
			log.warn(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMStaplingService, ErrorCode.CAUSE_Repository), e.getMessage());
		} catch (AccessDeniedException e) {
			log.warn(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMStaplingService, ErrorCode.CAUSE_AccessDenied), e.getMessage());
		} catch (PathNotFoundException e) {
			log.warn(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMStaplingService, ErrorCode.CAUSE_PathNotFound), e.getMessage());
		}
		return stapList;
	}
	
	
	@Override
	public void remove(String id) throws OKMException {
		try {
			StapleGroupDAO.delete(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			log.warn(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMStaplingService, ErrorCode.CAUSE_NumberFormatException), e.getMessage());
		} catch (DatabaseException e) {
			log.warn(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMStaplingService, ErrorCode.CAUSE_DatabaseException), e.getMessage());
		}
	}

	@Override
	public void removeStaple(String id) throws OKMException {
		try {
			StapleGroupDAO.deleteStaple(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			log.warn(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMStaplingService, ErrorCode.CAUSE_NumberFormatException), e.getMessage());
		} catch (DatabaseException e) {
			log.warn(e.getMessage(), e);
			throw new OKMException(ErrorCode.get(ErrorCode.ORIGIN_OKMStaplingService, ErrorCode.CAUSE_DatabaseException), e.getMessage());
		}
	}
}
