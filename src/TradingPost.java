import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TradingPost {

    /**
     * Determines whether StdIn (Command line input redirection) is used or FileInputStream.
     */
    private static boolean USE_STDIN = false;

    private static int minCost = Integer.MAX_VALUE;

    LinkedList<Integer> shortest;

    public static void main(String[] args) {

        generateTestData();

        String[] smallFileNames = {"sample_input_size10.txt", "sample_input_size15.txt", "sample_input_size20.txt",
                                      "sample_input_size25.txt"};

        for (String fileName : smallFileNames) {

            testBruteForce(fileName);
            System.out.println();
            testDivideAndConquer(fileName);
            System.out.println();
        }

        String[] largeFileNames = {"sample_input_size100.txt", "sample_input_size200.txt", "sample_input_size400.txt",
                "sample_input_size600.txt", "sample_input_size800.txt"};

        for (String fileName : largeFileNames) {
            String inputStr = readInput(fileName);
            int[][] rowData = parseInput(inputStr);

            testDynProg(rowData);
            System.out.println();
        }
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
     * Assumes the CRLF ('\r''\n') line endings are used and tabs ('\t') delimit values.
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
            String row = inputStr.substring(0, inputStr.indexOf('\n')); // get next row to parse
            int[] rowCosts = new int[colCount];
            while (row.length() > 0) {
                // check for next tab char index, if not found, carriage return index returned.
                int charIndex = (row.indexOf('\t') > 0) ? row.indexOf('\t') : row.indexOf('\r');
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
     *
     *
     * @param n
     * @param max
     * @param list
     * @param comp
     */
    private static void printPartitions(int n, int max, ArrayList<Integer> list, ArrayList<ArrayList<Integer>> comp) {
        if (n == 0) {
            //TODO this is where we permute the list and insert the perms
            for (ArrayList<Integer> temp : permuteUnique(toIntArray(list)))
                //System.out.println(temp);
                comp.add(temp);
            return;
        }

        for (int i = Math.min(max, n); i >= 1; i--) {
            //string is immutable remember
            ArrayList<Integer> temp = new ArrayList<>(list);
            temp.add(i);
            printPartitions(n-i, i, temp, comp);
        }

    }

    /**
     *
     *
     * @param graph
     * @param list
     * @return
     */
    private static int walkGraph(int[][] graph, ArrayList<Integer> list) {
        int i = 0, j = 0, cost = 0;

        for (int temp : list) {
            cost += graph[i][j + temp];
            //System.out.println(graph[i][j + temp]); TODO delete
            i += temp;
            j += temp;
        }
        return cost;
    }

    /**
     *
     *
     * @param graph
     * @param paths
     * @param shortest
     * @return
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
     *
     *
     * @param fileName
     */
    private static void testBruteForce(String fileName) {
        String inputStr = readInput(fileName);
        int[][] rowData = parseInput(inputStr);

        ArrayList<Integer> shortest = new ArrayList<>();
        ArrayList<ArrayList<Integer>> comp = new ArrayList<>();
        ArrayList<Integer> list = new ArrayList<>();

        System.out.println("Testing Brute Force (Size " + rowData.length + "): ");
        long start = System.currentTimeMillis();
        //set to n - 1
        printPartitions(rowData.length - 1, rowData.length - 1,list, comp);
        //actually does stuff
        int cost = brutePath(rowData, comp, shortest);

        long end = System.currentTimeMillis();
        System.out.println("  -  Elapsed Time (ms): " + (end - start));
        // System.out.println(shortest + " cost: " + cost);
        System.out.println("  -  Minimum Cost     : " + cost);
        System.out.println("  -  Path Sequence    : " + shortest.toString());
    }


    //========================================================================================
    //
    // Divide and Conquer Methods
    //
    //========================================================================================


    /**
     *
     *
     * @param graph
     * @param visited
     * @return
     */
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

    /**
     *
     *
     * @param graph
     * @param visited
     * @param cost
     */
    private static void dft(int[][] graph, LinkedList<Integer> visited, int cost) {

        //our last node is always this
        int end = graph.length - 1;
        LinkedList<Integer> adjacentNodes = new LinkedList<>();
        //fill our list of adjacent nodes
        for (int i = visited.getLast() + 1; i <= end; i++) {
            adjacentNodes.add(i);
        }

        //check adj nodes for the end
        for (int node : adjacentNodes) {
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

                    // System.out.println(visited + " cost: " + cost + " min: " + minCost);
                    cost = cost - graph[lastNode][node];
                    visited.removeLast();
                    break;
                }
            }
        }

        //keep going
        for(int node : adjacentNodes) {
            if (!visited.contains(node) && node != end) {
                int lastNode = visited.getLast();
                visited.addLast(node);
                //dont go down a path with a greater total weight than our min
                //System.out.println("ay mincost is: " + minCost );
                if (cost + graph[lastNode][node] < minCost)
                    dft(graph, visited, cost + graph[lastNode][node]);
                visited.removeLast();
            }
        }

        // System.out.println(adjacentNodes);
    }

    /**
     *
     *
     * @param fileName
     */
    private static void testDivideAndConquer(String fileName) {
        String inputStr = readInput(fileName);
        int[][] rowData = parseInput(inputStr);

        LinkedList<Integer> visited = new LinkedList();
        visited.add(0);
        System.out.println("Testing Divide and Conquer (Size " + rowData.length + "): ");
        long start = System.currentTimeMillis();
        dft(rowData, visited, 0);
        long end = System.currentTimeMillis();
        System.out.println("  -  Elapsed Time (ms): " + (end - start));
        System.out.println("  -  Minimum Cost     : " + minCost);
        // System.out.println("  -  Path Sequence    : ");
    }


    //========================================================================================
    //
    // Dynamic Programming Methods
    //
    //========================================================================================


    /**
     * Tests the Dynamic Programming approach with the rowData provided and prints the results.
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
        // System.out.println("  -  Path Sequence    : " + getShortestPath(shortestPath));
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
     * Gets the shortest path given a sequence provided by the dynamic programming algorithm.
     *
     * @param pathSequence The minimum cost sequence of ports to get to the destination port.
     * @return A string representing the sequence for the shortest path.
     */
    private static String getShortestPath(List<int[]> pathSequence) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");

        for (int i = pathSequence.size() - 1; i > 0; i -= 2) {
            if (i == 1) sb.append(pathSequence.get(i)[1]);
            else sb.append(pathSequence.get(i)[1]).append(", ");
        }

        sb.append(" ]");

        return sb.toString();
    }


    //========================================================================================
    //
    // Helper Methods
    //
    //========================================================================================


    private static ArrayList<ArrayList<Integer>> permuteUnique(int[] num) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
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
        ArrayList<Integer> item = new ArrayList<>();
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

    private static int[] toIntArray(ArrayList<Integer> list){
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
}