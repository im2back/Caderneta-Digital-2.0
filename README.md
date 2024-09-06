# Status de Desenvolvimento 06/09/2024 🟢 
Projeto finalizado, sujeito a futuras melhorias !
  
<br>

### Downloads 📥
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

# ▶️ Consumindo a API Via Docker e Explicando Funcionalidades
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

# Testes Unitários
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


# 💻 Tecnologias utilizadas
## Back-end
- Java 17
- Spring Boot
- JPA 
- Maven
- Spring Cloud OpenFeign
- H2 Database
- Docker
- MySql
- SonarQube
- Jacoco
## Front-end
- Angular 17
- Type Script
- HTML/CSS/JS
- PrimeNg

<br><br>

# Como executar o projeto

## Utilizando dependencias locais
Pré-requisitos: Java 17, VsCode, IDE de sua preferência

```bash
# Clonar repositório
git clone git@github.com:im2back/Caderneta-Digital-2.0.git

# executar o projeto back-end
- Entrar na pasta de cada microsserviço :
👉 ./mvnw spring-boot:run

# executar o projeto Frontend
- Entrar na pasta do projeto :
 👉  npm start
📢 Para acessar o projeto via rede WIFI, através do celular, basta digitar ipconfig e alterar a url de localhost pelo seu ipv4
vai ficar algo como : https://192.168.1.111:4200/

#Observações : ATENTAR-SE PARA AS PORTAS QUE ESTÃO SENDO USADAS NO PROJETO !!! VERIFICAR DISPONIBILIDADE DAS PORTAS !!!
```

## Utilizando o docker
Pré-requisitos: Docker

```bash
# Baixar o arquivo docker compose disponibilizado na sessão downloads

# Entrar na pasta onde encontra-se o arquivo docker-compose
- Abrir um terminal e executar o comando.
👉 docker-compose up -d
Aguardar o download das imagens.
Após baixar as imagens automaticamente, também, será inciado os containers, caso os containers do back end não iniciem
será por conta do atraso da iniciaização do container do Banco de dados. para corrigir isso basta entrar na interface do docker e clicar em "▷ start" ao lado do container.;


📢 Para acessar o projeto via rede WIFI, através do celular, basta digitar ipconfig e alterar a url de localhost pelo seu ipv4
vai ficar algo como : https://192.168.1.111:4200/

#Observações : ATENTAR-SE PARA AS PORTAS QUE ESTÃO SENDO USADAS NO PROJETO !!! VERIFICAR DISPONIBILIDADE DAS PORTAS !!!
```

# Autor

Jefferson Richards Sena de Souza

<a href="https://www.linkedin.com/in/jefferson-richards-sena-de-souza-4110a3222/" target="_blank"><img loading="lazy" src="https://img.shields.io/badge/-LinkedIn-%230077B5?style=flat&logo=linkedin&logoColor=white" target="_blank"></a>

