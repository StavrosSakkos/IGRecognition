package graph.objects;

//import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import graph.Vertex;

public class Clique {
	
	private HashSet<Vertex> clique;
	
	public Clique() {
		clique = new HashSet<Vertex>();
	}
	
	public Clique(HashSet<Vertex> clique) {
		this.clique = clique;
	}
	
	public Clique(Clique other) {
		clique = new HashSet<Vertex>();
		for(Vertex v: other.clique) {
			clique.add(new Vertex(v));
		}
	}
	public void addVertex(Vertex v) {
		clique.add(v);
	}
	
	public void addCollection(HashSet<Vertex> h) {
		clique.addAll(h);
	}
	
	public boolean containsLabels(HashSet<String> labels) {
		HashSet<String> X = getCliqueAsStrings();
		return X.containsAll(labels);
	}
	
	public boolean containsLabels(String a, String b) {
		HashSet<String> X = getCliqueAsStrings();
		return X.contains(a) && X.contains(b);
	}

	@Override
	public int hashCode() {
		return Objects.hash(clique);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Clique other = (Clique) obj;
		return Objects.equals(clique, other.clique);
	}

	public HashSet<Vertex> getClique() {
		return clique;
	}
	
	public HashSet<String> getCliqueAsStrings() {
		HashSet<String> X = new HashSet<String>();
		for(Vertex v: clique) {
			X.add(v.getLabel());
		}
		return X;
	}
	
	public int getSize() {
		return clique.size();
	}
	
	public String toString() {
		return clique.toString();
	}
}