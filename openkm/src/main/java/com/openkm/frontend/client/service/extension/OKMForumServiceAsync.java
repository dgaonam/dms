/**
 *  OpenKM, Open Document Management System (http://www.openkm.com)
 *  Copyright (c) 2006-2011  Paco Avila & Josep Llort
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

package com.openkm.frontend.client.service.extension;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.openkm.frontend.client.bean.extension.GWTForumPost;
import com.openkm.frontend.client.bean.extension.GWTForumTopic;

/**
 * OKMForumServiceAsync
 * 
 * @author jllort
 *
 */
public interface OKMForumServiceAsync extends RemoteService {
	public void getTopicsByUuid(String uuid, AsyncCallback<List<GWTForumTopic>> callback);
	public void createTopic(int id, String uuid, GWTForumTopic topic, AsyncCallback<GWTForumTopic> callback);
	public void findTopicByPK(int id, AsyncCallback<GWTForumTopic> callback);
	public void createPost(int id, GWTForumPost post, AsyncCallback<?> callback);
	public void increaseTopicView(int id, AsyncCallback<?> callback);
	public void increateTopicReplies(int id, AsyncCallback<?> callback);
}