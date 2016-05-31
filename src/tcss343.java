import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
/**
 * 
 * @author Nicholas A. Hays, Ben Pasero, Ethan Rowell
 */
public class tcss343 {

    /** Determines whether StdIn (Command line input redirection) is used or FileInputStream. */
    private static boolean USE_STDIN = true;

    /** Keeps track of the minimum cost path */
    private static int minCost = Integer.MAX_VALUE;

    /** Holds shortest path for Divide and Conquer */
    static LinkedList<Integer> shortest = new LinkedList<>();

    /**
     * Main entry point.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
    	 String inputStr = readInput("sample_input.txt");
         int[][] rowData = parseInput(inputStr);
         
    	 minCost = Integer.MAX_VALUE;
         testBruteForce(rowData);
         System.out.println();

         minCost = Integer.MAX_VALUE;
         testDivideAndConquer(rowData);
         System.out.println(); 

         testDynProg(rowData);
         System.out.println();
    }

    /**
     * Reads the input file into a string.
     *
     * @param fileName File name used for using FileInputStream.
     * @return A string with the port data to be parsed.
     */
    private static String readInput(String fileName) {
        StringBuilder sb = new StringBuilder();
        if (USE_STDIN) {
            InputStreamReader cin = new InputStreamReader(System.in);
            try {
                while (cin.ready()) {
                    sb.append((char) cin.read());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                System.setIn(new FileInputStream(fileName));
                while (System.in.available() > 0) {
                    sb.append((char) System.in.read());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    /**
     * Assumes the CRLF ('\n') line endings are used and tabs ('\t') delimit values.
     *
     * @param inputStr The string to parse for port data.
     * @return 2-D array of port data.
     */
    private static int[][] parseInput(String inputStr) {
        int colCount = 1, rowCount = 0;

        // find number of columns
        for (int i = 0; i < inputStr.indexOf('\n'); i++)
            if (inputStr.charAt(i) == '\t') colCount++;
        int[][] rowData = new int[colCount][colCount];

        while (inputStr.length() > 0) {
            int i = 0;
            String row = inputStr.substring(0, inputStr.indexOf('\n') + 1); // get next row to parse
            int[] rowCosts = new int[colCount];
            while (row.length() > 0) {
            	
                // check for next tab char index, if not found, carriage return index returned.
                int charIndex = (row.indexOf('\t') > 0) ? row.indexOf('\t') : row.indexOf('\n');
                String value = row.substring(0, charIndex);

                // check for NA to add sentinel, otherwise, parse the int
                rowCosts[i] = (value.equals("NA")) ? Integer.MAX_VALUE : Integer.parseInt(value);

                i++;
                row = row.substring(charIndex + 1); // remove recently add cost section of the string
            }
            rowData[rowCount] = rowCosts;
            rowCount++;
            inputStr = inputStr.substring(inputStr.indexOf('\n') + 1); // remove parsed row from input Str
        }
        return rowData;
    }


    //========================================================================================
    //
    // Brute Force Methods
    //
    //========================================================================================


    /**
     * Splits an integer n into its combinatorial partitions. Generates
     * unique permutations of those partitions and adds them to a list.
     * @param n Partition sum/target integer
     * @param max Max value in sum to partition.
     * @param list current list of number in partition
     * @param comp list of compositions
     */
    static void partition(int n, int max, ArrayList<Integer> list, ArrayList<ArrayList<Integer>> comp) {
        if (n == 0) {
            // this is where we permute the list and insert the perms
            for (ArrayList<Integer> temp : permute(toIntArray(list)))
                comp.add(temp);
            return;
        }

        for (int i = Math.min(max, n); i >= 1; i--) {
            ArrayList<Integer> temp = new ArrayList<Integer>(list);
            temp.add( new Integer(i) );
            partition(n-i, i, temp, comp);
        }

    }

    /**
     * Traveses the matrix of costs between trading posts based on a list
     * of moves (moves are not posts)
     *
     * @param graph 2d array representing the graph
     * @param list list of moves
     * @return cost of the path taken by the moves in list
     */
    private static int walkGraph(int[][] graph, ArrayList<Integer> list) {
        int i = 0, j = 0, cost = 0;

        for (int temp : list) {
            cost += graph[i][j + temp];
            i += temp;
            j += temp;
        }
        return cost;
    }

    /**
     * Traveses every path in paths list and keeps track of the shortest path
     * and its cost.
     *
     * @param graph 2d array of posts
     * @param paths list of lists of moves
     * @param shortest shortest path
     * @return cost of the shortest path
     */
    private static int brutePath(int[][] graph, ArrayList<ArrayList<Integer>> paths,
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

    /**
     * Takes a list of moves and prints them as post indexes
     *
     * @param moves list containing moves
     */
    private static void printMovesToNodes(ArrayList<Integer> moves) {
        int node = 0;
        System.out.print("[0");
        for (int move : moves) {
            node += move;
            System.out.print(", " +node);
        }
        System.out.println("]");
    }

    /**
     * Tests the Brute Force approach with the fileName data provided and prints the results.
     *
     * @param fileName the sample input to parse
     */
    private static void testBruteForce(int[][] rowData) {

        ArrayList<Integer> shortest = new ArrayList<>();
        ArrayList<ArrayList<Integer>> comp = new ArrayList<>();
        ArrayList<Integer> list = new ArrayList<>();

        System.out.println("Testing Brute Force (Size " + rowData.length + "): ");
        long start = System.currentTimeMillis();
        //set to n - 1
        partition(rowData.length - 1, rowData.length - 1,list, comp);
        //actually does stuff
        int cost = brutePath(rowData, comp, shortest);

        long end = System.currentTimeMillis();
        System.out.println("  -  Elapsed Time (ms): " + (end - start));
        // System.out.println(shortest + " cost: " + cost);
        System.out.println("  -  Minimum Cost     : " + cost);
        System.out.print("  -  Path Sequence    : "  );
        printMovesToNodes(shortest);
        shortest.clear();
        minCost = Integer.MAX_VALUE;
    }


    //========================================================================================
    //
    // Divide and Conquer Methods
    //
    //========================================================================================


    /**
     * Performs a depth first search from the start node to the end. travels
     * all paths from the start of the graph to the end
     *
     * @param graph graph to search represended as a 2d array
     * @param marked all nodes that have been marked by the search
     * @param cost the current cost of the traversal
     */
    private static void divConq(int[][] graph, LinkedList<Integer> marked, int cost) {

        //our last node is always this
        int end = graph.length - 1;
        LinkedList<Integer> adjacent = new LinkedList<Integer>();
        //fill our list of adjacent nodes
        for (int i = marked.getLast() + 1; i <= end; i++) {
            adjacent.add(i);
        }

        //check adj nodes for the end
        for (int node : adjacent) {
            if (!marked.contains(node)) {
                if (node == end) {
                    int lastNode = marked.getLast();
                    marked.addLast(node);
                    cost += graph[lastNode][node];
                    //remember min cost
                    if (cost < minCost) {
                        minCost = cost;
                        shortest.clear();
                        shortest.addAll(marked);
                    }

                    //System.out.println(marked + " cost: " + cost + " min: " + minCost);
                    cost = cost - graph[lastNode][node];
                    marked.removeLast();
                    break;
                }
            }
        }

        //keep visiting nodes we have not marked before
        for(int node : adjacent) {
            if (!marked.contains(node) && node != end) {
                int lastNode = marked.getLast();
                marked.addLast(node);
                //dont go down a path with a greater total weith than our min
                if (cost + graph[lastNode][node] < minCost)
                    divConq(graph, marked, cost + graph[lastNode][node]);
                marked.removeLast();
            }
        }

    }

    /**
     * Tests the Dynamic Programming approach with the fileName data provided and prints the results.
     *
     * @param fileName the sample input to parse
     */
    private static void testDivideAndConquer(int[][] rowData) {

        LinkedList<Integer> marked = new LinkedList<Integer>();
        marked.add(0);
        System.out.println("Testing Divide and Conquer (Size " + rowData.length + "): ");
        long start = System.currentTimeMillis();
        divConq(rowData, marked, 0);
        long end = System.currentTimeMillis();
        System.out.println("  -  Elapsed Time (ms): " + (end - start));
        System.out.println("  -  Minimum Cost     : " + minCost);
        System.out.println("  -  Path Sequence    : " + shortest);
        shortest.clear();
        minCost = Integer.MAX_VALUE;
    }


    //========================================================================================
    //
    // Dynamic Programming Methods
    //
    //========================================================================================


    /**
     * Tests the Divide and Conquer approach with the rowData provided and prints the results.
     *
     * @param rowData Port data provided by parseInput.
     */
    private static void testDynProg(int[][] rowData) {
        long startTime = System.currentTimeMillis();

        int[][] costArray = dynamicProg(rowData);
        List<int[]> shortestPath = findDynProgPath(costArray);

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        System.out.println("Testing Dynamic Programming (Size " + costArray.length + "): ");
        System.out.println("  -  Elapsed Time (ms): " + elapsedTime);
        System.out.println("  -  Minimum Cost     : " + shortestPath.get(0)[2]);
        System.out.println("  -  Path Sequence    : " + getShortestPath(shortestPath));
    }

    /**
     * Calculates the minimum cost to get to each port.
     *
     * @param thePorts The port data.
     * @return A cost array the shows the cost to each port.
     */
    private static int[][] dynamicProg(int[][] thePorts) {
        int[][] costArray = new int[thePorts.length][thePorts[0].length];
        for (int k = 0; k < thePorts.length; k++) {
            costArray[k] = thePorts[k].clone();
        }

        for (int i = 0; i < costArray.length - 1; i++) {
            int currCost = 0;
            for (int j = i; j < costArray[i].length; j++) {
                if (j == i && i != 0) {
                    // find min originating port
                    int minCost = Integer.MAX_VALUE;
                    for (int k = i - 1; k >= 0; k--) {
                        if (costArray[k][j] < minCost) {
                            currCost = costArray[k][j];
                            minCost = currCost;
                        }
                    }
                }
                costArray[i][j] += currCost;
            }
        }
        return costArray;
    }

    /**
     * Finds the shortest path to the destination port given a cost array.
     *
     * @param costArray The cost array used to determine the minimum cost.
     * @return A list of coordinates and the cost at the coordinate.
     *          I.e. { 4, 6, 16 } would be interpreted as (4, 6) with cost 16.
     */
    private static List<int[]> findDynProgPath(int[][] costArray) {
        List<int[]> shortestPath = new ArrayList<>();
        int colPtr = costArray.length - 1;

        while (colPtr != 0) {
            int minOrigPort = 0;
            int minOrigCost = Integer.MAX_VALUE;

            // add path where canoe was rented if not on first iteration.
            if (colPtr < costArray.length - 1)
                shortestPath.add(new int[]{colPtr, colPtr, costArray[colPtr][colPtr]});

            for (int k = 0; k < colPtr; k++) {
                if (costArray[k][colPtr] <= minOrigCost) {
                    minOrigCost = costArray[k][colPtr];
                    minOrigPort = k;
                }
            }
            shortestPath.add(new int[]{minOrigPort, colPtr, costArray[minOrigPort][colPtr]}); // add port coordinates to path list.
            colPtr = minOrigPort; // set colPtr to new min originating port
        }
        shortestPath.add(new int[]{0, 0, 0}); // add starting port

        return shortestPath;
    }

    /**
     * Prints the cost array that was used to determine the minimum cost.
     *
     * @param costArray The cost array to print.
     */
    private static void printCostArray(int[][] costArray) {
        StringBuilder sb = new StringBuilder();
        for (int[] aCostArray : costArray) {
            sb.append("[ ");
            for (int k = 0; k < costArray[0].length; k++) {
                if (k < costArray[0].length - 1) {
                    if (aCostArray[k] != Integer.MAX_VALUE) sb.append(aCostArray[k]).append(", ");
                    else sb.append("X, ");
                } else {
                    sb.append(aCostArray[k]).append(" ");
                }
            }
            sb.append("]");
            System.out.println(sb.toString());
            sb.setLength(0);
        }
    }

    /**
     * Gets the shortest path given a sequence provided by the dynamic programming algorithm.
     *
     * @param pathSequence The minimum cost sequence of ports to get to the destination port.
     * @return A string representing the sequence for the shortest path.
     */
    private static String getShortestPath(List<int[]> pathSequence) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = pathSequence.size() - 1; i >= 0; i -= 2) {
        	//System.out.println(pathSequence.get(i)[1]);
            if (i == 1) sb.append(pathSequence.get(i)[1]);
            else sb.append(pathSequence.get(i)[1]).append(", ");
        }
        sb.append(", " + pathSequence.get(0)[1]);
        sb.append("]");

        return sb.toString();
    }


    //========================================================================================
    //
    // Helper Methods
    //
    //========================================================================================


    /**
     * Generates test input files using an array of sizes and array of step sizes to derive costs.
     */
    private static void generateTestData() {
        int[] testSizes = new int[]{ 10, 15, 20, 25, 50, 100, 200, 400, 600, 800 };
        int[] steps = new int[]{ 1, 2, 3 };

        Random rand = new Random();
        StringBuilder sb = new StringBuilder();

        for (int inputSize : testSizes) {
            File testFile = new File("sample_input_size" + inputSize + ".txt");
            sb.setLength(0);
            int offset = 0,
                    lastRandInt = 0,
                    currRandInt;

            // insert test data in StringBuilder
            for (int i = 0; i < inputSize; i++) {
                for (int j = 0; j < inputSize; j++) {
                    if (j < offset) {
                        sb.append("NA");
                    } else if (j == offset){
                        sb.append(0);
                        lastRandInt = 0;
                    } else {
                        // increments by a random step size greater than the previous number.
                        currRandInt = lastRandInt + steps[rand.nextInt(steps.length)];
                        sb.append(currRandInt);
                        lastRandInt = currRandInt;
                    }
                    // use carriage return instead of tab before the line feed
                    if (j != inputSize - 1) sb.append('\t');
                    else sb.append('\r');
                }
                sb.append('\n');
                offset++;
            }
            // write test data to file
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(testFile), "UTF-8"))) {
                writer.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Calls the recursive permute method.
     *
     * @param num input array to permute
     * @return list of lists containing permutations
     */
    private static ArrayList<ArrayList<Integer>> permute(int[] num) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        permute(num, 0, result);
        return result;
    }

    /**
     * Permutes a given array. Does so by swapping the start value with
     * each other value in the array and recursively permuting on those
     * permutations.
     *
     * @param num Array to permute.
     * @param start Index to swap
     * @param result Lists of lists containing permutations
     */
    private static void permute(int[] num, int start, ArrayList<ArrayList<Integer>> result) {

        //base case
        if (start >= num.length ) {
            ArrayList<Integer> item = arrayToList(num);
            result.add(item);
        }

        //swap with all values and recurse
        for (int j = start; j <= num.length-1; j++) {
            //same as a regular permute algo but dont do anything for duplicates
            if (containsNoDuplicate(num, start, j)) {
                swap(num, start, j);
                permute(num, start + 1, result);
                swap(num, start, j);
            }
        }
    }

    /**
     * Converts an int array to a list.
     *
     * @param num input array
     * @return List containing the same values as the array
     */
    private static ArrayList<Integer> arrayToList(int[] num) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < num.length; i++) {
            result.add(num[i]);
        }
        return result;
    }

    /**
     * Checks for duplicate values in an array between an index start and
     * index end.
     *
     * @param arr the array
     * @param start start index
     * @param end end index
     * @return true if no duplicates were found.
     */
    private static boolean containsNoDuplicate(int[] arr, int start, int end) {
        boolean result = true;
        for (int i = start; i <= end-1; i++) {
            if (arr[i] == arr[end]) {
                result = false;
            }
        }
        return result;
    }

    /**
     * Converts an Integer list to an int array.
     *
     * @param list List of Integers
     * @return Array of ints
     */
    private static int[] toIntArray(ArrayList<Integer> list) {
        int[] result = new int[list.size()];
        for(int i = 0;i < result.length;i++)
            result[i] = list.get(i);
        return result;
    }

    /**
     * Swaps two values of an array.
     *
     * @param arr array of ints
     * @param i index to swap
     * @param j index to swap
     */
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}