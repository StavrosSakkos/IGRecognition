package demo;

import java.util.ArrayList;

import graph.Graph;
import graph.objects.Clique;
import util.Pair;

public class IntervalGraphDemo {

	private static void initGraphs(Graph firstGraph, Graph thirdGraph) {
		firstGraph.getVertex("A").pushNeighbors("B", "C", "D");
		firstGraph.getVertex("B").pushNeighbors("A", "C");
		firstGraph.getVertex("C").pushNeighbors("A", "B", "D", "E", "F");
		firstGraph.getVertex("D").pushNeighbors("A", "C", "E", "F");
		firstGraph.getVertex("E").pushNeighbors("C", "D");
		firstGraph.getVertex("F").pushNeighbors("C", "D", "G");
		firstGraph.getVertex("G").pushNeighbors("F");
		firstGraph.setEdges();
		
		thirdGraph.getVertex("X").pushNeighbors("A");
		thirdGraph.getVertex("A").pushNeighbors("X", "B");
		thirdGraph.getVertex("B").pushNeighbors("A", "C", "D");
		thirdGraph.getVertex("C").pushNeighbors("B", "Z");
		thirdGraph.getVertex("Z").pushNeighbors("C");
		thirdGraph.getVertex("D").pushNeighbors("B", "Y");
		thirdGraph.getVertex("Y").pushNeighbors("D");
		thirdGraph.setEdges();
	}
	
	public static void main(String[] args) {
		String[] firstExample = {"A", "B", "C", "D", "E", "F", "G"};
		String[] thirdExample = {"X", "A", "B", "C", "Z", "D", "Y"};

		Graph firstGraph = new Graph(firstExample);
		Graph thirdGraph = new Graph(thirdExample);

		initGraphs(firstGraph, thirdGraph);
		
		Pair<ArrayList<Clique>, Boolean> firstGraphResult = firstGraph.intervalGraphRecognition();
		Pair<ArrayList<Clique>, Boolean> thirdGraphResult = thirdGraph.intervalGraphRecognition();
		boolean isFirstGraphIG = firstGraphResult.getSecond();
		boolean isThirdGraphIG = thirdGraphResult.getSecond();
		
		System.out.println("First graph is an interval graph: " + isFirstGraphIG);
		System.out.println("Third graph is an interval graph: " + isThirdGraphIG);
	}

}
