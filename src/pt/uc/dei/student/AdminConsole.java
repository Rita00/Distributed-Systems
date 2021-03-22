package pt.uc.dei.student;

import pt.uc.dei.student.elections.Candidacy;
import pt.uc.dei.student.elections.Department;
import pt.uc.dei.student.elections.Election;
import pt.uc.dei.student.elections.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminConsole {


    private final int RETURN = 0;
    private final int EDIT = -1;
    private final int ADD = -1;
    private final int REMOVE = -2;

	private RMI rmiServer;
	
	public AdminConsole(RMI rmiServer) {
	    this.rmiServer=rmiServer;
	}

    /**
     * Menu que apresenta as opções que os administradores podem realizar
     *
     * @param command valor da instrução a realizar
     * @throws IOException exceção de I/O
     */
    public void admin(int command) throws IOException {
        try {
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
        } catch (InputMismatchException ime) {
            //volta para este menu caso o input esteja errado
            this.admin(-1);
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
        int cargo = 0, ndep = 0, num_phone, num_cc, ano_cc, mes_cc, dia_cc;
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
        while (!(ndep >= 1 && ndep <= 11)) {
            System.out.println("Departamento que frequenta: ");
            this.listDepart();
            System.out.print("\t");
            ndep = input.nextInt();
        }
        System.out.print("Número de telemóvel: ");
        num_phone = input.nextInt();
        System.out.print("Morada: ");
        address = reader.readLine();
        System.out.print("Número do cartão de cidadão: ");
        num_cc = input.nextInt();
        System.out.print("Validade do cartão de cidadão: ");
        ano_cc = input.nextInt();
        mes_cc = input.nextInt();
        dia_cc = input.nextInt();
        try {
            if (!this.rmiServer.insertPerson(this.decideCargo(cargo), pass, ndep, num_phone, address, num_cc, ano_cc, mes_cc, dia_cc)) {
                System.out.println("Impossível inserir registo :(");
            } else {
                System.out.println("Registo feito com sucesso! :)");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void listDepart() {
        ArrayList<Department> departments;
        try {
            departments = this.rmiServer.getDepartments();
            for (Department dep : departments) {
                System.out.printf("\t(%d)- %s%n", dep.getId(), dep.getName());
            }
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Função para decidir em formato String o cargo da pessoa
     * Usado para proteção de dados
     * @param cargo Inteiro escolhido pela pessoa para representar o seu cargo
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
     *
     * @throws IOException exceção de I/O
     */
    public void createElection() throws IOException {
        int anoIni, mesIni, diaIni, horaIni, minIni, anoFim, mesFim, diaFim, horaFim, minFim, type_ele = 0, restr = -1, ndep = -1;
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
        System.out.print("Título da Eleição: ");
        titulo = reader.readLine();
        System.out.print("Breve descrição: ");
        descricao = reader.readLine();
        while (restr != 1 && restr != 2) {
            System.out.println("Restringir Eleição?");
            System.out.println("\t(1)- Sim");
            System.out.println("\t(2)- Não");
            System.out.print("\t");
            restr = input.nextInt();
        }
        if (restr == 1) {
            while (!(ndep >= 1 && ndep <= 11)) {
                System.out.println("\tDepartamento: ");
                listDepart();
                System.out.print("\t");
                ndep = input.nextInt();
            }
        }
        while (type_ele != 1 && type_ele != 2 && type_ele != 3) {
            System.out.println("Tipo de eleição: ");
            System.out.println("\t1 - Estudante");
            System.out.println("\t2 - Docente");
            System.out.println("\t3 - Funcionário");
            System.out.print("\t");
            type_ele = input.nextInt();
        }
        try {
            int id = this.rmiServer.insertElection(anoIni, mesIni, diaIni, horaIni, minIni, anoFim, mesFim, diaFim, horaFim, minFim, titulo, descricao, this.decideCargo(type_ele));
            if (id == -1) {
                System.out.println("Impossível inserir eleição :(");
            } else {
                System.out.println("Eleição criada com sucesso! :)");
                try {
                    this.rmiServer.insertElectionDepartment(id, ndep);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void listElections() {
        int command = -1;
        while(command != RETURN){
            try {
                /*
                 * LISTAR ELEICOES
                 */
                ArrayList<Election> elections = this.rmiServer.getElections();
                if (elections.size() > 0) {
                    System.out.println("Ver a eleição:");
                    for (Election e : elections) {
                        System.out.printf("\t(%s)- %s%n", elections.indexOf(e) + 1, e.getTitle());
                    }
                } else {
                    System.out.println("Não existem eleições\n");
                }
                System.out.println("(" + RETURN + ")-Voltar");
                /*
                 * ESPERAR PELA ESCOLHA DO UTILIZADOR
                 */
                Scanner input = new Scanner(System.in);
                command = input.nextInt();
                switch (command) {
                    case RETURN:
                        break;
                    default:
                        if (0 < command && command <= elections.size()) {
                            this.manageElection(elections.get(command - 1));
                        }else{
                            //volta para este menu caso o numero do comando esteja errado
                            this.listElections();
                        }
                        break;
                }
            }catch (InputMismatchException ime) {
                //volta para este menu caso o input esteja errado
                this.listElections();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void manageElection(Election election) {
        int command = -1;
        while(command != RETURN){
            try {
                /*
                 * LISTAR LISTAS CANDIDATAS
                 */
                System.out.println(election.toString());
                ArrayList<Candidacy> candidacies = this.rmiServer.getCandidacies(election.getId());
                if(candidacies.size()>0){
                    System.out.println("Ver lista:");
                    for (Candidacy c : candidacies) {
                        System.out.printf("\t(%s)- %s%n",candidacies.indexOf(c)+1,c.getName());
                    }
                }else{
                    System.out.println("A eleição não tem listas\n");
                }
                System.out.println("("+EDIT+")-Editar");
                System.out.println("("+RETURN+")-Voltar");
                /*
                 * ESPERAR PELA ESCOLHA DO UTILIZADOR
                 */
                Scanner input = new Scanner(System.in);
                command = input.nextInt();
                switch (command) {
                    case EDIT:
                        this.editElection(election);
                        break;
                    case RETURN:
                        break;
                    default:
                        if (0<command && command<=candidacies.size()) {
                            this.manageCandidacy(candidacies.get(command - 1));
                        }else{
                            //volta para este menu caso o numero do comando esteja errado
                            this.manageElection(election);
                        }
                        break;
                }
            } catch (InputMismatchException ime) {
                //volta para este menu caso os input esteja errado
                this.manageElection(election);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void editElection(Election election) throws RemoteException, InterruptedException {
        int command = -1;
        while (command != RETURN) {
            System.out.println(election.toString());
            //opcoes
            System.out.println("Editar:");
            System.out.println("\t(1)- Nome");
            System.out.println("\t(2)- Tipo");
            System.out.println("\t(3)- Descricao");
            System.out.println("\t(4)- Data Inicio");
            System.out.println("\t(5)- Data Fim");
            System.out.println("(" + RETURN + ")-Voltar");
            //esperar pelo input
            Scanner input = new Scanner(System.in);
            command = input.nextInt();
            switch (command) {
                case RETURN:
                    //VAZIO PARA VOLTAR
                    break;
                case 1:
                    System.out.println("Editar titulo:");
                    election.setTitle(input.next());
                    this.rmiServer.updateElections(election);
                    break;
                case 2:
                    int type=-1;
                    while(type>3 || 0>type){
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
                    this.rmiServer.updateElections(election);
                    break;
                case 3:
                    System.out.println("Editar descricao:");
                    election.setDescription(input.next());
                    this.rmiServer.updateElections(election);
                    break;
                case 4:
                    System.out.println("Editar data de inicio (YYYY-MM-DD HH:mm:SS):");
                    while(!election.setBegin(input.next(),input.next())) System.out.println("Data invalida - formato (YYYY-MM-DD HH:mm:SS)");
                    this.rmiServer.updateElections(election);
                    break;
                case 5:
                    System.out.println("Editar data de fim (YYYY-MM-DD HH:mm:SS):");
                    while(!election.setEnd(input.next(),input.next())) System.out.println("Data invalida - formato (YYYY-MM-DD HH:mm:SS)");
                    this.rmiServer.updateElections(election);
                    break;
                default:
                    //volta para este menu caso esteja algo errado
                    this.editElection(election);
                    break;
            }
        }
    }
    private void manageCandidacy(Candidacy candidacy){
        int command = -1;
        while(command != RETURN){
            System.out.println(candidacy.toString());
            try {
                ArrayList<Person> people = this.rmiServer.getPeople(candidacy.getId());
                //listar pessoas
                if(people.size()>0){
                    System.out.println("Remover a pessoa com o CC:");
                    for (Person p : people) {
                        System.out.printf("\t(%s)- %s%n",people.indexOf(p)+1,p.getCc_number());
                    }
                }else{
                    System.out.println("A lista não tem pessoas\n");
                }
                System.out.println("("+ADD+")-Adicionar pessoa");
                System.out.println("("+REMOVE+")-Remover lista");
                System.out.println("("+RETURN+")-Voltar");
                //esperar pelo input
                Scanner input = new Scanner(System.in);
                command = input.nextInt();
                switch (command) {
                    case ADD:
                        System.out.println("Numero do Cartao de Cidadao da pessoa:");
                        this.rmiServer.insertPersonIntoCandidacy(candidacy.getId(),input.nextInt());
                        break;
                    case REMOVE:
                        this.rmiServer.removeOnDB("candidacy","id", candidacy.getId());
                        this.rmiServer.removeOnDB("candidacy_person", "candidacy_id",candidacy.getId());
                        command=RETURN;//para voltar ao menu das eleicoes
                        break;
                    case RETURN:
                        //VAZIO PARA VOLTAR
                        break;
                    default:
                        if (0<command && command<=people.size()) {
                            this.rmiServer.removeOnDB("candidacy_person","person_cc_number",people.get(command-1).getCc_number());
                        }else{
                            //volta para este menu caso esteja algo errado
                            this.manageCandidacy(candidacy);
                        }
                        break;
                }
            } catch (InputMismatchException ime) {
                //volta para este menu caso o input esteja errado
                this.manageCandidacy(candidacy);
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
