package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import graph.objects.Clique;
import util.CommonHelper;
import util.IntervalRecHelper;
import util.Pair;

public class Graph {
	
	private final CommonHelper CH;
	private final IntervalRecHelper IRH;
	private ArrayList<Edge> myEdges;
	private HashMap<String, Vertex> myVertices;

	public Graph() {
		CH = new CommonHelper();
		IRH = new IntervalRecHelper();
		myEdges = new ArrayList<Edge>();
		myVertices = new HashMap<String, Vertex>();
	}
	
	public Graph(String... args) {
		CH = new CommonHelper();
		IRH = new IntervalRecHelper();
		myEdges = new ArrayList<Edge>();
		myVertices = new HashMap<String, Vertex>();
		for(String str: args) {
			myVertices.put(str, new Vertex(str));
		}
	}
	
	public Graph(ArrayList<Vertex> vertices) {
		CH = new CommonHelper();
		IRH = new IntervalRecHelper();
		myEdges = new ArrayList<Edge>();
		myVertices = new HashMap<String, Vertex>();
		for(Vertex i: vertices) {
			myVertices.put(i.getLabel(), new Vertex(i));
		}
	}
	
	public Graph(Graph other) {
		CH = new CommonHelper();
		IRH = new IntervalRecHelper();
		myEdges = new ArrayList<Edge>();
		myVertices = new HashMap<String, Vertex>();
		for(Edge e: other.myEdges) {
			myEdges.add(new Edge(e));
		}
		for(String str: other.myVertices.keySet()) {
			myVertices.put(str, new Vertex(other.myVertices.get(str)));
		}
	}
	
	public void addNewEdge(Edge myEdge) {
		for(Edge i: myEdges) {
			if((myEdge.getA().hasSameName(i.getA()) && myEdge.getB().hasSameName(i.getB()))
					|| (myEdge.getA().hasSameName(i.getB())
							&& myEdge.getB().hasSameName(i.getA()))) {
				return;
			}
		}
		myEdges.add(myEdge);
	}
	
	public void addNewVertex(Vertex v) {
		if(!myVertices.containsKey(v.getLabel())) {
			myVertices.put(v.getLabel(), v);
		}
		setNeighbors();
	}

	/* For testing purposes, i have create a method named createPerms
	 * With this method i can check if intervalGraphRecognition has failed.
	 */
	public Pair<ArrayList<Clique>, Boolean> intervalGraphRecognition() {
		ArrayList<Clique> ogMC = IRH.getMaximalCliques(this);
		Pair<ArrayList<Clique>, Boolean> og =
				intervalGraphRecognition(ogMC);
		/*
		if(og.getSecond()) {
			return og;
		} else {
			//System.out.println("Using perms...");
			ArrayList<ArrayList<Clique>> mCPerms =
					CH.createPerms(IRH.getMaximalCliques(this));
			for(ArrayList<Clique> mC: mCPerms) {
				Pair<ArrayList<Clique>, Boolean> current =
						intervalGraphRecognition(mC);
				if(current.getSecond()) {
					return current;
				}
			}
		}
		*/
		return og;
	}
	
