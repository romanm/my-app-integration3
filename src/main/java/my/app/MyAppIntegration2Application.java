package my.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableConfigurationProperties(HelloWorldMessageObject.class)
@ImportResource("classpath:config-app-spring.xml")
public class MyAppIntegration2Application {

	public static void main(String[] args) {
		SpringApplication.run(MyAppIntegration2Application.class, args);
	}
	
}
