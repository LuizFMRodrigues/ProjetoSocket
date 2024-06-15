# Projeto Socket!

Este é um projeto para faculdade que tem como base a finalidade de aprender a utilizar **Socket**. O sistema foi feito em **Java** utilizando o **GitHub** e o **IntelliJ**.

O projeto funciona como um atendimento inicial de um restaurante, realizando o pedido do cliente.


# Execução

Para executar o programa, basta rodar a **Main** do **Restaurante.java** e depois executar o **Cliente.java**.

### Rodar em máquinas diferentes

Para rodar em máquinas diferente é preciso alterar as propriedades do socket. Eu criei o servidor na main do **Restaurante.java**: `new ServerSocket(1234);`. Já no **Cliente.java**: `new Socket("localhost", 1234);` (o localhost precisa ser alterado para o IP da outra máquina).
