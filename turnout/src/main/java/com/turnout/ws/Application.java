package com.turnout.ws;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application class is a root class of all other classes. This class contains main method Fiternity application.
 * 
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Application extends SpringBootServletInitializer {

	/**
	 * A main method is a starting point and configure the Fiternity application.
	 * Normally all you would need to do it add sources (e.g. config classes) because other settings have sensible defaults.
	 * 
	 * @param args Not used
	 */
	public static void main(String[] args) {
		new Application()
		.configure(new SpringApplicationBuilder(Application.class))
		.run(args);
	}
	
	/**
	 * Configure the application. Normally all you would need to do it add sources (e.g. config classes) because other settings have sensible defaults.
	 * 
	 * @param application a builder for the application context.
	 * @return the application builder.
	 * @see SpringApplicationBuilder.
	 */
	@Override
	protected final SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

}
