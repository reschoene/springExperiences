package br.com.reschoene.JPATests;

import br.com.reschoene.JPATests.services.DbService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class JpaTestsApplication implements CommandLineRunner {

	private final DbService dbService;

	public static void main(String[] args) {
		SpringApplication.run(JpaTestsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		dbService.populateDb();
		dbService.listDb();
	}
}
