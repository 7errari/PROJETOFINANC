# Sistema de Gerenciamento Financeiro Pessoal

Um aplicativo Java com interface gráfica Swing para gerenciamento de finanças pessoais. Permite aos usuários cadastrar receitas e despesas, organizá-las por categorias e visualizar um resumo financeiro.

## Funcionalidades

- Cadastro e autenticação de usuários
- Registro de transações financeiras (receitas e despesas)
- Consulta de histórico com filtros por data, tipo e categoria
- Resumo financeiro com cálculo de saldo
- Gerenciamento de categorias personalizadas

## Requisitos

- Java SE 8 ou superior
- Swing (incluído no JDK)

## Como executar

1. Compile todas as classes do projeto
2. Execute a classe `Main.java`
3. Use as credenciais padrão para entrar:
   - Usuário: `admin`
   - Senha: `admin`
   - Ou crie uma nova conta

## Estrutura do Projeto

O projeto segue o padrão de arquitetura MVC:

- **Model**: Classes que representam os dados (User, Transaction, Category)
- **View**: Interfaces gráficas para interação com o usuário
- **Controller**: Classes que gerenciam a lógica de negócio

## Requisitos Implementados

- **RF001**: Cadastro de Transações
- **RF002**: Consulta de Histórico
- **RF003**: Resumo Financeiro
- **RF004**: Gerenciamento de Categorias
- **RF005**: Cadastro e Autenticação de Usuário

## Tecnologias Utilizadas

- Java como linguagem de programação
- Swing para interface gráfica
- Estruturas de dados em memória para armazenamento
