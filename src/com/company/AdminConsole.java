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
        System.out.println("\tAno: ");
        ano_cc = input.nextInt();
        System.out.println("\tMês: ");
        mes_cc = input.nextInt();
        System.out.println("\tDia: ");
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

    static public void createElection() {

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
