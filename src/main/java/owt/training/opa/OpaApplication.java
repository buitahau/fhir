package owt.training.opa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableFeignClients
@EnableMethodSecurity
public class OpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpaApplication.class, args);
	}

}
