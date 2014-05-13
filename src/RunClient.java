import java.io.IOException;

import javax.swing.JFrame;


public class RunClient {

	public static void main(String[] args) throws IOException {
		//ADD YOUR OWN IP FOR THE SERVER
		String serverIP ="***.***.*.***";
		 Client chatClient = new Client(serverIP); //must start server before client
		 chatClient.startClient();
	}
}
