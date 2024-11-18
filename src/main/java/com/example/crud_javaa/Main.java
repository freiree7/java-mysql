package com.example.crud_javaa;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        int opcao;
        do {
            exibirMenu();
            String opcaoStr = reader.nextLine();

            if (!opcaoStr.isEmpty()) {
                try {
                    opcao = Integer.parseInt(opcaoStr);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Por favor, insira um número.");
                    // garante que um numero seja digitado
                    continue;
                }

                switch (opcao) {
                    case 0 -> salvarUsuario(reader);
                    case 1 -> buscarTodosUsuarios();
                    case 2 -> buscarUsuarioPorId(reader);
                    case 3 -> atualizarUsuario(reader);
                    case 4 -> excluirUsuario(reader);
                    case 5 -> System.exit(0);
                    default -> System.out.println("Opção inválida!");
                    // menu para navegar pelo sistema
                }
            } else {
                System.out.println("Opção não pode ser vazia. Por favor, insira um número.");
            }
        } while (true);
    }

    private static void exibirMenu() {
        System.out.println("\n### Menu de Operações ###");
        System.out.println("0. Salvar novo Usuario");
        System.out.println("1. Buscar todos Usuarios");
        System.out.println("2. Buscar Usuario por Matricula");
        System.out.println("3. Atualizar Usuario");
        System.out.println("4. Excluir Usuario");
        System.out.println("5. Sair do programa");
        System.out.print("Escolha uma opção: ");
    }

    public static void salvarUsuario(Scanner reader) {
        System.out.println("\n### Criar Novo Usuario ###");

        System.out.print("Matricula = ");
        var matricula = reader.nextInt();
        reader.nextLine();

        System.out.print("Nome = ");
        var nome = reader.nextLine();
        if (nome.trim().isEmpty()) {
            System.out.println("Erro: O nome não pode estar vazio.");
            return;
        }

        System.out.print("Email = ");
        var email = reader.nextLine();

        System.out.print("Curso = ");
        var curso = reader.nextLine();
        if (curso.trim().isEmpty()) {
            System.out.println("Erro: O curso não pode estar vazio.");
            return;
        }

        try (var conexao = Conexao.obterconexao()) {

            System.out.println("Banco conectado com sucesso");
            var sql = "INSERT INTO usuarios (matricula, nome, email, curso) VALUES (" + matricula + ", '" + nome + "', '" + email + "', '" + curso + "')";
            System.out.println("Usuário cadastrado com sucesso");

            try (Statement stmt = conexao.createStatement()) {
                stmt.executeUpdate(sql);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao salvar usuário no banco de dados: " + e.getMessage());
        }
    }
    // le todos os dados e garante que todos os dados sejam inseridos 

    private static void buscarTodosUsuarios() {
        System.out.println("\n### Buscar Todos ###");

        try (var conexao = Conexao.obterconexao()) {
            var sql = "SELECT * FROM usuarios";
            try (Statement stmt = conexao.createStatement()) {
                var consulta = stmt.executeQuery(sql);
                // execute query: executa a instrução my sql que esta na variavel correspondente
                while (consulta.next()) {
                    int matricula = consulta.getInt("matricula");
                    String nome = consulta.getString("nome");
                    String email = consulta.getString("email");
                    String curso = consulta.getString("curso");

                    System.out.println("Matricula: " + matricula + ", Nome: " + nome + ", Email: " + email + ", Curso: " + curso);
                }

            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuários: " + e.getMessage());
        }
    }
    // busca todos os dados do banco

    private static void buscarUsuarioPorId(Scanner reader) {
        System.out.println("\n### Buscar Usuario por Matricula ###");

        try (var conexao = Conexao.obterconexao()) {
            try (Statement stmt = conexao.createStatement()) {
                System.out.print("Insira a matricula que deseja buscar: ");
                int matricula = reader.nextInt();
                reader.nextLine();

                var sql = "SELECT matricula, nome, email, curso FROM usuarios WHERE matricula = " + matricula;
                var consulta = stmt.executeQuery(sql);

                if (consulta.next()) {
                    int matricula1 = consulta.getInt("matricula");
                    String nome = consulta.getString("nome");
                    String email = consulta.getString("email");
                    String curso = consulta.getString("curso");

                    System.out.println("Matricula: " + matricula1 + ", Nome: " + nome + ", Email: " + email + ", Curso: " + curso);
                } else {
                    System.out.println("Usuario não encontrado com a Matricula: " + matricula);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário por matrícula: " + e.getMessage());
        }
    }
    //  busca por usuarios atraves do id, e avisa se não existir usuario com o id desejado

    private static void atualizarUsuario(Scanner reader) {
        System.out.println("\n### Atualizar Usuario ###");

        try (var conexao = Conexao.obterconexao()) {
            try (Statement stmt = conexao.createStatement()) {

                System.out.print("Digite a Matricula do Usuario que deseja alterar os dados: ");
                var matricula = reader.nextInt();
                reader.nextLine();

                var sql2 = "SELECT matricula, nome, email, curso FROM usuarios WHERE matricula = " + matricula;
                var consultaid = stmt.executeQuery(sql2);

                if (consultaid.next()) {
                    System.out.print("Digite o novo nome: ");
                    var novoNome = reader.nextLine();
                    if (novoNome.trim().isEmpty()) {
                        System.out.println("Erro: O nome não pode estar vazio.");
                        return;
                    }

                    System.out.print("Digite o novo curso: ");
                    var novoCurso = reader.nextLine();
                    if (novoCurso.trim().isEmpty()) {
                        System.out.println("Erro: O curso não pode estar vazio.");
                        return;
                    }

                    System.out.print("Digite o novo email: ");
                    var novoEmail = reader.nextLine();

                    var sqlUpdate = "UPDATE usuarios SET nome = '" + novoNome + "', email = '" + novoEmail + "', curso = '" + novoCurso + "' WHERE matricula = " + matricula;
                    stmt.executeUpdate(sqlUpdate);
                    System.out.println("Usuário atualizado com sucesso.");
                } else {
                    System.out.println("Usuário não encontrado com a matrícula: " + matricula);
                }

            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar usuário: " + e.getMessage());
        }
    }
    // atualiza todos os dados de um determinado usuario e garante que todos os campos sejam preenchidos

    private static void excluirUsuario(Scanner reader) {
        System.out.println("\n### Excluir Usuario ###");
        try (var conexao = Conexao.obterconexao()) {
            try (Statement stmt = conexao.createStatement()) {

                System.out.println("1 - Remover todos usuários ");
                System.out.println("2 - Remover o usuário ");

                // pode escolher se quer que todos os usuarios sejam deletados, ou somente um especifico através da sua matricula
                int resposta = reader.nextInt();
                reader.nextLine();
                if (resposta == 1) {
                    var sql4 = "DELETE FROM usuarios";
                    stmt.executeUpdate(sql4);
                    System.out.println("Todos os usuários foram deletados");
                } else if (resposta == 2) {
                    System.out.print("Digite a Matricula do Usuario que deseja excluir: ");
                    var matricula = reader.nextInt();
                    reader.nextLine();
                    var sql2 = "DELETE FROM usuarios WHERE matricula = " + matricula;
                    stmt.executeUpdate(sql2);
                    System.out.println("Usuario com a matricula " + matricula + " foi removido");
                }

            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir usuário: " + e.getMessage());
        }
    }
}

