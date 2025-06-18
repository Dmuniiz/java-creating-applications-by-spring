package br.com.alura.screenfrases.repository;

import br.com.alura.screenfrases.model.Frase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FraseRepository extends JpaRepository<Frase, Long> {

    @Query("SELECT f FROM Frase f order by function('RANDOM') LIMIT 1")
    Frase buscaFraseAleatoria();

}
