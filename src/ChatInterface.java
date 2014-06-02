import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class ChatInterface extends JFrame{
	private JTextArea inputArea = new JTextArea("Enter message here");	
	private JTextArea outputArea = new JTextArea("Incoming messages go here");
	private JPanel inputOutput = new JPanel(new GridLayout(2,1));
	private JPanel buttons = new JPanel(new GridLayout(1,3));
	private JScrollPane inputScroll = new JScrollPane(inputArea);
	private JScrollPane outputScroll = new JScrollPane(outputArea);
	
	public ChatInterface(String title,int width,int height){
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
		buttons.add(new JButton("Send"));
		buttons.add(new JButton("Quit"));
		buttons.add(new JButton("Connect"));
		
		add(buttons,BorderLayout.SOUTH);
		add(inputOutput);
		
	}
	
	
	
	
	public static void main(String[] args){
		ChatInterface ci = new ChatInterface("Test",600,600);
	}
}