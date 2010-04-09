/**
 *  OpenKM, Open Document Management System (http://www.openkm.com)
 *  Copyright (C) 2006-2010  Paco Avila & Josep Llort
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

package es.git.openkm.frontend.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author jllort
 *
 */
public interface OKMMailServiceAsync {
	public void getChilds(String fldPath, AsyncCallback<?> callback);
	public void delete(String mailPath, AsyncCallback<?> callback);
	public void move(String docPath, String destPath, AsyncCallback<?> callback);
	public void purge(String mailPath, AsyncCallback<?> callback);
	public void copy(String mailPath, String fldPath, AsyncCallback<?> callback);
	public void getProperties(String mailPath, AsyncCallback<?> callback);
}