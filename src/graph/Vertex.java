package graph;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Vertex {
	
	//private ArrayList<String> neighborsInOrder;
	private HashSet<String> neighbors;
	private String label;
		
	public Vertex() {
		neighbors = new HashSet<String>();
		label = "Dummy";
	}
	
	public Vertex(String label) {
		this.label = label;
		neighbors = new HashSet<String>();
	}
	
	public Vertex(Vertex other) {
		this.label = other.label;
		neighbors = new HashSet<String>();
		for(String str: other.neighbors) {
			neighbors.add(str);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(label, neighbors);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		return this.label.equals(other.label) &&
				this.neighbors.equals(other.neighbors);
	}
	
	public boolean hasSameName(Vertex other) {
		return other.label.contentEquals(this.label);
	}
	
	public void pushNeighbor(String arg) {
		if(!neighbors.contains(arg)) {
			neighbors.add(arg);
		}
	}
	
	public void pushNeighbors(ArrayList<Vertex> args) {
		for(Vertex v : args) {
			if(!neighbors.contains(v.getLabel())) {
				neighbors.add(v.getLabel());
			}
		}
	}
	
	public void pushNeighbors(String... args) {
		for(String i : args) {
			if(!neighbors.contains(i)) {
				neighbors.add(i);
			}
		}
	}
	
	public void removeNeighbor(String neighbor) {
		neighbors.remove(neighbor);
	}
	
	public HashSet<String> getNeighbors() {
		return neighbors;
	}
	
	public void setNeighbors(HashSet<String> neighbors) {
		this.neighbors = new HashSet<String>(neighbors);
	}
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String toString() {
		return label;
	}
}