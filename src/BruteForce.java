import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class BruteForce {

	int minCost = Integer.MAX_VALUE;

	public static ArrayList<ArrayList<Integer>> permuteUnique(int[] num) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		permuteUnique(num, 0, result);
		return result;
	}
	 
	private static void permuteUnique(int[] num, int start, ArrayList<ArrayList<Integer>> result) {
	 
		if (start >= num.length ) {
			ArrayList<Integer> item = convertArrayToList(num);
			result.add(item);
		}
	 
		for (int j = start; j <= num.length-1; j++) {
			if (containsDuplicate(num, start, j)) {
				swap(num, start, j);
				permuteUnique(num, start + 1, result);
				swap(num, start, j);
			}
		}
	}
	 
	private static ArrayList<Integer> convertArrayToList(int[] num) {
		ArrayList<Integer> item = new ArrayList<Integer>();
		for (int h = 0; h < num.length; h++) {
			item.add(num[h]);
		}
		return item;
	}
	 
	private static boolean containsDuplicate(int[] arr, int start, int end) {
		for (int i = start; i <= end-1; i++) {
			if (arr[i] == arr[end]) {
				return false;
			}
		}
		return true;
	}
	    int[] toIntArray(ArrayList<Integer> list){
    	  int[] ret = new int[list.size()];
    	  for(int i = 0;i < ret.length;i++)
    	    ret[i] = list.get(i);
    	  return ret;
    	} 
	private static void swap(int[] a, int i, int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	
	void printPartitions(int n, int max, ArrayList<Integer> list, ArrayList<ArrayList<Integer>> comp) {
		  if (n == 0) {
			  //TODO this is where we permute the list and insert the perms
			for (ArrayList<Integer> temp : permuteUnique(toIntArray(list)))
				//System.out.println(temp);
				comp.add(temp);
	            return;
	        }

	        for (int i = Math.min(max, n); i >= 1; i--) {
	        	//string is immutable remember
			ArrayList<Integer> temp = new ArrayList<Integer>(list);
	        	temp.add( new Integer(i) );
	            printPartitions(n-i, i, temp, comp);
	        }

	}
	
	public int walkGraph(int[][] graph, ArrayList<Integer> list) {
		int i = 0, j = 0, cost = 0;
		
		for (int temp : list) {
			cost += graph[i][j + temp];
			//System.out.println(graph[i][j + temp]); TODO delete
			i += temp;
			j += temp;
		}
		return cost;
	}
	
	public int brutePath(int[][] graph, ArrayList<ArrayList<Integer>> paths, 
						ArrayList<Integer> shortest) {
		
		for (ArrayList<Integer> temp : paths) {
			int cost = walkGraph(graph, temp);
			if (cost < minCost) {
				minCost = cost;
				shortest.clear();
				shortest.addAll(temp);
			}
		}
		
		return minCost;
	}
	
	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		//HashSet<ArrayList<Character>> set = new HashSet<ArrayList<Character>>();
		ArrayList<ArrayList<Integer>> comp = new ArrayList<ArrayList<Integer>>();
		
		BruteForce test = new BruteForce();
		test.printPartitions(9,9,list, comp);
		
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
		
		ArrayList<Integer> shortest = new ArrayList<Integer>();
		int cost = test.brutePath(testGraph, comp, shortest);
		System.out.println(shortest + " cost: " + cost);
		
		//for (ArrayList<Integer> temp : comp)
			//System.out.println(temp);

				
	}

}
