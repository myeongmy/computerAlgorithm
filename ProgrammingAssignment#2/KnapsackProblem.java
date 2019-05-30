


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// 각 물건에 대한 정보를 저장하기 위해 클래스 Item 선언(인덱스, 이윤, 무게, pi/wi 값 저장)
class Item {
	int index;
	int profit;
	int weight;
	double p_w;
}

public class KnapsackProblem {
	// 전역변수 선언
	static int n; // 물건의 개수
	static int totprofit;
	static int max_profit = 0; // backtracking시 최종 max_profit 저장할 변수 값 초기화
	static int M;
	static int[] bestset; // backtracking시 현재 최상의 조합 배열
	static int[] include;

	// 두 값 중 최대 값 고르는 함수
	static int max(int a, int b) {
		if (a > b)
			return a;
		else
			return b;
	}

	// 동적계획법 풀이 함수
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
		int result = profit[item.length - 1][M]; // 최종 max_profit 값
		System.out.println("\t" + "The maximum profit is $" + result + ".");
		// 해 벡터 구하는 과정
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
		// 해 벡터 출력
		System.out.print("\t" + "The solution vector X = (");
		for (int i = 0; i < item.length - 1; i++) {
			if (i == item.length - 2)
				System.out.println(solution_vector[i] + ")");
			else
				System.out.print(solution_vector[i] + ", ");
		}
	}

	// 내림차순 QuickSort시 필요한 partition 함수(단위 무게 당 이윤 값을 기준으로 내림차순 정렬을 해야함)
	static int partition(Item[] item, int left, int right) {
		double pivot = item[right].p_w; // pivot 값 설정
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

	// backtracking시 필요한 promising 함수
	static int promising(int i, int weight, Item[] item) {
		int j, k;
		int totweight;
		float bound;
		// weight가 담을 수 있는 최대 무게를 넘으면 재귀함수 탈출
		if (weight >= M)
			return 0;
		else {
			j = i + 1;
			bound = totprofit;
			totweight = weight;
			// 무게가 허락하는데 까지 다음 아이템을 포함시키는 과정
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

	// backtracking 함수
	static void backtracking(int i, int profit, int weight, Item[] item) {
		if (weight <= M && profit >= max_profit) {
			max_profit = profit;
			// include 배열을 bestset에 복사
			System.arraycopy(include, 0, bestset, 0, n + 1);
		}
		// 현재 아이템이 유망하다면
		if (promising(i, weight, item) == 1) {
			include[i + 1] = 1; // 포함시킨다
			totprofit = profit + item[i + 1].profit;
			backtracking(i + 1, profit + item[i + 1].profit, weight + item[i + 1].weight, item);
			include[i + 1] = 0;
			totprofit = profit;
			backtracking(i + 1, profit, weight, item);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName = "p2data6.txt"; // 입력 파일 이름 입력
		Scanner s = null;
		try {
			s = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(fileName);
		n = s.nextInt(); // 물건의 개수
		System.out.println("n = " + n);
		Item[] item = new Item[n + 1]; // 객체 배열 item 선언
		for (int i = 1; i <= n; i++) {
			item[i] = new Item(); // 객체 생성
		}
		System.out.print("pi = ");
		for (int i = 1; i <= n; i++) {
			// 각 item 마다 profit과 index 저장
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
		// backtracking을 하기 앞서 전처리 과정으로 item을 pi/wi가 큰 순서로 내림차순 정렬
		quickSort(item, 1, item.length - 1);
		bestset = new int[n + 1];
		include = new int[n + 1];
		int[] result = new int[n + 1]; // bestset 배열을 다시 원순서로 되돌린 값을 저장할 배열
										// result 선언
		long startTimeOfBT = System.nanoTime();
		backtracking(0, 0, 0, item);
		long endTimeOfBT = System.nanoTime();
		// backtracking으로 구한 max_profit 출력
		System.out.println("\t" + "The maximum profit is $" + max_profit + ".");
		for (int i = 1; i <= item.length - 1; i++) {
			result[item[i].index] = bestset[i];
		}
		// 해 벡터 출력
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