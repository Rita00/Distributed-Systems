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
    private String beginStr;
    /**
     * Data e hora de fim da eleição
     */
    private LocalDateTime end;
    private String endStr;
    /**
     * Construtor do Objeto Eleição
     *
     * @param id identificador da eleicao
     * @param title nome da eleicao
     * @param type tipo que permite identificar para que grupo de pessoa a eleicao decorre (Estudantes, Docentes, Funcionarios)
     * @param description descricao da eleicao
     * @param begin data e hora de inicio da eleicao
     * @param end data e hora de fim da eleicao
     */
    public Election(int id, String title, String type, String description, String begin, String end) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.begin = LocalDateTime.parse(begin.replace('T',' '), DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"));
        this.beginStr = begin.replace('T',' ');
        this.end = LocalDateTime.parse(end.replace('T',' '), DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"));
        this.endStr = end.replace('T',' ');
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
			dateTime = LocalDateTime.parse(date+" "+time,DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"));
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

    public String getBeginStr() {
        return beginStr;
    }

    public String getEndStr() {
        return endStr;
    }

    public void setBeginStr(String beginStr) {
        this.beginStr = beginStr;
    }

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
     * Setter do nome da eleicao
     *
     * @param title nome da eleicao
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * Setter do tipo da eleicao
     *  Se 1: Estudante
     *  Se 2: Docente
     *  Se 3: Funcionario
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
		if (dateTime!=null){
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
		if (dateTime!=null){
			this.end = dateTime;
			return true;
		}
		return false;
	}
}