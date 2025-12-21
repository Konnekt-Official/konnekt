package konnekt.view;

import konnekt.network.Server;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Chatbox extends javax.swing.JFrame {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Chatbox() {
        initComponents();
        connectToServer();
        receiveMessages();
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 5000); // connect to local server
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connected to server!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessages() {
        Thread receiveThread = new Thread(() -> {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    Recievermessage.setText(message);  // simple example: last message displayed
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receiveThread.start();
    }

    private void SendMouseClicked(java.awt.event.MouseEvent evt) {
        String message = Message.getText();
        if (!message.isEmpty()) {
            out.println(message);
            Sendermessage.setText(message); // display locally
            Message.setText("");
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        profilepicture = new javax.swing.JLabel();
        Name = new javax.swing.JLabel();
        Username = new javax.swing.JLabel();
        Audiocall = new javax.swing.JLabel();
        videocall = new javax.swing.JLabel();
        Camara = new javax.swing.JLabel();
        Send = new javax.swing.JLabel();
        Message = new javax.swing.JTextField();
        Voice = new javax.swing.JLabel();
        Emoji = new javax.swing.JTextField();
        Sendermessage = new javax.swing.JTextField();
        Recievermessage = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        profilepicture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/konnekt/resources/images/Screenshot.png")));
        Name.setText("Ana Bhatt");
        Username.setText("Bhatt567");
        Audiocall.setText("🔊");
        videocall.setText("🎥");
        Camara.setText("📷");

        Send.setText("✈️");
        Send.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SendMouseClicked(evt);
            }
        });

        Message.setText("Type message...");
        Emoji.setText("🙂");
        Sendermessage.setText("");
        Recievermessage.setText("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Emoji, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addComponent(Message, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addComponent(Send))
            .addComponent(Sendermessage, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(Recievermessage, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Sendermessage, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Recievermessage, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Emoji, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Message, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Send))
            )
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1));

        pack();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new Chatbox().setVisible(true));
    }

    // Variables
    private javax.swing.JLabel Audiocall;
    private javax.swing.JLabel Camara;
    private javax.swing.JTextField Emoji;
    private javax.swing.JTextField Message;
    private javax.swing.JLabel Name;
    private javax.swing.JTextField Recievermessage;
    private javax.swing.JTextField Sendermessage;
    private javax.swing.JLabel Send;
    private javax.swing.JLabel Username;
    private javax.swing.JLabel Voice;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel profilepicture;
    private javax.swing.JLabel videocall;
}

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        profilepicture = new javax.swing.JLabel();
        Name = new javax.swing.JLabel();
        Username = new javax.swing.JLabel();
        Audiocall = new javax.swing.JLabel();
        videocall = new javax.swing.JLabel();
        Camara = new javax.swing.JLabel();
        Send = new javax.swing.JLabel();
        Message = new javax.swing.JTextField();
        Voice = new javax.swing.JLabel();
        Emoji = new javax.swing.JTextField();
        Sendermessage = new javax.swing.JTextField();
        Sendermessage2 = new javax.swing.JTextField();
        Sendermessage3 = new javax.swing.JTextField();
        Recievermessage = new javax.swing.JTextField();
        Recievermessage1 = new javax.swing.JTextField();
        Recievermessage3 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        profilepicture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/konnekt/resources/images/Screenshot 2025-12-15 172125 - Copy.png"))); // NOI18N
        profilepicture.setText("jLabel1");

        Name.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Name.setText("Ana Bhatt");

        Username.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Username.setText("Bhatt567");

        Audiocall.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        Audiocall.setText("🔊 ");

        videocall.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        videocall.setText("🎥");

        Camara.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        Camara.setText("📷 ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(profilepicture, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Name)
                    .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(videocall)
                .addGap(18, 18, 18)
                .addComponent(Audiocall, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Camara)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(Audiocall, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(videocall)
                                    .addComponent(Camara, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(Name)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Username)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(profilepicture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        Send.setBackground(new java.awt.Color(204, 204, 204));
        Send.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        Send.setText("✈️");
        Send.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Message.setBackground(new java.awt.Color(204, 204, 204));
        Message.setForeground(new java.awt.Color(102, 102, 102));
        Message.setText("                                                                                               message");
        Message.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Message.addActionListener(this::MessageActionPerformed);

        Voice.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        Voice.setText("🎤");
        Voice.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Emoji.setBackground(new java.awt.Color(204, 204, 204));
        Emoji.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        Emoji.setText("🙂 ");
        Emoji.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Sendermessage.setText("       hyy! what are you doing?");
        Sendermessage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Sendermessage.addActionListener(this::SendermessageActionPerformed);

        Sendermessage2.setText("same, i'm bored already");
        Sendermessage2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Sendermessage2.addActionListener(this::Sendermessage2ActionPerformed);

        Sendermessage3.setText("deal, you're paying though");
        Sendermessage3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Recievermessage.setBackground(new java.awt.Color(0, 0, 0));
        Recievermessage.setForeground(new java.awt.Color(255, 255, 255));
        Recievermessage.setText("Nothing much, just scrolling on my phone you?");

        Recievermessage1.setBackground(new java.awt.Color(0, 0, 0));
        Recievermessage1.setForeground(new java.awt.Color(255, 255, 255));
        Recievermessage1.setText("wanna grab some snack?");
        Recievermessage1.addActionListener(this::Recievermessage1ActionPerformed);

        Recievermessage3.setBackground(new java.awt.Color(0, 0, 0));
        Recievermessage3.setForeground(new java.awt.Color(255, 255, 255));
        Recievermessage3.setText("Always you, let's go before i change my mind");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Sendermessage, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Sendermessage2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Sendermessage3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(Emoji, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(Message, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Send, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Voice)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Recievermessage3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Recievermessage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Recievermessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Sendermessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(Recievermessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addComponent(Sendermessage2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(Recievermessage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(Sendermessage3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(Recievermessage3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Message, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Send, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Voice)
                    .addComponent(Emoji, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MessageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MessageActionPerformed

    private void SendermessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendermessageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SendermessageActionPerformed

    private void Sendermessage2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Sendermessage2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Sendermessage2ActionPerformed

    private void Recievermessage1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Recievermessage1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Recievermessage1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Chatbox().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Audiocall;
    private javax.swing.JLabel Camara;
    private javax.swing.JTextField Emoji;
    private javax.swing.JTextField Message;
    private javax.swing.JLabel Name;
    private javax.swing.JTextField Recievermessage;
    private javax.swing.JTextField Recievermessage1;
    private javax.swing.JTextField Recievermessage3;
    private javax.swing.JLabel Send;
    private javax.swing.JTextField Sendermessage;
    private javax.swing.JTextField Sendermessage2;
    private javax.swing.JTextField Sendermessage3;
    private javax.swing.JLabel Username;
    private javax.swing.JLabel Voice;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel profilepicture;
    private javax.swing.JLabel videocall;
    // End of variables declaration//GEN-END:variables


