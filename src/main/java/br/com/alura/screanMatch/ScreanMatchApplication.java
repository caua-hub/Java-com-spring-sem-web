package br.com.alura.screanMatch;

import br.com.alura.screanMatch.model.DadosSerie;
import br.com.alura.screanMatch.principal.Principal;
import br.com.alura.screanMatch.service.ConsumoApi;
import br.com.alura.screanMatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreanMatchApplication  implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreanMatchApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {

        Principal  principal = new Principal();
        principal.menu();

    }
}
