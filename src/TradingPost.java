package src;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TradingPost {
	static ArrayList<int[]> portData;

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
		int[][] rows = new int[portData.size()][arrSize];
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
}
