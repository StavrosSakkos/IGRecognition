package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import graph.Graph;
import graph.Vertex;
import graph.objects.Clique;

public class IntervalRecHelper {

	private CommonHelper CH = new CommonHelper();
	
	/*
	 * Used in Algorithm 9
	 * NOTES: When the while-loop ends,
	 * L will be in this form: [[clique1], [clique2],..., [cliqueN]]
	 * I will use LL in order to bring this in this simpler form: [clique, clique,.., clique]
	 * Trigger -> When consecutiveness ends. || consecutive -> if its consecutive ~ current state
	 */
	public boolean areInConsecutiveOrder(ArrayList<ArrayList<Clique>> L,
			HashSet<Vertex> vertices) {
		ArrayList<Clique> LL = new ArrayList<Clique>();
		for(ArrayList<Clique> i: L) {
			LL.add(i.get(0));
		}
		for(Vertex v: vertices) {
			boolean trigger = false;
			boolean consecutive = false;
			for(Clique c: LL) {
				if(c.getClique().contains(v)) {
					if(trigger) {
						return false;
					}
					consecutive = true;
				} else {
					if(consecutive) {
						trigger = true;
					}
				}
			}
		}
		return true;
	}
	
	/*Used in Algorithm 9
	 * NOTE: Calculates pivots U (CICJ) -> (pivots U CI)(pivots U CJ) 
	 */
	public ArrayList<Vertex> calcDistributiveSet(Clique CI, Clique CJ,
			ArrayList<Vertex> pivots) {
		HashSet<Vertex> UA = new HashSet<Vertex>(CI.getClique());
		HashSet<Vertex> UB = new HashSet<Vertex>(CJ.getClique());
		UA.retainAll(UB);
		UA.addAll(pivots);
		return new ArrayList<Vertex>(UA);
	}

