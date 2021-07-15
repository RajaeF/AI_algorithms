package AssignmentOne;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class LazerBeamALGM extends HillClimbingALGM {

	public static void main(String[] args) {
		csv("LazerBeamOne.csv", 2, lazerBeamOne(0.05, 2));
		csv("LazerBeamOne.csv", 4, lazerBeamOne(0.05, 4));
		csv("LazerBeamOne.csv", 8, lazerBeamOne(0.05, 8));
		csv("LazerBeamOne.csv", 16, lazerBeamOne(0.05, 16));
		csv("LazerBeamTwo.csv", 2, lazerBeamTwo(0.05, 2));
		csv("LazerBeamTwo.csv", 4, lazerBeamTwo(0.05, 4));
		csv("LazerBeamTwo.csv", 8, lazerBeamTwo(0.05, 8));
		csv("LazerBeamTwo.csv", 16, lazerBeamTwo(0.05, 16));

	}

	/*
	 * Outputs csv files of the 100 tries, and the respective means / standard
	 * deviations
	 */
	public static void csv(String filePathName, int width, ArrayList<ArrayList<Double>> arr) {

		File file = new File(filePathName);
		FileWriter fw;
		try {
			if (file.exists()) {
				fw = new FileWriter(file, true);
			} else {
				fw = new FileWriter(file);
			}
			fw.append("Width");
			fw.append("," + width);
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
	
	public static ArrayList<ArrayList<Double>> lazerBeamOne(double stepSize, int width) {

		// stores the number of steps and the maximum value for each iteration
		ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();
		result.add(new ArrayList<Double>());
		result.add(new ArrayList<Double>());

		// 100 runs
		for (int i = 0; i < 100; i++) {

			// stores all the children's coordinates + function values
			ArrayList<ArrayList<Double>> list = new ArrayList<ArrayList<Double>>();

			Random ran = new Random();

			// tracks the maximum function value so far
			double valueTop = Integer.MIN_VALUE;

			// generating parents
			for (int k = 0; k < width; k++) {
				list.add(new ArrayList<Double>());
				list.get(k).add(10 * ran.nextDouble());
				list.get(k).add(10 * ran.nextDouble());
				list.get(k).add(fOne(list.get(k).get(0), list.get(k).get(1)));
				if (valueTop < list.get(k).get(2)) {
					valueTop = list.get(k).get(2);
				}
			}

			// tracks number of steps
			double steps = 0;

			// iterates until the maximum possible value is reached
			while (true) {
				// contains all the children
				ArrayList<ArrayList<Double>> nList = new ArrayList<ArrayList<Double>>();
				for (int k = 0; k < width; k++) {
					nList.addAll(neighborGenOne(list.get(k).get(0), list.get(k).get(1), stepSize));
				}

				// stores the k children with the maximum function value
				ArrayList<ArrayList<Double>> maxKiddos = new ArrayList<ArrayList<Double>>();

				// finding the k children with the maximum function value
				while (maxKiddos.size() != width) {
					double max = Integer.MIN_VALUE;
					int index = -1;

					for (int k = 0; k < nList.size(); k++) {
						if (nList.get(k).get(2) > max && nList.get(k).get(0) >= 0 && nList.get(k).get(1) >= 0
								&& nList.get(k).get(0) <= 10 && nList.get(k).get(1) <= 10) {
							max = nList.get(k).get(2);
							index = k;
						}
					}
					maxKiddos.add(nList.get(index));
					nList.remove(index);
				}

				int check = -1;
				for (int k = 0; k < width; k++) {
					if (valueTop < maxKiddos.get(k).get(2)) {
						valueTop = maxKiddos.get(k).get(2);
						check = 0;
					}
				}

				// if no kid was better, break
				if (check == -1) {
					break;
				}

				// replacing the parent by their k best children
				list.clear();
				list.addAll(maxKiddos);

				maxKiddos.clear();
				// what if one of the parent is bigger than one of the children, but not bigger
				// than all of them : prof said keep on going.
				steps++;
			}
			result.get(0).add(valueTop);
			result.get(1).add(steps);
		}
		return result;
	}

	//same thing as above but with the second function
	public static ArrayList<ArrayList<Double>> lazerBeamTwo(double stepSize, int width) {
		ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();
		result.add(new ArrayList<Double>());
		result.add(new ArrayList<Double>());
		for (int i = 0; i < 100; i++) {
			ArrayList<ArrayList<Double>> list = new ArrayList<ArrayList<Double>>();
			Random ran = new Random();
			int indexTop = -1;
			double valueTop = Integer.MIN_VALUE;
			for (int k = 0; k < width; k++) {
				list.add(new ArrayList<Double>());
				list.get(k).add(10 * ran.nextDouble());
				list.get(k).add(10 * ran.nextDouble());
				list.get(k).add(fTwo(list.get(k).get(0), list.get(k).get(1)));
				if (valueTop < list.get(k).get(2)) {
					valueTop = list.get(k).get(2);
					indexTop = k;
				}
			}
			double steps = 0;
			while (true) {
				ArrayList<ArrayList<Double>> nList = new ArrayList<ArrayList<Double>>();
				for (int k = 0; k < width; k++) {
					nList.addAll(neighborGenTwo(list.get(k).get(0), list.get(k).get(1), stepSize));
				}
				ArrayList<ArrayList<Double>> maxKiddos = new ArrayList<ArrayList<Double>>();

				while (maxKiddos.size() != width) {
					double max = Integer.MIN_VALUE;
					int index = -1;
					for (int k = 0; k < nList.size(); k++) {
						if (nList.get(k).get(2) > max && nList.get(k).get(0) >= 0 && nList.get(k).get(1) >= 0
								&& nList.get(k).get(0) <= 10 && nList.get(k).get(1) <= 10) {
							max = nList.get(k).get(2);
							index = k;
						}
					}
					maxKiddos.add(nList.get(index));
					nList.remove(index);
				}
				int check = -1;
				for (int k = 0; k < width; k++) {
					if (valueTop < maxKiddos.get(k).get(2)) {
						valueTop = maxKiddos.get(k).get(2);
						check = 0;
					}
				}
				if (check == -1) {
					break;
				}
				list.clear();
				list.addAll(maxKiddos);
				maxKiddos.clear();
				// what if one of the parent is bigger than one of the children, but not bigger
				// than all of them
				steps++;
			}
			result.get(0).add(valueTop);
			result.get(1).add(steps);
		}
		return result;
	}

	
	// generates neighbors
	public static ArrayList<ArrayList<Double>> neighborGenOne(double x, double y, double stepSize) {
		ArrayList<ArrayList<Double>> list = new ArrayList<ArrayList<Double>>();
		// first neighbor
		list.add(new ArrayList<Double>());
		list.get(0).add(x);
		list.get(0).add(y + stepSize);
		// second neighbor
		list.add(new ArrayList<Double>());
		list.get(1).add(x);
		list.get(1).add(y - stepSize);
		// third neighbor
		list.add(new ArrayList<Double>());
		list.get(2).add(x + stepSize);
		list.get(2).add(y);
		// fourth neighbor
		list.add(new ArrayList<Double>());
		list.get(3).add(x - stepSize);
		list.get(3).add(y);
		// fifth neighbor
		list.add(new ArrayList<Double>());
		list.get(4).add(x + stepSize);
		list.get(4).add(y + stepSize);
		// sixth neighbor
		list.add(new ArrayList<Double>());
		list.get(5).add(x - stepSize);
		list.get(5).add(y - stepSize);
		// seventh neighbor
		list.add(new ArrayList<Double>());
		list.get(6).add(x - stepSize);
		list.get(6).add(y + stepSize);
		// eighth neighbor
		list.add(new ArrayList<Double>());
		list.get(7).add(x + stepSize);
		list.get(7).add(y - stepSize);

		for (int k = 0; k < 8; k++) {
			list.get(k).add(fOne(list.get(k).get(0), list.get(k).get(1)));
		}
		return list;
	}

	public static ArrayList<ArrayList<Double>> neighborGenTwo(double x, double y, double stepSize) {
		ArrayList<ArrayList<Double>> list = new ArrayList<ArrayList<Double>>();
		// first neighbor
		list.add(new ArrayList<Double>());
		list.get(0).add(x);
		list.get(0).add(y + stepSize);
		// second neighbor
		list.add(new ArrayList<Double>());
		list.get(1).add(x);
		list.get(1).add(y - stepSize);
		// third neighbor
		list.add(new ArrayList<Double>());
		list.get(2).add(x + stepSize);
		list.get(2).add(y);
		// fourth neighbor
		list.add(new ArrayList<Double>());
		list.get(3).add(x - stepSize);
		list.get(3).add(y);
		// fifth neighbor
		list.add(new ArrayList<Double>());
		list.get(4).add(x + stepSize);
		list.get(4).add(y + stepSize);
		// sixth neighbor
		list.add(new ArrayList<Double>());
		list.get(5).add(x - stepSize);
		list.get(5).add(y - stepSize);
		// seventh neighbor
		list.add(new ArrayList<Double>());
		list.get(6).add(x - stepSize);
		list.get(6).add(y + stepSize);
		// eighth neighbor
		list.add(new ArrayList<Double>());
		list.get(7).add(x + stepSize);
		list.get(7).add(y - stepSize);

		for (int k = 0; k < 8; k++) {
			list.get(k).add(fTwo(list.get(k).get(0), list.get(k).get(1)));
		}
		return list;
	}

}