	/* Algorithm 9 Interval Graph Recognition
	 * NOTES: HashSet<Vertex> is a Clique, ArrayList<HashSet<Vertex>> is an ArrayList with cliques
	 * In this case all maximal cliques. ArrayList<ArrayList<HashSet<Vertex>> is the ordered list.
	 * Init xaOffset/xbOffset to "safe" values
	 * I checked both (CI,CJ) and (CJ,CI) because they are not "linked" (false negatives)
	 * The last part of the algorithm (for loop through vertices -> checking consecutive order)
	 * is in IntervalRecHelper
	 * Observation: If its interval, then L is clique path (clique chain)
	 */
	public Pair<ArrayList<Clique>, Boolean> intervalGraphRecognition(
			ArrayList<Clique> maxC) {
		ArrayList<ArrayList<Clique>> L = new ArrayList<ArrayList<Clique>>();
		ArrayList<Clique> X = IRH.getMaximalCliques(this);
		ArrayList<Clique> C = new ArrayList<Clique>();
		ArrayList<Vertex> pivots = new ArrayList<Vertex>();
		Clique CL = new Clique();
		HashSet<Pair<Clique, Clique>> cliqueEdges = 
				IRH.createPairSet(IRH.getCliqueEdges(X).getFirst());
		HashSet<String> proccessedV = new HashSet<String>();
		HashSet<Clique> CC = new HashSet<Clique>();
		Iterator<Pair<Clique, Clique>> it = cliqueEdges.iterator();
		int numOfMaxCliques = X.size();
		L.add(maxC);
		while(!(L.size() == numOfMaxCliques)) {
			C = new ArrayList<Clique>();
			CC = new HashSet<Clique>();
			if(pivots.isEmpty()) {
				int offset = 0;
				C.clear();
				for(int i = 0; i < L.size(); i++) {
					if(L.get(i).size() > 1) {
						offset = i;
						break;
					}
				}
				CL = L.get(offset).remove(0);
				C.add(CL);
				L.add(offset + 1, C);
				CC = new HashSet<Clique>();
				CC.add(CL);
				L.removeIf(p -> p.isEmpty());
			} else {
				Vertex pivot = pivots.remove(pivots.size() - 1);
				CC = new HashSet<Clique>();
				while(proccessedV.contains(pivot.getLabel())) {
					if(pivots.isEmpty()) {
						break;
					}
					pivot = pivots.remove(pivots.size() - 1);
				}
				proccessedV.add(pivot.getLabel());
				for(Clique c: X) {
					if(c.getClique().contains(pivot)) {
						CC.add(c);
					}
				}
				Pair<Integer, Integer> xOffsets = IRH.getOffsets(L, CC);
				int xaOffset = xOffsets.getFirst();
				int xbOffset = xOffsets.getSecond();
				ArrayList<Clique> XA = new ArrayList<Clique>(
						L.get(xaOffset));
				ArrayList<Clique> XB = new ArrayList<Clique>(
						L.get(xbOffset));
				XA.retainAll(CC);
				XB.retainAll(CC);
				L.get(xaOffset).removeAll(CC);
				L.get(xbOffset).removeAll(CC);
				L.add(xbOffset, XB);
				L.add(xaOffset + 1, XA);
				L.removeIf(p -> p.isEmpty());
			}
			while(it.hasNext()) {
				Pair<Clique, Clique> p = it.next();
				Clique CI = p.getFirst();
				Clique CJ = p.getSecond();
				int trigger = 0;
				if((CC.contains(CI) && 
						!CC.contains(CJ))) {
					trigger = 1;
				} else if((!CC.contains(CI) && 
						CC.contains(CJ))) {
					trigger = 2;
				}
				if(trigger == 1) {
					pivots = IRH.calcDistributiveSet(CI, CJ, pivots);
					it.remove();
				} else if(trigger == 2) {
					pivots = IRH.calcDistributiveSet(CJ, CI, pivots);
					it.remove();
				}
			}
			L.removeIf(p -> p.isEmpty());
		}
		return new Pair<ArrayList<Clique>, Boolean>(CH.createCPGClique(L),
				IRH.areInConsecutiveOrder(L, new HashSet<Vertex>(myVertices.values())));
	}
	
	//Algorithm 2. Lex-BFS Ordering
	public ArrayList<Vertex> lexBFSOrdering() {
		ArrayList<Vertex> V = new ArrayList<Vertex>(myVertices.values());
		ArrayList<Vertex> P = new ArrayList<Vertex>();
		ArrayList<ArrayList<Vertex>> L = 
				new ArrayList<ArrayList<Vertex>>(Arrays.asList(V));
		while(!L.isEmpty()) {
			int offset = 0;
			ArrayList<Vertex> XA = L.get(0);
			Vertex x = XA.remove(0);
			P.add(0, x);
			while(offset < L.size()) {
				ArrayList<Vertex> XB = L.get(offset);
				ArrayList<Vertex> Y = new ArrayList<Vertex>();
				for(Vertex j: XB) {
					if(x.getNeighbors().contains(j.getLabel())) {
						Y.add(j);
					}
				}
				XB.removeAll(Y);
				if(!Y.isEmpty()) {
					L.add(L.indexOf(XB), Y);
					offset++;
				}
				if(XB.isEmpty()) {
					L.remove(offset);
					offset--;
				}
				offset++;
			}

		}
		return P;
	}
	
	public Vertex removeVertex(Vertex v) {
		if(myVertices.containsKey(v.getLabel())) {
			for(Vertex i: myVertices.values()) {
				if(i.getNeighbors().contains(v.getLabel())) {
					i.removeNeighbor(v.getLabel());
				}
			}
			return myVertices.remove(v.getLabel());
		}
		return null;
	}
	
	public ArrayList<Edge> getEdges() {
		return myEdges;
	}
	
	public Vertex getVertex(String name) {
		return myVertices.get(name);
	}
	
	public HashMap<String, Vertex> getVertices() {
		return myVertices;
	}

	public void setEdges() {
		myEdges.clear();
		for(Vertex i: myVertices.values()) {
			for(String j: i.getNeighbors()) {
				Edge myEdge = new Edge(i, myVertices.get(j));
				addNewEdge(myEdge);
			}
		}
	}
	
	public void setNeighbors() {
		HashSet<String> labelsOfV = new HashSet<String>(myVertices.keySet());
		for(String str: labelsOfV) {
			for(String n: myVertices.get(str).getNeighbors()) {
				if(labelsOfV.contains(n)) {
					myVertices.get(n).pushNeighbor(str);
				}
			}
		}
	}
	
	public String toString() {
		String returnString = "";
		for(Vertex i : myVertices.values()) {
			returnString = returnString + i.getLabel() + " " 
					+ i.getNeighbors().toString() + "\n";
		}
		return returnString;
	}
	
}