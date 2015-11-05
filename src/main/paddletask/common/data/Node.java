package main.paddletask.common.data;

public class Node {
	String data;
	Node next;
	Node previous;
	
	public Node(String data, Node next, Node previous){
		this.data = data;
		this.next = next;
		this.previous = previous;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public Node getPrevious() {
		return previous;
	}

	public void setPrevious(Node previous) {
		this.previous = previous;
	}

	public Node(String data) {
		this.data = data;
	}

	public String toString() {
		return "{" + data + "} ";
	}
}