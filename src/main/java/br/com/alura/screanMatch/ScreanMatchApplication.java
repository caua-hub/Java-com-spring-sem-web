package br.com.alura.screanMatch;

import br.com.alura.screanMatch.model.DadosSerie;
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
        System.out.println("Hello World");
        var consumoApi = new ConsumoApi();
        var json = consumoApi.obterDados("https://www.omdbapi.com/?t=the+good+place&apikey=e7ec0ba5");
        System.out.println(json);
        var converteDados = new ConverteDados();
        var n =  converteDados.obterDados(json, DadosSerie.class);
        System.out.println(n);

    }
}
