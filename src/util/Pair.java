package util;

public class Pair<U, V> {
	
	private U first;
	private V second;
	
	public Pair(U first, V second) {
		this.first = first;
		this.second = second;
	}
	
	public U getFirst() {
		return first;
	}
	
	public V getSecond() {
		return second;
	}

	public void setFirst(U first) {
		this.first = first;
	}
	
	public void setSecond(V second) {
		this.second = second;
	}
	
	public boolean compareWith(Pair<?, ?> other) {
		if((this.first.equals(other.first) && this.second.equals(other.second)) || 
				(this.first.equals(other.second) && this.second.equals(other.first))){
			return true;
		}
		return false;
	}
	
	
	public String toString() {
		return "C1: " + first.toString() + " || C2 :" + second.toString();
	}
}