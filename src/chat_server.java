/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Samjhana
 */
public class chat_server {
}
    static Vector<Socket> clientSockets = new Vector<>();
    static Vector<String> usernames = new Vector<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1201);
        System.out.println("Server started...");

        while (true) {
            Socket client = serverSocket.accept();
            clientSockets.add(client);
            new ClientHandler(client).start();
        }
    }

    static class ClientHandler extends Thread {
        Socket socket;
        DataInputStream dis;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                dis = new DataInputStream(socket.getInputStream());
                new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
            }
        }

        public void run() {
            String msg;
            try {
                while ((msg = dis.readUTF()) != null) {
                    for (Socket s : clientSockets) {
                        if (s != socket) {
                            new DataOutputStream(s.getOutputStream()).writeUTF(msg);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Client disconnected.");
            }
        }
    }

