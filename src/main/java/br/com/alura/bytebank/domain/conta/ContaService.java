package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.connection.ConnectionFactory;
import br.com.alura.bytebank.domain.application.RegraDeNegocioException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaService {

    private ConnectionFactory connection;

    public ContaService() {

        this.connection = new ConnectionFactory();

    }

    private Set<Conta> contas = new HashSet<>();

    public Set<Conta> listarContasAbertas() {

        Connection con = connection.recuperaConexao();
        return new ContaDAO(con).listar();

    }

    public BigDecimal consultarSaldo(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        return conta.getSaldo();
    }

    public void abrir(DadosAberturaConta dadosDaConta) {

        Connection con = connection.recuperaConexao();
        new ContaDAO(con).salvar(dadosDaConta);

    }

    public void realizarSaque(Integer numeroDaConta, BigDecimal valor) {

        var conta = buscarContaPorNumero(numeroDaConta);

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do saque deve ser superior a zero!");
        }

        if (valor.compareTo(conta.getSaldo()) > 0) {
            throw new RegraDeNegocioException("Saldo insuficiente!");
        }

        BigDecimal novoValor = conta.getSaldo().subtract(valor);
        Connection con = connection.recuperaConexao();
        new ContaDAO(con).alterar(conta.getNumero(),novoValor);

    }

    public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) {

        var conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do deposito deve ser superior a zero!");
        }

        BigDecimal novoValor = conta.getSaldo().add(valor);
        Connection con = connection.recuperaConexao();
        new ContaDAO(con).alterar(conta.getNumero(),novoValor);

    }

    public void realizarTransferencia(Integer numeroDaContaOrigem, Integer numeroDaContaDestino, BigDecimal valor) {

        this.realizarSaque(numeroDaContaOrigem,valor);
        this.realizarDeposito(numeroDaContaDestino,valor);

    }

    public void encerrar(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (conta.possuiSaldo()) {
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
        }

        Connection con = connection.recuperaConexao();
        new ContaDAO(con).deletar(conta.getNumero());

    }

    private Conta buscarContaPorNumero(Integer numero) {

        Connection con = connection.recuperaConexao();
        Conta conta = new ContaDAO(con).listarPorNumero(numero);

        if (conta != null) {
            return conta;
        } else {
            throw new RegraDeNegocioException("Não existe conta cadastrada com este número!");
        }

    }
}
