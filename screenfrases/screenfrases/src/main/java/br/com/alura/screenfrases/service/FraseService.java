package br.com.alura.screenfrases.service;

import br.com.alura.screenfrases.DTO.FraseDTO;
import br.com.alura.screenfrases.model.Frase;
import br.com.alura.screenfrases.repository.FraseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FraseService {

    @Autowired
    private FraseRepository repositorio;

    public FraseDTO obterFraseAleatoria() {
        Frase frase = repositorio.buscaFraseAleatoria();
        return new FraseDTO(frase.getTitulo(), frase.getFrase(), frase.getPersonagem(), frase.getPoster());
    }

}
