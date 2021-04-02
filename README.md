# eVoting: Voto Eletrónico na UC
## Instalação
1.  Abrir um terminal para cada maquina
2.  Assegurar-se de estar na diretoria certa para cada maquina:
- ***rmiserver_jar/*** para o servidor RMI
- ***console_jar/*** para a consola de administração
- ***server_jar/*** para os servidores multicast
- ***terminal_jar/*** para os terminais de voto

## Configuração
### Servidor RMI
```bash
java -jar rmiserver.jar
```
### Consola de Administração
```bash
java -jar console.jar
```
### Servidores Multicast
Duas possibilidades de configuração:
1. Sem Endereço em argumento
```bash
java -jar server.jar
```
2. Com Endereço em argumento
Por exemplo para o endereço 224.3.2.1
```bash
java -jar server.jar 224.3.2.1
```
### Terminal de Voto
Duas possibilidades de configuração:
1. Sem Endereço em argumento
```bash
java -jar terminal.jar
```
2. Com Endereço em argumento
Por exemplo para o endereço 224.3.2.1
```bash
java -jar terminal.jar 224.3.2.1
```

## Documentação
A Javadoc do projeto está acessível na diretoria ***javadoc/index.html***
