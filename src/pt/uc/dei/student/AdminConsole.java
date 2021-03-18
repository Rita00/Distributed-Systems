package pt.uc.dei.student;

import pt.uc.dei.student.elections.Election;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminConsole {
	
	private RMI rmiServer;
	
	public AdminConsole(RMI rmiServer) {
	    this.rmiServer=rmiServer;
	}

    /**
     * Menu que apresenta as opções que os administradores podem realizar
     * @param command valor da instrução a realizar
     * @throws IOException
     */
    public void admin(int command) throws IOException, InterruptedException {
        while (command != 0) {
            System.out.println("1- Registar Pessoas");
            System.out.println("2- Criar Eleição");
            System.out.println("3- Gerir Eleição");
            System.out.println("0- Sair");
            Scanner input = new Scanner(System.in);
            command = input.nextInt();
            switch (command) {
                case 1:
                    this.register();
                    break;
                case 2:
                    this.createElection();
                    break;
                case 3:
                    this.manageElection();
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
    public void register() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner input = new Scanner(System.in);
        String pass, dep, address;
        int cargo = 0, num_phone, num_cc, ano_cc, mes_cc, dia_cc;
        while (cargo != 1 && cargo != 2 && cargo != 3) {
            System.out.println("Cargo: ");
            System.out.println("\t1 - Estudante");
            System.out.println("\t2 - Docente");
            System.out.println("\t3 - Funcionário");
            System.out.print("\t");
            cargo = input.nextInt();
        }
        System.out.print("Password: ");
        pass = reader.readLine();
        System.out.print("Nome do departamento que frequenta: ");
        dep = reader.readLine();
        System.out.print("Número de telemóvel: ");
        num_phone = input.nextInt();
        System.out.print("Morada: ");
        address = reader.readLine();
        System.out.print("Número do cartão de cidadão: ");
        num_cc = input.nextInt();
        System.out.print("validade do cartão de cidadão: ");
        ano_cc = input.nextInt();
        mes_cc = input.nextInt();
        dia_cc = input.nextInt();
        try {
            if (!this.rmiServer.insertPerson(this.decideCargo(cargo), pass, dep, num_phone, address, num_cc, ano_cc, mes_cc, dia_cc)) {
                System.out.println("Impossível inserir registo :(");
            } else {
                System.out.println("Registo feito com sucesso! :)");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Função para decidir em formato String o cargo da pessoa
     * Usado para proteção de dados
     * @param cargo Inteiro escolhido pela pessoa para representar o seu cargoo
     * @return String que corresponde ao seu cargo
     */
    public String decideCargo(int cargo) {
        switch (cargo) {
            case 1:
                return "Estudante";
            case 2:
                return "Docente";
            case 3:
                return "Funcionário";
            default:
                return null;
        }
    }

    /**
     * Lê da consola a informação necessária para criar uma eleição
     * As eleições serão introduzidas na base de dados no servidor RMI, por questões de segurança
     * @throws IOException
     */
    public void createElection() throws IOException {
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
            if (!this.rmiServer.insertElection(anoIni, mesIni, diaIni, horaIni, minIni, anoFim, mesFim, diaFim, horaFim, minFim, titulo, descricao)) {
                System.out.println("Impossível inserir eleição :(");
            } else {
                System.out.println("Eleição criada com sucesso! :)");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void manageElection() {
        int command = -1;
        while(command != 0){
            //buscar eleiçoes à BD
            ArrayList<Election> elections = null;
            try {
                elections = this.rmiServer.getElections();
                //listar eleiçoes
                System.out.println("Remover a eleição:");
                for (Election e: elections) {
                    System.out.println("\t"+(elections.indexOf(e)+1)+"- "+e.getTitle());
                }
                System.out.println("0-Voltar");
                //esperar pelo input
                Scanner input = new Scanner(System.in);
                command = input.nextInt();
                switch (command) {
                    case 0:
                        this.admin(-1);
                        break;
                    default:
                        this.rmiServer.removeElection(elections.get(command-1).getTitle());
                        break;
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
	}

    public static void main(String[] args) {
        try {
        	RMI rmiServer = (RMI) LocateRegistry.getRegistry(7000).lookup("admin");
            String message = rmiServer.saySomething();
            System.out.println("Hello Admin: " + message);
            AdminConsole console = new AdminConsole(rmiServer);
            console.admin(-1);
        } catch (Exception e) {
            System.out.println("Exception in Admin: " + e);
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
