package study.datajpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@SpringBootApplication
// @EnableJpaRepositories(basePackages = "study.datajpa.repository") //스프링부트에서 해줌
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() { //@CreatedBy와 @LastModifiedBy를 채워줌
		//실제: SpringSecurity에서 세션정보를 꺼내와야됨
		return () -> Optional.of(UUID.randomUUID().toString());
		// return () -> Optional.of("승복이");
	}
}
