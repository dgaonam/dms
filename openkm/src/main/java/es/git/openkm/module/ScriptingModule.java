/**
 *  OpenKM, Open Document Management System (http://www.openkm.com)
 *  Copyright (C) 2006  GIT Consultors
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

package es.git.openkm.module;

import es.git.openkm.core.AccessDeniedException;
import es.git.openkm.core.PathNotFoundException;
import es.git.openkm.core.RepositoryException;

public interface ScriptingModule {

	/**
	 * Add script to node.
	 * 
	 * @param token The session authorization token.
	 * @param nodePath The complete path to the node.
	 * @param code Script BeanShell code.
	 * @throws PathNotFoundException If the node defined by nodePath do not exists.
	 * @throws AccessDeniedException If the token authorization information is not valid.
	 * @throws RepositoryException If there is any error accessing to the repository.
	 */
	public void setScript(String token, String nodePath, String code) 
			throws PathNotFoundException, AccessDeniedException, RepositoryException;
	
	/**
	 * Remove script from node.
	 * 
	 * @param token The session authorization token.
	 * @param nodePath The complete path to the node.
	 * @throws PathNotFoundException If the node defined by nodePath do not exists.
	 * @throws AccessDeniedException If the token authorization information is not valid.
	 * @throws RepositoryException If there is any error accessing to the repository.
	 */
	public void removeScript(String token, String nodePath)
			throws PathNotFoundException, AccessDeniedException, RepositoryException;
	
	/** 
	 * Get node script (document or folder).
	 * 
	 * @param token The session authorization token.
	 * @param nodePath The complete path to the node.
	 * @return A hashmap with pairs of user / permissions.
	 * @throws PathNotFoundException If the node defined by nodePath do not exists.
	 * @throws AccessDeniedException If the token authorization information is not valid.
	 * @throws RepositoryException If there is any error accessing to the repository.
	 */
	public String getScript(String token, String nodePath) throws PathNotFoundException,
			AccessDeniedException, RepositoryException;
}
