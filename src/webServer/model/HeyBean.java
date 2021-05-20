package webServer.model;

import pt.uc.dei.student.elections.*;
import pt.uc.dei.student.others.*;
import webServer.ws.WebSocket;

import javax.lang.model.type.ArrayType;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class HeyBean {
    private RMI server;
    /**
     * Guarda o número de cartão de cidadão de uma pessoa, serve como identificação única, como por exemplo para inicio de sessão
     */
    private int ccnumber; // username and password supplied by the user
    /**
     * Guarda a password de uma determinada pessoa, serve para inicio de sessão
     */
    private String password;
    /**
     * Guarda o id de uma determinada eleição que uma determinada pessoa escolheu para votar
     */
    private int election_id;
    /**
     * Guarda o id da lista que o eleitor votou
     */
    private int candidacy_id;

    // Fields for register
    /**
     * Guarda o nome de um utilizador aquando o seu registo
     */
    private String name;

    /**
     * Guarda o cargo de um utilizador aquando o seu registo
     */
    private String cargo;

    /**
     * Guarda a morada de um utilizador aquando o seu registo
     */
    private String address;

    /**
     * Guarda a data do cartão de cidadão de um utilizador aquando o seu registo
     */
    private String ccDate;

    /**
     * Guarda o número de telemóvel de um utilizador aquando o seu registo
     */
    private int phone;

    /**
     * Guarda o departamento de um utilizador aquando o seu registo
     */
    private int dep;

    // Fields for create an election

    /**
     * Guarda o título de uma eleição aquando o sua criação
     */
    private String title;

    /**
     * Guarda a descrição de uma eleição aquando o sua criação
     */
    private String description;

    /**
     * Guarda o tipo de uma eleição aquando o sua criação
     */
    private String type;

    /**
     * Guarda a data de início de uma eleição aquando o sua criação
     */
    private String iniDate;

    /**
     * Guarda a data de fim de uma eleição aquando o sua criação
     */
    private String fimDate;

    /**
     * Guarda se uma eleição tem ou não restrição quanto ao departmento aquando o sua criação
     */
    private String restriction;

    // Fields for candidacies
    /**
     * Guarda o nome de uma lista
     */
    private String candidacy_name;

    /**
     * Guarda o tipo de uma lista
     */
    private String candidacy_type;
    // Field for add a list to an election
    /**
     * Guarda o nome de uma nova lista a ser adicionada a uma eleição
     */
    private String list_name;
    /**
     * Guarda o cartão de cidadão de uma determinada pessoa que se candidata como membro a uma lista
     */
    // Field for add a person to a list
    /**
     * Guarda o cartão de cidadão de um utilizador para poder ser adicionado com membro de uma lista
     */
    private int person_cc;
    //Fields for see details election

    /**
     * Guarda o link a ser redirecionado aqundo ações feitas com a API Rest
     */
    private String authorizationURL;

    /**
     * Guarda o núemro de votos nulos de uma eleição
     */
    private int null_votes;

    /**
     * Guarda o núemro de votos brancos de uma eleição
     */
    private int blank_votes;

    /**
     * Guarda a percentagem de votos nulos de uma eleição
     */
    private float null_percent;

    /**
     * Guarda a percentagem de votos brancos de uma eleição
     */
    private float blank_percent;

    /**
     * Guarda o id de um departamento
     */
    private int department_id;

    /**
     * Conecta-se ao RMI
     */
    public HeyBean() {
        // Connect to RMI Server
        try {
            server = (RMI) LocateRegistry.getRegistry("127.0.0.1", 7000).lookup("server");
//            server = (RMI) Naming.lookup("server");
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter para o id de um departamento
     *
     * @return id do departamento
     */
    public int getDepartment_id() {
        return department_id;
    }

    /**
     * Setter do id do departamento
     *
     * @param department_id id do departamento
     */
    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    /**
     * Getter da percentagem de votos nulos
     *
     * @return percentagem de votos nulos
     */
    public float getNull_percent() {
        return null_percent;
    }

    /**
     * Getter da percentagem de votos brancos
     *
     * @return percentagem de votos brancos
     */
    public float getBlank_percent() {
        return blank_percent;
    }

    /**
     * Getter do número de votos nulos
     *
     * @return número de votos nulos
     */
    public int getNull_votes() {
        return null_votes;
    }

    /**
     * Getter do número de votos brancos
     *
     * @return número de votos brancos
     */
    public int getBlank_votes() {
        return blank_votes;
    }

    /**
     * Getter do id do departamento
     *
     * @return id do departamento
     */
    public int getCandidacy_id() {
        return candidacy_id;
    }

    /**
     * @return Devolve o número de cartão de cidadão de uma pessoa caso seja necessário apresentar na view
     */
    public int getPerson_cc() {
        return person_cc;
    }

    /**
     * Getter do número de cartão de cidadão
     *
     * @return número do cartão de cidadão
     */
    public int getCcnumber() {
        return ccnumber;
    }

    /**
     * @return Devolve o nome da lista adicionada caso seja necessário apresentar na view
     */
    public String getList_name() {
        return list_name;
    }

    /**
     * @return Devolve o nome da lista selecionada caso seja necessário apresentar na view
     */
    public String getCandidacy_name() {
        return candidacy_name;
    }

    /**
     * @return Devolve o tipo da lista selecionada caso seja necessário apresentar na view
     */
    public String getCandidacy_type() {
        return candidacy_type;
    }

    /**
     * @return Devolve o titulo da eleição selecionada caso seja necessário apresentar na view
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return Devolve a descrição da eleição selecionada caso seja necessário apresentar na view
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Devolve o tipo da eleição selecionada caso seja necessário apresentar na view
     */
    public String getType() {
        return type;
    }

    /**
     * @return Devolve a data inicial da eleição selecionada caso seja necessário apresentar na view
     */
    public String getIniDate() {
        return iniDate;
    }

    /**
     * @return Devolve a data final da eleição selecionada caso seja necessário apresentar na view
     */
    public String getFimDate() {
        return fimDate;
    }

    /**
     * Getter do URL para redirecionamento
     *
     * @return URL
     */
    public String getAuthorizationURL() {
        return authorizationURL;
    }

    /**
     * Setter de número de votos nulos
     *
     * @param null_votes número de votos nulos
     */
    public void setNull_votes(int null_votes) {
        this.null_votes = null_votes;
    }

    /**
     * Setter no número de votos brancos
     *
     * @param blank_votes número de votos brancos
     */
    public void setBlank_votes(int blank_votes) {
        this.blank_votes = blank_votes;
    }

    /**
     * Setter do nome de um utilizador
     *
     * @param name nome de utilizaor
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter do cargo de um utilizador
     *
     * @param cargo cargo do utilizador
     */
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    /**
     * Setter do departamento de um utilizador
     *
     * @param dep departamento do utilizador
     */
    public void setDep(int dep) {
        this.dep = dep;
    }

    /**
     * Setter da morada de um utilizador
     *
     * @param address morada do utilizador
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Setter do número de telefone de um utilizador
     *
     * @param phone telefone do utilizador
     */
    public void setPhone(int phone) {
        this.phone = phone;
    }

    /**
     * Setter da validade do cartão de cidadão de um utilizador
     *
     * @param ccDate validade do cartão de cidadão de um utilizador
     */
    public void setCcDate(String ccDate) {
        this.ccDate = ccDate;
    }

    /**
     * Setter do número de cartão de cidadão de um utilizador
     *
     * @param ccnumber número cartão de cidadão de um utilizador
     */
    public void setCcnumber(int ccnumber) {
        this.ccnumber = ccnumber;
    }

    /**
     * Setter da password de um utilizador
     *
     * @param password password de um utilizador
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Setter do id de uma eleição
     *
     * @param election_id id de uma eleição
     */
    public void setElection_id(int election_id) {
        this.election_id = election_id;
    }

    /**
     * Setter do id de uma lista
     *
     * @param candidacy_id id de uma lista
     */
    public void setCandidacy_id(int candidacy_id) {
        this.candidacy_id = candidacy_id;
    }

    /**
     * Setter do título de uma eleição
     *
     * @param title título de uma eleição
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Setter da descrição de uma eleição
     *
     * @param description decrição de uma eleição
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Setter da tipo de uma eleição
     *
     * @param type tipo de uma eleição
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Setter da data de início de uma eleição
     *
     * @param iniDate data de início de uma eleição
     */
    public void setIniDate(String iniDate) {
        this.iniDate = iniDate;
    }

    /**
     * Setter da data de fim de uma eleição
     *
     * @param fimDate data de fim de uma eleição
     */
    public void setFimDate(String fimDate) {
        this.fimDate = fimDate;
    }

    /**
     * Setter do nome da lista
     *
     * @param list_name nome da lista
     */
    public void setList_name(String list_name) {
        this.list_name = list_name;
    }

    /**
     * Setter da restrição de uma eleição
     *
     * @param restriction restrição de uma eleição
     */
    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    /**
     * Setter do nome de uma lista
     *
     * @param candidacy_name nome da lista
     */
    public void setCandidacy_name(String candidacy_name) {
        this.candidacy_name = candidacy_name;
    }

    /**
     * Setter do tipo de uma lista
     *
     * @param candidacy_type tipo de uma lista
     */
    public void setCandidacy_type(String candidacy_type) {
        this.candidacy_type = candidacy_type;
    }

    /**
     * Setter do cartão de cidadão de uma pessoa
     *
     * @param person_cc cartão de cidadão de uma pessoa
     */
    public void setPerson_cc(int person_cc) {
        this.person_cc = person_cc;
    }

    /**
     * Setter do URL para rederecionamento
     *
     * @param authorizationURL URL
     */
    public void setAuthorizationURL(String authorizationURL) {
        this.authorizationURL = authorizationURL;
    }

    /**
     * Procura uma determinada pessoa através do token do facebook
     *
     * @param FbId token do facebook
     * @return Pessoa caso exista ou null caso não exista
     * @throws RemoteException falha no RMI
     */
    public Person getUserFb(String FbId) throws RemoteException {
        Person p = null;
        try {
            p = server.getPersonFb(FbId);
        } catch (
                InterruptedException e) {
            e.printStackTrace();
        }
        return p;
    }

    /**
     * Procura uma determinada pessoa através do número de cartão de cidadão e password
     *
     * @return Pessoa caso exista, null caso contrário
     * @throws RemoteException falha no RMI
     */
    public Person getUser() throws RemoteException {
        Person p = null;
        try {
            p = server.getPerson(String.valueOf(this.ccnumber), password);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return p;
    }

    /**
     * Chama um método no RMI que devolve todas as eleições a decorrer no momento na base de dados
     *
     * @return lista com todas as eleições a decorrer no momento
     */
    public ArrayList<Election> getElections() {
        ArrayList<Election> elections = null;
        try {
            elections = server.getCurrentElectionsPerson(String.valueOf(this.ccnumber));
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return elections;
    }

    /**
     * Chama um método no RMI que devolve todas as eleições presentes na base de dados
     * Serve para consultar os detalhes de eleições passadas
     * Modificar detalhes de uma determinada eleição que ainda não tenha começado
     * Consultar os detalhes das listas de uma determinada eleição, mesmo que esta esteja a decorrer no momento
     *
     * @return lista com todas as eleições presentes na base de dados
     */
    public ArrayList<Election> getAllElections() {
        ArrayList<Election> allElections = null;
        try {
            allElections = server.getElections();
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return allElections;
    }

    /**
     * Chama um método no RMI que devolve as listas de uma determinada eleição
     *
     * @return lista com as candidaturas a uma determinada eleição
     */
    public ArrayList<Candidacy> getCandidacies() {
        ArrayList<Candidacy> candidacies = null;
        try {
            candidacies = server.getCandidacies(this.election_id);
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return candidacies;
    }

    /**
     * Chama um método no RMI que devolve todos os departamentos da Universidade de Coimbra
     * Serve para listar os departamentos, quando uma pessoa se está a registar escolher o departamento que frequenta, por exemplo
     *
     * @return lista com todos os departamentos da Universidade de Coimbra
     */
    public ArrayList<Department> getDepartments() {
        ArrayList<Department> departments = null;
        try {
            departments = server.getDepartments();
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return departments;
    }

    /**
     * Chama método no RMI para obter todos os registos de voto
     *
     * @return ArrayList com todos os registos de voto
     */
    public ArrayList<VotingRecord> getVotingRecords() {
        ArrayList<VotingRecord> votingRecords = null;
        try {
            votingRecords = server.getVotingRecords();
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return votingRecords;
    }

    /**
     * Chama método no RMI para obter todas as eleições que ainda não tenham começado
     *
     * @return ArrayList com todas as eleições que ainda não começaram
     */
    public ArrayList<Election> getElectionsNotStarted() {
        ArrayList<Election> electionNotStarted = null;
        try {
            electionNotStarted = server.getElectionsNotStarted();
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return electionNotStarted;
    }

    /**
     * Chama método no RMI que devolve todas as mesas de voto que estejama tivas
     *
     * @return ArrayList com todos os departamentos que têm mesa de voto ativa
     */
    public ArrayList<Department> getNonAssociativePollingStations() {
        ArrayList<Department> nonAssociativePollingStations = null;
        try {
            nonAssociativePollingStations = server.selectNoAssociatedPollingStation(this.election_id);
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return nonAssociativePollingStations;
    }

    /**
     * Chama um método no RMI que devolve todos os membros pertencentes a uma determianda lista
     *
     * @return lista com os membros de uma determinada lista
     */
    public ArrayList<Person> getCandidaciesPeople() {
        ArrayList<Person> candidaciesPeople = null;
        try {
            candidaciesPeople = server.getPeople(this.candidacy_id);
        } catch (RemoteException | InterruptedException remoteException) {
            remoteException.printStackTrace();
        }
        return candidaciesPeople;
    }

    /**
     * Chama um método no RMI que dá update à base de dados aquando a realização de um voto
     * Na base de dados fica que o voto foi realizado online
     */
    public void updateVotes() {
        try {
            if (this.candidacy_id == -1) {
                server.updateNullVotes(String.valueOf(this.election_id), String.valueOf(this.ccnumber), String.valueOf(12));
            } else if (this.candidacy_id == -2) {
                server.updateBlankVotes(String.valueOf(this.election_id), String.valueOf(this.ccnumber), String.valueOf(12));
            } else {
                server.updateCandidacyVotes(String.valueOf(this.election_id), String.valueOf(this.candidacy_id), String.valueOf(this.ccnumber), String.valueOf(12));
            }
            server.updateAllVotes(this.election_id);
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Chama método no RMI que verifica na base de dados se já existe algum registo
     * que uma determinada pessoa já tenha votado numa determinada eleição
     *
     * @return se já tiver votado devolve true, caso contrário devolve false
     */
    public boolean checkIfAlreadyVotes() {
        try {
            return server.checkIfAlreadyVote(this.ccnumber, this.election_id);
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Chama método no RMI que tenta inserir na base de dados um novo utilizador aquando o seu registo
     *
     * @return devolve true ou false caso tenha tido sucesso ou não, respetivamente
     */
    public boolean insertRegister() {
        try {
            return server.insertPerson(this.name, this.cargo, this.password, this.dep, this.phone, this.address, this.ccnumber, this.ccDate);
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Chama método no RMI que tenta inserir na base de dados uma nova eleição aquando a sua criação
     *
     * @return devolve true ou false caso tenha tido sucesso ou não, respetivamente
     */
    public int insertElection() {
        int id_election = -1;
        try {
            id_election = server.insertElection(this.iniDate, this.fimDate, this.title, this.description, this.type);
            if (this.restriction.equals("yes")) {
                server.insertElectionDepartment(id_election, this.dep);
            } else
                server.insertElectionDepartment(id_election, -1);
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return id_election;
    }

    /**
     * Verifica se uma determinada lista pertence realmente à eleição em questão
     * Verifica se uma determinada pessoa pode votar numa determinada eleição
     * Previne que alguem tente votar (caso não possa) passando logo para a página
     * se escolher as listas e não ter passado pela página de escolher uma eleição
     *
     * @return se já tiver votado devolve true, caso contrário devolve false
     * se a lista não pertencer à eleição especifica devolve false
     */
    public boolean checkIfAlreadyVoteOnVoteForm() {
        int election_id;
        try {
            election_id = server.getElectionFromCandidacy(this.candidacy_id);
            // Pra simplificar considera-se que caso uma elição não exista o utilizador já tenha votado nela
            if (election_id == 0 && server.checkIfAlreadyVote(this.ccnumber, election_id))
                return true;
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Chama método no RMI que devolve o título de uma eleição através de uma determinada lista dessa mesma eleição
     *
     * @return título da eleição
     */
    public String getTitleElection() {
        try {
            return server.getElectionTitleFromCandidacy(this.candidacy_id);
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Verifica se uma determinada eleição existe
     * Previne que o utilizador não vá diretamente à pagina de uma determinada eleição sem passar pelo menu de a escolher primeiro
     *
     * @return true ou false consoante exista ou não, respetivamente
     */
    public boolean checkIfSelectedElectionExists() {
        return Utilitary.hasElection(this.election_id, getAllElections());
    }

    /**
     * Verifica se uma determinada lista existe e se pertence a uma determinada eleição
     * Previne que o utilizador não vá diretamente à pagina de uma determinada lista sem passar pelo menu de escolher primeiro a eleição e a lista
     *
     * @return true ou false consoante exista ou não, respetivamente
     */
    public boolean checkSelectedCandidacy_Election() {
        try {
            if (server.checkElectionHasCandidacy(this.election_id, this.candidacy_id) != 0) {
                return true;
            }
        } catch (RemoteException | InterruptedException remoteException) {
            remoteException.printStackTrace();
        }
        return false;
    }

    /**
     * Verifica se uma determinada eleição ainda não começou e se por isso pode ser editada
     *
     * @return devolve 1 caso ainda não tenha começado ou 0 se já tiver começado
     */
    public int checkIfCanEdit() {
        try {
            return server.checkIfElectionNotStarted(this.election_id);
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Chama método no RMI para atualizar uma determinda eleição
     *
     * @return devolve true se conseguir atualizar ou false caso contrário
     */
    public boolean editElection() {
        if (checkIfSelectedElectionExists()) {
            try {
                return server.updateElectionOnEdit(this.election_id, this.title, this.type, this.description, this.iniDate, this.fimDate);
            } catch (RemoteException | InterruptedException remoteException) {
                remoteException.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Chama método no RMI para adicionar uma lista a uma determinada eleição
     *
     * @return devolde false se ela já tiver começado e por isso não se poder adicionar lista ou true caso tenha conseguido adcionar
     */
    public boolean addListToAnElection() {
        try {
            if (server.checkIfElectionNotStarted(this.election_id) != 0) {
                return server.insertCandidacyIntoElection(this.list_name, this.type, this.election_id);
            }
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Chama método no RMI que verifica se uma determinada pessoa existe
     * Se existir adiciona essa pessoa como membro de uma determinada lista
     *
     * @return devolve erro caso não tenha sido adicionada ou null caso a pessoa não exista
     */
    public String checkIfPersonExists() {
        try {
            String name_person = server.checkIfPersonExists(this.person_cc);
            if (name_person != null) {
                return server.insertPersonIntoCandidacy(this.election_id, this.candidacy_id, this.person_cc);
            }
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //TODO CONTINUAR COM JAVADOC

    public ArrayList<Election> getEndedElections() {
        ArrayList<Election> endedElections = null;
        try {
            endedElections = server.getEndedElections();
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return endedElections;
    }

    public ArrayList<Candidacy> getCandidaciesWithVotes() {
        ArrayList<Candidacy> candidaciesWithVotes = null;
        try {
            candidaciesWithVotes = server.getCandidaciesWithVotes(this.election_id);
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return candidaciesWithVotes;
    }

    public boolean associateFbId(String fbId) {
        if (getAssociatedFbId() == null) {
            try {
                return server.associateFbId(this.ccnumber, fbId);
            } catch (RemoteException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public String getAssociatedFbId() {
        String associatedFbId = null;
        try {
            return server.getAssociatedFbId(this.ccnumber);
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkIfIsAdmin() {
        try {
            if (server.checkIfIsAdmin(this.ccnumber) == 1) {
                return true;
            }
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setNull_percent(float null_percent) {
        this.null_percent = null_percent;
    }

    public void setBlank_percent(float blank_percent) {
        this.blank_percent = blank_percent;
    }

    /*******************************************ESTADO MESAS DE VOTO****************************************/
    /**
     * Ativa o notifier para o estado das mesas de voto e respetivos terminais
     * @param NOTIFIER notifier do estado das mesas de voto e respetivos terminais
     */
    public void setRealTimePollingStationOn(NotifierCallBack NOTIFIER) {
        while (true) {
            try {
                this.server.initializeRealTimePolls(NOTIFIER);
                break;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Desativa o notifier para o estado das mesas de voto e respetivos terminais
     * @param NOTIFIER notifier do estado das mesas de voto e respetivos terminais
     */
    public void setRealTimePoolingStationOff(NotifierCallBack NOTIFIER) {
        try {
            this.server.endRealTimeInfo(NOTIFIER);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * Devolve informação sobre as mesas de voto e respetivos termianais até ao momento
     * @return informação sobre as mesas de voto e respetivos termianais
     */
    public String getInfoPollingStations() {
        ArrayList<InfoPolls> info = null;
        try {
            info = server.getInfoPolls();
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        String strWeb = "";
        String last = "";
        for (InfoPolls i : info) {
            if (!i.getDep_name().equals(last)) {
                if (i.getStatusPoll() == 1) {
                    strWeb = String.format("%s<label>%s 🟢</label>", strWeb, i.getDep_name());
                } else {
                    strWeb = String.format("%s<label>%s 🔴</label>", strWeb, i.getDep_name());
                }
            }
            last = i.getDep_name();
            if (i.getTerminal_id() != 0) {
                if (i.getStatusTerminal() == 0) {
                    strWeb = String.format("%s<p>Terminal #%s 🔴</p>", strWeb, i.getTerminal_id());
                } else {
                    strWeb = String.format("%s<p>Terminal #%s 🟢</p>", strWeb, i.getTerminal_id());
                }
            }
        }
        strWeb = strWeb.replace("</label><label>", "</label><p></p><label>");
        return strWeb;
    }

    /*******************************************VOTOS EM TEMPO REAL****************************************/

    /**
     * Ativa o callback para os votos em tempo real
     * @param NOTIFIER notifier dos votos em tempo real
     */
    public void setRealTimeVotesOn(NotifierCallBack NOTIFIER) {
        while (true) {
            try {
                this.server.initializeRealTimeVotes(NOTIFIER);
                break;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Desativa o callback para os votos em tempo real
     * @param NOTIFIER notifier dos votos em tempo real
     */
    public void setRealTimeVotesOff(NotifierCallBack NOTIFIER) {
        try {
            this.server.endRealTimeInfo(NOTIFIER);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Devolve informação sobre os votos até ao momento
     * @return info sobre os votos
     */
    public String getInfoVotes() {
        ArrayList<InfoElectors> info = null;
        try {
            info = server.getInfoElectors();
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        String strWeb = "";
        if (info != null && info.size() > 0) {
            info.sort(Comparator.comparing(InfoElectors::getElection_title));
            String before = "";
            for (InfoElectors i : info) {
                if (!before.equals(i.getElection_title())) {
                    strWeb = String.format("%s<label>%s</label>", strWeb, i.getElection_title());
                }
                strWeb = String.format("%s<p>%s: %s voto(s)</p>", strWeb, i.getDep_name(), i.getCount());
                before = i.getElection_title();
            }
        } else {
            strWeb = "<p>Sem dados para apresentar atualmente.<p>";
        }
        return strWeb;
    }

    public boolean checkIfPollingStationIsActive() {
        ArrayList<Department> pollingStations;
        try {
            pollingStations = server.getPollingStation();
            if (pollingStations != null && Utilitary.hasDep(this.department_id, pollingStations))
                return true;
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addPollingStation() {
        try {
            return server.insertPollingStation(this.election_id, this.department_id);
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}