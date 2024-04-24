package assignment04;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class RemoteOpener {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Usage: java RemoteOpener <host url> <port number>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[1]);
		String host = args[0];
		try (
				var reportSocket = new Socket(host, portNumber);
				// for sending text data
				var out = new PrintWriter(reportSocket.getOutputStream(), true);
				
				// in Data could be used to receive binary data such as int but
				// this time the server is not set up to send simple binary data
				// var inData = new DataInputStream(reportSocket.getInputStream());

				// just as an illustration we can use Scanner instead of BufferedReader
				// to read text
				var inScanner = new Scanner(reportSocket.getInputStream());

				// we can use inStr to read text
				var inStr = new BufferedReader(new InputStreamReader(
						reportSocket.getInputStream()));
				
				// we use inObj to read serialized data
				var inObj = new ObjectInputStream(reportSocket.getInputStream());
				
				// stdIn is used to read the client's keyboard
				var stdIn = new Scanner(System.in);) {
			System.out.println("Make Status Request");	
			String userInput;
			while(stdIn.hasNextLine()) {
				userInput = stdIn.nextLine();
				if(userInput.toLowerCase().startsWith("set code")) {
					// must be used in the form "set code u v w x" where u, v, w, x are
					// single digits to set the code.
					// There is no error checking
					out.println(userInput.toLowerCase()); 
					continue;
				}
				switch(userInput.toLowerCase()) {
				case "door id":
					// TODO use out.println to send the message "getDoor"
					
					// the following prints the server response
					System.out.println("Door is " + inScanner.nextLine());
					break;
				case "door state":
					// TODO use out.println to send the message "getDoorState"
					
					// the following deserializes the response and prints it
					GarageDoorOpener.DoorState dstate = (GarageDoorOpener.DoorState)inObj.readObject();
					System.out.println("Door State is " + dstate);
					break;
				case "state":
					// TODO use out.println to send the message "getState"
					
					// TODO use code similar to the code above for DoorState to receive,
					// deserialize, and print the State of the server
					break;
				case "0", "1", "2", "3", "4", "5", "6", "7", "8", "9":
					// send the digits to the server
					out.println(userInput);
					break;
				case "open-close":
					// send the code for open/close to the server
					out.println("-1");
					break;
				default:
					System.out.println("Request \"door id\", \"door state\","
							+ " \"state\", \"set code d1 d2 d3 d4\", \n\"open-close\", \"0 through 9\" ");
				}
				System.out.println("Enter Command");	
			}
		} catch (ClassNotFoundException e) {
			System.err.println("Serialization problem");
			System.exit(1);
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + host);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " +
					host);
			System.exit(1);
		} 	
	}
}
