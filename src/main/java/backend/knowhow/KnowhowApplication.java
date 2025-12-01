package backend.knowhow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KnowhowApplication {

	public static void main(String[] args) {
		SpringApplication.run(KnowhowApplication.class, args);
	}

}
