package graph;

public class Edge {
	private Vertex a;
	private Vertex b;
	private double weight;
	
	public Edge(Vertex a, Vertex b) {
		this.a = a;
		this.b = b;
		weight = 0;
	}
	
	public Edge(Vertex a, Vertex b,double weight) {
		this.a = a;
		this.b = b;
		this.weight = weight;
	}
	
	public Edge(Edge other) {
		a = new Vertex(other.a);
		b = new Vertex(other.b);
		this.weight = other.weight;
	}
	public double getWeight() {
		return weight;
	}
	
	public Vertex getA() {
		return a;
	}
	
	public Vertex getB() {
		return b;
	}
	
	public String toString() {
		return a.getLabel() + "<->" + b.getLabel();
	}
}