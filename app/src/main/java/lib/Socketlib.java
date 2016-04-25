package lib;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Socketlib extends Thread {
    private Socket socket;
    InputStream in;
    DataInputStream dis;
    OutputStream out;
    DataOutputStream dos;
    private String serverip;
    private int selection;
    private StringBuffer received = new StringBuffer();
    Socketlib t = new Socketlib(serverip);
    private StringBuffer sending = new StringBuffer();

    public Socketlib(String serverIp) {
        serverip = serverIp;
    }

    public void selection(int input) {// 인풋 값을 통해 해당 쓰레드에 필요한 기능을 지시.
        selection = input;

    }

    public Socketlib(String serverIp, Socket socket) {
        serverip = serverIp;
        this.socket = socket;
    }

    public void Connect() {// 인풋 값 1
        t.selection(1);
        t.start();
    }

    public void DisConnect() {// 인풋 값 2
        t.selection(2);
        t.start();

    }

    public void Sendmessage(String send) {// 메시지 송신
        t.selection(3);

        t.start();
    }

    public void Receivemessage() {// 콜백 함수로 제작하기. 추후 추가 예정
        t.selection(4);
        t.start();

    }

    public void run() {
        switch (selection) {
            case 1: {// Connect
                try {
                    // 소켓을 생성하여 연결을 요청한다.
                    socket = new Socket(serverip, 80);// 서버 ip, 포트

                    // 소켓의 입력스트림을 얻는다.
                    in = socket.getInputStream();
                    dis = new DataInputStream(in);

                    // 소켓으로부터 받은 데이터를 출력한다.
                    System.out.println("서버로부터 받은 메세지 : " + dis.readUTF());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            case 2: {// Disconnect
                // 스트림과 소켓을 닫는다.
                try {
                    dis.close();
                    System.out.println("DataInputStream Closed");
                } catch (IOException e) {
                    System.out.println("No DataInputStream");
                    e.printStackTrace();
                }
                try {
                    socket.close();
                    System.out.println("Socket closed");
                } catch (IOException e) {
                    System.out.println("No Socket");
                    e.printStackTrace();
                }

            }
            case 3: {// Message Send
                try {
                    out = socket.getOutputStream();
                    dos = new DataOutputStream(out);
                    dos.writeBytes(sending.toString());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            case 4: {

            }
        }

    }

}
