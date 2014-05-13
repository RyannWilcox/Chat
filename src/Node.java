/**
 * 
 * @author ryanwilcox
 * Simple Node class for linked list
 */
public class Node {
	private String message ="";
	private Node next;
	
	public Node(){
		message = null;
		next = null;
	}
	
	public Node getNext(){
		return next;
	}
	
	public void setNext(Node nextNode){
		next = nextNode;
	}
	
	public String getMessage(){
		return message;
	}
	
	public void setMessage(String newMessage){
		message = newMessage;
	}
}
