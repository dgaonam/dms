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

package com.openkm.dao.bean;

import java.io.Serializable;

public class UserProfileMenu implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean fileVisible;
	private boolean editVisible;
	private boolean toolsVisible;
	private boolean bookmarksVisible;
	private boolean helpVisible;

	public boolean isFileVisible() {
		return fileVisible;
	}

	public void setFileVisible(boolean fileVisible) {
		this.fileVisible = fileVisible;
	}
	
	public boolean isEditVisible() {
		return editVisible;
	}

	public void setEditVisible(boolean editVisible) {
		this.editVisible = editVisible;
	}

	public boolean isToolsVisible() {
		return toolsVisible;
	}

	public void setToolsVisible(boolean toolsVisible) {
		this.toolsVisible = toolsVisible;
	}

	public boolean isBookmarksVisible() {
		return bookmarksVisible;
	}

	public void setBookmarksVisible(boolean bookmarksVisible) {
		this.bookmarksVisible = bookmarksVisible;
	}

	public boolean isHelpVisible() {
		return helpVisible;
	}

	public void setHelpVisible(boolean helpVisible) {
		this.helpVisible = helpVisible;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("fileVisible="); sb.append(fileVisible);
		sb.append(", editVisible="); sb.append(editVisible);
		sb.append(", toolsVisible="); sb.append(toolsVisible);
		sb.append(", bookmarksVisible="); sb.append(bookmarksVisible);
		sb.append(", helpVisible="); sb.append(helpVisible);
		sb.append("}");
		return sb.toString();
	}
}