	/* 
	 * Algorithm 3. Chordality Test
	 * NOTE: Didnt create tree T and loop goes from 1 to n-1 (no RN,no parent)
	 */
	public boolean chordalityTest(ArrayList<Vertex> lexBFSOrdering) {
		HashMap<Vertex, ArrayList<Vertex>> RN = 
				createRN(lexBFSOrdering);
		HashMap<Vertex, Vertex> parents = 
				createParents(lexBFSOrdering, RN);
		for(int i = 0; i < lexBFSOrdering.size() - 1; i++) {
			Vertex v = lexBFSOrdering.get(i);
			HashSet<Vertex> rnWithoutParent = new HashSet<Vertex>(RN.get(v));
			rnWithoutParent.remove(parents.get(v));
			if(!RN.get(parents.get(v)).containsAll(rnWithoutParent)) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Used in Algorithm 9
	 * NOTE: The fastest way i could think of is retainAll with HashSets.
	 * If y doesnt contain anything from x, y.size() is 0 
	 */
	public boolean classContainsX(ArrayList<Clique> C, HashSet<Clique> X) {
		HashSet<Clique> y = new HashSet<Clique>(C);
		y.retainAll(X);
		return y.size() != 0;
	}
	
	/*
	 * Not used as Algorithm 4: Clique Tree
	 * vWithSameParents : vertex parent -> vertices with this parent
	 */
	public Graph cliqueTree(ArrayList<Vertex> lexBFSOrdering) {
		Graph T = new Graph();
		HashMap<String, Vertex> TS = new HashMap<String, Vertex>();
		HashMap<Vertex, ArrayList<Vertex>> RN = createRN(lexBFSOrdering);
		HashMap<Vertex, Vertex> parents = createParents(lexBFSOrdering, RN);
		Vertex root = new Vertex(lexBFSOrdering.remove(lexBFSOrdering.size() - 1).getLabel());
		TS.put(root.getLabel(), root);
		for(int i = 0; i < lexBFSOrdering.size(); i++) {
			Vertex v = lexBFSOrdering.get(i);
			Vertex newI = new Vertex(v.getLabel());
			if(RN.containsKey(v)) {
				newI.pushNeighbor(parents.get(v).getLabel());
				TS.put(newI.getLabel(), newI);
			}
		}
		for(String str: TS.keySet()) {
			for(String n: TS.get(str).getNeighbors()) {
				TS.get(n).pushNeighbor(str);
			}
		}
		
		for(Vertex v: TS.values()) {
			T.addNewVertex(v);
		}
		T.setEdges();
		return T;
	}
	
	//Used in Algorithm 3 and 4 : Get Parents 
	public HashMap<Vertex, Vertex> createParents(ArrayList<Vertex> lexBFSOrdering, 
			HashMap<Vertex, ArrayList<Vertex>> RN){
		HashMap<Vertex, Vertex> parents = new HashMap<Vertex, Vertex>();
		for(Vertex i: lexBFSOrdering) {
			if(!RN.get(i).isEmpty()) {
				parents.put(i, RN.get(i).get(0));
			}
		}
		return parents;
	}
	
	
	//Used in Algorithm 3 and 4 : Get RN
	public HashMap<Vertex, ArrayList<Vertex>> createRN(ArrayList<Vertex> lexBFSOrdering) {
		HashMap<Vertex, ArrayList<Vertex>> RN = new HashMap<Vertex, ArrayList<Vertex>>();
		for(int i = 0; i < lexBFSOrdering.size(); i++) {
			Vertex v = lexBFSOrdering.get(i);
			ArrayList<Vertex> rnOfVertex = new ArrayList<Vertex>();
			ArrayList<Vertex> verticesOnRight = 
					new ArrayList<Vertex>(lexBFSOrdering.subList(i + 1, lexBFSOrdering.size()));
			for(int j = 0; j < verticesOnRight.size(); j++) {
				if(v.getNeighbors().contains(verticesOnRight.get(j).getLabel())) {
					rnOfVertex.add(verticesOnRight.get(j));
				}
			}
			RN.put(v, rnOfVertex);
		}
		return RN;
	}
	
	public HashSet<Pair<Clique, Clique>> createPairSet(HashMap<Clique, Clique> C) {
		HashSet<Pair<Clique, Clique>> X = new HashSet<Pair<Clique, Clique>>();
		for(Clique i: C.keySet()) {
			X.add(new Pair<Clique, Clique>(i, C.get(i)));
		}
		return X;
	}
	
	/*
	 * Its (similar with) Algorithm 4: Clique Tree
	 * It returns cliques in a HashMap instead of a tree
	 * NOTE: If something goes wrong, check the code in "else" block.
	 */
	public Pair<HashMap<Vertex, Clique>, ArrayList<Clique>> createTree(Graph G) {
		ArrayList<Vertex> lexBFSOrdering = G.lexBFSOrdering();
		ArrayList<Clique> mC = new ArrayList<Clique>();
		Graph T = cliqueTree(G.lexBFSOrdering());
		HashMap<Vertex, Clique> cliques = 
				new HashMap<Vertex, Clique>();
		HashMap<Vertex, ArrayList<Vertex>> RN = createRN(lexBFSOrdering);
		HashMap<Vertex, Vertex> parents = createParents(lexBFSOrdering, RN);
		ArrayList<String> preOrder = getPreOrderOfTree(T,
				lexBFSOrdering.get(lexBFSOrdering.size() - 1).getLabel());
		T.getVertices().remove(lexBFSOrdering.get(lexBFSOrdering.size() - 1).getLabel());
		lexBFSOrdering.remove(lexBFSOrdering.size() - 1);
		preOrder.remove(0);
		for(String str: preOrder) {
			Vertex v = T.getVertices().get(str);
			Vertex chosenV = null;
			for(Vertex i: RN.keySet()) {
				if(i.getLabel().equals(v.getLabel())) {
					chosenV = i;
					break;
				}
			}
			HashSet<Vertex> rnWithoutParent = new HashSet<Vertex>(RN.get(chosenV));
			HashSet<Vertex> rnOfParent = new HashSet<Vertex>(RN.get(parents.get(chosenV)));
			rnWithoutParent.remove(parents.get(chosenV));
			if(!rnWithoutParent.equals(rnOfParent)) {
				Clique C = new Clique();
				Clique cliqueParentX = new Clique(new HashSet<Vertex>(RN.get(chosenV)));
				cliqueParentX.addVertex(chosenV);
				C.addCollection(cliqueParentX.getClique());
				C.addVertex(chosenV);
				cliques.put(chosenV, C);
				mC.add(C);
			} else {
				Clique cliqueParentX = new Clique(new HashSet<Vertex>(RN.get(chosenV)));
				for(Vertex vv: cliques.keySet()) {
					if(cliques.get(vv).containsLabels(cliqueParentX.getCliqueAsStrings())
							&& cliqueParentX.getSize() == cliques.get(vv).getSize()) {
						cliques.remove(vv);
						break;
					}
				}
				cliqueParentX.addVertex(chosenV);
				cliques.put(chosenV, cliqueParentX);
				mC.add(cliqueParentX);
			}
		}
		return new Pair<HashMap<Vertex, Clique>, 
				ArrayList<Clique>>(cliques, mC);
	}
	
	/*
	 * Used in Algorithm 9
	 * NOTE: I have to reverse the lexBFSOrdering in order to get the correct
	 * order for the cliques which is last found first
	 */
	public ArrayList<Clique> getMaximalCliques(Graph G) {
		HashMap<Vertex, Clique> T = createTree(G).getFirst();
		ArrayList<Vertex> orderForCliques = G.lexBFSOrdering();
		ArrayList<Clique> X = new ArrayList<Clique>();
		Collections.reverse(orderForCliques);
		for(Vertex i: orderForCliques) {
			boolean trigger = true;
			for(Vertex j: orderForCliques) {
				if(T.containsKey(i) && T.containsKey(j)) {
					boolean A = T.get(j).getClique().containsAll(T.get(i).getClique());
					boolean B = !T.get(i).getClique().containsAll(T.get(j).getClique());
					if(A && B) {
						trigger = false;
					}
				}
			}
			if(trigger) {
				if(T.containsKey(i)) {
					X.add(T.get(i));
				}
			}
		}
		return X;
	}
	
	/*
	 * Used in Algorithm 9
	 * NOTE: Creates edges.
	 */
	public Pair<HashMap<Clique, Clique>, ArrayList<HashSet<Vertex>>> getCliqueEdges(
			ArrayList<Clique> C) {
		ArrayList<HashSet<Vertex>> treeEdges = new ArrayList<HashSet<Vertex>>();
		HashMap<Clique, Clique> X = new HashMap<Clique, Clique>();
		Pair<HashMap<Clique, Clique>, ArrayList<HashSet<Vertex>>> p;
		for(int i = 0; i < C.size(); i++) {
			boolean trigger = true;
			HashMap<Clique, Clique> current =
					new HashMap<Clique, Clique>();
			for(int j = 0; j < C.size(); j++) {
				if(i != j) {
					HashSet<Vertex> k = new HashSet<Vertex>(C.get(i).getClique());
					k.retainAll(C.get(j).getClique());
					if(k.size() > 0) {
						current.put(C.get(i), C.get(j));
					}
				}
			}
			for(Clique c: X.keySet()) {
				if(current.containsKey(c)) {
					if(current.get(c).equals(X.get(c))) {
						current.remove(c);
					}
				}
				if(current.containsKey(X.get(c))) {
					if(current.get(X.get(c)).equals(c)) {
						current.remove(X.get(c));
					}
				}
			}
			if(current.isEmpty()) {
				trigger = false;
			}
			if(trigger) {
				for(Clique c: current.keySet()) {
					X.put(c, current.get(c));
					treeEdges.add(C.get(i).getClique());
				}
			}
		}
		p = new Pair<HashMap<Clique, Clique>, ArrayList<HashSet<Vertex>>>(X, treeEdges);
		return p;
	}

	/*
	 * Used in Algorithm 9 in order to get XA class and XB class location in the list
	 * so called "offsets" in this case
	 */
	public Pair<Integer, Integer> getOffsets(ArrayList<ArrayList<Clique>> L,
			HashSet<Clique> CC){
		boolean checkerXA = false;
		boolean checkerXB = false;
		int xaOffset = 0;
		int xbOffset = L.size() - 1;
		for(int i = 0; i < L.size(); i++) {
			if(!checkerXA) {
				if(classContainsX(L.get(i),
						new HashSet<Clique>(CC))) {
					xaOffset = i;
					checkerXA = true;
				}
			}
			if(!checkerXB) {
				if(classContainsX(L.get(L.size() - i - 1),
						new HashSet<Clique>(CC))) {
					xbOffset = L.size() - i - 1;
					checkerXB = true;
				}
			}
			if(!checkerXA && !checkerXB) {
				break;
			}
		}
		return new Pair<Integer, Integer>(xaOffset, xbOffset);
	}
	
	public ArrayList<String> getPreOrderOfTree(Graph T, String labelOfRoot) {
		ArrayList<String> order = new ArrayList<String>();
		int offset = 0;
		getPreOrderOfTree(T, labelOfRoot, order, offset);
		return order;
	}
	
	public void getPreOrderOfTree(Graph T, String labelOfRoot,
			ArrayList<String> order, int offset) {
		ArrayList<String> neighborsOfV = 
				CH.getSortedArr(T.getVertex(labelOfRoot).getNeighbors());
		if(order.size() > 0) {
			neighborsOfV.remove(order.get(order.size() - 1));
		}
		order.add(labelOfRoot);
		for(String str: neighborsOfV) {
			if(!order.contains(str)) {
				getPreOrderOfTree(T, str, order, offset + 1);
			}		
		}
	}
	
}