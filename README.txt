eVoting: Voto Eletrónico na UC

Instalação

1.  Os JAR files estão na diretoria "out/artifacts/"
2.  Certificar-se de ter na mesma diretoria a base de dados "Election.db" e o property file "config.properties"
3.  Abrir um terminal para cada maquina

Configuração
- Servidor Web

Dar deploy do ficheiro .war no tomcat

- Servidor RMI

	java -jar rmiserver.jar

- Consola de Administração

	java -jar console.jar

- Servidores Multicast

Duas possibilidades de configuração:
	1. Sem Endereço em argumento

		java -jar server.jar

	2. Com Endereço em argumento (por exemplo para o endereço 224.3.2.1)

		java -jar server.jar 224.3.2.1

- Terminal de Voto

Duas possibilidades de configuração:
	1. Sem Endereço em argumento

		java -jar terminal.jar

	2. Com Endereço em argumento (por exemplo para o endereço 224.3.2.1)

		java -jar terminal.jar 224.3.2.1

Documentação

- A Javadoc do projeto está acessível na diretoria "javadoc/index.html"
- O codigo fonte está acessível na diretoria "src/pt/uc/dei/student/"
