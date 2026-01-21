package br.com.alura.screanMatch.principal;

import br.com.alura.screanMatch.model.DadosSerie;
import br.com.alura.screanMatch.service.ConsumoApi;
import br.com.alura.screanMatch.service.ConverteDados;

import java.util.Scanner;

public class Principal {
    private Scanner input = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados converteDados = new ConverteDados();
    private final String  ENDERECO = "https://www.omdbapi.com/?t=";
    private final String  API_KEY = "&apikey=e7ec0ba5";

    public void  menu() {

        System.out.println("Digite o nome da serie para busca:");
        String nomeBusca = input.nextLine();
        String endereco =  ENDERECO +
                nomeBusca
                        .strip()
                        .replaceAll("\\s+", " ")
                        .replace(" ", "+")
                + API_KEY;
        var json = consumoApi.obterDados(endereco);
        var n =  converteDados.obterDados(json, DadosSerie.class);
        System.out.println(n);
    }
}