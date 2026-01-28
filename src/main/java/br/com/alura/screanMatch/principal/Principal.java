package br.com.alura.screanMatch.principal;

import br.com.alura.screanMatch.model.DadosEpisodio;
import br.com.alura.screanMatch.model.DadosSerie;
import br.com.alura.screanMatch.model.DadosTemporada;
import br.com.alura.screanMatch.model.Episodio;
import br.com.alura.screanMatch.service.ConsumoApi;
import br.com.alura.screanMatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner input = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados converteDados = new ConverteDados();
    private final String  ENDERECO = "https://www.omdbapi.com/?t=";
    private final String  API_KEY = "&apikey=e7ec0ba5";
    private List<DadosTemporada> temporadas = new ArrayList<>();

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
        var objeto =  converteDados.obterDados(json, DadosSerie.class);
        System.out.println(objeto);



        for(int i = 1; i <= objeto.totalDeTemporadas(); i++) {
            json = consumoApi.obterDados ( ENDERECO + nomeBusca.strip().replaceAll("\\s+", " ").replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = converteDados.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);

        }
        temporadas.forEach(System.out::println);

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\n Top 5 episódios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios =  temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("Digite um trecho do episodio que sera buscado: ");
        var episodioBuscado = input.nextLine();
        Optional<Episodio> episodioBusca = episodios.stream()
                        .filter(t -> t.getTitulo().toLowerCase().contains(episodioBuscado.toLowerCase()))
                                .findFirst();
        if(episodioBusca.isPresent()) {
            System.out.println("Nome: " + episodioBusca.get().getTitulo() + "\n" + "Temporada: " + episodioBusca.get().getTemporada() + "\n" + "numero: " + episodioBusca.get().getNumero());
        } else {
            System.out.println("Nenhum episodio encontrado");
        }

        System.out.println("A partir de que ano voce deseja ver os episodios?");
        var ano = input.nextInt();
        input.nextLine();

        LocalDate databusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(databusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Episodio: " + e.getTitulo() +
                                " Data de Lancamento: " + e.getDataLancamento().format(formatter)
                ));

        Map<Integer, Double> avaliacoesTemporadas = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesTemporadas);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println(est);
    }
}