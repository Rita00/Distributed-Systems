package pt.uc.dei.student.others;

import pt.uc.dei.student.elections.*;

import java.rmi.*;
import java.util.ArrayList;

/**
 * Interface do RMI
 *
 * @author Ana Rita Rodrigues
 * @author Dylan Gonçalves Perdigão
 * @see Remote
 */
public interface RMI extends Remote {
    /**
     * Manda mensagem para dizer que está a funcionar
     *
     * @return "I'm alive!"
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    String saySomething() throws java.rmi.RemoteException, InterruptedException;

    /**
     * Verifica se uma determinada mesa de voto se pode ligar <br>
     * Pode-se ligar, caso o máximo de mesas de voto ativas ainda não tenha sido atingido
     * e o departamento escolhido ainda não tenha uma mesa de voto ativa
     *
     * @param dep_id   ID do departamento
     * @param NOTIFIER notifier
     * @return nome da mesa de voto criada ou null
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    String initializeMulticast(int dep_id, Notifier NOTIFIER) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Adiciona callback de admin à lista de callbacks para envio de informação de tempo real sobre os votos
     * e envia de imediato a informação mais recente disponível
     *
     * @param NOTIFIER notifier
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void initializeRealTimeVotes(Notifier NOTIFIER) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Adiciona callback de admin à lista de callbacks para envio de informação de tempo real sobre as mesas de voto
     * e envia de imediato a informação mais recente disponível
     *
     * @param NOTIFIER notifier
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void initializeRealTimePolls(Notifier NOTIFIER) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Remove callback de admin da lista de callbacks para envio de informação de tempo real sobre as mesas de voto
     *
     * @param NOTIFIER notifier
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void endRealTimePolls(Notifier NOTIFIER) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Adiciona callback para users online
     *
     * @param NOTIFIER notifier
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void initializeRealTimeOnlineUsers(Notifier NOTIFIER) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Remove callback de users online
     *
     * @param NOTIFIER notifier
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void endRealTimeOnlineUsers(Notifier NOTIFIER) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Conta as linhas na base de dados consoante o comando e o atributo
     *
     * @param sql         comando sql
     * @param returnCount atributo para contar
     * @return quantidade contada
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    int countRowsBD(String sql, String returnCount) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Altera o para "desligado" o estado de uma mesa de voto
     * para um determinado departamento na base de dados
     *
     * @param department_id ID do departamento
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void turnOffPollingStation(int department_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Verifica se uma determinada pessoa já votou para uma eleição
     *
     * @param cc       numero de cartão de cidadão
     * @param election ID da eleição
     * @return true se já votou, false caso contrário
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    boolean checkIfAlreadyVote(int cc, int election) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Remove callback de admin da lista de callbacks para envio de informação de tempo real sobre os votos
     *
     * @param NOTIFIER notifier
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void endRealTimeInfo(Notifier NOTIFIER) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Insere uma determinada pessoa na base de dados e
     * encripta a palavra passe
     *
     * @param name        nome
     * @param cargo       cargo (Estudante, Docente ou Funcionário)
     * @param pass        código de acesso para aceder a uma determinada eleição
     * @param dep         departamento a que a pessoa pertence
     * @param num_phone   número de telemóvel
     * @param address     morada
     * @param num_cc      número de cartão de cidadão
     * @param cc_validity formato da data (ano, mes, dia)
     * @return true ou false caso tenha sido inserido com sucesso ou não na base de dados
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    boolean insertPerson(String name, String cargo, String pass, int dep, int num_phone, String address, int num_cc, String cc_validity) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Insere uma determinada eleição na base de dados
     *
     * @param begin_data data de inicio da eleição
     * @param end_data   data de fim da eleição
     * @param titulo     título da eleição
     * @param descricao  descrição da eleição
     * @param type_ele   tipo da eleição (Estudante, Docente ou Funcionário)
     * @return true ou false caso tenha sido inserido com sucesso ou não na base de dados
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    int insertElection(String begin_data, String end_data, String titulo, String descricao, String type_ele) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Atribui uma eleição a um departamento na entidade election_department da base de dados
     *
     * @param id_election ID da eleição
     * @param id_dep      ID do departamento
     * @return true em caso de sucesso na inserção, false caso contrário
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    boolean insertElectionDepartment(int id_election, int id_dep) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Insere uma lista na entidade candidacy da base de dados
     *
     * @param name        nome da lista
     * @param type        tipo da lista
     * @param election_id ID da eleição
     * @return true em caso de sucesso na inserção, false caso contrário
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    boolean insertCandidacyIntoElection(String name, String type, int election_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Atribui uma pessoa a uma lista na entidade candidacy_person da base de dados,
     * e verifica se a pessoa pode candidatar-se.
     *
     * @param election_id  ID da eleição da lista
     * @param candidacy_id ID da lista
     * @param cc_number    número de cartão de cidadão da pessoa
     * @return String com a pensagem de erro
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    String insertPersonIntoCandidacy(int election_id, int candidacy_id, int cc_number) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Associa uma eleição a um departamento na base de dados
     *
     * @param election_id   ID da eleição
     * @param department_id ID do departamento
     * @return true em caso de sucesso na inserção, false caso contrário
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    boolean insertPollingStation(int election_id, int department_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Inserir um registo de voto na base de dados
     *
     * @param id_election ID da eleição
     * @param cc          numero de cartão de cidadão
     * @param ndep        ID do departamento
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void insertVotingRecord(String id_election, String cc, String ndep) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Insere dados sobre um terminal de voto na base de dados
     *
     * @param id     ID do terminal de voto
     * @param dep_id ID do departamento
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void insertTerminal(String id, int dep_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Remove na base de dados,
     * devolve mensagem de sucesso caso a remoção seja bem sucedida
     * ou de erro caso contrário
     *
     * @param table  entidade onde é feita a remoção
     * @param idName nome da chave primária
     * @param id     valor da chave a remover
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void removeOnDB(String table, String idName, int id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Remove um departamento de uma eleição na base de dados
     *
     * @param department_id ID do departamento
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void removePollingStation(int department_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Atualiza dados de uma eleição,
     * devolve mensagem de sucesso caso a atualização seja bem sucedida
     * ou de erro caso contrário
     *
     * @param e eleição para atualizar
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void updateElections(Election e) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Dá update de uma determianda eleição quando se edita os seus detalhes
     *
     * @param election_id identificador da eleição
     * @param name        titulo da eleição
     * @param type        tipo da eleição
     * @param description descrição da eleiçõa
     * @param begin_date  inicio da eleição
     * @param end_date    fim da eleição
     * @return devolve true ou false consoante tenha efetuado a atualização com sucesso ou não, respetivamente
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    boolean updateElectionOnEdit(int election_id, String name, String type, String description, String begin_date, String end_date) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Verifica se uma lista pertence a uma determinada eleição
     *
     * @param election_id  identificador de uma determinada eleição
     * @param candidacy_id identificar de uma determinada lista
     * @return devolve 1 ou 0 consoante pertença ou não a uma determinada eleição, respetivamente
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    int checkElectionHasCandidacy(int election_id, int candidacy_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Atualiza a informação sobre a pessoa no terminal de voto
     *
     * @param cc_number  número de cartão de cidadão
     * @param idTerminal ID do terminal de voto
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void updateTerminalInfoPerson(int cc_number, String idTerminal) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Atualiza a informação sobre a eleição atribuida ao terminal de voto para votar
     *
     * @param election_id ID da eleição
     * @param idTerminal  ID do terminal de voto
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void updateTerminalInfoElection(int election_id, String idTerminal) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Atualiza o numero de votos de uma lista na base de dados
     * e regista o voto do eleitor
     *
     * @param id_election     ID da eleição
     * @param candidacyOption ID da lista
     * @param cc              numero de cartão de cidadão
     * @param ndep            ID do departamento
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void updateCandidacyVotes(String id_election, String candidacyOption, String cc, String ndep) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Atualiza o numero de votos em branco de uma eleição na base de dados
     * e regista o voto do eleitor
     *
     * @param id_election ID da eleição
     * @param cc          numero de cartão de cidadão
     * @param ndep        ID do departamento
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void updateBlankVotes(String id_election, String cc, String ndep) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Atualiza o numero de votos nulos de uma eleição na base de dados
     * e regista o voto do eleitor
     *
     * @param id_election ID da eleição
     * @param cc          numero de cartão de cidadão
     * @param ndep        ID do departamento
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void updateNullVotes(String id_election, String cc, String ndep) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Atualiza estado de um terminal de voto na base de dados
     *
     * @param id     ID do terminal de voto
     * @param status estado do terminal de voto
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    void updateTerminalStatus(String id, String status) throws java.rmi.RemoteException, InterruptedException;

    /**
     * seleciona os departamentos associados a uma determinada eleição
     *
     * @param election_id ID da eleição
     * @return departamentos
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    ArrayList<Department> selectPollingStation(int election_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Seleciona as mesas de voto que não estejam associadas a uma determinada eleição
     *
     * @param election_id ID da eleição
     * @return departamentos
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    ArrayList<Department> selectNoAssociatedPollingStation(int election_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Procura todas as eleições na base de dados
     *
     * @return todas as eleições
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    ArrayList<Election> getElections() throws java.rmi.RemoteException, InterruptedException;

    /**
     * Procura todas as eleições que não começaram na base de dados
     *
     * @return todas as eleições que não começaram
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    ArrayList<Election> getElectionsNotStarted() throws java.rmi.RemoteException, InterruptedException;

    /**
     * Procura todas as listas de uma determinada eleição na base de dados
     *
     * @param election_id ID da eleição
     * @return todas as listas da eleição
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    ArrayList<Candidacy> getCandidacies(int election_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Procura uma pessoa pelas suas cardenciais na base de dados
     *
     * @param username número de cartão de cidadão
     * @param password código de acesso
     * @return pessoa pesquisada ou null caso não seja encontrada
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    Person getPerson(String username, String password) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Pesquisa todas as pessoas atribuidas a uma determinada lista
     *
     * @param candidacy_id ID da lista
     * @return todas as pessoas atribuidas à lista
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    ArrayList<Person> getPeople(int candidacy_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Procura todos os departamentos na base de dados
     *
     * @return departamentos da base de dados
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    ArrayList<Department> getDepartments() throws java.rmi.RemoteException, InterruptedException;

    /**
     * Procura o ID da eleição a ser votada num determinado terminal de voto
     *
     * @param id ID do terminal de voto
     * @return ID da eleição
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    int getElectionIdFromTerminal(String id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Procura o numero de cartão de cidadão da pessoa que está
     * a votar num determinado terminal de voto
     *
     * @param id ID do terminal de voto
     * @return numero de cartão de cidadão da pessoa
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    int getElectorInfo(String id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Procura as eleições passadas na base de dados
     *
     * @return eleições passadas
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    ArrayList<Election> getEndedElections() throws java.rmi.RemoteException, InterruptedException;

    /**
     * Procura as eleições a decorrerem para um
     * determinado departamento na base de dados
     *
     * @param department_id ID do departamento
     * @return eleições a decorrerem no departamento
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    ArrayList<Election> getCurrentElections(int department_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Devolve as eleições em que uma determinada pessoa pode votar
     *
     * @param cc identificador de um determinado utilizador
     * @return Array com todas as eleições que um determinado utilizador pode votar
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    ArrayList<Election> getCurrentElectionsPerson(String cc) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Devolve o numero de votos em branco para uma
     * determinada eleição na base de dados
     *
     * @param id_election ID da eleição
     * @return numero de votos em branco da eleição
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    int getBlankVotes(int id_election) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Devolve o numero de votos nulos para uma
     * determinada eleição na base de dados
     *
     * @param id_election ID da eleição
     * @return numero de votos nulos da eleição
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    int getNullVotes(int id_election) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Devolve o numero de votos para uma lista numa eleição na base de dados
     *
     * @param id_election  ID da eleição
     * @param id_candidacy ID da lista
     * @return numero de votos
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    int getVotesCandidacy(int id_election, int id_candidacy) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Calcula a percentagem de votos para uma lista ou
     * calcula a percentagem de votos em branco caso o ID da lista é -1
     *
     * @param id_election  ID da eleição
     * @param id_candidacy ID da lista
     * @return percentagem de votos
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    float getPercentVotesCandidacy(int id_election, int id_candidacy) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Procura pessoas na base de dados cujo os dados correpondam
     *
     * @param election_id   ID da eleição
     * @param department_id ID do departamento
     * @param campo         nome do que está a ser pesquisado (i.e. nome da pessoa, nome da rua,...)
     * @param campo_sql     nome do atributo da pessoa a ser pesquisado (i.e. nome, rua, ...)
     * @param campo_num     nome do que está a ser pesquisado (i.e. numero de cc, numero de telemovel, ...)
     * @return lista de pessoas
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    ArrayList<Person> getRegisPeople(int election_id, int department_id, String campo, String campo_sql, int campo_num) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Pesquisa o se o ID requerido pelo terminal de voto está a ser utilizado
     *
     * @param required_id ID requirido pelo terminal de voto
     * @return -1 caso o ID esteja disponivel ou o estado do terminal caso esteja indisponivel
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    int getTerminal(String required_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Procura todos os registos de voto na base de dados
     *
     * @return registos de voto
     * @throws RemoteException      falha no RMI
     * @throws InterruptedException thread interrompida
     */
    ArrayList<VotingRecord> getVotingRecords() throws java.rmi.RemoteException, InterruptedException;

