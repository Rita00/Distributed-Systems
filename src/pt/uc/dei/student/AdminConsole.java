package pt.uc.dei.student;

import pt.uc.dei.student.elections.Candidacy;
import pt.uc.dei.student.elections.Election;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminConsole {

	private final int RETURN = 0;
    private final int EDIT = -1;
	private RMI rmiServer;
	
	public AdminConsole(RMI rmiServer) {
	    this.rmiServer=rmiServer;
	}

    /**
     * Menu que apresenta as opções que os administradores podem realizar
     * @param command valor da instrução a realizar
     * @throws IOException exceção de I/O
     */
    public void admin(int command) throws IOException, InterruptedException {
        while (command != 0) {
            System.out.println("(1)- Registar Pessoas");
            System.out.println("(2)- Criar Eleição");
            System.out.println("(3)- Gerir Eleição");
            System.out.println("(0)- Sair");
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
                    this.listElections();
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
     * @throws IOException exceção de I/O
     */
    public void register() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner input = new Scanner(System.in);
        String pass, address;
        int cargo = 0, num_phone, num_cc, ano_cc, mes_cc, dia_cc, dep;
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
        dep = input.nextInt();
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
     * @throws IOException exceção de I/O
     */
    public void createElection() throws IOException {
        int anoIni, mesIni, diaIni, horaIni, minIni, anoFim, mesFim, diaFim, horaFim, minFim, type_ele = 0;
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
        while (type_ele != 1 && type_ele != 2 && type_ele != 3) {
            System.out.println("Tipo de eleição: ");
            System.out.println("\t1 - Estudante");
            System.out.println("\t2 - Docente");
            System.out.println("\t3 - Funcionário");
            System.out.print("\t");
            type_ele = input.nextInt();
        }
        try {
            if (!this.rmiServer.insertElection(anoIni, mesIni, diaIni, horaIni, minIni, anoFim, mesFim, diaFim, horaFim, minFim, titulo, descricao, this.decideCargo(type_ele))) {
                System.out.println("Impossível inserir eleição :(");
            } else {
                System.out.println("Eleição criada com sucesso! :)");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void listElections() {
        int command = -1;
        while(command != RETURN){
            //buscar eleiçoes à BD
            ArrayList<Election> elections;
            try {
                elections = this.rmiServer.getElections();
                //listar eleiçoes
                System.out.println("Ver a eleição:");
                for (Election e: elections) {
                    System.out.println(String.format("\t(%s)- %s",elections.indexOf(e)+1,e.getTitle()));
                }
                System.out.println("("+RETURN+")-Voltar");
                //esperar pelo input
                Scanner input = new Scanner(System.in);
                command = input.nextInt();
                switch (command) {
                    case RETURN:
                        this.admin(-1);
                        break;
                    default:
                        this.manageElection(elections.get(command - 1));
                        break;
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
	}
	private void manageElection(Election election){
        int command = -1;
        while(command != RETURN){
            System.out.println(election.toString());
            try {
                ArrayList<Candidacy> candidacies = this.rmiServer.getCandidacies(election.getId());
                //listar eleiçoes
                System.out.println("Remover lista:");
                for (Candidacy c : candidacies) {
                    System.out.println(String.format("\t(%s)- %s",candidacies.indexOf(c)+1,c.getName()));
                }
                System.out.println("("+EDIT+")-Editar");
                System.out.println("("+RETURN+")-Voltar");
                //esperar pelo input
                Scanner input = new Scanner(System.in);
                command = input.nextInt();
                switch (command) {
                    case EDIT:
                        this.editElection(election);
                        break;
                    case RETURN:
                        this.listElections();
                        break;
                    default:
                        this.rmiServer.removeOnDB("candidacy",candidacies.get(command-1).getId());
                        break;
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void editElection(Election election) throws RemoteException, InterruptedException {
        int command = -1;
        while(command != RETURN){
            System.out.println(election.toString());
            //opcoes
            System.out.println("Editar:");
            System.out.println("\t(1)- Nome");
            System.out.println("\t(2)- Tipo");
            System.out.println("\t(3)- Descricao");
            System.out.println("\t(4)- Data Inicio");
            System.out.println("\t(5)- Data Fim");
            System.out.println("("+RETURN+")-Voltar");
            //esperar pelo input
            Scanner input = new Scanner(System.in);
            command = input.nextInt();
            switch (command) {
                case RETURN:
                    return;
                case 1:
                    System.out.println("Editar titulo:");
                    election.setTitle(input.next());
                    break;
                case 2:
                    int type=-1;
                    while(type>Election.types.values().length || 0>type){
                        System.out.println("Editar tipo de eleição:");
                        System.out.println("\t(1)- Estudante");
                        System.out.println("\t(2)- Docente");
                        System.out.println("\t(3)- Funcionario");
                        System.out.println("(0)- Voltar");
                        type = input.nextInt();
                        if (type == RETURN) {
                            this.editElection(election);
                        } else {
                            election.setType(type);
                        }
                    }
                    election.setType(type-1);
                    break;
                case 3:
                    System.out.println("Editar descricao:");
                    election.setDescription(input.next());
                    break;
                case 4:
                    System.out.println("Editar data de inicio (YYYY-MM-DD HH:mm:SS):");
                    if(!election.setBegin(input.next(),input.next())){
                        System.out.println("Data invalida");
                    }
                    break;
                case 5:
                    System.out.println("Editar data de fim (YYYY-MM-DD HH:mm:SS):");
                    if(!election.setEnd(input.next(),input.next())){
                        System.out.println("Data invalida");
                    }
                    break;
            }
            this.rmiServer.updateElections(election);
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
