package pt.uc.dei.student.elections;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Classe do Objeto Eleição
 *
 * @author Ana Rita Rodrigues
 * @author Dylan Gonçalves Perdigão
 */
public class Election implements Serializable {
    /**
     * ID da eleição
     */
    private final int id;

    /**
     * Titulo da eleição
     */
    private String title;

    /**
     * Tipo da eleição (Estudantes, Docentes, Funcionarios)
     */
    private String type;

    /**
     * Descrição da eleição
     */
    private String description;

    /**
     * Data e hora de início da eleição
     */
    private LocalDateTime begin;

    /**
     * Dara e hora de inicio da eleição em formato string
     */
    private String beginStr;

    /**
     * Data e hora de fim da eleição
     */
    private LocalDateTime end;

    /**
     * Dara e hora de fim da eleição em formato string
     */
    private String endStr;

    /**
     * Número de votos nulos
     */
    private int null_votes;

    /**
     * Número de votos brancos
     */
    private int blank_votes;

    /**
     * Percentagem de votos nulos
     */
    private float null_percent;

    /**
     * Percentagem de votos brancos
     */
    private float blank_percent;

    /**
     * Construtor do Objeto Eleição
     *
     * @param id          identificador da eleicao
     * @param title       nome da eleicao
     * @param type        tipo que permite identificar para que grupo de pessoa a eleicao decorre (Estudantes, Docentes, Funcionarios)
     * @param description descricao da eleicao
     * @param begin       data e hora de inicio da eleicao
     * @param end         data e hora de fim da eleicao
     */
    public Election(int id, String title, String type, String description, String begin, String end) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.begin = LocalDateTime.parse(begin.replace('T', ' '), DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"));
        this.beginStr = begin.replace('T', ' ');
        this.end = LocalDateTime.parse(end.replace('T', ' '), DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"));
        this.endStr = end.replace('T', ' ');
    }

    /**
     * Construtor do Objeto Eleição
     *
     * @param id            identificador da eleicao
     * @param title         nome da eleicao
     * @param type          tipo que permite identificar para que grupo de pessoa a eleicao decorre (Estudantes, Docentes, Funcionarios)
     * @param description   descricao da eleicao
     * @param begin         data e hora de inicio da eleicao
     * @param end           data e hora de fim da eleicao
     * @param null_votes    número de votos nulos
     * @param blank_votes   número de votos brancos
     * @param null_percent  percentagem de votos nulos
     * @param blank_percent percentagem de votos brancos
     */
    public Election(int id, String title, String type, String description, String begin, String end, int null_votes, int blank_votes, float null_percent, float blank_percent) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.begin = LocalDateTime.parse(begin.replace('T', ' '), DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"));
        this.beginStr = begin.replace('T', ' ');
        this.end = LocalDateTime.parse(end.replace('T', ' '), DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"));
        this.endStr = end.replace('T', ' ');
        this.null_votes = null_votes;
        this.blank_votes = blank_votes;
        this.blank_percent = blank_percent;
        this.null_percent = null_percent;
    }

    /**
     * Converte duas strings contendo a data e a hora num objeto do tipo LocalDateTime
     *
     * @param date data (ano-mes-dia)
     * @param time hora (hora:minutos:segundos)
     * @return objeto LocalDateTime com parse da string efetuado ou null se erro
     */
    private LocalDateTime parseDateTime(String date, String time) {
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(date + " " + time, DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"));
        } catch (DateTimeParseException e) {
            return null;
        }
        return dateTime;
    }

    /**
     * Devolve uma string com informacoes relativas a uma eleicao
     *
     * @return string com informacoes relativas à eleicao
     */
    public String toString() {
        return "===========ELEICAO===========\n" +
                String.format("%s\t(%s)\n", this.title, this.type) +
                String.format("%s\n", this.description) +
                String.format("Inicio - %s às %s\n", this.begin.toLocalDate().toString(), this.begin.toLocalTime().toString()) +
                String.format("Fim - %s às %s\n", this.end.toLocalDate().toString(), this.end.toLocalTime().toString()) +
                "-----------------------------\n";
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
     * Getter da data de inicio da eleição em formato String
     *
     * @return data de inicio da eleição
     */
    public String getBeginStr() {
        return beginStr;
    }

    /**
     * Getter da data de fim da eleição em formato String
     *
     * @return data de fim da eleição
     */
    public String getEndStr() {
        return endStr;
    }

    /**
     * Setter da data de início da eleição em formato String
     *
     * @return data de inicio da eleição
     */
    public void setBeginStr(String beginStr) {
        this.beginStr = beginStr;
    }

    /**
     * Setter da data de fim da eleição em formato String
     *
     * @return data de fim da eleição
     */
    public void setEndStr(String endStr) {
        this.endStr = endStr;
    }

    /**
     * Getter do id da eleicao
     *
     * @return id da eleicao
     */
    public int getId() {
        return this.id;
    }

    /**
     * Getter do nome da eleicao
     *
     * @return nome da eleicao
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Getter do tipo da eleicao
     *
     * @return tipo da eleicao
     */
    public String getType() {
        return this.type;
    }

    /**
     * Getter da descricao da eleicao
     *
     * @return descricao da eleicao
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Getter da data de inicio da eleicao
     *
     * @return data de inicio da eleicao
     */
    public LocalDateTime getBegin() {
        return this.begin;
    }

    /**
     * Getter da data de fim da eleicao
     *
     * @return data de fim da eleicao
     */
    public LocalDateTime getEnd() {
        return this.end;
    }

    /**
     * Setter da percentagem de votos nulos
     *
     * @param null_percent percentagem de votos nulos
     */
    public void setNull_percent(float null_percent) {
        this.null_percent = null_percent;
    }

    /**
     * Setter da percentagem de votos brancos
     *
     * @param blank_percent percentagem de votos brancos
     */
    public void setBlank_percent(float blank_percent) {
        this.blank_percent = blank_percent;
    }

    /**
     * Setter do número de votos nulos
     *
     * @param null_votes Número de votos nulos
     */
    public void setNull_votes(int null_votes) {
        this.null_votes = null_votes;
    }

    /**
     * Setter do número de votos brancos
     *
     * @param blank_votes número de votos nulos
     */
    public void setBlank_votes(int blank_votes) {
        this.blank_votes = blank_votes;
    }

    /**
     * Setter do nome da eleicao
     *
     * @param title nome da eleicao
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Setter do tipo da eleicao
     * Se 1: Estudante
     * Se 2: Docente
     * Se 3: Funcionario
     *
     * @param type tipo da eleicao
     */
    public void setType(int type) {
        switch (type) {
            case 1:
                this.type = "Estudante";
                break;
            case 2:
                this.type = "Docente";
                break;
            case 3:
                this.type = "Funcionário";
                break;
        }
    }

    /**
     * Setter da descricao da eleicao
     *
     * @param description descricao da eleicao
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Setter da data e hora de inicio da eleicao
     *
     * @param date data de inicio da eleicao
     * @param time hora de inicio da eleicao
     * @return true se nao houve problemas ou false caso um problema ocorra
     */
    public boolean setBegin(String date, String time) {
        LocalDateTime dateTime = parseDateTime(date, time);
        if (dateTime != null) {
            this.begin = dateTime;
            return true;
        }
        return false;
    }

    /**
     * Setter da data e hora de fim da eleicao
     *
     * @param date data de fim da eleicao
     * @param time hora de fim da eleicao
     * @return true se nao houve problemas ou false caso um problema ocorra
     */
    public boolean setEnd(String date, String time) {
        LocalDateTime dateTime = parseDateTime(date, time);
        if (dateTime != null) {
            this.end = dateTime;
            return true;
        }
        return false;
    }
}