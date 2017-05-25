package com.infotop.common;

//Import log4j classes.
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class LogTest {
	   // 在任何需要记录日志的类中   
	   private static Logger logger = LogManager.getLogger(LogTest.class);   
	  
	  

	    public static void main(final String... args) {
	 
	        // Set up a simple configuration that logs on the console.
	 
	        logger.trace("Entering application.");
	       
	        logger.error("Didn't do it."+LogTest.class.getName(),new Exception());
	        
	        logger.trace("Exiting application.");
	    } 
	  
	
}
