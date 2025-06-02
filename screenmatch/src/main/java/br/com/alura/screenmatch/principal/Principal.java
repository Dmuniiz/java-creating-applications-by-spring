package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=537c355c";
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu() {

        var menu = """
                1 - Buscar séries
                2 - Buscar temporadas infos > série
                3 - Buscar episodio temporada + ano 
                4 - Buscar por episdio de uma temporada titulo 
                5 - Resumo de estatistica da temporada
                
                0 - Sair                                 
                """;

        System.out.println(menu);
        var opcao = leitura.nextInt();
        leitura.nextLine();

        switch (opcao) {
            case 1:
                buscarSerieWeb();
                break;
            case 2:
                buscarTemporadaPorSerie();
                break;
            case 3:
                buscarEpisodioPorAno();
                break;
            case 4:
                buscarEpisodioPorTrecho();
                break;
            case 5:
                ResumoEstatisticasTemporadas();
                break;
            case 0:
                System.out.println("Saindo...");
                break;
            default:
                System.out.println("Opção inválida");
        }
    }

        /* TRABALHANDO COM EPISODIOS

         List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                 .flatMap(t -> t.episodios().stream())
                 .collect(Collectors.toList());

         dadosEpisodios.stream()
                 .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                 .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                 .limit(10)
                 //.peek(e -> System.out.println(""))
                 .map(e -> e.titulo().toUpperCase())
                 .forEach(System.out::println); //e -> System.out.println(e)
     */

    private void buscarSerieWeb(){
        DadosSerie dados = getDadosSerie();
        System.out.println(dados);
    }

    private void buscarEpisodioPorAno(){
        var obterEpisodios = getDadosEpisodio();

        System.out.println("A partir de que ANO você deseja ver os episódios? ");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        obterEpisodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Episódio: " + e.getTitulo() +
                                " Data de lancamento: " + e.getDataLancamento().format(formatador)
                ));
    }

    private void buscarEpisodioPorTrecho(){
        var obterEpisodios = getDadosEpisodio();

        System.out.println("Digite um trecho do titulo do episódio");
        var trechoTitulo = leitura.nextLine();

        Optional<Episodio> episodioBuscado = obterEpisodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if(episodioBuscado.isPresent()){
            System.out.println("Episódio Encontrado!");
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
            System.out.println("Nome: " + episodioBuscado.get().getTitulo());
        }else{
            System.out.println("Episódio não encontrado");
        }
    }

    private void buscarTemporadaPorSerie(){
        getTemporadaPorSerie().forEach(System.out::println);
    }

    private List<DadosTemporada> getTemporadaPorSerie(){
        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for(int i = 1; i <= dadosSerie.totalTemporadas(); i++){
            var json = consumo.obterJson(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        return temporadas;
    }

    private DadosSerie getDadosSerie(){
        System.out.println("Digite o nome da série para busca");
        String nomeSerie = leitura.nextLine();
        String json = consumo.obterJson(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private List<Episodio> getDadosEpisodio(){
        List<Episodio> episodios = getTemporadaPorSerie().stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());
        return episodios;
    }

    private void ResumoEstatisticasTemporadas(){
        var obterEpisodios = getDadosEpisodio();
        obterEpisodios.forEach(System.out::println);

        //assiação de valores com diferentes tipos
        /*Map<Integer, Double> avaliacoesPorTemporada = obterEpisodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);*/

        DoubleSummaryStatistics est = obterEpisodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("\nMédia: " + est.getAverage());
        System.out.println("Melhor episódio: " + est.getMax());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());
    }

}
