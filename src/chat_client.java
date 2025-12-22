import javax.swing.JFrame;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class chat_client extends JFrame {

    static Socket socket;
    static DataInputStream dis;

    public static void main(String[] args) {

        try {
            socket = new Socket("localhost", 1201);
            dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            // Thread to receive messages
            new Thread(() -> {
                try {
                    while (true) {
                        String msg = dis.readUTF(); // blocks until message arrives
                        System.out.println("Received: " + msg);
                        // update UI here
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed");
                }
            }).start();

        } catch (IOException e) {
        }
    }
}
