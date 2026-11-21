package br.com.gabriela.safra_control;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
		info = @Info(
				title = "SafraControl API",
				version = "1.0.0",
				description = "API REST para gerenciamento de safras, " +
						"gastos agrícolas e relatórios financeiros."
		)
)

@SpringBootApplication
public class SafraControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafraControlApplication.class, args);
	}

}
