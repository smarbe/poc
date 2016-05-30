package lv.nixx.poc.camel.simple;

import java.util.Date;

import org.apache.camel.builder.RouteBuilder;

import lv.nixx.poc.camel.common.CamelRunner;

public class SimpleFlow {
	
	public static void main(String[] args) throws Exception {
		
		CamelRunner cr = new CamelRunner();
		
		cr.main().addRouteBuilder(new RouteBuilder() {
			
			public void configure() {
				from("timer://myTimer?period=2000")
				//.from("quartz://myTimer/abc?cron=0+45+16+?+*+*")
				.setBody().simple("A\nB\nC")
				.log("The message body before the splitter contains:\n${body}" + new Date())
				.split().tokenize("\n")
				.log("The message body after the splitter contains ${body}")
				.end();
			}
			
		});
		
		
		cr.run();
	}

}
