import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TradingPost {
    private static ArrayList<int[]> portData;

    /**
     * Determines whether StdIn is user or FileInputStream.
     */
    private static boolean USE_STDIN = false;

    public static void main(String[] args) {
        portData = new ArrayList<>();

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
                System.setIn(new FileInputStream("sample_input_size100.txt"));
                while (System.in.available() > 0) {
                    sb.append((char) System.in.read());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int[][] rows = parseInput(sb);
        int[][] costArray = dynamicProg(rows);
        findDynProgPath(costArray);
        // generateTestData();


    }

    private static int[][] parseInput(StringBuilder input) {
        String inputStr = input.toString();

        int colCount = 1;
        // find number of columns
        for (int i = 0; i < inputStr.indexOf('\n'); i++)
            if (input.charAt(i) == '\t') colCount++;

        int[][] rowData = new int[colCount][colCount];
        int charIndex,
            rowCount = 0;

        while (inputStr.length() > 0) {
            int i = 0;
            String row = inputStr.substring(0, inputStr.indexOf('\n'));
            int[] rowCosts = new int[colCount];
            while (row.length() > 0) {
                String value;
                charIndex = row.indexOf('\t');

                if (charIndex > 0) { // tab char exists
                    value = row.substring(0, charIndex);
                }
                else { // hit end of row that had carriage return
                    charIndex = row.indexOf('\r');
                    value = row.substring(0, charIndex);
                }

                if (value.equals("NA")) {
                    rowCosts[i] = Integer.MAX_VALUE; // add sentinel
                    i++;
                } else {
                    rowCosts[i] = Integer.parseInt(value); // add cost
                    i++;
                }
                row = row.substring(charIndex + 1); // remove recently add cost section of the string
            }
            rowData[rowCount] = rowCosts;
            rowCount++;
            inputStr = inputStr.substring(inputStr.indexOf('\n') + 1);
        }
        return rowData;
    }

    private static int[][] dynamicProg(int[][] thePorts) {
        int[][] costArray = new int[thePorts.length][thePorts[0].length];
        for (int k = 0; k < thePorts.length; k++) {
            costArray[k] = thePorts[k].clone();
        }
        for (int i = 0; i < costArray.length - 1; i++) {
            int x = 0;
            for (int j = i; j < costArray[i].length; j++) {
                if (j == i && i != 0) {
                    x = costArray[i - 1][j];
                }
                costArray[i][j] += x;
            }
        }
        return costArray;
    }

    private static List<int[]> findDynProgPath(int[][] costArray) {
        List<int[]> shortestPath = new ArrayList<>();
        int colPtr = costArray.length - 1;

        while (colPtr != 0) {
            int minOrigPort = 0;
            int minOrigCost = Integer.MAX_VALUE;

            // add path where canoe was rented if not on first iteration.
            if (colPtr < costArray.length - 1)
                shortestPath.add(new int[]{colPtr, colPtr});

            for (int k = 0; k < colPtr; k++) {
                if (costArray[k][colPtr] <= minOrigCost) {
                    minOrigCost = costArray[k][colPtr];
                    minOrigPort = k;
                }
            }
            shortestPath.add(new int[]{minOrigPort, colPtr}); // add port coordinates to path list.
            colPtr = minOrigPort; // set colPtr to new min originating port
        }

        shortestPath.add(new int[]{0, 0}); // add starting port

        // print cost array
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

        return shortestPath;
    }

    private static void generateTestData() {
        int[] testSizes = new int[]{ 10, 20, 50, 100, 200, 400, 600, 800 };
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
                        currRandInt = lastRandInt + steps[rand.nextInt(steps.length)];
                        sb.append(currRandInt);
                        lastRandInt = currRandInt;
                    }
                    // use carriage return instead of tab at end
                    if(j != inputSize - 1)
                        sb.append('\t');
                    else
                        sb.append('\r');
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



    public static void BruteForce(int[][] thePorts) {

    }
}