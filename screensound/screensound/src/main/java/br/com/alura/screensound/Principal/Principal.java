package br.com.alura.screensound.Principal;

import br.com.alura.screensound.Model.Artista;
import br.com.alura.screensound.Model.Musica;
import br.com.alura.screensound.Model.TipoArtista;
import br.com.alura.screensound.repository.ArtistaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private final ArtistaRepository repositorio;
    private Scanner leitura = new Scanner(System.in);

    public Principal(ArtistaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;

        while (opcao!= 9) {
            var menu = """
                    *** Screen Sound Músicas ***                    
                    
                    1- Cadastrar artistas
                    2- Cadastrar músicas
                    3- Listar músicas
                    4- Buscar músicas por artistas
                    
                    9 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarArtistas();
                    break;
                case 2:
                    cadastrarMusicas();
                    break;
                case 3:
                    listarMusicas();
                    break;
                case 4:
                    buscarMusicasPorArtista();
                    break;
                /*case 5:
                    pesquisarDadosDoArtista();
                    break;*/
                case 9:
                    System.out.println("Encerrando a aplicação!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    /*private void pesquisarDadosDoArtista() {
    }*/

    private void buscarMusicasPorArtista() {
        System.out.println("Buscar músicas de que artista? ");
        String nome = leitura.nextLine();

        List<Musica> musicas = repositorio.buscaMusicaPorArtista(nome);
        musicas.forEach(System.out::println);
    }

    private void listarMusicas() {
        List<Artista> artistas = repositorio.findAll();
        //artistas.forEach(a -> a.getMusicas().forEach(System.out::println));
        artistas.forEach(System.out::println);
    }

    //salvar musica no artista
    private void cadastrarMusicas() {
        System.out.println("Cadastrar música de que artista? ");
        var nome = leitura.nextLine();

        //busca o artista
        Optional<Artista> artista = repositorio.findByNomeContainingIgnoreCase(nome);


        if(artista.isPresent()){
            System.out.println("Informe o título da música: ");
            var nomeMusica = leitura.nextLine();
            Musica musica = new Musica(nomeMusica);

            //mapeia o artista na musica
            musica.setArtista(artista.get());

            artista.get().getMusicas().add(musica);

            repositorio.save(artista.get());
        }else{
            System.out.println("Artista não encontrado");
        }
    }

    private void cadastrarArtistas() {
        var cadastrarNovo = "S";

        while(cadastrarNovo.equalsIgnoreCase("S")) {
            System.out.println("Informe o nome desse artista: ");
            var nome = leitura.nextLine();

            System.out.println("Informe o tipo desse artista(solo,dupla ou banda)");
            var tipo = leitura.nextLine();

            TipoArtista tipoArtista = TipoArtista.valueOf(tipo.toUpperCase());
            Artista artista = new Artista(nome, tipoArtista);
            repositorio.save(artista);

            System.out.println("Cadastrar novo artista?(S/N)");
            cadastrarNovo = leitura.nextLine();
        }

    }

}
