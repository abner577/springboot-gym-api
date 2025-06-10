package practice.spring_gym_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude =  {SecurityAutoConfiguration.class})
public class SpringGymApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringGymApiApplication.class, args);
	}

}