    /**
     * Retorna o id de uma eleição a partir de uma das suas listas
     *
     * @param candidacy_id identificar de uma determinada lista
     * @return retorna o id da eleição ou 0 caso não pertença a nenhuma eleição
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interromida
     */
    int getElectionFromCandidacy(int candidacy_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Verifica se uma eleição já começou ou não
     *
     * @param election_id identificador de uma determinda eleição
     * @return retorna 1 ou 0 consoante tenha ou não começado, respetivamente
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    int checkIfElectionNotStarted(int election_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Verifica se uma determinada pessoa existe
     *
     * @param cc_number identificador de uma determinada pessoa
     * @return retorna o nome da pessoa ou null caso exista ou não, respetivamente
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    String checkIfPersonExists(int cc_number) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Devolve todas as listas com os respetivos votos, de uma determinada eleição
     *
     * @param election_id identificador de uma determinda eleição
     * @return array com todas as listas de uma determinda eleição
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    ArrayList<Candidacy> getCandidaciesWithVotes(int election_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Associa o token de facebook a um determinado utilizador
     *
     * @param ccnumber identificador de um determinado utilizador
     * @param fbId     token associado a um determinado utlizador
     * @return devolve true ou false caso tenha conseguidp associar o token
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    boolean associateFbId(int ccnumber, String fbId) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Devolve o token de facebook associado a um determinado utilizador
     *
     * @param cc_number identificador de um determinado uilizador
     * @return devolve o token
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    String getAssociatedFbId(int cc_number) throws java.rmi.RemoteException, InterruptedException;

    /**
     * devolve uma determinada pessoa a partir do seu token de facebook
     *
     * @param fbId token de facbook associado
     * @return devolve uma pessoa com o token fbId
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    Person getPersonFb(String fbId) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Verifica se um determinado utilizador é administrador
     *
     * @param cc_number identificador de um determinado utilizador
     * @return devolve 1 ou 0 caso seja ou não administrador respetivamente
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    int checkIfIsAdmin(int cc_number) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Devolve eleições com os repsetivos votos
     *
     * @param sql comando sql que procura eleições e as devolve com os respetivos votos
     * @return Array com as eleições
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    ArrayList<Election> selectElectionsWithVotes(String sql) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Calcula a percentagem de votos nulos de uma determinada eleição
     *
     * @param id_election identificar de uma determinada eleição
     * @return percentagem de votos nulos
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    float getPercentNullVotes(int id_election) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Calcula a percentagem de votos brancos de uma determinada eleição
     *
     * @param id_election identificar de uma determinada eleição
     * @return percentagem de votos brancos
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    float getPercentBlankVotes(int id_election) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Dá update da percentagem de votos nulos de uma determinda eleição
     *
     * @param id_election identificar de uma determinada eleição
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    void updatePercentNull(int id_election) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Dá update da percentagem de votos brancos de uma determinda eleição
     *
     * @param id_election identificar de uma determinada eleição
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    void updatePercentBlank(int id_election) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Dá update da percentagem de votos de uma determinda lista
     *
     * @param election_id  identificar de uma determinada eleição
     * @param candidacy_id identificar de uma determinada lista
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    void updatePercentVotesCandidacy(int election_id, int candidacy_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Sempre que um utilizador efetua um voto, todas as percentagens são atualizadas
     *
     * @param election_id identificar de uma determinada eleição
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    void updateAllVotes(int election_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Devolve o título de uma determinada eleição a partir do id de uma lista
     *
     * @param candidacy_id identificar de uma determinada lista
     * @return título da eleição cuja lista tem o id: candidacy_id
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    String getElectionTitleFromCandidacy(int candidacy_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Devolve todos os registo de votos até ao momento
     *
     * @return array com o registo dos votos
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    ArrayList<InfoElectors> getInfoElectors() throws java.rmi.RemoteException, InterruptedException;

    /**
     * Devolve todos os registo de votos até ao momento (diferenciados pelos jobs)
     *
     * @return array com o registo dos votos
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    ArrayList<InfoElectors> getDiferenciedInfoElectors() throws java.rmi.RemoteException, InterruptedException;

    /**
     * Devolve todos as mesas de voto e respetivos terminais de voto ativos
     *
     * @return array com as mesas e terminais de voto ativos
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    ArrayList<InfoPolls> getInfoPolls() throws java.rmi.RemoteException, InterruptedException;

    /**
     * Devolve todas as pessoas online
     *
     * @return array com as pessoas online
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    ArrayList<InfoOnline> getInfoOnlineUsers() throws java.rmi.RemoteException, InterruptedException;

    /**
     * Devolve todas as mesas de voto ativas
     *
     * @return Array com todas as mesas de voto ativas
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    ArrayList<Department> getPollingStation() throws java.rmi.RemoteException, InterruptedException;

    /**
     * Obtém o título de uma eleição através do seu id
     *
     * @param election_id id da eleição
     * @return título da eleição
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    String getTitleElection(int election_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Obtém o tipo de uma eleição através do seu id
     *
     * @param election_id id da eleição
     * @return tipo da eleição
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    String getTypeElection(int election_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Obtém a descrição de uma eleição através do seu id
     *
     * @param election_id id da eleição
     * @return descrição da eleição
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    String getDescriptionElection(int election_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Obtém a data de início de uma eleição através do seu id
     *
     * @param election_id id da eleição
     * @return data de início da eleição
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    String getIniDateElection(int election_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Obtém a data de fim de uma eleição através do seu id
     *
     * @param election_id id da eleição
     * @return data de fim da eleição
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    String getEndDateElection(int election_id) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Verifica se uma eleição já terminou ou não
     *
     * @param election_id id da eleição
     * @return 1 se tiver terminado, 0 caso contrário
     * @throws java.rmi.RemoteException falha no RMI
     * @throws InterruptedException     thread interrompida
     */
    int checkEndElection(int election_id) throws java.rmi.RemoteException, InterruptedException;

    void addTerminal(int cc_number) throws java.rmi.RemoteException, InterruptedException;
}

