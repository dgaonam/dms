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

package com.openkm.workflow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jbpm.mail.AddressResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openkm.core.RepositoryException;
import com.openkm.module.direct.DirectAuthModule;

public class OKMAddressResolver implements AddressResolver {
	private static Logger log = LoggerFactory.getLogger(OKMAddressResolver.class);
	private static final long serialVersionUID = 1L;

	public Object resolveAddress(String actorId) {
		log.info("resolveAddress("+actorId+")");
		String email = null;
		
		try {
			List<String> users = new ArrayList<String>();
			users.add(actorId);
			List<String> emails = new DirectAuthModule().getMails(null, users);
			
			for (Iterator<String> it = emails.iterator(); it.hasNext(); ) {
				email = it.next();
			}
		} catch (RepositoryException e) {
			log.warn(e.getMessage());
		}
		
		log.info("resolveAddress: "+email);
		return email;
	}
}
