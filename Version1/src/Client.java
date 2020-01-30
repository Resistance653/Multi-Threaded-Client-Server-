import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;


public class Client extends Thread
{
	private Socket sock; // socket
	private BufferedReader readIn; // used to receive data from server
	private DataOutputStream sendOut; //used to send data to server
	private static String address; //ip = 192.168.101.131
	private static int port;
	private static String command = "0";
	private static long totalFinalTime = 0;


	public static void main (String args[])
	{
		if(args.length !=2)
		{
			System.out.println("Enter a an address and port number");
			System.exit(1);
		}

		address = args[0];
		port = Integer.parseInt(args[1]);

		//DataInputStream inStream;
		//DataOutputStream outStream;
		Scanner input = new Scanner(System.in);
		//String menu; // used to store menu given by the server
		//String serverResponse = null; // holds server response
		int threadCount;
		int check = 0; //used for thread input check
		Client threads[];

		// the do while is to get the thread count from user and only
		// accepts numbers that are 0 or greater
		// it looks messy but works
		System.out.println("Please Enter the amount of client threads to make:");
		do {
			try {
				if(check != 0)
				{
					System.out.println("Please Enter a number that is 1 or greater");
				}
				threadCount = input.nextInt();
				check++;
			}

			catch(InputMismatchException mm)
			{
				System.out.println("Please enter a valid number");
				threadCount = -1;
				input.next();
			}
		} while (threadCount<0);

		threads = new Client[1000];


		System.out.println("Connecting to Server");
		//cSocket = new Socket(address, port); //connects to server
		System.out.println("Now connected to Server");

		//dis = new DataInputStream(
		//	new BufferedInputStream(cSocket.getInputStream()));
		//int printMenu = 0;

		//menu = readIn.readUTF();
		//System.out.print("\n\n" + menu);
		printMenu();
		while (!command.equals("7"))
		{

			//menu = readIn.readUTF();// get menu from server
			//System.out.print("\n\n" + menu); // prints menu for client
			command = input.next(); // get user input
			if(command.equals("7"))
			{
				System.out.println("Ending Connection");
				System.exit(0);
			}
			//teting below
			//sendOut.writeUTF(command);
			//serverResponse = inStream.readUTF();
			//System.out.println(serverResponse);

			// send/recv the command
			for (int i = 0; i < threadCount; i++)
				threads[i] = new Client(address, port, command);

			for (int i = 0; i < threadCount; i++)
				threads[i].start();

			System.out.println("Running Threads");

			for (int i = 0; i < threadCount; i++)
			{
				try
				{
					threads[i].join(); //waits 30 seconds per thread
				}
				catch (InterruptedException interupt)
				{

				}
			}

			System.out.println("\n\nThe average time in milliseconds: " + totalFinalTime/threadCount + "\n\n\n");
			totalFinalTime = 0;

			for(int i = 0; i < threadCount; i++)
				threads[i] = null;

			System.out.println("Please Enter the number of client threads to make:");
			do {
				try {
					if(check != 0)
					{
						System.out.println("Please Enter a number that is 1 or greater");
					}
					threadCount = input.nextInt();
					check++;
				}

				catch(InputMismatchException mm)
				{
					System.out.println("Please enter a valid number");
					threadCount = -1;
					input.next();
				}
			} while (threadCount<0);

			printMenu();
		}

		input.close();
	}
	//runs the threads
	public void run()
	{
		String serverResponse = "";
		String full = "";
		try
		{
			//System.out.println("Starting Thread");
			//connects thread to server and sets up input/outputs
			sock = new Socket(address, port);
			//sock.setSoTimeout(10000); //10 seconds to timeout
			readIn = new BufferedReader(new InputStreamReader(sock.getInputStream()), 1); // gets input from server
			sendOut = new DataOutputStream(sock.getOutputStream());
			//readIn = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
			//sendOut = new DataOutputStream(sock.getOutputStream());

			//send command and receive response

			//System.out.println("Sending Command");
			//start timer
			long startTIme = System.currentTimeMillis();
			sendOut.writeUTF(command);//send command

			while((serverResponse = readIn.readLine()) != null)
				full = full + serverResponse + '\n';


			//System.out.println("Command Sent");
			//end timer
			long finalTime = System.currentTimeMillis() - startTIme;
			totalFinalTime = totalFinalTime + finalTime;

			//while((serverResponse = readIn.readLine()) != null)
			//System.out.println(serverResponse);

			//System.out.println(full + '\n');
			sock.close();
			readIn.close();
			sendOut.close();
		}
		catch (IOException io)
		{
			System.out.println(io);
		}
	}

	//creates a Client using an address ,  port, and a command to run
	public Client (String address, int port, String command)
	{
		this.address = address;
		this.port = port;
		this.command = command;
	}

	private static void printMenu()
	{
		//prints out the menu for the user
		System.out.print("1)   Host current Date and Time\n" +
				"2)   Host uptime\n" +
				"3)   Host memory use\n" +
				"4)   Host Netstat\n" +
				"5)   Host Current users\n" +
				"6)   Host running processes\n" +
				"7)   Quit\n" +
				"     Please Select one of the options:");
	}

}
