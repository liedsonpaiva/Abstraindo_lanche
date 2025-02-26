package com.example.demo;

import com.example.demo.applications.LancheApplication;
import com.example.demo.entities.Lanche;
import com.example.demo.facade.LancheFacade;
import com.example.demo.repositories.LancheRepository;
import com.example.demo.services.LancheService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class DemoApplication {
    private static LancheRepository lancheRepository;
    private static LancheService lancheService;
    private static LancheApplication lancheApplication;
    private static LancheFacade lancheFacade;

    private static void injetarDependencias() {
        lancheRepository = new LancheRepository();
        lancheService = new LancheService();
        lancheApplication = new LancheApplication(lancheService, lancheRepository);
        lancheFacade = new LancheFacade(lancheApplication);
    }

    public static void main(String[] args) {
        injetarDependencias();
        Scanner scanner = new Scanner(System.in);

        int codigo = 1;
        boolean quit = false;

        while (!quit) {
            System.out.println("""
                    1 - Cadastrar lanche
                    2 - Listar lanches
                    3 - Comprar lanche
                    4 - Atualizar lanche
                    5 - Excluir lanche
                    6 - Sair""");

            System.out.print("Escolha uma opção: ");
            int op = scanner.nextInt();

            switch (op) {
                case 1 -> {
                    System.out.print("Nome do lanche: ");
                    String nome = scanner.nextLine();

                    System.out.print("Preço: ");
                    double preco = scanner.nextDouble();

                    System.out.print("URL da imagem: ");
                    String img_url = scanner.nextLine();

                    lancheFacade.cadastrar(new Lanche(codigo++, nome, preco, img_url));
                    System.out.println("Lanche cadastrado com sucesso\n");
                }
                case 2 -> {
                    System.out.println("--------------------------------------\n CÓDIGO\tNOME\t\tPRECO\n--------------------------------------");
                    List<Lanche> lanches = lancheFacade.buscar();
                    for (Lanche l : lanches) {
                        System.out.println(l.getCodigo() + "\t\t" + l.getNome() + "\t\t" + l.getPreco() + "\n");
                    }
                }
                case 3 -> {
                    System.out.print("Digite o código do lanche: ");
                    int cod = scanner.nextInt();

                    System.out.print("Digite a quantidade: ");
                    int qtd = scanner.nextInt();

                    Lanche lanche = lancheFacade.buscarPorCodigo(cod);
                    String total = String.format("%.2f", lancheFacade.calcularLanche(lanche, qtd));
                    System.out.println("Total: R$ " + total);
                }
                case 4 -> { // Atualizar lanche
                    System.out.println("Digite o código do lanche: ");
                    int cod = scanner.nextInt();

                    Lanche lancheExistente = lancheFacade.buscarPorCodigo(cod);
                    if (lancheExistente != null) {
                        System.out.print("Nome do lanche: ");
                        String nome = scanner.nextLine();

                        System.out.print("Preço: ");
                        double preco = scanner.nextDouble();

                        System.out.print("URL da imagem: ");
                        String img_url = scanner.nextLine();

                        lancheFacade.atualizar(cod, new Lanche(cod, nome, preco, img_url));
                        System.out.println("Lanche atualizado com sucesso\n");
                    } else {
                        System.out.println("Código inválido.");
                    }
                }
                case 5 -> {
                    System.out.println("Digite o código do lanche que deseja excluir: ");
                    int cod = scanner.nextInt();

                    Lanche lancheExistente = lancheFacade.buscarPorCodigo(cod);
                    if (lancheExistente != null) {
                        lancheFacade.excluir(cod);
                        System.out.println("Lanche excluído com sucesso.");
                    } else {
                        System.out.println("Código inválido.");
                    }
                }
                case 6 -> {
                    System.out.println("Programa encerrado!");
                    quit = true;
                }
                default -> System.out.println("Opção inválida!");
            }
        }

        scanner.close();
    }
}


