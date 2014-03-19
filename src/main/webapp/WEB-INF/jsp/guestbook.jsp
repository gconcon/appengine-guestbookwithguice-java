<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.datastore.DatastoreService"%>
<%@ page
	import="com.google.appengine.api.datastore.DatastoreServiceFactory"%>
<%@ page import="com.google.appengine.api.datastore.Entity"%>
<%@ page import="com.google.appengine.api.datastore.FetchOptions"%>
<%@ page import="com.google.appengine.api.datastore.Key"%>
<%@ page import="com.google.appengine.api.datastore.KeyFactory"%>
<%@ page import="com.google.appengine.api.datastore.Query"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="java.util.List"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>

<body>

	<c:choose>
		<c:when test="${it.user != null}">
			<p>
				Hello, ${fn:escapeXml(it.user.nickname)}! (You can <a
					href="${fn:escapeXml(it.logoutURL)}">sign out</a>.)
			</p>
		</c:when>
		<c:otherwise>
			<p>
				Hello! <a href="${fn:escapeXml(it.loginURL)}">Sign in</a> to include
				your name with greetings you post.
			</p>
		</c:otherwise>
	</c:choose>

	<c:choose>
		<c:when test="${empty it.greetings}">
			<p>Guestbook '${fn:escapeXml(it.guestbookName)}' has no messages.
			</p>
		</c:when>
		<c:otherwise>
			<p>Messages in Guestbook '${fn:escapeXml(it.guestbookName)}'.</p>
			<c:forEach items="${it.greetings}" var="greeting">
				<c:choose>
					<c:when test="${greeting.properties.user == null}">
						<p>An anonymous person wrote:</p>
					</c:when>
					<c:otherwise>
						<p>
							<b>${fn:escapeXml(greeting.properties.user.nickname)}</b> wrote:
						</p>
					</c:otherwise>
				</c:choose>
				<blockquote>${fn:escapeXml(greeting.properties.content)}</blockquote>
			</c:forEach>
		</c:otherwise>
	</c:choose>

	<form action="/sign" method="post"
		enctype="application/x-www-form-urlencoded">
		<div>
			<textarea name="content" rows="3" cols="60"></textarea>
		</div>
		<div>
			<input type="submit" value="Post Greeting" />
		</div>
		<input type="hidden" name="guestbookName"
			value="${fn:escapeXml(it.guestbookName)}" />
	</form>

	<form action="/" method="get">
		<div>
			<input type="text" name="guestbookName"
				value="${fn:escapeXml(it.guestbookName)}" />
		</div>
		<div>
			<input type="submit" value="Switch Guestbook" />
		</div>
	</form>

</body>
</html>
