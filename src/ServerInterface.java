import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class ServerInterface extends JFrame implements ActionListener{
	private JTextArea inputArea = new JTextArea("Enter message here");	
	private JTextArea outputArea = new JTextArea("Incoming messages go here");
	private JPanel inputOutput = new JPanel(new GridLayout(2,1));
	private JPanel buttons = new JPanel(new GridLayout(1,3));
	private JScrollPane inputScroll = new JScrollPane(inputArea);
	private JScrollPane outputScroll = new JScrollPane(outputArea);
	private JButton quit = new JButton("Quit");
	private JButton send = new JButton("Send");
	private JButton connect = new JButton("Connect") ;  
	
	public ServerInterface(String title,int width,int height){
		super("Chat");
		setUpFrame(title,width,height);
		setVisible(true);
	}
	
	/*
	* This will set up the chat frame
	* @param t Title of the frame
	* @param w width of the frame
	* @param h height of the frame
	*/
	public void setUpFrame(String t,int w,int h){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(w,h);
		setLayout(new BorderLayout());
		
		/*
		* Fields to input text and to recieve text from
		* the other user are set up
		*/
		outputArea.setEditable(false); // so you cant alter the other message
		outputArea.setBorder(BorderFactory.createLineBorder(Color.GRAY,5));
		outputArea.setLineWrap(true);
		outputScroll = new JScrollPane(outputArea);
		
		inputArea.setBorder(BorderFactory.createLineBorder(Color.BLACK,5));
		inputArea.setLineWrap(true);
		inputScroll = new JScrollPane(inputArea);
		
		
		inputOutput.add(outputScroll);
		inputOutput.add(inputScroll);
		
		/* Buttons to control chat*/
		send.addActionListener(this);
		quit.addActionListener(this);
		connect.addActionListener(this);
		buttons.add(send);
		buttons.add(quit);
		buttons.add(connect);
		
		add(buttons,BorderLayout.SOUTH);
		add(inputOutput);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		Object obj = e.getSource();
		
		if(obj == quit){
			System.exit(0);
		}
	}
	
	
	
	public static void main(String[] args){
		ServerInterface si = new ServerInterface("Test",600,600);
	}
}