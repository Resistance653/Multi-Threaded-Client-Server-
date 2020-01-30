import java.net.*;
import java.io.*;
//import java.util.*;
import java.lang.*;

public class Server extends Thread
{
	private Socket sock = null;
	public static void main (String args[]) throws IOException, InterruptedException
	{
		int port = 2553; // initailize port number variable

		ServerSocket server = new ServerSocket(port); // creates server socket w/ port
		//Socket socket = null;
		//Server threads= null;
		int i = 0; // used for counting
		int threadCount = 0;

		System.out.println("Server has been started");
		System.out.println("Waiting for client handshake ...");

		while(true) // endless loop causes server to run forever
		{
			//ServerSocket server = new ServerSocket(port);
			Socket socket = server.accept();
			Server thread = new Server(socket);
			thread.start();
			thread.join(10);

		} // end while
	} // end main

	private Server (Socket socket)
	{
		sock = socket;
	}

	public void run()
	{
		try
		{
			DataInputStream clientInput = new DataInputStream(sock.getInputStream());
			DataOutputStream sendOut = new DataOutputStream(sock.getOutputStream());
			String userInput = "0";
			//System.out.println("Connection has been established");
			userInput = clientInput.readUTF(); // used to hold clients input

			Process process;
			InputStream inputStream;
			BufferedReader bufferedReader;
			String x = "";
			String line = "";
			String full = "";
			boolean inputValid = true;

			switch (userInput) {
				case "1":
					x = "date";
					break;
				case "2":
					x = "uptime";
					break;
				case "3":
					x = "free";
					break;
				case "4":
					x = "netstat";
					break;
				case "5":
					x = "who";
					break;
				case "6":
					x = "ps -e";
					break;

			}
			if(inputValid == true)
			{
				process = Runtime.getRuntime().exec(x);
				inputStream = process.getInputStream();
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 1);
				while ((line = bufferedReader.readLine()) != null)
					full = full + line + '\n';
				process.destroy();
				sendOut.writeUTF(full);

			} // end if

			//System.out.println("Ending Connection");
			sendOut.close();
			sock.close();
			clientInput.close();
		}
		catch (IOException io)
		{
			System.out.println(io);
		}
	}

} // Server class
