package br.com.alura.bytebank.domain.application;

import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;
import br.com.alura.bytebank.domain.conta.ContaService;
import br.com.alura.bytebank.domain.conta.DadosAberturaConta;

import java.util.Scanner;

public class BytebankApplication {

    private static ContaService service = new ContaService();
    private static Scanner teclado = new Scanner(System.in).useDelimiter("\n");

    public static void exibirMenu() {

        int opcao = 0;

        while (opcao != 7) {

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
                            "7 - Sair\n" +
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
                    default:
                        break;
                }
            } catch (RegraDeNegocioException e) {
                System.out.println("Erro: " +e.getMessage());
                System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu");
                teclado.next();
            }

        }

    }

    private static void listarContas() {

        System.out.println("Contas cadastradas:");
        var contas = service.listarContasAbertas();
        contas.stream().forEach(System.out::println);

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

    }

    private static void encerrarConta() {

        System.out.print("Digite o número da conta: ");
        var numeroDaConta = teclado.nextInt();

        service.encerrar(numeroDaConta);

        System.out.println("Conta encerrada com sucesso!");

    }

    private static void consultarSaldo() {

        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();
        var saldo = service.consultarSaldo(numeroDaConta);
        System.out.println("Saldo da conta: " +saldo);

    }

    private static void realizarSaque() {

        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();

        System.out.println("Digite o valor do saque:");
        var valor = teclado.nextBigDecimal();

        service.realizarSaque(numeroDaConta, valor);
        System.out.println("Saque realizado com sucesso!");

    }

    private static void realizarDeposito() {

        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();

        System.out.println("Digite o valor do depósito:");
        var valor = teclado.nextBigDecimal();

        service.realizarDeposito(numeroDaConta, valor);

        System.out.println("Depósito realizado com sucesso!");

    }

}
