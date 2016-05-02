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
	private int serverport;
	Thread[] t;
	private byte[] message;
	private byte bs[];

	public Socketlib(String serverIp, int serverport) {
		serverip = serverIp;
		this.serverport = serverport;
		
		t = new Thread[2];
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
		t[2].start();
	}

	public void Sendmessage(String send) {// 메시지 송신(String, 암호화 필요하지 않을 시 사용
		message = send.getBytes();
		t[1].start();

	}public void Sendmessage(byte[] send) {// 메시지 송신(byte 자료형. 암호화 필요 시 사용
		message = send;
		t[1].start();

	}

	public String Receivemessage() {// 콜백 함수로 제작하기. 추후 추가 예정
		if (t[0].isAlive()) {
			bs = null;
			try {
				in.read(bs);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return bs.toString();
		}
		return "Error";
	}

	public void run() {
		if (t[0].getId() == (Thread.currentThread()).getId()) {// 만약 첫번째 스레드라면
																// 연결을 유지한다.
																// 연결되면 두번째 스레드를
																// start한다.
			try {
				// 소켓을 생성하여 연결을 요청한다.
				socket = new Socket(serverip, serverport);// 서버 ip, 포트
				out = socket.getOutputStream();// 송신
				in = socket.getInputStream();// 수신
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (t[1].getId() == (Thread.currentThread()).getId()) {// 만약 두번째
																		// 쓰레드라면
																		// 송신
																		// 명령이다.
			try {
				out.write(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {// 만약 세 번째 스레드라면 Disconnect 명령이다.
			try {
				socket.close();
				System.out.println("Socket closed");
				in.close();
				out.close();
				System.out.println("Stream closed");

			} catch (IOException e) {
				System.out.println("No Socket or Stream");
				e.printStackTrace();
			}

		}

	}

}
