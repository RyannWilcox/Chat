import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
/**
 *  
 * @author ryanwilcox
 *
 */

public class Server extends Thread{
	private ArrayList<String> serverInformation = new ArrayList<String>();
	private int port = 0;
	private int messagesRecieved = 0;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connect;
	private BigInteger clientsMessage;
	private String typedMessage ="";
	private Scanner scanner = new Scanner(new InputStreamReader(System.in));
  	private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	private Calendar cal;
  	private LinkedList messageList = new LinkedList();
  	private RSA rsa = new RSA();
  	
  	/*FOR DECRYPTING INCOMING MESSAGES 
  	 * server private key(d,n)*/
  	private String servern = "11358399545168601447310738867217140918851397209335881820977780677"
  			+ "89819406952739076493427897764438305289815003963291656423426336641648844"
  			+ "7172472651500662062570010566442918508798611583410971982254277071325210900793"
  			+ "9865073241439867793620116095492815562498994702016168492311530795821861504416822482786427035015289";
  	
  	private String serverd ="61983066104507777706593035839775746678155581989100109379475345921348448299972334065395114882625020751845406804698089235471852320403432646993940829"
  			+ "87857988054312763148882764185716650753131391390"
  			+ "8939981118539017268423256649831522938572357512322682902198150180461909510400271501016714066801764457824886412135239";
  	
  	private BigInteger ServersD = new BigInteger(serverd);
  	private BigInteger ServersN = new BigInteger(servern);
  	/*>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
  	
  	
  	/*FOR ENCRYPTING OUTGOING MESSAGES
  	 * client public key(n,e) */
    private String clientn ="1555717617898471144739065971176777021345052597780024950628137714415443841561096386575514142440519697453493422295218681877502206666496022"
  			+ "83223205534235569311952723694100246923854814709474560015"
  			+ "277465687689003792940905371426511922051185650827107960189406942026362245235229790165017994405851253787600629920830457";
  		
  	private String cliente = "13080413965817974396588715876604258417858732829415916397891069741190769350192582555862358862071522689712936751072641179793055284063070084695"
  			+ "721376248228313693089659700895351836170"
  			+ "4437040696907005963011503936765067868049252661764084386672292999790830718123375307169998164731913686391240997718577274061103454393";
  	
  	private BigInteger ClientsN = new BigInteger(clientn);
  	private BigInteger ClientsE = new BigInteger(cliente);
  	/*>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
  	
	/**
	 * The user can name any port for the server.
	 * 
	 * @param the port number for the server
	 */

	public Server(int portNum){
			System.out.println("Messages are encrypted.");
			setPort(portNum);
	}
	
	/**
	 * Function to completely setup server communications.
	 * A new server socket is created and then it waits for a client.
	 * After that input output streams are created(setUpInputOutput()), after that the 
	 * function to check for incoming messages is called(checkForIncomingMessages())
	 * endConnection() will close connect, input and output.  
	 */
	public void startServer(){
		  try {
				cal = Calendar.getInstance();
			  	
			  	server = new ServerSocket(getPort(),100);
				System.out.println("Waiting for connection...");
				connect = server.accept(); // wait for client to connect to this server 
				System.out.println("Now connected to " + connect.getInetAddress().getHostName());
				
				//add info to ArrayList
				serverInformation.add("Server was connected to " + connect.getInetAddress().getHostName());
				serverInformation.add("Connected at "+ dateFormat.format(cal.getTime()));
			  
				/*Create input output stream to the client*/
				setUpInputOutput();
			  	
			    /*Main loop for the server*/
				checkForIncomingMessages();
			}catch (IOException e) {e.printStackTrace();}
		  	  finally{
		  		  endConnection();
				  cal = Calendar.getInstance();
				  
				  //add info to ArrayList
		  		  serverInformation.add("Disconnected at "+ dateFormat.format(cal.getTime()));
		  		  serverInformation.add("Server Recieved " + messagesRecieved + " message(s) from client.");
		  		  saveConversationToFile();
		  		  formatServerInfo(); // print to console
		  		  System.exit(1);
			}
	}
	  
