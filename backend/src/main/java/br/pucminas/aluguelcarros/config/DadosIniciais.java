package br.pucminas.aluguelcarros.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.pucminas.aluguelcarros.model.Agente;
import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.model.TipoAgente;
import br.pucminas.aluguelcarros.model.TipoProprietario;
import br.pucminas.aluguelcarros.repository.AgenteRepository;
import br.pucminas.aluguelcarros.repository.AutomovelRepository;

/**
 * Roda uma vez quando o sistema inicia.
 * Se o banco estiver vazio, cria 2 agentes (uma empresa e um banco) e alguns carros,
 * para dar para testar o sistema logo de cara.
 *
 * Logins criados (senha 123456):
 *   locadora -> agente do tipo EMPRESA
 *   banco    -> agente do tipo BANCO
 */
@Component
public class DadosIniciais implements CommandLineRunner {

    private final AgenteRepository agenteRepository;
    private final AutomovelRepository automovelRepository;
    private final PasswordEncoder passwordEncoder;

    public DadosIniciais(AgenteRepository agenteRepository, AutomovelRepository automovelRepository,
                         PasswordEncoder passwordEncoder) {
        this.agenteRepository = agenteRepository;
        this.automovelRepository = automovelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (agenteRepository.count() == 0) {
            criarAgente("locadora", "Locadora Minas", "11.111.111/0001-11", TipoAgente.EMPRESA);
            criarAgente("banco", "Banco Horizonte", "22.222.222/0001-22", TipoAgente.BANCO);
        }

        if (automovelRepository.count() == 0) {
            criarAutomovel("MAT-0001", 2022, "Fiat", "Argo", "ABC1D23");
            criarAutomovel("MAT-0002", 2023, "Volkswagen", "Polo", "DEF4G56");
            criarAutomovel("MAT-0003", 2021, "Chevrolet", "Onix", "HIJ7K89");
            criarAutomovel("MAT-0004", 2024, "Toyota", "Corolla", "LMN0P12");
        }
    }

    private void criarAgente(String login, String nome, String cnpj, TipoAgente tipo) {
        Agente agente = new Agente();
        agente.setLogin(login);
        agente.setSenha(passwordEncoder.encode("123456"));
        agente.setNome(nome);
        agente.setCnpj(cnpj);
        agente.setTipo(tipo);
        agenteRepository.save(agente);
    }

    private void criarAutomovel(String matricula, int ano, String marca, String modelo, String placa) {
        Automovel automovel = new Automovel();
        automovel.setMatricula(matricula);
        automovel.setAno(ano);
        automovel.setMarca(marca);
        automovel.setModelo(modelo);
        automovel.setPlaca(placa);
        automovel.setProprietario(TipoProprietario.EMPRESA);
        automovelRepository.save(automovel);
    }
}
