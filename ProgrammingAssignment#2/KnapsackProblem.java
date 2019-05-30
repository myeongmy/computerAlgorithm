


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// �� ���ǿ� ���� ������ �����ϱ� ���� Ŭ���� Item ����(�ε���, ����, ����, pi/wi �� ����)
class Item {
	int index;
	int profit;
	int weight;
	double p_w;
}

public class KnapsackProblem {
	// �������� ����
	static int n; // ������ ����
	static int totprofit;
	static int max_profit = 0; // backtracking�� ���� max_profit ������ ���� �� �ʱ�ȭ
	static int M;
	static int[] bestset; // backtracking�� ���� �ֻ��� ���� �迭
	static int[] include;

	// �� �� �� �ִ� �� ���� �Լ�
	static int max(int a, int b) {
		if (a > b)
			return a;
		else
			return b;
	}

	// ������ȹ�� Ǯ�� �Լ�
	static void dynamic_pro(Item[] item, int M) {
		int[][] profit = new int[item.length][M + 1];
		int[] solution_vector = new int[item.length - 1];
		for (int i = 0; i < item.length; i++) {
			for (int y = 0; y <= M; y++) {
				if (i == 0 || y == 0)
					profit[i][y] = 0;
				else if (item[i].weight > y) {
					profit[i][y] = profit[i - 1][y];
				} else {
					profit[i][y] = max(profit[i - 1][y], item[i].profit + profit[i - 1][y - item[i].weight]);
				}
			}
		}
		int result = profit[item.length - 1][M]; // ���� max_profit ��
		System.out.println("\t" + "The maximum profit is $" + result + ".");
		// �� ���� ���ϴ� ����
		int w = M;
		for (int i = item.length - 1; i > 0 && result > 0; i--) {
			if (result == profit[i - 1][w])
				continue;
			else {
				solution_vector[i - 1] = 1;
				result = result - item[i].profit;
				w = w - item[i].weight;
			}
		}
		// �� ���� ���
		System.out.print("\t" + "The solution vector X = (");
		for (int i = 0; i < item.length - 1; i++) {
			if (i == item.length - 2)
				System.out.println(solution_vector[i] + ")");
			else
				System.out.print(solution_vector[i] + ", ");
		}
	}

	// �������� QuickSort�� �ʿ��� partition �Լ�(���� ���� �� ���� ���� �������� �������� ������ �ؾ���)
	static int partition(Item[] item, int left, int right) {
		double pivot = item[right].p_w; // pivot �� ����
		int idx = left - 1;
		for (int i = left; i < right; i++) {
			if (item[i].p_w > pivot)
				swap(item, ++idx, i);
		}
		swap(item, ++idx, right);
		return idx;
	}

	static void swap(Item[] item, int a, int b) {
		Item temp = item[a];
		item[a] = item[b];
		item[b] = temp;
	}

	static void quickSort(Item[] item, int left, int right) {
		if (left < right) {
			int pivotNewIndex = partition(item, left, right);
			quickSort(item, left, pivotNewIndex - 1);
			quickSort(item, pivotNewIndex + 1, right);
		}
	}

	// backtracking�� �ʿ��� promising �Լ�
	static int promising(int i, int weight, Item[] item) {
		int j, k;
		int totweight;
		float bound;
		// weight�� ���� �� �ִ� �ִ� ���Ը� ������ ����Լ� Ż��
		if (weight >= M)
			return 0;
		else {
			j = i + 1;
			bound = totprofit;
			totweight = weight;
			// ���԰� ����ϴµ� ���� ���� �������� ���Խ�Ű�� ����
			while (j <= n && totweight + item[j].weight <= M) {
				totweight = totweight + item[j].weight;
				bound = bound + item[j].profit;
				j++;
			}
			k = j;
			if (k <= n)
				bound = bound + (M - totweight) * (float) item[k].p_w;
			return (bound > max_profit) == true ? 1 : 0;
		}
	}

	// backtracking �Լ�
	static void backtracking(int i, int profit, int weight, Item[] item) {
		if (weight <= M && profit >= max_profit) {
			max_profit = profit;
			// include �迭�� bestset�� ����
			System.arraycopy(include, 0, bestset, 0, n + 1);
		}
		// ���� �������� �����ϴٸ�
		if (promising(i, weight, item) == 1) {
			include[i + 1] = 1; // ���Խ�Ų��
			totprofit = profit + item[i + 1].profit;
			backtracking(i + 1, profit + item[i + 1].profit, weight + item[i + 1].weight, item);
			include[i + 1] = 0;
			totprofit = profit;
			backtracking(i + 1, profit, weight, item);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName = "p2data6.txt"; // �Է� ���� �̸� �Է�
		Scanner s = null;
		try {
			s = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(fileName);
		n = s.nextInt(); // ������ ����
		System.out.println("n = " + n);
		Item[] item = new Item[n + 1]; // ��ü �迭 item ����
		for (int i = 1; i <= n; i++) {
			item[i] = new Item(); // ��ü ����
		}
		System.out.print("pi = ");
		for (int i = 1; i <= n; i++) {
			// �� item ���� profit�� index ����
			item[i].profit = s.nextInt();
			item[i].index = i;
			System.out.print(item[i].profit + " ");
		}
		System.out.println();
		System.out.print("wi = ");
		for (int i = 1; i <= n; i++) {
			item[i].weight = s.nextInt();
			System.out.print(item[i].weight + " ");
		}
		System.out.println();
		System.out.print("pi/wi = ");
		for (int i = 1; i <= n; i++) {
			item[i].p_w = (double) item[i].profit / item[i].weight;
			System.out.printf("%.1f ", item[i].p_w);
		}
		System.out.println();
		M = s.nextInt();
		System.out.println("M = " + M);
		System.out.println();
		System.out.println("(1) Dynamic Programming");
		double startTimeOfDy = System.nanoTime();
		dynamic_pro(item, M);
		double endTimeOfDy = System.nanoTime();
		double measuredTimeOfDy = (endTimeOfDy - startTimeOfDy) / (double) 1000000;
		System.out.println("\t" + "The execution time is " + measuredTimeOfDy + " milliseconds.");
		System.out.println();
		System.out.println("(2) Backtracking (or FIFO Branch-and-Bound)");
		// backtracking�� �ϱ� �ռ� ��ó�� �������� item�� pi/wi�� ū ������ �������� ����
		quickSort(item, 1, item.length - 1);
		bestset = new int[n + 1];
		include = new int[n + 1];
		int[] result = new int[n + 1]; // bestset �迭�� �ٽ� �������� �ǵ��� ���� ������ �迭
										// result ����
		long startTimeOfBT = System.nanoTime();
		backtracking(0, 0, 0, item);
		long endTimeOfBT = System.nanoTime();
		// backtracking���� ���� max_profit ���
		System.out.println("\t" + "The maximum profit is $" + max_profit + ".");
		for (int i = 1; i <= item.length - 1; i++) {
			result[item[i].index] = bestset[i];
		}
		// �� ���� ���
		System.out.print("\t" + "The solution vector X = (");
		for (int u = 1; u <= item.length - 1; u++) {
			if (u == item.length - 1)
				System.out.println(result[u] + ")");
			else
				System.out.print(result[u] + ", ");
		}
		double measuredTimeOfBT = (endTimeOfBT - startTimeOfBT) / (double) 1000000;
		System.out.println("\t" + "The execution time is " + measuredTimeOfBT + " milliseconds.");
	}
}