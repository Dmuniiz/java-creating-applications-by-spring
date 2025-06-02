package Revisao;

import java.util.Arrays;
import java.util.List;


interface OperacaoMatematica{
    int calcular(int a, int b);
}

public class LambdasFunctions {

    public static void main(String[] args) {

        List<String> palavras = Arrays.asList("Davy","Maria","Valquiria","Cicero");

        List<String> palavrasFiltradas = palavras.stream().
                filter(palavra -> palavra.length() > 5).toList();

        palavrasFiltradas.forEach(System.out::println);
        palavrasFiltradas.forEach(palavraMaiorCinco -> System.out.println(palavraMaiorCinco));

        for(String palavrasMaiorCinco: palavrasFiltradas){
            System.out.println(palavrasMaiorCinco);
        }

        OperacaoMatematica soma = new OperacaoMatematica() {
            @Override
            public int calcular(int a, int b) {
                return a + b;
            }
        };

        //soma.calcular()

        OperacaoMatematica somaFL = (a, b) -> a + b;
        System.out.println(somaFL.calcular(1, 2));

    }

}
