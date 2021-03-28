package pt.uc.dei.student.elections;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe do Objeto Pessoa
 *
 * @author Dylan Gonçãoves Perdigão
 * @author Ana Rita Rodrigues
 */
public class Person implements Serializable {
    private String name;
    private final int cc_number;
    private LocalDate cc_validity;
    private String password;
    private String address;
    private int phone;
    private String job;
    private int department_id;

    /**
     * Construtor do Objeto Pessoa
     *
     * @param name          nome e apelido
     * @param cc_number     numero do cartao de cidadao e identificador da pessoa, serve de nome de utilisador
     * @param cc_validity   data de validade do cartao de cidadao
     * @param password      palavra passe do utilizador
     * @param address       mordada
     * @param phone         numero de telemovel
     * @param job           identifica o cargo da pessoa (i.e. Estudante, Docente, Funcionario)
     * @param department_id identificador do departamento a que pertence
     */
    public Person(String name, int cc_number, String cc_validity, String password, String address, int phone, String job, int department_id) {
        this.name = name;
        this.cc_number = cc_number;
        this.cc_validity = LocalDate.parse(cc_validity, DateTimeFormatter.ofPattern("yyyy-M-d"));
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.department_id = department_id;
        this.job = job;

    }

    /**
     * Getter para o nome da pessoa
     *
     * @return nome da pessoa
     */
    public String getName() {
        return name;
    }

    /**
     * Getter para o numero de cartao de cidadao da pessoa
     *
     * @return numero de cartao de cidadao da pessoa
     */
    public int getCc_number() {
        return cc_number;
    }

    /**
     * Getter para o numero de cartao de cidadao censurado da pessoa
     *
     * @param n numero de digitos a censurar
     * @return numero de cartao de cidadao da pessoa censurado ou sem censura se o
     * parametro não é inferior ao tamanho do numero do cartão de cidadão
     */
    public String getCensoredCc_number(int n) {
        String str = String.format("%s", this.cc_number);
        String aux = "";
        int size = str.length();
        if (n < size) {
            for (int i = n; i < size; i++) {
                aux += "*";
            }
            str = str.substring(n, size);
            return aux + str;
        }
        return String.format("%s", this.cc_number);
    }

    /**
     * Getter para a validade do cartao de cidadao
     *
     * @return validade do cartao de cidadao
     */
    public LocalDate getCc_validity() {
        return cc_validity;
    }

    /**
     * Getter para a password da pessoa no sistema
     *
     * @return password da pessoa
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter para a morada da pessoa
     *
     * @return morada da pessoa
     */
    public String getAddress() {
        return address;
    }

    /**
     * Getter para o numero de telemovel da pessoa
     *
     * @return numero de telemovel da pessoa
     */
    public int getPhone() {
        return phone;
    }

    /**
     * Getter para o cargo da pessoa
     *
     * @return cargo da pessoa
     */
    public String getJob() {
        return job;
    }

    /**
     * Getter para id do departamento em que a pessoa trabalha
     *
     * @return id do departamento
     */
    public int getDepartment_id() {
        return department_id;
    }

}
