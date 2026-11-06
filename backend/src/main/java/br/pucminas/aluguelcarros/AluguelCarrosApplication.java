package br.pucminas.aluguelcarros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Classe principal: é por aqui que o Spring Boot inicia o sistema
@SpringBootApplication
public class AluguelCarrosApplication {

    public static void main(String[] args) {
        SpringApplication.run(AluguelCarrosApplication.class, args);
    }
}
