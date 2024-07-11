# Status de Desenvolvimento 11/07/2024 🟢 
  Em Desenvolvimento do Back-end.
  
<br>

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

# Fluxograma do Ciclo de Compra
- Este Fluxograma faz uma representação do principal processo da aplicação, a compra de um produto e como a requisição interage/percorre os microsserviços. <br>
  📌OBS: Este fluxograma leva em consideração apenas o caso de sucesso.
<br>

![microservices](https://github.com/im2back/Caderneta-Digital-2.0/assets/117541466/1baaf31d-53ba-4c5e-adaf-7063fe8fe0a7)

<br>

# Tecnologias utilizadas
## Back-end
- Java 17
- Spring Boot
- JPA 
- Maven
- Spring Cloud OpenFeign
- H2 Database
- Docker
- RabbitMq
## Front-end
- Angular 17
- Type Script
- HTML/CSS/JS
- PrimeNg

<br><br>

# Como executar o projeto

## Utilizando dependencias locais
Pré-requisitos: Java 17

```bash
# Clonar repositório
git clone git@github.com:im2back/Caderneta-Digital-2.0.git

# executar o projeto
- Entrar na pasta de cada microsserviço :
./mvnw spring-boot:run

#Observações : ATENTAR-SE PARA AS PORTAS QUE ESTÃO SENDO USADAS NO PROJETO !!! VERIFICAR DISPONIBILIDADE DAS PORTAS !!!
```

# Autor

Jefferson Richards Sena de Souza

<a href="https://www.linkedin.com/in/jefferson-richards-sena-de-souza-4110a3222/" target="_blank"><img loading="lazy" src="https://img.shields.io/badge/-LinkedIn-%230077B5?style=flat&logo=linkedin&logoColor=white" target="_blank"></a>

