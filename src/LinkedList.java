
public class LinkedList {
	private Node head;
	private Node cur;
	private int size;
	
	public LinkedList(){
		head = null;
		cur = null;
		size = 0;
	}
	
	/**
	 * Adds a new Node to the end of the linked list
	 * @param message
	 */
	public void addToEnd(String message){
		Node newNode = new Node();
		newNode.setMessage(message);
		newNode.setNext(null);
		
		if(isEmpty()){
			head = newNode;
		}
		if(size == 1){
			head.setNext(newNode);
		}
		else{
			cur = head;
			// go through list to the last node
			for(int i = 0;i< size - 1;i++){
				cur = cur.getNext();
			}
			// put the new node right after the last node
			cur.setNext(newNode);
		}
		size++;
	}
	
	/**
	 * prints the list one node at a time
	 */
	public void printList(){
		Node current = head;
		while(current != null){
			System.out.println("[ "+current.getMessage()+" ] ");
			current = current.getNext();
		}
	}
	/**
	 * This will go through the list until the correct index
	 * is found.  The data at that index is returned.
	 * @param index
	 * @return the contents of the node at the index
	 */
	public String findAtIndex(int index){
		cur = head;
		String message="";
		
		if(index > getSize() || index < 0){
			return "Users index is not within the scope of this list";
		}
		else{
			// cycle through the list
			for(int i = 0; i< index;i++){
				cur = cur.getNext();
			}
			message = cur.getMessage();
		}
		return message;
	}
	
	public int getSize(){
		return size;
	}
	
	public boolean isEmpty(){
		return size == 0;
	}
}
