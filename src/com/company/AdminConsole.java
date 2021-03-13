package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class AdminConsole {
    static RMI rmiServer;

    public static void main(String[] args) {
        try {
            rmiServer = (RMI) LocateRegistry.getRegistry(7000).lookup("admin");
            String message = rmiServer.saySomething();
            System.out.println("Hello Admin: " + message);
            admin(-1);
        } catch (Exception e) {
            System.out.println("Exception in Admin: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Menu que apresenta as opções que os administradores podem realizar
     * @param command valor da instrução a realizar
     * @throws IOException
     */
    static public void admin(int command) throws IOException {
        while (command != 0) {
            System.out.println("1- Registar Pessoas");
            System.out.println("2- Criar Eleição");
            System.out.println("0- Sair");
            Scanner input = new Scanner(System.in);
            command = input.nextInt();
            switch (command) {
                case 1:
                    register();
                    break;
                case 2:
                    createElection();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Lê da consola a informação pessoal de uma determinada pessoa.
     * As pessoas serão introduzidas na base de dados no servidor RMI, por questões de segurança
     *
     * @throws IOException
     */
    static public void register() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner input = new Scanner(System.in);
        String cargo, pass, dep, address;
        int num_phone, num_cc, ano_cc, mes_cc, dia_cc;
        System.out.println("Cargo: ");
        cargo = reader.readLine();
        System.out.println("Password: ");
        pass = reader.readLine();
        System.out.println("Nome do departamento que frequenta: ");
        dep = reader.readLine();
        System.out.println("Número de telemóvel: ");
        num_phone = input.nextInt();
        System.out.println("Morada: ");
        address = reader.readLine();
        System.out.println("Número do cartão de cidadão: ");
        num_cc = input.nextInt();
        System.out.println("validade do cartão de cidadão: ");
        ano_cc = input.nextInt();
        mes_cc = input.nextInt();
        dia_cc = input.nextInt();
        try {
            if (!rmiServer.insertPerson(cargo, pass, dep, num_phone, address, num_cc, ano_cc, mes_cc, dia_cc)) {
                System.out.println("Impossível inserir registo :(");
            } else {
                System.out.println("Registo feito com sucesso! :)");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lê da consola a informação necessária para criar uma eleição
     * As eleições serão introduzidas na base de dados no servidor RMI, por questões de segurança
     * @throws IOException
     */
    static public void createElection() throws IOException {
        int anoIni, mesIni, diaIni, horaIni, minIni, anoFim, mesFim, diaFim, horaFim, minFim;
        String titulo, descricao;
        Scanner input = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Início da Eleição: ");
        anoIni = input.nextInt();
        mesIni = input.nextInt();
        diaIni = input.nextInt();
        horaIni = input.nextInt();
        minIni = input.nextInt();
        System.out.println("Fim da Eleição: ");
        anoFim = input.nextInt();
        mesFim = input.nextInt();
        diaFim = input.nextInt();
        horaFim = input.nextInt();
        minFim = input.nextInt();
        System.out.println("Título da Eleição: ");
        titulo = reader.readLine();
        descricao = reader.readLine();
        try {
            if (!rmiServer.insertElection(anoIni, mesIni, diaIni, horaIni, minIni, anoFim, mesFim, diaFim, horaFim, minFim, titulo, descricao)) {
                System.out.println("Impossível inserir eleição :(");
            } else {
                System.out.println("Eleição criada com sucesso! :)");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/*
1
estudante
123
dei
123456789
Rua de Coimbra
12345678
23
5
2028
 */
