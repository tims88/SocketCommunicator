package com.tim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Communicator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Fire off the server thread
		new Thread(new MessageReceiver()).start();
		
		Scanner keyboard = new Scanner(System.in);
		String messageToSend = "";
		
		
		do {
			
			messageToSend = keyboard.nextLine();
			
			try {
				Socket client = new Socket("192.168.1.102", 15555);
				PrintWriter pw = new PrintWriter(client.getOutputStream());
				
				pw.write(messageToSend);
				pw.flush();
				pw.close();
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		} while (!messageToSend.equals("exit"));
		
		keyboard.close();
	}

}


class MessageReceiver implements Runnable {

	@Override
	public void run() {
		ServerSocket serverSocket = null;
		String message = "";
		try {
			serverSocket = new ServerSocket(4444);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		do {
				
			Socket phone;
			try {
				System.out.println("Listening on port 4444");
				phone = serverSocket.accept();
				System.out.println("connection accepted");
				InputStreamReader inReader = new InputStreamReader(phone.getInputStream());
				BufferedReader buffReader = new BufferedReader(inReader);
				
				message = buffReader.readLine();
				
				System.out.println("Message received from " + phone.getInetAddress().toString());
				System.out.println("Message = " + message);
				
				phone.close();
				inReader.close();
				buffReader.close();
				
				openSwtWindow(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} while (!message.equals("exit"));
		
	}
	
	private void openSwtWindow(String message) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText(message);
		shell.open();
		while(!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	
}
