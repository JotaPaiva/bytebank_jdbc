package br.com.alura.bytebank.domain.application;

import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;
import br.com.alura.bytebank.domain.conta.ContaService;
import br.com.alura.bytebank.domain.conta.DadosAberturaConta;

import java.sql.SQLException;
import java.util.Scanner;

public class BytebankApplication {

    private static ContaService service = new ContaService();
    private static Scanner teclado = new Scanner(System.in).useDelimiter("\n");

    public static void exibirMenu() {

        int opcao = 0;

        while (opcao != 8) {

            System.out.print(
                    "\n===============================\n" +
                            "Bem vindo ao ByteBank - Opções:\n" +
                            "===============================\n" +
                            "\n1 - Listar contas abertas\n" +
                            "2 - Abertura de conta\n" +
                            "3 - Encerramento de conta\n" +
                            "4 - Consultar saldo de uma conta\n" +
                            "5 - Realizar saque em uma conta\n" +
                            "6 - Realizar depósito em uma conta\n" +
                            "7 - Realizar transferência para uma conta\n" +
                            "8 - Sair\n" +
                            "\nEscolha a opção desejada: "
            );

            opcao = teclado.nextInt();

            try {
                switch (opcao) {
                    case 1:
                        listarContas();
                        break;
                    case 2:
                        abrirConta();
                        break;
                    case 3:
                        encerrarConta();
                        break;
                    case 4:
                        consultarSaldo();
                        break;
                    case 5:
                        realizarSaque();
                        break;
                    case 6:
                        realizarDeposito();
                        break;
                    case 7:
                        realizarTransferencia();
                    default:
                        System.out.println("\nObrigado por usar o Bytebank, até logo!");
                        break;
                }

            } catch (RegraDeNegocioException | SQLException e) {
                System.out.println("\nErro: " +e.getMessage());
                System.out.print("\nInsira qualquer valor para voltar ao menu: ");
                teclado.next();
            }

        }

    }

    private static void listarContas() throws SQLException {

        System.out.println("\nExibindo contas cadastradas: \n");
        var contas = service.listarContasAbertas();
        contas.stream().forEach(System.out::println);

        System.out.print("\nInsira qualquer valor para voltar ao menu: ");
        teclado.next();

    }

    private static void abrirConta() {

        System.out.print("\nDigite o número da conta: ");
        var numeroDaConta = teclado.nextInt();

        System.out.print("Digite o nome do cliente: ");
        var nome = teclado.next();

        System.out.print("Digite o CPF do cliente: ");
        var cpf = teclado.next();

        System.out.print("Digite o e-mail do cliente: ");
        var email = teclado.next();

        service.abrir(new DadosAberturaConta(numeroDaConta, new DadosCadastroCliente(nome, cpf, email)));

        System.out.println("\nConta aberta com sucesso!");

        System.out.print("\nInsira qualquer valor para voltar ao menu: ");
        teclado.next();

    }

    private static void encerrarConta() {

        System.out.print("\nDigite o número da conta: ");
        var numeroDaConta = teclado.nextInt();

        service.encerrar(numeroDaConta);

        System.out.println("\nConta encerrada com sucesso!");

        System.out.print("\nInsira qualquer valor para voltar ao menu: ");
        teclado.next();

    }

    private static void consultarSaldo() {

        System.out.print("\nDigite o número da conta: ");
        var numeroDaConta = teclado.nextInt();

        var saldo = service.consultarSaldo(numeroDaConta);

        System.out.print("\nSaldo da conta: R$" + saldo);

        System.out.print("\nInsira qualquer valor para voltar ao menu: ");
        teclado.next();

    }

    private static void realizarSaque() {

        System.out.print("\nDigite o número da conta: ");
        var numeroDaConta = teclado.nextInt();

        System.out.print("Digite o valor do saque: R$");
        var valor = teclado.nextBigDecimal();

        service.realizarSaque(numeroDaConta, valor);
        System.out.println("\nSaque realizado com sucesso!");

        System.out.print("\nInsira qualquer valor para voltar ao menu: ");
        teclado.next();

    }

    private static void realizarDeposito() {

        System.out.print("\nDigite o número da conta: ");
        var numeroDaConta = teclado.nextInt();

        System.out.print("Digite o valor do depósito: R$");
        var valor = teclado.nextBigDecimal();

        service.realizarDeposito(numeroDaConta, valor);

        System.out.println("\nDepósito realizado com sucesso!");

        System.out.print("\nInsira qualquer valor para voltar ao menu: ");
        teclado.next();

    }

    private static void realizarTransferencia() {

        System.out.print("\nDigite o número da conta de origem: ");
        var numeroDaContaOrigem = teclado.nextInt();

        System.out.print("Digite o número da conta de destino: ");
        var numeroDaContaDestino = teclado.nextInt();

        System.out.print("Digite o valor da transferência: R$");
        var valor = teclado.nextBigDecimal();

        service.realizarTransferencia(numeroDaContaOrigem,numeroDaContaDestino,valor);

        System.out.println("\nTransferência realizada com sucesso!");

        System.out.print("\nInsira qualquer valor para voltar ao menu: ");
        teclado.next();

    }

}
