package common.data;

public class DoublyLinkedList {

	private Node first;
	private Node last;

	public DoublyLinkedList() {
		first = null;
		last = null;
	}

	public boolean isEmpty() {
		return first == null;
	}

	public Node getFirst(){
		return first;
	}

	public Node getLast(){
		return last;
	}

	public void insertFirst(String data) {
		Node newNode = new Node(data);
		if (isEmpty()){
			last = newNode;
		}else{
			first.previous = newNode;
		}
		newNode.next = first;
		first = newNode;
	}

	public void insertLast(String data) {
		Node newNode = new Node(data);
		if (isEmpty()){
			first = newNode;
		}else {
			last.next = newNode;
			newNode.previous = last;
		}
		last = newNode;
	}

	public Node deleteFirst() {
		Node temp = first;
		if (first.next == null){
			last = null;
		}else{
			first.next.previous = null;
		}
		first = first.next;
		return temp;
	}

	public Node deleteLast() {
		Node temp = last;
		if (first.next == null){
			first = null;
		}else{
			last.previous.next = null;
		}
		last = last.previous;
		return temp;
	}

	public Node deleteKey(String data) {
		Node current = first;
		while (current.data.equals(data)) {
			current = current.next;
			if (current == null)
				return null;
		}
		if (current == first){
			first = current.next;
		}else{
			current.previous.next = current.next;
		}

		if (current == last){
			last = current.previous;
		}else{
			current.next.previous = current.previous;
		}
		return current;
	}

	public String toString() {
		String str = "List (first-->last): ";
		Node current = first;
		while (current != null) {
			str += current.toString();
			current = current.next;
		}
		System.out.println("");
		System.out.print("List (last-->first): ");

		current = last;
		while (current != null) {
			str += current.toString();
			current = current.previous;
		}
		return str;
	}
}

