<<<<<<< HEAD
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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
                /*Scanner scanner = new Scanner(new FileInputStream("sample_input.txt"));
                while (scanner.hasNext()) {
                    sb.append(scanner.next());
                }*/

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        parseInput(sb);
        int arrSize = portData.get(0).length;
        int[][] rows = new int[portData.size()][arrSize];
        for (int i = 0; i < portData.size(); i++) {
            for (int j = 0; j < portData.get(i).length; j++) {
                rows[i][j] = portData.get(i)[j];
                // System.out.println(portData.get(i)[j]);
            }
        }

        int[][] costArray = dynamicProg(rows);
        findDynProgPath(costArray);
        //generateTestData();
    }

    private static void parseInput(StringBuilder input) {

        ArrayList<String> s = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '\n') {
                int[] row = new int[s.size()];
                for (int j = 0; j < s.size(); j++) {
                    row[j] = Integer.parseInt("" + s.get((j)));
                }
                portData.add(row);
                s.clear();
            } else if (c == 'N' || c == '	') {

            } else if (c == 'A') {
                s.add("" + Integer.MAX_VALUE);
            } else {
                s.add("" + c);
            }
        }
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
        return shortestPath;
    }

    private static void generateTestData() {
        int[] testSizes = new int[]{ 100, 200, 400, 600, 800 };
        int[] steps = new int[]{ 1, 2, 3 };

        Random rand = new Random();
        StringBuilder sb = new StringBuilder();

        for (int inputSize : testSizes) {
            sb.setLength(0);
            File testFile = new File("sample_input_size" + inputSize + ".txt");
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
                        currRandInt = lastRandInt + steps[rand.nextInt(steps.length)]; //rand.nextInt(inputSize - lastRandInt) + lastRandInt;
                        sb.append(currRandInt);
                        lastRandInt = currRandInt;
                    }
                    sb.append('\t');
                }
                sb.append('\n');
                offset++;
            }
            // write test data to file
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(testFile), "UTF-8"))) {
                writer.write(sb.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static void BruteForce(int[][] thePorts) {

    }
=======

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TradingPost {
	static ArrayList<int[]> portData;
	private static int[][] rows; // the 2d prim array

	public static void main(String[] args) {
		portData = new ArrayList<int[]>();
		InputStreamReader cin = new InputStreamReader(System.in);
		StringBuilder sb = new StringBuilder();
		try {
			while (cin.ready()) {
				sb.append((char) cin.read());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		parseInput(sb);
		int arrSize = portData.get(0).length;
		rows = new int[portData.size()][arrSize];
		for (int i = 0; i < portData.size(); i++) {
			for (int j = 0; j < portData.get(i).length; j++) {
				rows[i][j] = portData.get(i)[j];
				System.out.println(portData.get(i)[j]);
			}
		}
	}

	public static void parseInput(StringBuilder input) {

		ArrayList<String> s = new ArrayList<String>();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c == '\n') {
				int[] row = new int[s.size()];
				for (int j = 0; j < s.size(); j++) {
					row[j] = Integer.parseInt("" + s.get((j)));
				}
				portData.add(row);
				s.clear();
			} else if (c == 'N' || c == '	') {

			} else if (c == 'A') {
				s.add("" + Integer.MAX_VALUE);
			} else {
				s.add("" + c);
			}
		}
	}

	public static void BruteForce(int[][] thePorts) {

	}
>>>>>>> 0f6086e72844604136c3bd7bafa06705ea84ced1
}