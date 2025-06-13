package br.com.alura.screensound.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "musicas")
public class Musica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @ManyToOne
    private Artista artista;

    public Musica(){}

    public Musica(String nomeMusica) {
        this.titulo = nomeMusica;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    @Override
    public String toString() {
        return titulo + ", " + "\n";
    }
}
