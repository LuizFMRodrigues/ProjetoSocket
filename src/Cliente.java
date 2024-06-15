import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.Objects.nonNull;

public class Cliente {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private List<Pedido> pedidos = new ArrayList<>();
    private Scanner scanner;

    public Cliente(Socket socket) {
        try {
            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            scanner = new Scanner(System.in);
        } catch (IOException e) {
            fecharConexoes(socket, reader, writer);
        }
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    private void responder() {
        try {
            String mensagem = scanner.nextLine();
            writer.write(mensagem);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            fecharConexoes(socket, reader, writer);
        }
    }

    private void recebeMensagem() {
        new Thread(() -> {
            String mensagem;

            while (socket.isConnected()) {
                try {
                    mensagem = reader.readLine();

                    if (mensagem.equals("::")) {
                        responder();
                    } else {
                        System.out.println(mensagem);
                    }
                } catch (IOException e) {
                    fecharConexoes(socket, reader, writer);
                }
            }
        }).start();
    }

    private void fecharConexoes(Socket socket, BufferedReader reader, BufferedWriter writer) {
        try {
            if (nonNull(reader)) {
                reader.close();
            }

            if (nonNull(writer)) {
                writer.close();
            }

            if (nonNull(socket)) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            Cliente cliente = new Cliente(socket);
            cliente.recebeMensagem();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
