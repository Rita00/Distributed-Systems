package pt.uc.dei.student;

import pt.uc.dei.student.elections.*;
import pt.uc.dei.student.others.Notifier;
import pt.uc.dei.student.others.NotifierCallBack;
import pt.uc.dei.student.others.RMI;
import pt.uc.dei.student.others.Utilitary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class AdminConsole {


    private final String OPTION_STRING = ">>> ";
    private final int RETURN = 0;
    private final int EDIT = -1;
    private final int REMOVE = -1;
    private final int ADD = -2;

    private RMI rmiServer;

    private final NotifierCallBack NOTIFIER = new NotifierCallBack();

    private boolean isMonitoring;

    static AdminConsole admin;

    public AdminConsole(RMI rmiServer) throws RemoteException {
        this.rmiServer = rmiServer;
        this.isMonitoring = false;
    }

    /**
     * Menu que apresenta as opções que os administradores podem realizar
     *
     * @param command valor da instrução a realizar
     * @throws IOException exceção de I/O
     */
    public void admin(int command) throws IOException {
        System.out.println("========CONSOLA ADMINISTRADORA========".hashCode());
        try {
            Scanner input = new Scanner(System.in);
            while (command != 0) {
                System.out.println("========CONSOLA ADMINISTRADORA========");
                System.out.println("\t(1)- Registar Pessoas");
                System.out.println("\t(2)- Criar Eleição");
                System.out.println("\t(3)- Gerir Eleição");
                System.out.println("\t(4)- Gerir Mesas de Voto");
                System.out.println("\t(5)- Local em que cada eleitor votou");
                System.out.println("\t(6)- Consultar resultados detalhados de eleições passadas");
                System.out.println("\t(7)- Consultar estado das mesas de voto e respetivos terminais de voto");
                System.out.println("\t(8)- Consultar contagem de votos em tempo real");
                System.out.println("(0)- Sair");
                System.out.print(OPTION_STRING);
                command = input.nextInt();
                switch (command) {
                    case 1:
                        this.register();
                        break;
                    case 2:
                        this.createElection();
                        break;
                    case 3:
                        this.listElectionsToManage();
                        break;
                    case 4:
                        this.listElectionToManagePollingStation();
                        break;
                    case 5:
                        this.listVotingRecord();
                        break;
                    case 6:
                        this.electionsResults();
                        break;
                    case 7:
                        this.statusPollingStation();
                        break;
                    case 8:
                        this.statusVotes();
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

    private void statusVotes() {
        int command;
        Scanner input = new Scanner(System.in);
        while (true) {
            try {
                while (true) {
                    try {
                        this.rmiServer.initializeRealTimeVotes(NOTIFIER);
                        break;
                    } catch (RemoteException | InterruptedException e) {
                        //e.printStackTrace();
                        reconnectToRMI();
                    }
                }
                System.out.println("(" + RETURN + ")-\t\tVoltar");
                System.out.print(OPTION_STRING);
                command = input.nextInt();
                if (command == 0) {
                    while (true) {
                        try {
                            this.rmiServer.endRealTimeInfo(NOTIFIER);
                            this.admin(-1);
                            break;
                        } catch (IOException e) {
                            //e.printStackTrace();
                            reconnectToRMI();
                        }
                    }
                }
                break;
            } catch (InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }

    }

    private void statusPollingStation() {
        this.isMonitoring = true;
        int command;
        Scanner input = new Scanner(System.in);
        //TODO mostrar dados
        System.out.println("==========ESTADO DAS MESAS DE VOTO E TERMINAIS DE VOTO==========");
        while (true) {
            try {
                while (true) {
                    try {
                        this.rmiServer.initializeRealTimePolls(NOTIFIER);
                        break;
                    } catch (RemoteException | InterruptedException e) {
                        //e.printStackTrace();
                        reconnectToRMI();
                    }
                }
                System.out.println("(" + RETURN + ")-\t\tVoltar");
                System.out.print(OPTION_STRING);
                command = input.nextInt();
                if (command == 0) {
                    while (true) {
                        try {
                            this.rmiServer.endRealTimePolls(NOTIFIER);
                            this.admin(-1);
                            break;
                        } catch (IOException e) {
                            //e.printStackTrace();
                            reconnectToRMI();
                        }
                    }
                }
                break;
            } catch (InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }

        this.isMonitoring = false;
    }

    public void listVotingRecord() {
        Scanner input = new Scanner(System.in);
        String command;

        ArrayList<VotingRecord> votingRecords;
        while (true) {
            try {
                votingRecords = this.rmiServer.getVotingRecords();
                break;
            } catch (RemoteException | InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }
        if (votingRecords.size() == 0) System.out.println("Sem registo de votos!");
        else {
            for (VotingRecord vr : votingRecords) {
                System.out.printf("%s\t\t%s\t\t%s\t\t%s\n", vr.getElection_title(), vr.getPerson_name(), vr.getDepartment_name(), vr.getVote_date());
            }
        }
        System.out.println("(" + RETURN + ")-\t\tVoltar");
        System.out.print(OPTION_STRING);
        try {
            command = input.nextLine();
            if (!command.equals("0")) {
                this.listVotingRecord();
            }
        } catch (InputMismatchException ime) {
            //volta para este menu caso os input esteja errado
            this.listVotingRecord();
            return;
        }
    }

    public void electionsResults() {
        int election = 0;
        Scanner input = new Scanner(System.in);

        ArrayList<Election> elections;
        while (true) {
            try {
                elections = this.rmiServer.getEndedElections();
                break;
            } catch (RemoteException | InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }
        if (elections.size() != 0) {
            while (!Utilitary.hasElection(election, elections)) {
                System.out.println("\tEscolha a eleição: ");
                Utilitary.listElections(elections);
                System.out.println("(" + RETURN + ")-  Voltar");
                System.out.print(OPTION_STRING);
                try {
                    election = input.nextInt();
                } catch (InputMismatchException ime) {
                    //volta para este menu caso os input esteja errado
                    this.electionsResults();
                    return;
                }
                if (election == 0) {
                    return;
                }
            }
            int blankVotes;
            while (true) {
                try {
                    blankVotes = this.rmiServer.getBlackVotes(election);
                    break;
                } catch (RemoteException | InterruptedException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
            float PercBlankVotes;
            while (true) {
                try {
                    PercBlankVotes = this.rmiServer.getPercentVotesCandidacy(election, -1);
                    break;
                } catch (RemoteException | InterruptedException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
            int NullVotes;
            while (true) {
                try {
                    NullVotes = this.rmiServer.getNullVotes(election);
                    break;
                } catch (RemoteException | InterruptedException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
            System.out.printf("\tVotos em branco: %d  (%.2f%%)\n", blankVotes, PercBlankVotes);
            System.out.println("\tVotos nulos: " + NullVotes);
            listCandidacyWithVotes(election);
        }
    }

    public void listCandidacyWithVotes(int id_election) {
        Scanner input = new Scanner(System.in);
        int command;
        while (true) {
            try {
                ArrayList<Candidacy> candidates;
                while (true) {
                    try {
                        candidates = this.rmiServer.getCandidacies(id_election);
                        break;
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        reconnectToRMI();
                    }
                }
                if (candidates != null) {
                    for (Candidacy cand : candidates) {
                        System.out.println("\tLista " + cand.getName());
                        System.out.println("\t\tNúmero total de votos: " + this.rmiServer.getVotesCandidacy(id_election, cand.getId()));
                        System.out.printf("\t\tPercentagem de votos: %.2f%%", this.rmiServer.getPercentVotesCandidacy(id_election, cand.getId()));
                        System.out.println("\n");
                    }
                } else {
                    System.out.println("\tSem candidatos");
                }
                while (true) {
                    System.out.println("(" + RETURN + ")-  Voltar");
                    System.out.print(OPTION_STRING);
                    try {
                        command = input.nextInt();
                    } catch (InputMismatchException ime) {
                        //volta para este menu caso os input esteja errado
                        this.listCandidacyWithVotes(id_election);
                        return;
                    }
                    if (command == 0) {
                        this.electionsResults();
                        return;
                    }//other cases if needed
                }
            } catch (RemoteException | InterruptedException e) {
                //e.printStackTrace(); //TODO TRATAR EXCEPCAO
                reconnectToRMI();
            }
        }
    }

    public void listElectionToManagePollingStation() {
        int election = -1;
        Scanner input = new Scanner(System.in);
        ArrayList<Election> elections;
        while (true) {
            try {
                elections = this.rmiServer.getElectionsNotStarted();
                break;
            } catch (RemoteException | InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }
        if (elections.size() != 0) {
            while (!Utilitary.hasElection(election, elections)) {
                System.out.println("\tEscolha a eleição: ");
                Utilitary.listElections(elections);
                System.out.println("(" + RETURN + ")-  Voltar");
                System.out.print(OPTION_STRING);
                try {
                    election = input.nextInt();
                } catch (InputMismatchException ime) {
                    //volta para este menu caso os input esteja errado
                    this.listElectionToManagePollingStation();
                    return;
                }
                if (election == 0) {
                    return;
                }
            }
            this.managePollingStation(election);
        } else System.out.println("Sem eleições por começar!");
    }

    public void managePollingStation(int election) {
        int option = -1;
        Scanner input = new Scanner(System.in);
        while (option < 0 || 2 < option) {
            System.out.println("\t(1)- Adicionar Mesa de Voto");
            System.out.println("\t(2)- Remover Mesa de Voto");
            System.out.println("(" + RETURN + ")-  Voltar");
            System.out.print(OPTION_STRING);
            try {
                option = input.nextInt();
            } catch (InputMismatchException ime) {
                //volta para este menu caso os input esteja errado
                this.managePollingStation(election);
                return;
            }
        }
        switch (option) {
            case 0:
                this.listElectionToManagePollingStation();
                break;
            case 1:
                this.addPollingStation(election);
                break;
            case 2:
                this.removePollingStation(election);
                break;
            default:
                break;
        }
    }

    private void addPollingStation(int election) {
        int mesaVoto = -1;
        Scanner input = new Scanner(System.in);
        while (true)
            try {
                ArrayList<Department> departments;
                while (true) {
                    try {
                        departments = this.rmiServer.selectNoAssociatedPollingStation(election);
                        break;
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        reconnectToRMI();
                    }
                }

                if (departments.size() == 0) {
                    System.out.println("Não existem mesas de voto para associar a esta eleição!");
                } else {
                    while (!hasDep(mesaVoto, departments)) {
                        System.out.println("Escolha a mesa de voto a adicionar");
                        Utilitary.listDepart(departments);
                        System.out.println("(" + RETURN + ")-  Voltar");
                        System.out.print(OPTION_STRING);
                        try {
                            mesaVoto = input.nextInt();
                        } catch (InputMismatchException ime) {
                            //volta para este menu caso os input esteja errado
                            this.addPollingStation(election);
                            return;
                        }
                        if (mesaVoto == 0) {
                            this.managePollingStation(election);
                            return;
                        }
                    }
                    while (true) {
                        try {
                            this.rmiServer.insertPollingStation(election, mesaVoto);
                            break;
                        } catch (InterruptedException e) {
                            //e.printStackTrace();
                            reconnectToRMI();
                        }
                    }
                }
                break;
            } catch (RemoteException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
    }

    private void removePollingStation(int election) {
        int mesaVoto = -1;
        Scanner input = new Scanner(System.in);
        while (true) {
            ArrayList<Department> departments;
            try {
                while (true) {
                    try {
                        departments = this.rmiServer.selectPollingStation(election);
                        break;
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        reconnectToRMI();
                    }
                }
                if (departments.size() == 0) {
                    System.out.println("Não existem mesas de voto associadas a esta eleição!");
                } else {
                    while (!hasDep(mesaVoto, departments)) {
                        System.out.println("Escolha a mesa de voto a remover: ");
                        Utilitary.listDepart(departments);
                        System.out.println("(" + RETURN + ")-  Voltar");
                        System.out.print(OPTION_STRING);
                        try {
                            mesaVoto = input.nextInt();
                        } catch (InputMismatchException ime) {
                            //volta para este menu caso os input esteja errado
                            this.removePollingStation(election);
                            return;
                        }
                        if (mesaVoto == 0) {
                            this.managePollingStation(election);
                            return;
                        }
                    }
                    while (true) {
                        try {
                            this.rmiServer.removePollingStation(mesaVoto);
                            break;
                        } catch (InterruptedException e) {
                            //e.printStackTrace();
                            reconnectToRMI();
                        }
                    }
                }
            } catch (RemoteException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }
    }

    /*
    MESMO METoDO QUE O DE BAIXO MAS PARA STRING EM INPUT, E VERIFICA SE A STRING é UM NUMERO
     */
    public boolean hasDep(String dep, ArrayList<Department> departments) {
        try {
            return this.hasDep(Integer.parseInt(dep), departments);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean hasDep(int dep, ArrayList<Department> departments) {
        for (Department department : departments) {
            if (department.getId() == dep) return true;
        }
        return false;
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
        String pass, address, nome, cargo = "0", ndep = "0";
        LocalDate cc_validity;
        int num_phone, num_cc;
        while (true) {
            try {
                System.out.print("Nome: ");
                nome = reader.readLine();
                while (!(cargo.equals("1") || cargo.equals("2") || cargo.equals("3"))) {
                    System.out.println("Cargo: ");
                    System.out.println("\t1 - Estudante");
                    System.out.println("\t2 - Docente");
                    System.out.println("\t3 - Funcionário");
                    System.out.print(OPTION_STRING);
                    cargo = input.nextLine();
                }
                System.out.print("Password: ");
                pass = reader.readLine();
                while (!(1 <= Integer.parseInt(ndep) && Integer.parseInt(ndep) <= 11)) {
                    do {
                        ArrayList<Department> departments = null;
                        while (true) {
                            try {
                                departments = this.rmiServer.getDepartments();
                                break;
                            } catch (RemoteException | InterruptedException e) {
                                //e.printStackTrace();
                                reconnectToRMI();
                            }
                        }
                        System.out.println("Departamento que frequenta: ");
                        Utilitary.listDepart(departments);
                        System.out.print(OPTION_STRING);
                        ndep = input.nextLine();
                    } while ((!Utilitary.isNumber(ndep)));
                }

                System.out.print("Número de telemóvel: ");
                num_phone = input.nextInt();
                while (!Utilitary.checkCorrectPhone(num_phone)) {
                    System.out.print("Número de telemóvel inválido! Insira novamente: ");
                    num_phone = input.nextInt();
                }

                System.out.print("Morada: ");
                address = reader.readLine();

                System.out.print("Número do cartão de cidadão: ");
                num_cc = input.nextInt();

                while (!Utilitary.checkCorrectCCNumber(num_cc)) {
                    System.out.print("Número de cartão de cidadão inválido! Insira novamente: ");
                    num_cc = input.nextInt();
                }

                System.out.print("Validade do cartão de cidadão (YYYY-MM-DD): ");
                while ((cc_validity = Utilitary.parseDate(reader.readLine())) == null) {
                    System.out.print("Data de validade do CC inválida, use este formato (YYYY-MM-DD): ");
                }

                boolean insertPers;
                while (true) {
                    try {
                        insertPers = this.rmiServer.insertPerson(nome, Utilitary.decideCargo(Integer.parseInt(cargo)), String.format("%s", (num_cc + pass).hashCode()), Integer.parseInt(ndep), num_phone, address, num_cc, cc_validity.toString());
                        break;
                    } catch (RemoteException | InterruptedException e) {
                        //e.printStackTrace(); //TODO TRATAR EXCEPCAO
                        reconnectToRMI();
                    }
                }
                if (!insertPers) {
                    System.out.println("Impossível inserir registo :(");
                } else {
                    System.out.println("Registo feito com sucesso! :)");
                }
                break;
            } catch (RemoteException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }
    }

    /**
     * Lê da consola a informação necessária para criar uma eleição
     * As eleições serão introduzidas na base de dados no servidor RMI, por questões de segurança
     *
     * @throws IOException exceção de I/O
     */
    public void createElection() throws IOException {
        String titulo, descricao, begin_data, end_data, restr = "", type_ele = "", ndep = "-1";
        Scanner input = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Início da Eleição (YYYY-MM-DD HH:mm): ");
        begin_data = reader.readLine();
        System.out.print("Fim da Eleição (YYYY-MM-DD HH:mm): ");
        end_data = reader.readLine();
        System.out.print("Título da Eleição: ");
        titulo = reader.readLine();
        System.out.print("Breve descrição: ");
        descricao = reader.readLine();
        while (!(restr.equals("1") || restr.equals("2"))) {
            System.out.println("Restringir Eleição?");
            System.out.println("\t(1)- Sim");
            System.out.println("\t(2)- Não");
            System.out.print(OPTION_STRING);
            restr = input.nextLine();
        }

        if (restr.equals("1")) {
            ArrayList<Department> departments;
            while (true) {
                try {
                    departments = this.rmiServer.selectPollingStation(-1);
                    break;
                } catch (RemoteException | InterruptedException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
            while (!hasDep(ndep, departments)) {
                System.out.println("Departamento: ");
                Utilitary.listDepart(departments);
                System.out.print(OPTION_STRING);
                ndep = input.nextLine();
            }
        }

        while (!(type_ele.equals("1") || type_ele.equals("2") || type_ele.equals("3"))) {
            System.out.println("Tipo de eleição: ");
            System.out.println("\t1 - Estudante");
            System.out.println("\t2 - Docente");
            System.out.println("\t3 - Funcionário");
            System.out.print(OPTION_STRING);
            type_ele = input.nextLine();
        }
        int id;
        while (true) {
            try {
                id = this.rmiServer.insertElection(begin_data, end_data, titulo, descricao, Utilitary.decideCargo(Integer.parseInt(type_ele)));
                break;
            } catch (RemoteException | InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }

        if (id == -1) {
            System.out.println("Impossível inserir eleição :(");
        } else {
            System.out.println("Eleição criada com sucesso! :)");
            while (true) {
                try {
                    this.rmiServer.insertElectionDepartment(id, Integer.parseInt(ndep));
                    break;
                } catch (RemoteException | InterruptedException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
        }
    }

    private void addCandidacy(Election election) {
        Scanner input = new Scanner(System.in);
        System.out.println("========ADICIONAR LISTA=======");
        System.out.print("Nome: ");
        while (true) {
            try {
                this.rmiServer.insertCandidacyIntoElection(input.nextLine(), election.getType(), election.getId());
                break;
            } catch (InterruptedException | RemoteException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }
    }

    private void listElectionsToManage() {
        int command = -1;
        while (command != RETURN) {
            try {
                /*
                 * LISTAR ELEICOES
                 */
                System.out.println("==========GERIR ELEICOES==========");
                ArrayList<Election> elections;
                while (true) {
                    try {
                        elections = this.rmiServer.getElections();
                        break;
                    } catch (RemoteException | InterruptedException e) {
                        //e.printStackTrace();
                        reconnectToRMI();
                    }
                }
                Utilitary.listElectionsByIndex(elections);
                System.out.println("(" + RETURN + ")-  Voltar");
                /*
                 * ESPERAR PELA ESCOLHA DO UTILIZADOR
                 */
                Scanner input = new Scanner(System.in);
                System.out.print(OPTION_STRING);
                command = input.nextInt();
                if (command != RETURN) {
                    if (0 < command && command <= elections.size()) {
                        this.manageElection(elections.get(command - 1));
                    } else {
                        //volta para este menu caso o numero do comando esteja errado
                        this.listElectionsToManage();
                    }
                }
            } catch (InputMismatchException ime) {
                //volta para este menu caso o input esteja errado
                this.listElectionsToManage();
            }
        }
    }

    private void manageElection(Election election) {
        int command = -1;
        while (command != RETURN) {
            try {
                /*
                 * LISTAR LISTAS CANDIDATAS
                 */
                System.out.println(election.toString());
                ArrayList<Candidacy> candidacies;
                while (true) {
                    try {
                        candidacies = this.rmiServer.getCandidacies(election.getId());
                        break;
                    } catch (RemoteException | InterruptedException e) {
                        //e.printStackTrace();
                        reconnectToRMI();
                    }
                }
                if (candidacies.size() > 0) {
                    System.out.println("Ver lista:");
                    for (Candidacy c : candidacies) {
                        System.out.printf("\t(%s)- %s%n", candidacies.indexOf(c) + 1, c.getName());
                    }
                } else {
                    System.out.println("A eleição não tem listas\n");
                }
                System.out.println("(" + EDIT + ")- Editar");
                System.out.println("(" + ADD + ")- Adicionar lista");
                System.out.println("(" + RETURN + ")-  Voltar");
                /*
                 * ESPERAR PELA ESCOLHA DO UTILIZADOR
                 */
                Scanner input = new Scanner(System.in);
                System.out.print(OPTION_STRING);
                command = input.nextInt();
                switch (command) {
                    case EDIT:
                        //verificar se a data é antes da atual
                        if (election.getBegin().isBefore(LocalDateTime.now())) {
                            this.editElection(election);
                        } else {
                            System.out.println("⚠️ Não é possivel editar eleições a decorrer/passadas!");
                            this.manageElection(election);
                            return;
                        }
                        break;
                    case ADD:
                        this.addCandidacy(election);
                        break;
                    case RETURN:
                        break;
                    default:
                        if (0 < command && command <= candidacies.size()) {
                            this.manageCandidacy(candidacies.get(command - 1));
                        } else {
                            //volta para este menu caso o numero do comando esteja errado
                            this.manageElection(election);
                        }
                        break;
                }
            } catch (InputMismatchException ime) {
                //volta para este menu caso os input esteja errado
                this.manageElection(election);
            }
        }
    }

    private void editElection(Election election) {
        System.out.println(election.toString());
        //opcoes
        System.out.println("Editar:");
        System.out.println("\t(1)- Nome");
        System.out.println("\t(2)- Tipo");
        System.out.println("\t(3)- Descricao");
        System.out.println("\t(4)- Data Inicio");
        System.out.println("\t(5)- Data Fim");
        System.out.println("(" + RETURN + ")-  Voltar");
        //esperar pelo input
        Scanner input = new Scanner(System.in);
        System.out.print(OPTION_STRING);
        String command = input.nextLine();
        if (command.equals("" + RETURN + "")) {
            //VAZIO PARA VOLTAR
        } else if (command.equals("1")) {
            System.out.println("Editar titulo:");
            election.setTitle(input.next());
            while (true) {
                try {
                    this.rmiServer.updateElections(election);
                    break;
                } catch (InterruptedException | RemoteException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
        } else if (command.equals("2")) {
            int type = -1;
            while (type > 3 || 0 > type) {
                System.out.println("Editar tipo de eleição:");
                System.out.println("\t(1)- Estudante");
                System.out.println("\t(2)- Docente");
                System.out.println("\t(3)- Funcionario");
                System.out.println("(0)-  Voltar");
                type = input.nextInt();
                if (type == RETURN) {
                    this.editElection(election);
                } else {
                    election.setType(type);
                }
            }
            election.setType(type - 1);
            while (true) {
                try {
                    this.rmiServer.updateElections(election);
                    break;
                } catch (InterruptedException | RemoteException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
        } else if (command.equals("3")) {
            System.out.println("Editar descricao:");
            election.setDescription(input.next());
            while (true) {
                try {
                    this.rmiServer.updateElections(election);
                    break;
                } catch (InterruptedException | RemoteException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
        } else if (command.equals("4")) {
            System.out.println("Editar data de inicio (YYYY-MM-DD HH:mm:SS):");
            while (!election.setBegin(input.next(), input.next()))
                System.out.println("Data invalida - formato (YYYY-MM-DD HH:mm:SS)");
            while (true) {
                try {
                    this.rmiServer.updateElections(election);
                    break;
                } catch (InterruptedException | RemoteException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
        } else if (command.equals("5")) {
            System.out.println("Editar data de fim (YYYY-MM-DD HH:mm:SS):");
            while (!election.setEnd(input.next(), input.next()))
                System.out.println("Data invalida - formato (YYYY-MM-DD HH:mm:SS)");
            while (true) {
                try {
                    this.rmiServer.updateElections(election);
                    break;
                } catch (InterruptedException | RemoteException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
        } else {//volta para este menu caso esteja algo errado
            this.editElection(election);
        }
    }

    private void manageCandidacy(Candidacy candidacy) {
        int command = -1;
        while (command != RETURN) {
            System.out.println(candidacy.toString());

            ArrayList<Person> people;
            while (true) {
                try {
                    people = this.rmiServer.getPeople(candidacy.getId());
                    break;
                } catch (InterruptedException | RemoteException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
            try {
                //listar pessoas
                if (people.size() > 0) {
                    System.out.println("Remover Sra./Sr.:");
                    for (Person p : people) {
                        System.out.printf("\t(%s)- %s (%s)\n", people.indexOf(p) + 1, p.getName(), p.getCensoredCc_number(4));
                    }
                } else {
                    System.out.println("A lista não tem pessoas\n");
                }
                System.out.println("(" + REMOVE + ")- Remover lista");
                System.out.println("(" + ADD + ")- Adicionar pessoa");
                System.out.println("(" + RETURN + ")-  Voltar");
                //esperar pelo input
                Scanner input = new Scanner(System.in);
                System.out.print(OPTION_STRING);
                command = input.nextInt();
                switch (command) {
                    case ADD:
                        System.out.println("Numero do Cartao de Cidadao da pessoa:");
                        while (true) {
                            try {
                                this.rmiServer.insertPersonIntoCandidacy(candidacy.getId(), input.nextInt());
                                break;
                            } catch (RemoteException | InterruptedException e) {
                                //e.printStackTrace();
                                reconnectToRMI();
                            }
                        }
                        break;
                    case REMOVE:
                        while (true) {
                            try {
                                this.rmiServer.removeOnDB("candidacy", "id", candidacy.getId());
                                break;
                            } catch (RemoteException | InterruptedException e) {
                                //e.printStackTrace();
                                reconnectToRMI();
                            }
                        }
                        while (true) {
                            try {
                                this.rmiServer.removeOnDB("candidacy_person", "candidacy_id", candidacy.getId());
                                break;
                            } catch (RemoteException | InterruptedException e) {
                                //e.printStackTrace();
                                reconnectToRMI();
                            }
                        }
                        command = RETURN;//para voltar ao menu das eleicoes
                        break;
                    case RETURN:
                        //VAZIO PARA VOLTAR
                        break;
                    default:
                        if (0 < command && command <= people.size()) {
                            while (true) {
                                try {
                                    this.rmiServer.removeOnDB("candidacy_person", "person_cc_number", people.get(command - 1).getCc_number());
                                    break;
                                } catch (RemoteException | InterruptedException e) {
                                    //e.printStackTrace();
                                    reconnectToRMI();
                                }
                            }
                        } else {
                            //volta para este menu caso esteja algo errado
                            this.manageCandidacy(candidacy);
                        }
                        break;
                }
            } catch (InputMismatchException ime) {
                //volta para este menu caso o input esteja errado
                this.manageCandidacy(candidacy);
            }
            break;
        }
    }

    public void reconnectToRMI() {
        while (true) {
            try {
                RMI rmiServer = (RMI) LocateRegistry.getRegistry(RMIServer.RMI_PORT).lookup("admin");
                admin.rmiServer = rmiServer;
                break;
            } catch (NotBoundException | IOException remoteException) {
                remoteException.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            RMI rmiServer = (RMI) LocateRegistry.getRegistry(7000).lookup("admin");
            String message = rmiServer.saySomething();
            System.out.println("Hello Admin: " + message);
            admin = new AdminConsole(rmiServer);
            admin.admin(-1);
        } catch (Exception e) {
            String message;
            while (true) {
                try {
                    RMI rmiServer = (RMI) LocateRegistry.getRegistry(7000).lookup("admin");
                    while (true) {
                        try {
                            message = rmiServer.saySomething();
                            break;
                        } catch (InterruptedException interruptedException) {
                            //interruptedException.printStackTrace();
                        }
                    }
                    System.out.println("Hello Admin: " + message);
                    admin = new AdminConsole(rmiServer);
                    admin.admin(-1);
                    break;
                } catch (NotBoundException | IOException remoteException) {
                    //remoteException.printStackTrace();
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (Exception ignored) {
                    }
                    main(args);
                }
            }
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
