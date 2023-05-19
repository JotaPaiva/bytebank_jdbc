package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.cliente.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContaDAO {

    private Connection con;

    ContaDAO(Connection connection) {
        this.con = connection;
    }

    public void salvar(DadosAberturaConta dadosDaConta) {

        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), cliente);

        String sql = "INSERT INTO CONTA (NUMERO, SALDO, CLIENTE_NOME, CLIENTE_CPF, CLIENTE_EMAIL)" +
                "VALUES (?, ?, ?, ?, ?);";

        try {

            PreparedStatement preparedStatement = con.prepareStatement(sql);

            preparedStatement.setInt(1,conta.getNumero());
            preparedStatement.setDouble(2,0.0);
            preparedStatement.setString(3,dadosDaConta.dadosCliente().nome());
            preparedStatement.setString(4,dadosDaConta.dadosCliente().cpf());
            preparedStatement.setString(5,dadosDaConta.dadosCliente().email());

            preparedStatement.execute();

            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
