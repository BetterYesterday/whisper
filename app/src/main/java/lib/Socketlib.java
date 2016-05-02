package lib;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Socketlib implements Runnable {
	private Socket socket;
	InputStream in;
	DataInputStream dis;
	OutputStream out;
	DataOutputStream dos;
	private String serverip;
	private int selection;
	private StringBuffer received = new StringBuffer();
	Thread[] t;
	private StringBuffer sending = new StringBuffer();
	private String message;

	public Socketlib(String serverIp) {
		serverip = serverIp;
		t = new Thread[2];
	}

	public void selection(int input) {// 인풋 값을 통해 해당 쓰레드에 필요한 기능을 지시.
		selection = input;

	}

	public void Connect() {// 인풋 값 1
		if (this.socket.isConnected()) {
			System.out.println("socket now Connected");
			return;
		}
		t[0] = new Thread(this);
		t[1] = new Thread(this);
		t[2] = new Thread(this);
		t[0].start();
		System.out.println("socket Connected");
	}

	public void DisConnect() {// 인풋 값 2

	}

	public void Sendmessage(String send) {// 메시지 송신
	}

	public void Receivemessage() {// 콜백 함수로 제작하기. 추후 추가 예정
	}

	public void run() {
		if(t[0].getId()==(Thread.currentThread()).getId()){//만약 첫번째 스레드라면 연결을 유지한다. 연결되면 두번째 스레드를 start한다.
			try {
				// 소켓을 생성하여 연결을 요청한다.
				socket = new Socket(serverip, 80);// 서버 ip, 포트
				out = socket.getOutputStream();//송신
				in = socket.getInputStream();//수신
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(t[1].getId()==(Thread.currentThread()).getId()){//만약 두번째 쓰레드라면 수신 메시지를 확인하고, 연결 상태가 false라면 쓰레드를 종료한다.
			
		}
		else{//만약 세 번째 스레드라면 메인에서 넘어온 명령으로, 송신 명령이므로 메시지를 송신하는 역할을 한다.
			
		}
		switch (selection) {
		case 1: {// Connect
			
		}
		case 2: {// Disconnect
			// Socket close
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
				out.write(message.length());
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
