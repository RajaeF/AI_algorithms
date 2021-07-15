package AssignmentOne;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class HillClimbingALGM {

	public static void main(String[] args) {
		csv("HillClimbingOne.csv", 0.01, hillClimbingOne(0.01));
		csv("HillClimbingOne.csv", 0.05, hillClimbingOne(0.05));
		csv("HillClimbingOne.csv", 0.1, hillClimbingOne(0.1));
		csv("HillClimbingOne.csv", 0.2, hillClimbingOne(0.2));
		csv("HillClimbingTwo.csv", 0.01, hillClimbingTwo(0.01));
		csv("HillClimbingTwo.csv", 0.05, hillClimbingTwo(0.05));
		csv("HillClimbingTwo.csv", 0.1, hillClimbingTwo(0.1));
		csv("HillClimbingTwo.csv", 0.2, hillClimbingTwo(0.2));
	}

	public static double fOne(double x, double y) {
		return Math.sin(x / 2) + Math.cos(2 * y);

	}

	public static double fTwo(double x, double y) {
		return -Math.abs(x - 2) - Math.abs(0.5 * y + 1) + 3;
	}

	/*
	 * Outputs csv files of the 100 tries, and the respective means / standard
	 * deviations
	 */
	public static void csv(String filePathName, double stepSize, ArrayList<ArrayList<Double>> arr) {

		File file = new File(filePathName);
		FileWriter fw;
		try {
			if (file.exists()) {
				fw = new FileWriter(file, true);
			} else {
				fw = new FileWriter(file);
			}
			fw.append("Stepsize");
			fw.append("," + stepSize);
			fw.append('\n');
			fw.append("Run");
			for (int i = 1; i < 101; i++) {
				fw.append("," + i);
			}
			fw.append('\n');
			fw.append("Maximum");
			for (int i = 0; i < 100; i++) {
				fw.append("," + arr.get(0).get(i));
			}
			fw.append('\n');
			fw.append("Steps");
			for (int i = 0; i < 100; i++) {
				fw.append("," + arr.get(1).get(i));
			}
			fw.append('\n');
			double maxMean = 0;
			for (Double d : arr.get(0)) {
				maxMean += d;
			}
			double tempMax = 0;
			for (Double d : arr.get(0)) {
				double x = Math.pow(d - maxMean / 100.0, 2);
				tempMax += x;
			}
			fw.append("Mean of the maximum");
			fw.append("," + maxMean / 100.0);
			fw.append('\n');
			fw.append("Standard deviation of the maximum");
			fw.append("," + Math.sqrt(tempMax / 100.0));
			fw.append('\n');
			double stepMean = 0;
			for (Double d : arr.get(1)) {
				stepMean += d;
			}
			double tempStep = 0;
			for (Double d : arr.get(1)) {
				double x = Math.pow(d - stepMean / 100.0, 2);
				tempStep += x;
			}
			fw.append("Mean of the steps");
			fw.append("," + stepMean / 100.0);
			fw.append('\n');
			fw.append("Standard deviation of the steps");
			fw.append("," + Math.sqrt(tempStep / 100.0));
			fw.append('\n');
			fw.append('\n');
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<ArrayList<Double>> hillClimbingOne(double stepSize) {

		// stores the number of steps and the maximum value for each iteration
		ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();
		result.add(new ArrayList<Double>());
		result.add(new ArrayList<Double>());

		// 100 runs
		for (int i = 0; i < 100; i++) {
			Random ran = new Random();
			// I do realize that nextDouble() is not inclusive of 1, but the probability of
			// getting a one, knowing that it is a double value, is really small
			double x = 10 * ran.nextDouble();
			double y = 10 * ran.nextDouble();
			double f = fOne(x, y);

			double steps = 0;

			// iterating until there is no higher value than the current max
			while (true) {

				// generate neighbors
				double[][] coordinates = new double[8][2];

				// first neighbor
				coordinates[0][0] = x;
				coordinates[0][1] = y + stepSize;
				// second neighbor
				coordinates[1][0] = x;
				coordinates[1][1] = y - stepSize;
				// third neighbor
				coordinates[2][0] = x + stepSize;
				coordinates[2][1] = y;
				// fourth neighbor
				coordinates[3][0] = x - stepSize;
				coordinates[3][1] = y;
				// fifth neighbor
				coordinates[4][0] = x + stepSize;
				coordinates[4][1] = y + stepSize;
				// sixth neighbor
				coordinates[5][0] = x - stepSize;
				coordinates[5][1] = y - stepSize;
				// seventh neighbor
				coordinates[6][0] = x - stepSize;
				coordinates[6][1] = y + stepSize;
				// eighth neighbor
				coordinates[7][0] = x + stepSize;
				coordinates[7][1] = y - stepSize;

				// function value of each neighbor
				double[] value = new double[8];
				for (int k = 0; k < 8; k++) {
					value[k] = fOne(coordinates[k][0], coordinates[k][1]);
				}

				// tracks the maximum value so far
				double fMax = f;
				int indexMax = -1;
				int changed = -1;
				for (int k = 0; k < 8; k++) {
					// invariant for the range
					if (value[k] > fMax && coordinates[k][0] <= 10 && coordinates[k][1] <= 10 && coordinates[k][0] >= 0
							&& coordinates[k][1] >= 0) {
						fMax = value[k];
						indexMax = k;
						changed = 0;
					}
				}

				// if function value did not change, no neighbor was better, so break
				if (fMax == f && changed == -1) {
					break;
				}
				f = fMax;
				x = coordinates[indexMax][0];
				y = coordinates[indexMax][1];
				steps++;
			}
			result.get(0).add(f);
			result.get(1).add(steps);
		}
		return result;
	}

	/*
	 * SAme as hillClimbingOne, but uses the second function
	 */
	public static ArrayList<ArrayList<Double>> hillClimbingTwo(double stepSize) {
		ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();
		result.add(new ArrayList<Double>());
		result.add(new ArrayList<Double>());
		for (int i = 0; i < 100; i++) {
			Random ran = new Random();
			// I do realize that nextDouble() is not inclusive of 1, but the probability of
			// getting a one, knowing that it is a double value, is really small
			double x = 10 * ran.nextDouble();
			double y = 10 * ran.nextDouble();
			double f = fTwo(x, y);
			double steps = 0;
			while (true) {

				// generate neighbors
				double[][] coordinates = new double[8][2];

				// first neighbor
				coordinates[0][0] = x;
				coordinates[0][1] = y + stepSize;
				// second neighbor
				coordinates[1][0] = x;
				coordinates[1][1] = y - stepSize;
				// third neighbor
				coordinates[2][0] = x + stepSize;
				coordinates[2][1] = y;
				// fourth neighbor
				coordinates[3][0] = x - stepSize;
				coordinates[3][1] = y;
				// fifth neighbor
				coordinates[4][0] = x + stepSize;
				coordinates[4][1] = y + stepSize;
				// sixth neighbor
				coordinates[5][0] = x - stepSize;
				coordinates[5][1] = y - stepSize;
				// seventh neighbor
				coordinates[6][0] = x - stepSize;
				coordinates[6][1] = y + stepSize;
				// eighth neighbor
				coordinates[7][0] = x + stepSize;
				coordinates[7][1] = y - stepSize;

				// function value of each neighbor
				double[] value = new double[8];
				for (int k = 0; k < 8; k++) {
					value[k] = fTwo(coordinates[k][0], coordinates[k][1]);
				}
				double fMax = f;
				int indexMax = -1;
				int changed = -1;
				for (int k = 0; k < 8; k++) {
					// make sure they stay in range
					if (value[k] > fMax && coordinates[k][0] <= 10 && coordinates[k][1] <= 10 && coordinates[k][0] >= 0
							&& coordinates[k][1] >= 0) {
						fMax = value[k];
						indexMax = k;
						changed = 0;
					}
				}

				// if function value did not change, no neighbor was better, so break
				if (fMax == f && changed == -1) {
					break;
				}
				f = fMax;
				x = coordinates[indexMax][0];
				y = coordinates[indexMax][1];
				steps++;
			}
			result.get(0).add(f);
			result.get(1).add(steps);
		}
		return result;
	}

}
