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

package com.openkm.module;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.openkm.bean.PropertyGroup;
import com.openkm.bean.form.FormElement;
import com.openkm.core.AccessDeniedException;
import com.openkm.core.DatabaseException;
import com.openkm.core.LockException;
import com.openkm.core.NoSuchGroupException;
import com.openkm.core.NoSuchPropertyException;
import com.openkm.core.ParseException;
import com.openkm.core.PathNotFoundException;
import com.openkm.core.RepositoryException;

public interface PropertyGroupModule {

	/**
	 * Add a property group to a document.
	 * 
	 * @param docPath The path that identifies an unique document.
	 * @param grpName The group name previously registered in the system.
	 * @throws NoSuchGroupException If there is no such registered group name.
	 * @throws LockException Can't modify a locked document. 
	 * @throws PathNotFoundException If there is no document in this 
	 * repository path.
	 * @throws AccessDeniedException If there is any security problem:
	 * you can't modify the document because of lack of permissions.
	 * @throws RepositoryException If there is any general repository problem.
	 */
	public void addGroup(String docPath, String grpName) throws NoSuchGroupException, LockException,
			PathNotFoundException, AccessDeniedException, RepositoryException, DatabaseException;

	/**
	 * Removes a property group from a document.
	 * 
	 * @param docPath The path that identifies an unique document.
	 * @param grpName The group name previously registered in the system.
	 * @throws NoSuchGroupException If there is no such registered group name.
	 * @throws LockException Can't modify a locked document. 
	 * @throws PathNotFoundException If there is no document in this 
	 * repository path.
	 * @throws RepositoryException If there is any general repository problem.
	 */
	public void removeGroup(String docPath, String grpName) throws AccessDeniedException, 
			NoSuchGroupException, LockException, PathNotFoundException, RepositoryException,
			DatabaseException;

	/**
	 * Get groups assigned to a document.
	 * 
	 * @param docPath The path that identifies an unique document.
	 * @throws PathNotFoundException If there is no document in this 
	 * repository path.
	 * @throws RepositoryException If there is any general repository problem.
	 */
	public List<PropertyGroup> getGroups(String docPath) throws IOException, ParseException,
			PathNotFoundException, RepositoryException, DatabaseException;

	/**
	 * Get all groups defined in the system.
	 * 
	 * @throws RepositoryException If there is any general repository problem.
	 */
	public List<PropertyGroup> getAllGroups() throws IOException, ParseException, RepositoryException,
			DatabaseException;

	/**
	 * Get all properties defined in a document by group.
	 * 
	 * @param docPath The path that identifies an unique document.
	 * @param grpName The group name previously registered in the system.
	 * @throws NoSuchGroupException If there is no such registered group name.
	 * @throws PathNotFoundException If there is no document in this 
	 * repository path.
	 * @throws RepositoryException If there is any general repository problem.
	 */
	public List<FormElement> getProperties(String docPath, String grpName) throws IOException,
			ParseException, NoSuchGroupException, PathNotFoundException, RepositoryException, DatabaseException;
	
	/**
	 * Set group properties to a document.
	 * 
	 * @param docPath The path that identifies an unique document.
	 * @param grpName The group name previously registered in the system.
	 * @param propName The category property name.
	 * @throws NoSuchPropertyException If there is no such registered category property.
	 * @throws NoSuchGroupException If there is no such registered group name.
	 * @throws LockException Can't modify a locked document. 
	 * @throws PathNotFoundException If there is no document in this 
	 * repository path.
	 * @throws AccessDeniedException If there is any security problem:
	 * you can't modify the document because of lack of permissions.
	 * @throws RepositoryException If there is any general repository problem.
	 */
	public void setProperties(String docPath, String grpName, Map<String, String[]> properties)
			throws NoSuchPropertyException, NoSuchGroupException, LockException, PathNotFoundException, 
			AccessDeniedException, RepositoryException, DatabaseException;

	/**
	 * Get all possible values which can have a property.
	 *  
	 * @param grpName The group name previously registered in the system.
	 * @throws IOException If there is any problem reading the property values.
	 * @throws RepositoryException If there is any general repository problem.
	 */
	public List<FormElement> getPropertyGroupForm(String grpName) throws ParseException, IOException, 
			RepositoryException, DatabaseException;
}
