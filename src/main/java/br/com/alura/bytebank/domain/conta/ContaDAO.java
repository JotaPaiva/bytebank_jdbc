package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ContaDAO {

    private Connection con;

    ContaDAO(Connection connection) {
        this.con = connection;
    }

    public void salvar(DadosAberturaConta dadosDaConta) throws SQLException {

        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), cliente);

        PreparedStatement ps = null;

        String sql = "INSERT INTO CONTA (NUMERO, SALDO, CLIENTE_NOME, CLIENTE_CPF, CLIENTE_EMAIL)" +
                "VALUES (?, ?, ?, ?, ?);";

        try {

            ps = con.prepareStatement(sql);

            ps.setInt(1,conta.getNumero());
            ps.setDouble(2,0.0);
            ps.setString(3,dadosDaConta.dadosCliente().nome());
            ps.setString(4,dadosDaConta.dadosCliente().cpf());
            ps.setString(5,dadosDaConta.dadosCliente().email());

            ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            con.close();
            ps.close();
        }

    }

    public Set<Conta> listar() throws SQLException {

        Set<Conta> contas = new HashSet<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM CONTA";

        try {

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while(rs.next()) {

                Integer numero = rs.getInt(1);
                double saldo = rs.getDouble(2);
                String nome = rs.getString(3);
                String cpf = rs.getString(4);
                String email = rs.getString(5);

                DadosCadastroCliente dadosCadastroCliente = new DadosCadastroCliente(nome,cpf,email);
                Cliente cliente = new Cliente(dadosCadastroCliente);
                contas.add(new Conta (numero,cliente));

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            con.close();
            rs.close();
            ps.close();
        }

        return contas;

    }


}
