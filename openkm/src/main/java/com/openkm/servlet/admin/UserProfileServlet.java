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
import java.security.NoSuchAlgorithmException;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openkm.core.DatabaseException;
import com.openkm.dao.UserProfileDAO;
import com.openkm.dao.bean.UserProfile;
import com.openkm.util.JCRUtils;
import com.openkm.util.UserActivity;
import com.openkm.util.WebUtil;

/**
 * User twitter accounts servlet
 */
public class UserProfileServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(UserProfileServlet.class);
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException {
		log.debug("doGet({}, {})", request, response);
		request.setCharacterEncoding("UTF-8");
		String action = WebUtil.getString(request, "action");
		Session session = null;
		updateSessionManager(request);
		
		try {
			session = JCRUtils.getSession();
			
			if (action.equals("create")) {
				create(session, request, response);
			} else if (action.equals("edit")) {
				edit(session, request, response);
			} else if (action.equals("delete")) {
				delete(session, request, response);
			}
			
			if (action.equals("") || WebUtil.getBoolean(request, "persist")) {
				list(session, request, response);
			}
		} catch (LoginException e) {
			log.error(e.getMessage(), e);
			sendErrorRedirect(request,response, e);
		} catch (RepositoryException e) {
			log.error(e.getMessage(), e);
			sendErrorRedirect(request,response, e);
		} catch (DatabaseException e) {
			log.error(e.getMessage(), e);
			sendErrorRedirect(request,response, e);
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
			sendErrorRedirect(request,response, e);
		} finally {
			JCRUtils.logout(session);
		}
	}
	
	/**
	 * New user
	 */
	private void create(Session session, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException, DatabaseException {
		log.debug("create({}, {}, {})", new Object[] { session, request, response });
		
		if (WebUtil.getBoolean(request, "persist")) {
			UserProfile up = new UserProfile();
			up.setId(WebUtil.getInt(request, "up_id"));
			up.setName(WebUtil.getString(request, "up_name"));
			up.setActive(WebUtil.getBoolean(request, "up_active"));
			up.setUserQuota(WebUtil.getLong(request, "up_user_quota"));
			up.setAdvancedFilters(WebUtil.getBoolean(request, "up_advanced_filter"));
			up.setWizardPropertyGroups(WebUtil.getString(request, "up_wizard_property_groups"));
			up.setWizardKeywords(WebUtil.getBoolean(request, "up_wizard_keywords"));
			up.setWizardCategories(WebUtil.getBoolean(request, "up_wizard_categories"));
			up.setChatEnabled(WebUtil.getBoolean(request, "up_chat_enabled"));
			up.setChatAutoLogin(WebUtil.getBoolean(request, "up_chat_auto_login"));
			up.setStackCategoriesVisible(WebUtil.getBoolean(request, "up_stack_categories_visible"));
			up.setStackThesaurusVisible(WebUtil.getBoolean(request, "up_stack_thesaurus_visible"));
			up.setStackPersonalVisible(WebUtil.getBoolean(request, "up_stack_personal_visible"));
			up.setStackMailVisible(WebUtil.getBoolean(request, "up_stack_mail_visible"));
			up.setMenuEditVisible(WebUtil.getBoolean(request, "up_menu_edit_visible"));
			up.setMenuToolsVisible(WebUtil.getBoolean(request, "up_menu_tools_visible"));
			up.setMenuBookmarksVisible(WebUtil.getBoolean(request, "up_menu_bookmarks_visible"));
			up.setMenuHelpVisible(WebUtil.getBoolean(request, "up_menu_help_visible"));
			up.setTabDesktopVisible(WebUtil.getBoolean(request, "up_tab_desktop_visible"));
			up.setTabSearchVisible(WebUtil.getBoolean(request, "up_tab_search_visible"));
			up.setTabDashboardVisible(WebUtil.getBoolean(request, "up_tab_dashboard_visible"));
			up.setDashboardUserVisible(WebUtil.getBoolean(request, "up_dashboard_user_visible"));
			up.setDashboardMailVisible(WebUtil.getBoolean(request, "up_dashboard_mail_visible"));
			up.setDashboardNewsVisible(WebUtil.getBoolean(request, "up_dashboard_news_visible"));
			up.setDashboardGeneralVisible(WebUtil.getBoolean(request, "up_dashboard_general_visible"));
			up.setDashboardWorkflowVisible(WebUtil.getBoolean(request, "up_dashboard_workflow_visible"));
			up.setDashboardKeywordsVisible(WebUtil.getBoolean(request, "up_dashboard_keywords_visible"));
			
			UserProfileDAO.create(up);
			
			// Activity log
			UserActivity.log(session.getUserID(), "ADMIN_USER_PROFILE_CREATE", null, up.toString());
		} else {
			ServletContext sc = getServletContext();
			UserProfile up = new UserProfile();
			sc.setAttribute("action", WebUtil.getString(request, "action"));
			sc.setAttribute("persist", true);
			sc.setAttribute("up", up);
			sc.getRequestDispatcher("/admin/user_profile_edit.jsp").forward(request, response);
		}
		
		log.debug("create: void");
	}
	
	/**
	 * Edit user
	 */
	private void edit(Session session, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException, DatabaseException, NoSuchAlgorithmException {
		log.debug("edit({}, {}, {})", new Object[] { session, request, response });
		
		if (WebUtil.getBoolean(request, "persist")) {
			UserProfile up = new UserProfile();
			up.setId(WebUtil.getInt(request, "up_id"));
			up.setName(WebUtil.getString(request, "up_name"));
			up.setActive(WebUtil.getBoolean(request, "up_active"));
			up.setUserQuota(WebUtil.getLong(request, "up_user_quota"));
			up.setAdvancedFilters(WebUtil.getBoolean(request, "up_advanced_filter"));
			up.setWizardPropertyGroups(WebUtil.getString(request, "up_wizard_property_groups"));
			up.setWizardKeywords(WebUtil.getBoolean(request, "up_wizard_keywords"));
			up.setWizardCategories(WebUtil.getBoolean(request, "up_wizard_categories"));
			up.setChatEnabled(WebUtil.getBoolean(request, "up_chat_enabled"));
			up.setChatAutoLogin(WebUtil.getBoolean(request, "up_chat_auto_login"));
			up.setStackCategoriesVisible(WebUtil.getBoolean(request, "up_stack_categories_visible"));
			up.setStackThesaurusVisible(WebUtil.getBoolean(request, "up_stack_thesaurus_visible"));
			up.setStackPersonalVisible(WebUtil.getBoolean(request, "up_stack_personal_visible"));
			up.setStackMailVisible(WebUtil.getBoolean(request, "up_stack_mail_visible"));
			up.setMenuEditVisible(WebUtil.getBoolean(request, "up_menu_edit_visible"));
			up.setMenuToolsVisible(WebUtil.getBoolean(request, "up_menu_tools_visible"));
			up.setMenuBookmarksVisible(WebUtil.getBoolean(request, "up_menu_bookmarks_visible"));
			up.setMenuHelpVisible(WebUtil.getBoolean(request, "up_menu_help_visible"));
			up.setTabDesktopVisible(WebUtil.getBoolean(request, "up_tab_desktop_visible"));
			up.setTabSearchVisible(WebUtil.getBoolean(request, "up_tab_search_visible"));
			up.setTabDashboardVisible(WebUtil.getBoolean(request, "up_tab_dashboard_visible"));
			up.setDashboardUserVisible(WebUtil.getBoolean(request, "up_dashboard_user_visible"));
			up.setDashboardMailVisible(WebUtil.getBoolean(request, "up_dashboard_mail_visible"));
			up.setDashboardNewsVisible(WebUtil.getBoolean(request, "up_dashboard_news_visible"));
			up.setDashboardGeneralVisible(WebUtil.getBoolean(request, "up_dashboard_general_visible"));
			up.setDashboardWorkflowVisible(WebUtil.getBoolean(request, "up_dashboard_workflow_visible"));
			up.setDashboardKeywordsVisible(WebUtil.getBoolean(request, "up_dashboard_keywords_visible"));
			
			UserProfileDAO.update(up);
			
			// Activity log
			UserActivity.log(session.getUserID(), "ADMIN_USER_PROFILE_EDIT", Integer.toString(up.getId()), up.toString());
		} else {
			ServletContext sc = getServletContext();
			int upId = WebUtil.getInt(request, "up_id");
			sc.setAttribute("action", WebUtil.getString(request, "action"));
			sc.setAttribute("persist", true);
			sc.setAttribute("up", UserProfileDAO.findByPk(upId));
			sc.getRequestDispatcher("/admin/user_profile_edit.jsp").forward(request, response);
		}
		
		log.debug("edit: void");
	}
	
	/**
	 * Update user
	 */
	private void delete(Session session, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException, DatabaseException, NoSuchAlgorithmException {
		log.debug("delete({}, {}, {})", new Object[] { session, request, response });
		
		if (WebUtil.getBoolean(request, "persist")) {
			int upId = WebUtil.getInt(request, "up_id");
			UserProfileDAO.delete(upId);
			
			// Activity log
			UserActivity.log(session.getUserID(), "ADMIN_USER_PROFILE_DELETE", Integer.toString(upId), null);
		} else {
			ServletContext sc = getServletContext();
			int upId = WebUtil.getInt(request, "up_id");
			sc.setAttribute("action", WebUtil.getString(request, "action"));
			sc.setAttribute("persist", true);
			sc.setAttribute("up", UserProfileDAO.findByPk(upId));
			sc.getRequestDispatcher("/admin/user_profile_edit.jsp").forward(request, response);
		}
		
		log.debug("delete: void");
	}

	/**
	 * List user profiles
	 */
	private void list(Session session, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, DatabaseException {
		log.debug("list({}, {}, {})", new Object[] { session, request, response });
		ServletContext sc = getServletContext();
		sc.setAttribute("userProfiles", UserProfileDAO.findAll(false));
		sc.getRequestDispatcher("/admin/user_profile_list.jsp").forward(request, response);
		log.debug("list: void");
	}
}
