package com.guestbook;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class GuestbookModule extends ServletModule {
	@Override
	protected void configureServlets() {
		Map<String, String> initParams = new HashMap<String, String>();
//		initParams.put(ResourceConfig.FEATURE_TRACE, "true");
		initParams.put(GuiceContainer.JSP_TEMPLATES_BASE_PATH, "/WEB-INF/jsp");
		initParams
				.put(GuiceContainer.PROPERTY_WEB_PAGE_CONTENT_REGEX,
						"(/(_ah|appstats|mapreduce)/?.*)|(/.*\\.jsp)|(/WEB-INF/.*\\.jsp)|(/favicon\\.ico)");

		filterRegex("/(?!_ah).*").through(GuiceContainer.class, initParams);
		bind(GuestbookResource.class);
	}
}
