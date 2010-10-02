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

package com.openkm.servlet.admin;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openkm.api.OKMFolder;
import com.openkm.bean.ContentInfo;
import com.openkm.util.FormatUtil;
import com.openkm.util.WebUtil;
import com.openkm.util.impexp.HTMLInfoDecorator;
import com.openkm.util.impexp.ImpExpStats;
import com.openkm.util.impexp.RepositoryChecker;

/**
 * Repository checker servlet
 */
public class RepositoryCheckerServlet extends BaseServlet {
	private static Logger log = LoggerFactory.getLogger(RepositoryCheckerServlet.class);
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException {
		log.debug("doGet({}, {})", request, response);
		String repoPath = WebUtil.getString(request, "repoPath", "/okm:root");
		updateSessionManager(request);
		Writer out = response.getWriter();
		response.setContentType("text/html");
		out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
		out.write("<HTML>");
		out.write("<HEAD>");
		out.write("<STYLE type=\"text/css\">");
		out.write("body, td, a, div, .p { font-family:verdana,arial,sans-serif; font-size:10px; }");
		out.write("</STYLE>");
		out.write("<BODY>");
		out.flush();
		
		try {
			if (!repoPath.equals("")) {
				ContentInfo cInfo = OKMFolder.getInstance().getContentInfo(null, repoPath);
				long begin = System.currentTimeMillis();
				ImpExpStats stats = RepositoryChecker.checkDocuments(repoPath, out,	new HTMLInfoDecorator((int)cInfo.getDocuments()));
				long end = System.currentTimeMillis();
				out.write("<hr/>");
				out.write("<div class=\"ok\">Folder '"+repoPath+"'</div>");
				out.write("<br/>");
				out.write("<b>Documents:</b> "+stats.getDocuments()+"<br/>");
				out.write("<b>Folders:</b> "+stats.getFolders()+"<br/>");
				out.write("<b>Size:</b> "+FormatUtil.formatSize(stats.getSize())+"<br/>");
				out.write("<b>Time:</b> "+FormatUtil.formatSeconds(end - begin)+"<br/>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.write("</BODY>");
			out.write("</HTML>");
			out.flush();
		}
	}
}
