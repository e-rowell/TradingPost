import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class DivideAndConquer {

	private static int minCost = Integer.MAX_VALUE;

	LinkedList<Integer> shortest;

	private int getCost(int[][] graph, LinkedList<Integer> visited) {
		//TODO THERE IS AN EDGE CASE 2,2,2
		int totalCost = 0;
		int i = 0;
		for (int node : visited) {
			totalCost += graph[i][node];
			System.out.print(graph[i][node] + " ");
			i += node;
		}
		return totalCost;
	}

	public static void dft(int[][] graph, LinkedList<Integer> visited, int cost) {

		//our last node is always this
		int end = graph.length - 1;
		LinkedList<Integer> adjecentNodes = new LinkedList<Integer>();
		//fill our list of adjecent nodes
		for (int i = visited.getLast() + 1; i <= end; i++) {
			adjecentNodes.add(i);
		}

		//check adj nodes for the end
		for (int node : adjecentNodes) {
			if (!visited.contains(node)) {
				if (node == end) {
					int lastNode = visited.getLast();
					visited.add(node);
					//print the path
					cost += graph[lastNode][node];
					//min cost stuff
					if (cost < minCost) {
						minCost = cost;
					}

					System.out.println(visited + " cost: " + cost + " min: " + minCost);
					cost = cost - graph[lastNode][node];
					visited.removeLast();
					break;
				}
			}
		}

		//keep going
		for(int node : adjecentNodes) {
			if (!visited.contains(node) && node != end) {
				int lastNode = visited.getLast();
				visited.addLast(node);
				//dont go down a path with a greater total weith than our min
				//System.out.println("ay mincost is: " + minCost );
				if (cost + graph[lastNode][node] < minCost)
					dft(graph, visited, cost + graph[lastNode][node]);
				visited.removeLast();
			}
		}


		//System.out.println(adjecentNodes);
	}

	public static void testTime() {
		TradingPost tp = new TradingPost();
		String inputStr = tp.readInput("sample_input_size17.txt");
		int[][] rowData = tp.parseInput(inputStr);

		tp.generateTestData();
		LinkedList<Integer> visited = new LinkedList();
		visited.add(0);
		long start = System.currentTimeMillis();
		dft(rowData, visited, 0);
		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}

	public static void main(String[] args) {
		DivideAndConquer dac = new DivideAndConquer();
		testTime();
		//lets make out test array and list
		int[][] testGraph = new int[][]{
				{0,	1,	2,	5,	7,	9,	12,	15,	17,	19},
				{Integer.MAX_VALUE, 0,	2,	3,	4,	7,	10,	12,	14,	17},
				{Integer.MAX_VALUE,Integer.MAX_VALUE,	0,	3,	4,	7,	10,	12,	14,	16},
				{Integer.MAX_VALUE,Integer.MAX_VALUE,	Integer.MAX_VALUE,	0,	2,	3,	4,	6,	8,	10},
				{Integer.MAX_VALUE,Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	0,	2,	3,	6,	8,	9},
				{Integer.MAX_VALUE,Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	0,	1,	4,	7,	9},
				{Integer.MAX_VALUE,Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	0,	2,	4,	7},
				{Integer.MAX_VALUE,Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	0,	1,	2},
				{Integer.MAX_VALUE,Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	0,	1},
				{Integer.MAX_VALUE,Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	Integer.MAX_VALUE,	0}
		};
		//LinkedList<Integer> visited = new LinkedList();
		//    visited.add(0);

		//dac.dft(testGraph, visited, 0);

	}

}
