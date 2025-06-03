Lanchonete Fatec: Sistema de Caixa Automatizado

(Substitua esta imagem por uma captura de tela real da sua aplicação Java)

Este projeto é um sistema de caixa interativo desenvolvido para uma lanchonete fictícia da Fatec, combinando hardware (Arduino) e software (Java). Ele permite que os clientes selecionem produtos, gerenciem um carrinho de compras e realizem pagamentos de forma segura via senha, proporcionando uma experiência automatizada e eficiente.

🚀 Funcionalidades
Menu de Produtos Interativo: Navegação fácil entre categorias (Sucos, Salgados, Doces) com exibição de itens e preços.

Carrinho de Compras: Adicione múltiplos itens, visualize o total e o preço individual de cada item adicionado.

Pagamento por Senha: Sistema de pagamento seguro que requer uma senha numérica para finalizar a compra, com validação de saldo.

Gestão de Saldo (FateCoins): Simulação de moeda virtual para transações.

Modo Administrador:

Acesso restrito por senha (123).

Adicionar FateCoins: Recarregue o saldo do sistema.

Ver Log de Compras: Visualize um histórico detalhado de todas as transações realizadas (armazenamento local em memória).

Feedback Sonoro: Sons de "beep" ao digitar, "sucesso" na conclusão da compra e "erro" em situações como senha incorreta ou saldo insuficiente.

Interface Gráfica (GUI): Simula um display de caixa com duas linhas para mensagens e informações de saldo.

💻 Tecnologias Utilizadas
Hardware:

Arduino (Uno ou similar)

Teclado Matricial 4x4

Sensor Ultrassônico HC-SR04 (utilizado apenas para feedback visual do LED, não para pagamento)

Software (Java):

Java Development Kit (JDK)

Swing (para a interface gráfica)

JSerialComm (para comunicação serial com o Arduino)

IDE:

Arduino IDE (para o código do microcontrolador)

Eclipse (para o desenvolvimento Java)

⚙️ Configuração e Instalação
1. Hardware (Arduino)
Conexões do Teclado Matricial 4x4:

Conecte as linhas e colunas do teclado aos pinos digitais do Arduino conforme o código Arduino.

Exemplo de pinagem comum:

Linhas: D9, D8, D7, D6

Colunas: D5, D4, D3, D2

Conexões do Sensor Ultrassônico HC-SR04 (Opcional, apenas para LED):

VCC ao 5V do Arduino

GND ao GND do Arduino

Trig ao Digital 10 do Arduino

Echo ao Digital 11 do Arduino

Carregar o Código Arduino:

Abra o arquivo .ino (código Arduino) na Arduino IDE.

Verifique se os pinos no código correspondem às suas conexões físicas.

Selecione a placa (Tools > Board) e a porta serial (Tools > Port).

Faça o upload do código para o seu Arduino.

2. Software (Java)
Clone o Repositório:

git clone https://github.com/seu-usuario/seu-repositorio.git
cd seu-repositorio

Importar para o Eclipse:

Abra o Eclipse.

Vá em File > Import... > General > Existing Projects into Workspace > Next.

Selecione a pasta raiz do projeto clonado e clique em Finish.

Adicionar a Biblioteca JSerialComm:

Baixe a última versão da biblioteca JSerialComm em https://fazecast.github.io/jSerialComm/.

No Eclipse, clique com o botão direito no seu projeto (BancaFatecGUI).

Vá em Properties > Java Build Path > Libraries > Add External JARs....

Selecione o arquivo .jar da JSerialComm que você baixou.

Clique em Apply and Close.

Configurar Arquivos de Som:

Certifique-se de que os arquivos de som (beep.wav, success.wav, error.wav) estejam no formato PCM de 16 bits, 44100 Hz. Você pode convertê-los usando o Audacity.

Crie uma pasta chamada sounds dentro da pasta src do seu projeto Java.

Coloque os arquivos .wav convertidos dentro da pasta src/sounds/.

No Eclipse, clique com o botão direito no projeto e selecione Refresh (ou F5).

Compilar e Executar:

Limpe e reconstrua o projeto (Project > Clean... e Project > Build Project).

Execute a classe Main.java (clique com o botão direito Run As > Java Application).

🚀 Como Usar
Conectar à Porta Serial:

Ao iniciar a aplicação Java, selecione a porta COM correta do seu Arduino no menu suspenso.

Clique em "Conectar".

Interação com o Teclado (Hardware):

Use o teclado matricial conectado ao Arduino para navegar pelos menus e selecionar itens.

Menu Principal:

1: Sucos

2: Salgados

3: Doces

4: Saldo (visualiza FateCoins)

A: Modo Administrador

D: Adiciona 500 FateCoins (cheat para teste)

*: Voltar ao menu anterior ou cancelar operação.

#: Finalizar carrinho e ir para o pagamento.

Seleção de Itens: Dentro dos submenus (Sucos, Salgados, Doces), digite 1, 2 ou 3 para adicionar o item correspondente ao carrinho.

Pagamento: Após pressionar # para finalizar o carrinho, a tela pedirá a "SENHA PAGAR:". Digite a senha (456) para concluir a compra.

Modo Administrador:

Senha de acesso: 123

Opções: 1 (Adicionar FateCoins), 2 (Ver Log de Compras).

🤝 Contribuição
Sinta-se à vontade para fazer um fork deste repositório, propor melhorias e abrir Pull Requests.

📄 Licença
Este projeto está licenciado sob a licença MIT.

Desenvolvido por: Manuela Gadelho
