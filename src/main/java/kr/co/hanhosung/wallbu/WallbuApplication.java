package kr.co.hanhosung.wallbu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WallbuApplication {

	public static void main(String[] args) {
		SpringApplication.run(WallbuApplication.class, args);
	}

}
