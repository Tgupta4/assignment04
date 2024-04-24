import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

public class GarageDoorOpener {
	public static enum DoorState {UP, DOWN};

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Usage: java GarageDoorOpener <port number> <door description>");
			System.exit(1);
		}
		int portNumber = Integer.parseInt(args[0]); 
		var opener = new GarageDoorOpener(portNumber, args[1]);
		System.out.println("\nOpening socket and waiting");
		try (
				var serverSocket = new ServerSocket(portNumber);
				Socket clientSocket = serverSocket.accept();
				// outData is not used here but it could be used for sending
				// binary data--we used it last semester to send the count of the
				// the GunballMachine (an int)
				// var outData = new DataOutputStream(clientSocket.getOutputStream());
				
				// outText is used to send text 
				var outText = new PrintWriter(clientSocket.getOutputStream(), true);
				
				// outObj is used to send serialized objects
				var outObj = new ObjectOutputStream(clientSocket.getOutputStream());
				
				// in is used to read text from the client
				var in = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()))) {
			String inputLine; 
			while ((inputLine = in.readLine()) != null) {
				System.out.println("Received " + inputLine);
				if(inputLine.toLowerCase().equals("getdoor")) {
					outText.println(opener.getDoorDescription());
					// TODO use outText.println
					// to send the opener's doorDescription
				} else if(inputLine.toLowerCase().equals("getdoorstate")) {
					// this is how you send a opener's serialized stateOfDoor
					outObj.writeObject(opener.getStateOfDoor());
				} else if(inputLine.toLowerCase().equals("getstate")) {
					outObj.writeObject(opener.getCurrentState());
					// TODO send the opener's serialized currentState
				} else if(inputLine.toLowerCase().startsWith("set code")) {
					// TODO use inputLine.toLowerCase().split("\\s+") to get an
					// array of Strings and convert it to the code to set the
					// code of opener.
					// Note that the client will send a message such as
					// "set code 3 9 5 2"
					// so the array of Strings would be 
					// arr = ["set", "code", "3", "9", "5", "2"] 
					// you extract the int from a String such as "5" 
					// using arr[4].charAt(0)-'0'
					// or using Integer.parseIng(arr[4])
					String[] parts = inputLine.toLowerCase().split("\\s+");
						// Convert the string parts to integers and set the code
						if (parts.length == 6) { // Make sure the command includes exactly four numbers
							int[] newCode = new int[4];
							try {
								for (int i = 0; i < 4; i++) {
									newCode[i] = Integer.parseInt(parts[i+2]); // parts[2], parts[3], parts[4], parts[5]
								}
								opener.setCode(newCode);
								System.out.println("New code set: " + Arrays.toString(newCode));
							} catch (NumberFormatException e) {
								System.out.println("Invalid code format. Please enter four integers.");
							}
						} else {
							System.out.println("Incorrect number of code digits. Please enter exactly four integers.");
						}
				} else if (inputLine.length() == 1 
						&& '0' <= inputLine.charAt(0) && '9' >= inputLine.charAt(0)) {
					// we pass the keypad digit to opener 
					opener.enterKey(inputLine.charAt(0) - '0');
				} else if (inputLine.equals("-1")) {
					// we pass translate "-1" to the int -1 and pass it to the opener 
					opener.enterKey(-1);
					// opener.runDoorMotor();
				} else {
					outText.println("Invalid Request");
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port "
					+ portNumber + " or listening for a connection");
			System.out.println(e.getClass());
			System.out.println(e.getMessage());
		}
	}

	private String doorDescription;
	private int[] code = new int[4];
	private boolean valid = false;
	private State currentState = State.WAITING;
	private DoorState stateOfDoor = DoorState.DOWN;

	public GarageDoorOpener(int portNumber, String doorDescriptionIn) {
		doorDescription = doorDescriptionIn;
		setCode(1,2,3,4);
	}
	public void doorMotorOn() {
		if (stateOfDoor == DoorState.DOWN) {
			System.out.println("Opening Door " + doorDescription);
			stateOfDoor = DoorState.UP;
		} else {
			System.out.println("Closing Door " + doorDescription);
			stateOfDoor = DoorState.DOWN;			
		}
	}
	public void enterKey(int k) {
		if(k != -1) {
			// TODO get the new value for currentState 
			// by calling the enter_key of currentState
			currentState = currentState.enter_key(k, this);
			
		} else {
			// TODO get the new value for currentState
			currentState = currentState.press_open(this);
			// by calling the press_open of currentState
		}
		doorMotorOn();
		// the following output is just for tracking the state changes
		System.out.println(currentState + ", valid = " 
				+ valid + ", code = " + Arrays.toString(code));
	}
	public void setCode(int... codeIn) {
		code = codeIn;
		// the following output is just for tracking the code setting
		System.out.println(Arrays.toString(code));
	}
	public State getCurrentState() {
		return currentState;
	}
	public String getDoorDescription() {
		return doorDescription;
	}
	public DoorState getStateOfDoor() {
		return stateOfDoor;
	}
	// public int getCode(int i) {
	// 	return code[i];
	// }
	public void runDoorMotor() {
    // Implementation logic here, for example:
	if (stateOfDoor == DoorState.DOWN) {
		System.out.println("Opening Door " + doorDescription);
		stateOfDoor = DoorState.UP;
	} else {
		System.out.println("Closing Door " + doorDescription);
		stateOfDoor = DoorState.DOWN;         
	}
    // System.out.println("Door motor is running");
}

	public int[] getCode() {
		return new int[] {1, 2, 3, 4}; // Example code
	}
	
	public void setValid(boolean validIn) {
		valid = validIn;		
	}
	// NOTE that getter methods for booleans should be called
	// is... instead of get...
	public boolean isValid() { 
		return valid;
	}
}
