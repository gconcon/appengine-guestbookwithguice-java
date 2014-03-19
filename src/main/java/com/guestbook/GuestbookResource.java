/**
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.guestbook;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.sun.jersey.api.view.Viewable;

@Path("/")
public class GuestbookResource {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response sayHello(
			@Context HttpServletRequest request,
			@QueryParam("guestbookName") @DefaultValue("default") String guestbookName) {
		Map<String, Object> model = buildModel(request, guestbookName);
		return Response.ok(new Viewable("/guestbook", model)).build();
	}

	@POST
	@Path("/sign")
	@Produces(MediaType.TEXT_HTML)
	public Response sign(
			@Context HttpServletRequest request,
			@FormParam("guestbookName") @DefaultValue("default") String guestbookName,
			@FormParam("content") String content) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);
		Entity greeting = new Entity("Greeting", guestbookKey);
		greeting.setProperty("user", user);
		greeting.setProperty("date", new Date());
		greeting.setProperty("content", content);

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		datastore.put(greeting);

		Map<String, Object> model = buildModel(request, guestbookName);
		return Response.ok(new Viewable("/guestbook", model)).build();
	}

	private Map<String, Object> buildModel(HttpServletRequest request,
			String guestbookName) {
		UserService userService = UserServiceFactory.getUserService();
		User currentUser = userService.getCurrentUser();

		List<Entity> greetings = listGreetings(guestbookName);

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("guestbookName", guestbookName);
		model.put("greetings", greetings);
		if (currentUser != null) {
			model.put("user", currentUser);
			model.put("logoutURL",
					userService.createLogoutURL(request.getRequestURI()));
		} else {
			model.put("loginURL",
					userService.createLoginURL(request.getRequestURI()));
		}
		return model;
	}

	private List<Entity> listGreetings(String guestbookName) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);
		// Run an ancestor query to ensure we see the most up-to-date
		// view of the Greetings belonging to the selected Guestbook.
		Query query = new Query("Greeting", guestbookKey).addSort("date",
				Query.SortDirection.DESCENDING);
		List<Entity> greetings = datastore.prepare(query).asList(
				FetchOptions.Builder.withLimit(5));
		return greetings;
	}
}
