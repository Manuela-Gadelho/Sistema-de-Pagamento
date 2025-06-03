# 🧾 Lanchonete Fatec: Sistema de Caixa Automatizado

![image](https://github.com/user-attachments/assets/d990668a-9603-4e21-8897-3d5711d727e4)

Este projeto é um sistema de caixa interativo desenvolvido para uma lanchonete fictícia da Fatec, combinando **hardware (Arduino)** e **software (Java)**. Ele permite que os clientes selecionem produtos, gerenciem um carrinho de compras e realizem pagamentos de forma segura via senha, proporcionando uma experiência automatizada e eficiente.

---

## 🚀 Funcionalidades

- **Menu de Produtos Interativo**: Categorias (Sucos, Salgados, Doces) com exibição de itens e preços.
- **Carrinho de Compras**: Adição de múltiplos itens, total acumulado e preços individuais.
- **Pagamento por Senha**: Validação segura com senha numérica e verificação de saldo.
- **Gestão de Saldo (FateCoins)**: Simulação de moeda virtual para as transações.
- **Modo Administrador**:
  - Acesso por senha (`123`)
  - Adicionar FateCoins
  - Ver log de compras (armazenamento local)
- **Feedback Sonoro**: Sons para digitação, sucesso e erro.
- **Interface Gráfica (GUI)**: Simula um display de caixa com mensagens e saldo.

---

## 💻 Tecnologias Utilizadas

### 🔌 Hardware

- Arduino Uno (ou similar)
- Teclado Matricial 4x4
- Sensor Ultrassônico HC-SR04 (apenas para feedback visual com LED)

### 💻 Software

- Java (JDK)
- Swing (para interface gráfica)
- JSerialComm (para comunicação serial com o Arduino)

### 🛠 IDEs

- Arduino IDE (para código do microcontrolador)
- Eclipse (para o desenvolvimento Java)

---

## ⚙️ Configuração e Instalação

### 1. Hardware (Arduino)

**Conexões do Teclado Matricial 4x4:**

- Linhas: D9, D8, D7, D6  
- Colunas: D5, D4, D3, D2  

**Conexões do Sensor Ultrassônico (Opcional):**

- VCC → 5V  
- GND → GND  
- Trig → D10  
- Echo → D11  

**Upload do Código Arduino:**

1. Abra o arquivo `.ino` na Arduino IDE.
2. Verifique se os pinos no código correspondem às conexões físicas.
3. Selecione a placa correta (Tools > Board) e a porta (Tools > Port).
4. Faça o upload do código para o Arduino.

### 2. Software (Java)

**Clone o repositório:**

```bash
git clone https://github.com/seu-usuario/seu-repositorio.git
cd seu-repositorio
Importar no Eclipse:

File > Import... > General > Existing Projects into Workspace > Next

Selecione a pasta raiz do projeto clonado

Clique em Finish

Adicionar a Biblioteca JSerialComm:

Baixe a biblioteca em: https://fazecast.github.io/jSerialComm/

Eclipse > botão direito no projeto > Properties > Java Build Path > Libraries > Add External JARs...

Selecione o arquivo .jar baixado

Clique em Apply and Close

Configurar Arquivos de Som:

Certifique-se de que os arquivos beep.wav, success.wav, error.wav estão no formato PCM 16 bits, 44100 Hz

Crie a pasta src/sounds/ no projeto

Coloque os arquivos .wav na pasta

Clique com o botão direito no projeto e selecione Refresh

Compilar e Executar:

Project > Clean...

Project > Build Project

Clique com o botão direito na classe Main.java > Run As > Java Application

🧪 Como Usar
Conectar à Porta Serial:
Inicie a aplicação Java

Escolha a porta COM correta no menu suspenso

Clique em Conectar

Navegação pelo Teclado Matricial:
Tecla	Função
1	Menu Sucos
2	Menu Salgados
3	Menu Doces
4	Ver saldo (FateCoins)
A	Acessar Modo Administrador
D	Adiciona 500 FateCoins (modo de teste)
*	Voltar/cancelar
#	Finalizar carrinho e ir para pagamento

Seleção de Itens:
Dentro dos menus de produto (Sucos, Salgados, Doces), digite 1, 2 ou 3 para adicionar itens ao carrinho.

Pagamento:
Após finalizar o carrinho com #, digite a senha 456 para efetuar o pagamento.

Modo Administrador:
Senha de acesso: 123

Opções:

1: Adicionar FateCoins

2: Ver Log de Compras

🤝 Contribuição
Contribuições são bem-vindas!
Sinta-se à vontade para fazer um fork, propor melhorias e abrir um Pull Request.

📄 Licença
Este projeto está licenciado sob a Licença MIT.

Desenvolvido por: Manuela Gadelho
