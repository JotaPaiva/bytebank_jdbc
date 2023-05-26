package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

import java.math.BigDecimal;
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

    public void salvar(DadosAberturaConta dadosDaConta) {

        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), BigDecimal.ZERO, cliente);

        PreparedStatement ps;

        String sql = "INSERT INTO conta (NUMERO, SALDO, CLIENTE_NOME, CLIENTE_CPF, CLIENTE_EMAIL)" +
                "VALUES (?, ?, ?, ?, ?);";

        try {

            ps = con.prepareStatement(sql);

            ps.setInt(1,conta.getNumero());
            ps.setBigDecimal(2,BigDecimal.ZERO);
            ps.setString(3,dadosDaConta.dadosCliente().nome());
            ps.setString(4,dadosDaConta.dadosCliente().cpf());
            ps.setString(5,dadosDaConta.dadosCliente().email());

            ps.execute();

            con.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Set<Conta> listar() {

        Set<Conta> contas = new HashSet<>();
        PreparedStatement ps;
        ResultSet rs;

        String sql = "SELECT * FROM conta";

        try {

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while(rs.next()) {

                Integer numero = rs.getInt(1);
                BigDecimal saldo = rs.getBigDecimal(2);
                String nome = rs.getString(3);
                String cpf = rs.getString(4);
                String email = rs.getString(5);

                DadosCadastroCliente dadosCadastroCliente = new DadosCadastroCliente(nome,cpf,email);
                Cliente cliente = new Cliente(dadosCadastroCliente);
                contas.add(new Conta (numero,saldo, cliente));

            }

            con.close();
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contas;

    }

    public Conta listarPorNumero(Integer numero) {

        PreparedStatement ps;
        ResultSet rs;
        Conta conta = null;

        String sql = "SELECT * FROM conta WHERE numero = ?";

        try {

            ps = con.prepareStatement(sql);
            ps.setInt(1,numero);
            rs = ps.executeQuery();

            while(rs.next()) {

                Integer numeroRecuperado = rs.getInt(1);
                BigDecimal saldo = rs.getBigDecimal(2);
                String nome = rs.getString(3);
                String cpf = rs.getString(4);
                String email = rs.getString(5);

                DadosCadastroCliente dadosCadastroCliente = new DadosCadastroCliente(nome,cpf,email);
                Cliente cliente = new Cliente(dadosCadastroCliente);
                conta = new Conta(numeroRecuperado,saldo, cliente);

            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return conta;

    }

    public void alterar(Integer numeroDaConta, BigDecimal valor) {

        PreparedStatement ps;

        String sql = "UPDATE conta SET saldo = ? WHERE numero = ?";

        try {

            ps = con.prepareStatement(sql);

            ps.setBigDecimal(1,valor);
            ps.setInt(2,numeroDaConta);

            ps.execute();

            con.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void deletar(Integer numeroDaConta) {

        PreparedStatement ps;

        String sql = "DELETE FROM conta WHERE numero = ?";

        try {

            ps = con.prepareStatement(sql);
            ps.setInt(1,numeroDaConta);
            ps.execute();

            con.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
