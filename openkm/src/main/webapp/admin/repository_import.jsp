<%@ page import="java.io.File" %>
<%@ page import="org.apache.commons.io.FileUtils" %>
<%@ page import="com.openkm.core.Config" %>
<%@ page import="com.openkm.core.SessionManager" %>
<%@ page import="com.openkm.util.FormatUtil"%>
<%@ page import="com.openkm.util.impexp.RepositoryImporter" %>
<%@ page import="com.openkm.util.impexp.HTMLInfoDecorator" %>
<%@ page import="com.openkm.util.impexp.ImpExpStats"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="Shortcut icon" href="favicon.ico" />
  <link rel="stylesheet" type="text/css" href="css/style.css" />
  <title>Repository Import</title>
</head>
<body>
<%
	if (request.isUserInRole(Config.DEFAULT_ADMIN_ROLE)) {
		request.setCharacterEncoding("UTF-8");
		String repoPath = request.getParameter("repoPath");
		String fsPath = request.getParameter("fsPath");
		
		if (repoPath != null && !repoPath.equals("")) {
			repoPath = new String(repoPath.getBytes("ISO-8859-1"), "UTF-8");
		}
		
		if (fsPath != null && !fsPath.equals("")) {
			fsPath = new String(fsPath.getBytes("ISO-8859-1"), "UTF-8");
		}
		
		out.println("<h1>Repository import</h1>");
		out.println("<form action=\"repository_import.jsp\">");
		out.println("<table class=\"form\" align=\"center\">");
		out.println("<tr>");
		out.println("<td>Filesystem path</td>");
		out.println("<td><input type=\"text\" size=\"50\" name=\"fsPath\" value=\""+(fsPath==null?"":fsPath)+"\" ></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td>Repository path</td>");
		out.println("<td><input type=\"text\" size=\"50\" name=\"repoPath\" value=\""+(repoPath==null?"/okm:root":repoPath)+"\" ></td>");
		out.println("</td>");
		out.println("</tr>");
		out.println("<tr><td colspan=\"2\" align=\"right\">");
		out.println("<input type=\"submit\" value=\"Send\">");
		out.println("</td></tr>");
		out.println("</table>");
		out.println("</form>");

		try {
			if (repoPath != null && !repoPath.equals("") && fsPath != null && !fsPath.equals("")) {
				SessionManager sm = SessionManager.getInstance();
				String token = sm.getTokenByUserId(Config.SYSTEM_USER);
				out.println("<hr/>");
				File dir = new File(fsPath);
				int docs = FileUtils.listFiles(dir, null, true).size();
				long begin = System.currentTimeMillis();
				ImpExpStats stats = RepositoryImporter.importDocuments(token, dir, repoPath, out, new HTMLInfoDecorator(docs));
				long end = System.currentTimeMillis();
				out.println("<hr/>");
				out.println("<div class=\"ok\">Filesystem '"+new File(fsPath).getAbsolutePath()+"' imported into '"+repoPath+"'</div>");
				out.println("<br/>");
				out.println("<b>Documents:</b> "+stats.getDocuments()+"<br/>");
				out.println("<b>Folders:</b> "+stats.getFolders()+"<br/>");
				out.println("<b>Size:</b> "+FormatUtil.formatSize(stats.getSize())+"<br/>");
				out.println("<b>Time:</b> "+FormatUtil.formatSeconds(end - begin)+"<br/>");
			}
		} catch (Exception e) {
			out.println("<div class=\"error\">"+e.getMessage()+"<div>");
		}
	} else {
		out.println("<div class=\"error\"><h3>Only admin users allowed</h3></div>");
	}
%>
</body>
</html>