	/**
	 * This thread will run and continuously check for
	 * messages typed by the server.
	 * sendMessage(String theMessage) will format the server message.
	 * This thread is started at the beginning of checkForIncomingMessages().  
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run(){
		System.out.println("Enter text here: ");
		do{
			//Thread will wait here until there is text to read
			typedMessage = scanner.nextLine();
			messageList.addToEnd("SERVER- " + typedMessage);
			
			//Encrypt with RSA <><><><><><><><><><><><><><><><><><><><><><>
			BigInteger plainText = new BigInteger(typedMessage.getBytes());
			BigInteger cipherText = rsa.encrypt(plainText,ClientsE,ClientsN);
			// Message is now encrypted.  Send to client
			
			sendMessage(cipherText);
		}while(!typedMessage.equals("Quit"));
	}
	
	/**
	 * starts thread to check for messages typed by server.
	 * Message contains input from the client connected to server
	 * If client types quit, thread is stopped and connections will be closed.
	 * @throws IOException IF connection is lost server is logged out
	 * 
	 */
	public void checkForIncomingMessages() throws IOException{
		BigInteger plainText = null;
		String strMessage = "";
		start(); //start thread to create typed messages
		do{
			 try {
				// input grabs client message
				 clientsMessage = (BigInteger) input.readObject();
				 
				 messagesRecieved++; // counting messages from client
				
				//RSA DECRYPTION
				plainText = rsa.decrypt(clientsMessage, ServersD, ServersN);
				strMessage = new String("CLIENT- "+ new String(plainText.toByteArray()));
				
				messageList.addToEnd(strMessage);
				System.out.println(strMessage);
							
				}catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				 endConnection(); // close all connections to client
				 cal = Calendar.getInstance();
				
				// add information to ArrayList
		  		 serverInformation.add("Disconnected at "
									   + dateFormat.format(cal.getTime()));
		  		 serverInformation.add("Server Recieved " +
									   messagesRecieved + " message(s) from client.");
				
				//save all information
		  		 saveConversationToFile();
				
				// print all information to console
		  		 formatServerInfo();
		  		 System.exit(1);
			}
		}while(!strMessage.equals("CLIENT- Quit"));	
		//stop thread from looping
		interrupt();
	}
	
	/**
	 * The message typed by the server is formatted and
	 * sent to the client that is connected to the server.
	 * 
	 * @param The message that the server just typed.  
	 */
	public boolean sendMessage(BigInteger theMessage){
		try {
			output.writeObject(theMessage);
			output.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Streams are created when the server connects to the client program
	 *
	 * @throws IOException if there is an error setting up streams
	 */
	public boolean setUpInputOutput() throws IOException{
		try{
		output = new ObjectOutputStream(connect.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connect.getInputStream());
		System.out.println("Streams Created..");
		return true;
		}catch(IOException e){
			System.err.println("IOException!");
			return false;
		}
	}
	
	/**
	 * Close all connections to client.
	 * Then exit the program
	 */
	public void endConnection(){
		System.out.println("Ending Connection..");
		 try{
				output.close();
				input.close();
				connect.close();
			}catch(IOException e){e.printStackTrace();}
	}
	/**
	 * Formats all the information gathered 
	 * by the ArrayList serverInformation(String)
	 */

	public void formatServerInfo(){
		for(String data : serverInformation){
			System.out.println(data);
		}
	}
	
	/**
	 * All Server information is saved to a file 
	 * following the end of the conversation
	 */
	public void saveConversationToFile(){
		File log = new File("messageLog.txt");
		String serverInfo="";
		String messageStr="";
		try{
		    FileWriter fileWriter = new FileWriter(log, true);
			// saves the time and date
		    for(int i = 0; i< serverInformation.size();i++){
		    	// creates string for the server information
		    	serverInfo = serverInfo + serverInformation.get(i)+"\n";
		    	
		    }
			fileWriter.append(serverInfo);
			
			// saves the actual conversation
			for(int i = 1; i < messageList.getSize();i++){
				// creates a string for the conversation
				messageStr = messageStr + messageList.findAtIndex(i) +"\n";
			}
			fileWriter.append(messageStr);
			
			//just to split up conversations saved to the log file
			fileWriter.append("******************************\n");
			fileWriter.append("******************************\n");
			fileWriter.close();
			System.out.println("Information saved to file.");
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * A getter method for port number of server
	 * @return the current port number
	 */
	public int getPort() {
		return port;
	}
	/**
	 * sets a new port number for the server
	 * @param port new port number for server
	 */
	public void setPort(int port) {
		this.port = port;
	}
}
