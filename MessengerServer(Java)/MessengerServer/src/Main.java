
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	public static void main(String[] args) {
		
		//Scanner Object
		Scanner sc = new Scanner(System.in);
				
		//get port number
		int port = 0;
		while(port < Constants.MIN_PORT || port > Constants.MAX_PORT) {
			System.out.print("Please Enter Port Number from 1 to 65535: ");
			try {
				port = Integer.parseInt(sc.nextLine());
			}catch(NumberFormatException e) {
				System.out.println(e);
			}
		}
		sc.close();
		
		//Run server
		System.out.println("Running Server...");
		server(port);
	}

	private static void server(int port) {
		LinkedList<ClientHandler> clients = new LinkedList<ClientHandler>();
		LinkedList<String> chat = new LinkedList<String>();
		
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(port, 10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//accept client socket
		acceptClients(serverSocket, clients, chat);
		
		//close server
		try{
			serverSocket.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//accepts clients
	private static void acceptClients(ServerSocket serverSocket, LinkedList<ClientHandler> clients, LinkedList<String> chat) {
		while(true) {
			try{
				Socket client = serverSocket.accept(); 
				System.out.println("A new client has connected: " + client.getInetAddress());

				DataInputStream dis = new DataInputStream(client.getInputStream());
				DataOutputStream dos = new DataOutputStream(client.getOutputStream());
				
				System.out.println("Now creating thread for client " + client.getInetAddress());
				ClientHandler ch = new ClientHandler(client, dis, dos, chat, clients);
				Thread t = new Thread(ch);
				t.start();
				clients.addFirst(ch);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}
