
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@AllArgsConstructor
@NoArgsConstructor

public class MyServer {
    private ServerSocket serverSocket;


    public void startServer() {
        try {

            while (!serverSocket.isClosed()) {

                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (Exception e) {

        }


    }
    public void closeServerSocket() {
        try {
            if(serverSocket != null) {
                serverSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(9112);
        MyServer myServer = new MyServer(serverSocket);
        myServer.startServer();

    }
}
