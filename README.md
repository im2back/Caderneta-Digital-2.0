# Status de Desenvolvimento 25/12/2024 EM REFATORAÇÃO, API QUEBRADA ATÉ O TÉRMINO 🟢 
### Projeto passando por refatoração visando :
 - Desacoplar os microsserviços, melhorando sua coesão e modificando o funcionamento das comunicações
 - Melhora de performance
 - Melhorar documentação
 - Melhorar a escrita dos testes e ampliar sua cobertura
   
### Situação atual :
Acoplamento elevado e baixa coesão: A comunicação atual entre os microsserviços é altamente acoplada e apresenta pouca coesão. Muitos microsserviços realizam tarefas além de seu propósito original, esse comportamento aumenta complexidade e reduz a transparência das operações. Além disso, a falta de um orquestrador torna o fluxo de comunicação desorganizado e difícil de gerenciar. Outro ponto crítico é a ausência de um gateway, que seria fundamental para centralizar as requisições, gerenciar certificados HTTPS e, futuramente, implementar medidas de segurança.

 ### Desafios da nova arquitetura :
  De acordo com as necessidades/demandas do local onde a aplicação será utiilizada é inviavel o uso de comunicação assincrona por isso uso predominante de comunicação síncrona foi priorizado em detrimento da comunicação assíncrona, devido às restrições impostas pelas regras de negócio, que não permitem a implementação de fluxos tardios. Com a introdução do novo orquestrador, a necessidade de comunicação direta entre os microsserviços será eliminada, tornando o fluxo mais claro, eficiente e desacoplado. Houve necessidade de adicionar outros dois microsserviços : RELATORIOS-MS e VALIDATION-MS, estes por sua vez preenchem demandas afim de desafogar os outros microsserviços e preencher necessidades impostas pela comunicação sincrona.
   
### Prévias da refatoração, novos fluxos :
![Nova Arquitetura, Caderneta de Mercearia drawio](https://github.com/user-attachments/assets/ba39d837-bcfc-42be-8671-2fb2bc629aa7)



## ⚖️ Licença 
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/im2back/Voll.med/blob/main/LICENSE)  
<br>

### Downloads 📥
-Docker Compose :
https://drive.google.com/file/d/11OoiuY3xI_hGuP2o_8TLSRqfWA5b_tef/view?usp=sharing

# Sobre a API 
Esta aplicação tem como objetivo gerenciar pequenos comércios, abrangendo desde o controle de estoque até a administração das contas dos clientes.
As compras realizadas pelos clientes serão registradas, permitindo a geração de métricas valiosas a partir dos dados coletados. Com essas informações, 
será possível identificar o produto mais vendido, determinar os dias ou meses com maior volume de vendas, entre outras análises importantes que poderão ser representadas através de gráficos.

<br>

# Objetivos ao Desenvolver Esta Aplicação 🏋🏻‍♀️
Este projeto representa uma evolução significativa de um trabalho que desenvolvi há mais de um ano. Com o aprendizado adquirido desde então, 
estou revisando e aplicando meu conhecimento aprimorado para criar uma versão melhorada.
Meu objetivo principal é aplicar meus conhecimentos em arquitetura e incorporar as melhores práticas de desenvolvimento.
Estou igualmente empenhado em aprimorar minhas habilidades em gerenciamento de versionamento de código, utilizando Git - GitHub.
Esse esforço envolve trabalhar eficientemente com branches e pull requests para assegurar uma organização eficaz do código.<BR>
📌OBS: Alguns comentários foram mantidos para fins de estudo e consultas futuras.Os comentários são discretos e não comprometem a visibilidade.

<br><br>

## 🚀 Funcionalidades
- Gerenciamento de Estoque
- Gerenciamento de Usuários
- Realização e Gestão de Compras
- Dashboard de Métricas para acompanhamento de desempenho de vendas

