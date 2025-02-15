package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import graph.Graph;

class IntervalRecTest {

	@Test
	void test() {
		boolean isFirstGraphInterval = false;
		boolean isSecondGraphInterval = false;
		boolean isThirdGraphInterval = false;
		
		String [] firstExample = {"A", "B", "C", "D", "E", "F", "G"};
		String [] secondExample = {"1", "2", "3", "4", "5", "6", "7", "8"};
		String [] thirdExample = {"X", "A", "B", "C", "Z", "D", "Y"};
		
		Graph firstGraph = new Graph(firstExample);
		Graph secondGraph = new Graph(secondExample);
		Graph thirdGraph = new Graph(thirdExample);
		
		//This is from wikipedia. Its an interval graph.
		firstGraph.getVertex("A").pushNeighbors("B", "C", "D");
		firstGraph.getVertex("B").pushNeighbors("A", "C");
		firstGraph.getVertex("C").pushNeighbors("A", "B", "D", "E", "F");
		firstGraph.getVertex("D").pushNeighbors("A", "C", "E", "F");
		firstGraph.getVertex("E").pushNeighbors("C", "D");
		firstGraph.getVertex("F").pushNeighbors("C", "D", "G");
		firstGraph.getVertex("G").pushNeighbors("F");
		firstGraph.setEdges();
		
		//This is from the paper. It is interval
	 	secondGraph.getVertex("1").pushNeighbors("6");
		secondGraph.getVertex("2").pushNeighbors("8");
		secondGraph.getVertex("3").pushNeighbors("6", "8");
		secondGraph.getVertex("4").pushNeighbors("7", "8");
		secondGraph.getVertex("5").pushNeighbors("7", "8");
		secondGraph.getVertex("6").pushNeighbors("1", "3", "7", "8");
		secondGraph.getVertex("7").pushNeighbors("4", "5", "6", "8");
		secondGraph.getVertex("8").pushNeighbors("2", "3", "4", "5", "6", "7");
		secondGraph.setEdges();

		//Asteroidal triplet
		thirdGraph.getVertex("X").pushNeighbors("A");
		thirdGraph.getVertex("A").pushNeighbors("X", "B");
		thirdGraph.getVertex("B").pushNeighbors("A", "C", "D");
		thirdGraph.getVertex("C").pushNeighbors("B", "Z");
		thirdGraph.getVertex("Z").pushNeighbors("C");
		thirdGraph.getVertex("D").pushNeighbors("B", "Y");
		thirdGraph.getVertex("Y").pushNeighbors("D");
		thirdGraph.setEdges();
		
		isFirstGraphInterval = firstGraph.intervalGraphRecognition().getSecond();
		isSecondGraphInterval = secondGraph.intervalGraphRecognition().getSecond();
		isThirdGraphInterval = thirdGraph.intervalGraphRecognition().getSecond();

		if(isFirstGraphInterval && isSecondGraphInterval && !isThirdGraphInterval) {
			System.out.println("Passed");
		} else {
			System.out.println("Failed");
		}
		assertEquals(isFirstGraphInterval, true);
		assertEquals(isSecondGraphInterval, true);
		assertEquals(isThirdGraphInterval, false);
	}
}
