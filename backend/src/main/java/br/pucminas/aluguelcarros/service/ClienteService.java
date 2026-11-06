package br.pucminas.aluguelcarros.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.pucminas.aluguelcarros.dto.ClienteRequest;
import br.pucminas.aluguelcarros.dto.ClienteResponse;
import br.pucminas.aluguelcarros.dto.RendimentoDTO;
import br.pucminas.aluguelcarros.exception.NaoEncontradoException;
import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.PedidoAluguel;
import br.pucminas.aluguelcarros.model.Rendimento;
import br.pucminas.aluguelcarros.model.StatusPedido;
import br.pucminas.aluguelcarros.repository.ClienteRepository;
import br.pucminas.aluguelcarros.repository.PedidoRepository;
import br.pucminas.aluguelcarros.repository.UsuarioRepository;

/**
 * Regras de negócio do CRUD de cliente.
 * O controller só recebe a requisição; quem decide o que pode ou não é o service.
 */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository clienteRepository, UsuarioRepository usuarioRepository,
                          PedidoRepository pedidoRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE
    @Transactional
    public ClienteResponse cadastrar(ClienteRequest dados) {
        if (dados.senha() == null || dados.senha().length() < 6) {
            throw new RegraNegocioException("A senha precisa ter pelo menos 6 caracteres");
        }
        if (usuarioRepository.existsByLogin(dados.login())) {
            throw new RegraNegocioException("Este login já está em uso");
        }
        if (clienteRepository.existsByCpf(dados.cpf())) {
            throw new RegraNegocioException("Este CPF já está cadastrado");
        }

        Cliente cliente = new Cliente();
        cliente.setLogin(dados.login());
        cliente.setSenha(passwordEncoder.encode(dados.senha()));
        preencherDados(cliente, dados);

        return ClienteResponse.de(clienteRepository.save(cliente));
    }

    // READ (o próprio cliente)
    @Transactional(readOnly = true)
    public ClienteResponse buscarPerfil(String login) {
        return ClienteResponse.de(buscarClientePorLogin(login));
    }

    // READ (lista para os agentes)
    @Transactional(readOnly = true)
    public List<ClienteResponse> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(ClienteResponse::de)
                .toList();
    }

    // UPDATE
    @Transactional
    public ClienteResponse atualizar(String login, ClienteRequest dados) {
        Cliente cliente = buscarClientePorLogin(login);

        boolean trocouCpf = !cliente.getCpf().equals(dados.cpf());
        if (trocouCpf && clienteRepository.existsByCpf(dados.cpf())) {
            throw new RegraNegocioException("Este CPF já está cadastrado");
        }

        // Senha só muda se o usuário digitou uma nova
        if (dados.senha() != null && !dados.senha().isBlank()) {
            if (dados.senha().length() < 6) {
                throw new RegraNegocioException("A senha precisa ter pelo menos 6 caracteres");
            }
            cliente.setSenha(passwordEncoder.encode(dados.senha()));
        }
        preencherDados(cliente, dados);

        return ClienteResponse.de(clienteRepository.save(cliente));
    }

    // DELETE
    @Transactional
    public void excluir(String login) {
        Cliente cliente = buscarClientePorLogin(login);
        List<PedidoAluguel> pedidos = pedidoRepository.findByClienteIdOrderByIdDesc(cliente.getId());

        for (PedidoAluguel pedido : pedidos) {
            if (pedido.getStatus() == StatusPedido.CONTRATADO) {
                throw new RegraNegocioException("Não é possível excluir: você possui contrato ativo");
            }
        }

        pedidoRepository.deleteAll(pedidos);
        clienteRepository.delete(cliente);
    }

    public Cliente buscarClientePorLogin(String login) {
        return clienteRepository.findByLogin(login)
                .orElseThrow(() -> new NaoEncontradoException("Cliente não encontrado"));
    }

    // Copia os campos do DTO para a entidade (usado no cadastro e na atualização)
    private void preencherDados(Cliente cliente, ClienteRequest dados) {
        cliente.setNome(dados.nome());
        cliente.setRg(dados.rg());
        cliente.setCpf(dados.cpf());
        cliente.setEndereco(dados.endereco());
        cliente.setProfissao(dados.profissao());

        List<Rendimento> rendimentos = new ArrayList<>();
        if (dados.rendimentos() != null) {
            if (dados.rendimentos().size() > Cliente.MAX_RENDIMENTOS) {
                throw new RegraNegocioException("O cliente pode ter no máximo 3 rendimentos");
            }
            for (RendimentoDTO r : dados.rendimentos()) {
                rendimentos.add(r.paraModel());
            }
        }
        cliente.getRendimentos().clear();
        cliente.getRendimentos().addAll(rendimentos);
    }
}