# 📽️ Consumindo a API Via Docker e Explicando Funcionalidades
[![Assista ao meu vídeo no YouTube](https://img.youtube.com/vi/TzxQAzlENJ8/0.jpg)](https://www.youtube.com/watch?v=TzxQAzlENJ8)

<br><br>

# Layout's (Desktop e Mobile)

<br>

### Tela de Registro/Pesquisa de Usuário
![usuarios-desktop](https://github.com/user-attachments/assets/e98ed1be-c2db-481b-8f9e-b3c4db12e695)

<br>

### Tela de Gerênciamento de Usuário
![358869801-6a46f60e-68a1-4add-8453-46a04e195a8e](https://github.com/user-attachments/assets/0e63a6f5-a9db-43fc-88b3-a6d21a43c964)

<br>

### Tela de Compras 
![358869897-2d7d3363-eff9-4a3d-a38b-91e1a6236576](https://github.com/user-attachments/assets/57e6d9a1-cda1-4d8d-8bff-7e16e2bf3f19)


<br>

### Tela de Métricas
![358869927-a86208ef-58d4-414e-be90-90eace48889c](https://github.com/user-attachments/assets/9de5e6c4-c0ab-49ad-9a3e-408ac3f53a4d)

<br>

### Tela de Gerênciamento de Estoque
![STOCKPRINT](https://github.com/user-attachments/assets/55547653-f6aa-40dd-8335-80f626973dcf)



<br>

# Fluxograma do Ciclo de Compra
- Este Fluxograma faz uma representação do principal processo da aplicação, a compra de um produto e como a requisição interage/percorre os microsserviços. <br>
  📌OBS: Este fluxograma leva em consideração apenas o caso de sucesso.
<br>

![microservices](https://github.com/im2back/Caderneta-Digital-2.0/assets/117541466/1baaf31d-53ba-4c5e-adaf-7063fe8fe0a7)

<br>

# 🧩 Testes Unitários
- Testes de unidades feitos e integrados com ferramentas como : Jacoco e Sonarqube.

<br>
  
## Customer Microservice
![customer-jacoco](https://github.com/user-attachments/assets/ade08480-be21-4f0e-aae7-f891f66545bf)
![customer-sonarcube](https://github.com/user-attachments/assets/2f9a4338-d7fb-489b-9e01-756b4aa7a476)
![tests](https://github.com/user-attachments/assets/9ac97e26-1ff9-479a-865f-50b1a7a47131)

<br>
  
## Stock Microservice
![teststockms](https://github.com/user-attachments/assets/70fa3805-fc65-4f03-98f2-4a2dbe3b000d)

<br>

# Documentação (Resumo)
- Resumo da documentação. A documentação completa pode ser acessada rodando a aplicação e acessando : https://localhost:8080/swagger-ui/index.html#/ para o Customer-MS e https://localhost:8443/swagger-ui/index.html#/ para o Stock-MS

<br>
  
## Customer Microservice
![customer-doc](https://github.com/user-attachments/assets/41a3d80b-dc09-4a4d-847f-82c1d6103702)

<br>
  
## Stock Microservice
![dopcs-stock](https://github.com/user-attachments/assets/b845fa02-70c1-4a07-93ae-c44ca81a4949)



<br>


# 🛠️ Tecnologias Utilizadas
## Back-end
- Java 17
- Spring Boot
- Maven
- H2 Database
- Docker
- MySql
- SonarQube
- Jacoco
## Front-end
- Angular 17
- PrimeNg

<br><br>

# 📦 Como executar o projeto

## Utilizando dependencias locais
Pré-requisitos: 
- **Java 17**: Necessário para rodar o backend.
- **Git**: Para clonar o repositório.
- **Node.js**: Inclui o npm, necessário para o frontend.
- - **MySql**: Banco de Dados.

```bash
# Clonar o repositório
git clone git@github.com:im2back/Caderneta-Digital-2.0.git

# Ajustar o valor da propriedade " server.ssl.trust-store= " para que ela aponte para o seu diretório local, caso necessário instale o certificado.

# Ajustar os valores de PORTA, SENHA e Usuário do banco de dados no arquivo application.properties

# Executar o projeto Back-end
- Navegue até a pasta do microsserviço:
Abrir um terminal no diretório 👉 ./mvnw spring-boot:run
- Após executar o projeto o banco de dados será criado automaticamente, desde que a conexão esteje correta


🛠️Tutorial: Configuração do Banco de Dados
1. Acesse o MySQL
Abra o terminal e execute o comando para acessar o MySQL:
mysql -u root -p

2. Selecione o Banco de Dados
Escolha o banco de dados onde você deseja criar a tabela e inserir os dados. Neste caso, use o banco de dados mercearia2:
USE mercearia2;

3. Insira o Usuário de Teste
Agora você pode inserir o usuário de teste na tabela tb_customer. Execute o comando SQL abaixo:
INSERT INTO tb_customer (id, customer_name, document, email, phone, is_active, street_name, house_number, complement)
VALUES ('1', 'Usuario Fantasia', '7654321589', 'fantasia@gmail.com', '9999999', TRUE, 'tv e', '06', 'sem complemento');


# Executar o projeto Frontend
- Navegue até a pasta do projeto:
Abrir um terminal no diretório 👉 npm start

📢 Para acessar o projeto via rede Wi-Fi pelo celular, basta digitar `ipconfig` e substituir `localhost` pelo seu IPv4. A URL ficará algo como: https://192.168.1.111:4200/

# Observações: ATENTAR-SE PARA AS PORTAS QUE ESTÃO SENDO USADAS NO PROJETO!!! VERIFICAR A DISPONIBILIDADE DAS PORTAS!!!
```

## Utilizando o docker
Pré-requisitos: Docker

```bash
# Baixe o arquivo docker-compose disponibilizado na seção Downloads
# Ajuste os parametros do docker compose para refletir no seu ambiente local

# Navegue até a pasta onde se encontra o arquivo docker-compose.yml
cd caminho/para/docker-compose

# Inicie os containers com o comando:
👉 docker-compose up -d
🚨🗣📢 NO MOMENTO A DISPONIBILIDADE VIA DOCKER ESTA COM ERRO, POIS FALTA PARAMETRIZAR UM ATRIBUTO, LOGO SERÁ CORRIGIDO E DISPONIBILIZADA AS IMAGENS CORRETAS
# Aguarde o download das imagens e a inicialização dos containers. 
# Caso os containers do back-end não iniciem, pode ser devido ao atraso na inicialização do container do banco de dados.
# Para corrigir isso, acesse a interface do Docker e clique em "▷ Start" ao lado do container do banco de dados.

🛠️Tutorial: Configuração do Banco de Dados
1. Acesse o MySQL
Abra o terminal e execute o comando para acessar o MySQL:
mysql -u root -p

2. Selecione o Banco de Dados
Escolha o banco de dados onde você deseja criar a tabela e inserir os dados. Neste caso, use o banco de dados mercearia2:
USE mercearia2;

3. Insira o Usuário de Teste
Agora você pode inserir o usuário de teste na tabela tb_customer. Execute o comando SQL abaixo:
INSERT INTO tb_customer (id, customer_name, document, email, phone, is_active, street_name, house_number, complement)
VALUES ('1', 'Usuario Fantasia', '7654321589', 'fantasia@gmail.com', '9999999', TRUE, 'tv e', '06', 'sem complemento');

📢 Para acessar o projeto via rede Wi-Fi pelo celular, digite `ipconfig` no seu computador para encontrar o IPv4 e substitua `localhost` na URL pelo seu IPv4. A URL ficará algo como: https://192.168.1.111:4200/

# Observações: ATENTAR-SE PARA AS PORTAS QUE ESTÃO SENDO USADAS NO PROJETO!!! VERIFICAR A DISPONIBILIDADE DAS PORTAS!!!
```

# Autor

Jefferson Richards Sena de Souza

<a href="https://www.linkedin.com/in/jefferson-richards-sena-de-souza-4110a3222/" target="_blank"><img loading="lazy" src="https://img.shields.io/badge/-LinkedIn-%230077B5?style=flat&logo=linkedin&logoColor=white" target="_blank"></a>

