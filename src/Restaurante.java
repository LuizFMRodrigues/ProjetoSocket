import java.io.*;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;

import static java.util.Objects.nonNull;

public class Restaurante {

    private Cliente cliente;
    private ServerSocket serverSocket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public Restaurante(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    private void initialize() {
        preencherCardapio();
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                cliente = new Cliente(socket);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                System.out.println("Novo cliente chegou!");
                enviarCardapio();
                pegarPedido();
            }
        } catch (IOException e) {
            terminate();
        }
    }

    private void enviarCardapio() throws IOException {
        System.out.println("Enviando cardápio!");

        enviarMensagem("-------------- Bem vindo --------------");
        writer.newLine();

        for (Pedido prato : Pedido.getCardapio()) {
            enviarMensagem(MessageFormat.format("{0} - {1} - R$ {2}", prato.getNumPedido(), prato.getNomePrato(), prato.getValor()));
        }
    }

    private void pegarPedido() throws IOException {
        System.out.println("Anotando pedido!");

        writer.newLine();
        enviarMensagem("Faça seu pedido (digite 0 para finalizar):");

        String numPedido;

        while (!(numPedido = aguardarResposta()).equals("0")) {
            Pedido pedido = Pedido.procuraPedido(Integer.parseInt(numPedido));
            cliente.getPedidos().add(pedido);
        }

        enviarMensagem("----------------------------------");
        enviarMensagem("Resumo do pedido:");

        BigDecimal total = new BigDecimal(0);

        writer.newLine();
        for (Pedido pedido : cliente.getPedidos()) {
            enviarMensagem(MessageFormat.format("{0} - R$ {1}", pedido.getNomePrato(), pedido.getValor()));
            total = total.add(pedido.getValor());
        }

        writer.newLine();
        enviarMensagem("Total: R$ " + total);

        writer.newLine();
        enviarMensagem("Confirmar pedido? [S/n]");

        if (!aguardarResposta().equalsIgnoreCase("s")) {
            enviarMensagem("Pedido cancelado!");
        } else {
            enviarMensagem("Vamos preparar seu pedido!");
        }
    }

    private String aguardarResposta() throws IOException {
        writer.write("::");
        writer.newLine();
        writer.flush();
        return reader.readLine();
    }

    private void enviarMensagem(String mensagem) throws IOException {
        writer.write(mensagem);
        writer.newLine();
        writer.flush();
    }

    private void terminate() {
        try {
            if (nonNull(serverSocket)) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void preencherCardapio() {
        new Pedido(1, "Macarrão à carbonara", new BigDecimal("40.58"));
        new Pedido(2, "Risoto de camarão", new BigDecimal("30.49"));
        new Pedido(3, "Salmão ao molho de alcaparras", new BigDecimal("55.32"));
        new Pedido(4, "Fettuccine com cogumelo paris", new BigDecimal("37.89"));
        new Pedido(5, "Paillard de filet", new BigDecimal("70.42"));
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Restaurante restaurante = new Restaurante(serverSocket);
        restaurante.initialize();
    }
}
