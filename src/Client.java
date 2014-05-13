
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;

public class Client extends Thread{
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private BigInteger serversMessage;
	private String typedMessage="";
	private String serverIP;
	private Socket connect;
	private Scanner scanner = new Scanner(new InputStreamReader(System.in));
	private RSA rsa = new RSA();
	
	/* TO DECRYPT INCOMING MESSAGES 
	 client private key(d,n) */
	private String clientn ="1555717617898471144739065971176777021345052597780024950628137714415443841561096386575514142440519697453493422295218681877502206666496022"
  			+ "83223205534235569311952723694100246923854814709474560015"
  			+ "277465687689003792940905371426511922051185650827107960189406942026362245235229790165017994405851253787600629920830457";
  	
  	private String clientd = "1546552384767692137358327005710021325297156664842529856986672642218203735229284466801237210881873473496778851759823587748328515"
  			+ "54904656534338165794426301423078849694541540241161006735061945188072341601788222"
  			+ "232695891934367309334848322029232936297084369679700674580810003226759027238262481796989109287829031017";
  	private BigInteger ClientsD = new BigInteger(clientd);
  	private BigInteger ClientsN = new BigInteger(clientn);
  	/* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
  	
  	/* TO ENCRYPT OUTGOING MESSAGES 
  	 * server public key(n,e)*/
  	private String servern = "11358399545168601447310738867217140918851397209335881820977780677"
  			+ "89819406952739076493427897764438305289815003963291656423426336641648844"
  			+ "7172472651500662062570010566442918508798611583410971982254277071325210900793"
  			+ "9865073241439867793620116095492815562498994702016168492311530795821861504416822482786427035015289";
  	  	
  	private String servere = "9701758799274492119722418901520399447231059649816812497124346725538149758449741197791015960245967193059492904832359879146"
  			+ "797506385622362960763984519602579729025274370202910558874742121536127256330170733"
  			+ "9970141184291531531988830703862015072920562814602939082213017832130417768961451511511650874758805564038495";
  	private BigInteger ServersN = new BigInteger(servern);
  	private BigInteger ServersE = new BigInteger(servere);
	/*>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
  	
  	
	/**
	 * This constructor gets the server IP address of the server
	 * to want to connect to.  
	 * @param the hostName is the IP address of the server
	 */
	public Client(String hostName){
		System.out.println("Messages are encrypted.");
		serverIP = hostName;
	}
	
	/**
	 * Client will try and connect to the server using
	 * the IP address and then the port number
	 * After connecting to the server the data streams will be
	 * created with setUpInputOutput().  Then the main part of the
	 * program begins with checkForIncomingMessages().
	 */
	public void startClient(){
		try{
			
			System.out.println("Trying to connect to server...");
			connect = new Socket(InetAddress.getByName(serverIP), 6066);
			System.out.println("Now connected to " + connect.getInetAddress().getHostName()); 
			
			//object data streams are created
			setUpInputOutput();
			
			// the main loop for the program is contained in this function
			checkForIncomingMessages();
			
		}catch(IOException e){e.printStackTrace();}
		 finally{
			 // close all connections to server
			endConnection();
		}
	}
	
	/**
	 * Thread continuously loops to look for typed messages by the client
	 *  until the client would like to quit the chat.
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	
	public void run(){
		System.out.println("Enter text here: ");
		do{
			typedMessage = scanner.nextLine();
			
			//Encrypt with RSA <><><><><><><><><><><><><><><><><><><><><><>
			BigInteger plainText = new BigInteger(typedMessage.getBytes());
			BigInteger cipherText = rsa.encrypt(plainText,ServersE,ServersN);
			// MESSAGE is now encrypted.  Send to client to decrypt and read
				
			sendMessage(cipherText); // message is formatted and sent to Server
			
		}while(!typedMessage.equals("Quit"));
	}
	
	/**
	 * The thread to check for typed messages by the client
	 * starts here.  Then the rest of this function is for
	 * checking to see if the server has typed and sent any
	 * message.  This function will continue to loop until the server 
	 * decides to quit.  After that the thread will also be stopped.  
	 * 
	 * @throws IOException
	 */
	public void checkForIncomingMessages() throws IOException{
		BigInteger plainText = null;
		String strMessage = "";
		start();  //start thread to create typed messages
		do{
			try{
				//reads sent messages from the server program
				serversMessage = (BigInteger) input.readObject();
					
				//RSA will decrypt message
				plainText = rsa.decrypt(serversMessage, ClientsD, ClientsN);
				
				strMessage = "SERVER- "+ new String(plainText.toByteArray());
				System.out.println(strMessage);
				
			}catch (ClassNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				endConnection();
			}
		} while(!strMessage.equals("SERVER- Quit"));		
		interrupt();
	}
	
	/**
	 * Messages are formatted and sent to the server
	 * 
	 * @param the message that the client typed
	 */
	public void sendMessage(BigInteger theMessage){
		try {
			output.writeObject(theMessage);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The input and output data streams to the server are set up.  
	 * @throws IOException
	 */
	public void setUpInputOutput() throws IOException{
		output = new ObjectOutputStream(connect.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connect.getInputStream());
		System.out.println("Streams Created..");
	}
	
	/**
	 * All connections to the server closed off
	 * and the program is terminated.
	 */
	public void endConnection(){
		System.out.println("Ending Connection..");
		 try{
				output.close();
				input.close();
				connect.close();
				System.exit(1);
			}catch(IOException e){e.printStackTrace();}
	}


}
