import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//TODO
//change method names
//clean up and change code
//change variable names?
//backtrace to list sequence
//better permutations 

public class PartitionInteger {
    public static void perm2(int[] s, Set< ArrayList<Integer> > set) {
        int N = s.length;
        //char[] a = new char[N];
        //for (int i = 0; i < N; i++)
            //a[i] = s.charAt(i);
        perm2(s, N, set);
    }

    private static void perm2(int[] a, int n, Set<ArrayList<Integer>> set) {
        if (n == 1) {
        	//add the perm to the set of all of them
            //System.out.println(a);
            ArrayList<Integer> listC = new ArrayList<Integer>();
            for (int c : a) {
                listC.add(c);
            }
			System.out.println(listC);
        	set.add(listC);
            return;
        }
        for (int i = 0; i < n; i++) {
        	//if (a[i] != a[n-1]) {
        	//if (!set.contains(a)) {
				swap(a, i, n-1);
				perm2(a, n-1, set);
				swap(a, i, n-1);
        	//}
        }
    }  

    // swap the characters at indices i and j
    private static void swap(int[] a, int i, int j) {
        int c = a[i];
        a[i] = a[j];
        a[j] = c;
    }

    int[] toIntArray(ArrayList<Integer> list){
    	  int[] ret = new int[list.size()];
    	  for(int i = 0;i < ret.length;i++)
    	    ret[i] = list.get(i);
    	  return ret;
    	}
    
	//TODO master list of lists
	void printPartitions(int n, int max, ArrayList<Integer> list, ArrayList<ArrayList<Integer>> comp) {
		  if (n == 0) {
			  //TODO this is where we permute the list and insert the perms
			  //System.out.println(Arrays.toString(list.toArray()) );
			  HashSet<ArrayList<Integer>> set = new HashSet<ArrayList<Integer>>();
			  //perm2(list.toString().replace("[", "").replace("]", "").replace(",", "").replace(" ", ""), set);
			  perm2(toIntArray(list), set);
				for (ArrayList<Integer> temp : set)
					comp.add(temp);
					//System.out.println(temp);
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
		int minCost = Integer.MAX_VALUE;
		
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
		PartitionInteger test = new PartitionInteger();

		ArrayList<Integer> list = new ArrayList<Integer>();
		//HashSet<ArrayList<Character>> set = new HashSet<ArrayList<Character>>();
		ArrayList<ArrayList<Integer>> comp = new ArrayList<ArrayList<Integer>>();
		//get list containing composition lists
		test.printPartitions(4,4,list, comp);
		//for (ArrayList<Integer> temp : comp)
			//System.out.println(temp);
		
		//test out walking the graph
		ArrayList<Integer> testList = new ArrayList<Integer>();
		testList.add(1);
		testList.add(2);
		int[][] testGraph = new int[][]{
			{0,1,9,7,88},
			{0,0,1,4,6},
			{0,0,0,1,4},
			{0,0,0,0,1}
		};
		
		//ArrayList<Integer> shortest = new ArrayList<Integer>();
		//int cost = test.brutePath(testGraph, comp, shortest);
		//System.out.println(shortest + " cost: " + cost);
		//test.walkGraph(testGraph, testList);

	}

}
