

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ShortestPath {
	static final int MAX_INT = 2147483647;

	// �� ���������� �ִ� ��θ� �����ϴ� �迭 dist���� ���� ���� ���� ���� ����� index�� ã�� �Լ�  
	static int getSmallIndex(int[] dist, int[] visited) {
		int min = MAX_INT;  // ���� MAX_INT ������  min �ʱ�ȭ
		int index = -1;     // ���� ���� ����� index�� ������ ���� �ʱ�ȭ
		for (int i = 0; i < dist.length; i++) {
			// min���� �۰� �ش� ��尡 �湮�ߴ� ��尡 �ƴ� ��� min ���� index �� ����
			if (min > dist[i] && visited[i] == 0) { 
				min = dist[i];
				index = i;
			}
		}
		return index;
	}

	static void dijkstra(int start, int[] visited, int[] dist, int[][] weight) {
		for (int i = 0; i < dist.length - 1; i++) {  // ����� �� - 1 ��ŭ �ݺ�
			int current = getSmallIndex(dist, visited);  // ���� ª�� �Ÿ��� ���� ����� �ε����� current�� ����
			visited[current] = 1;
			for (int j = 0; j < dist.length; j++) {
				if (visited[j] == 0) {
					if (dist[current] + (long) weight[current][j] < dist[j])  // �����÷ο� �߻��� ���� ���� (long)���� Ÿ�� ĳ�������ش�.
						dist[j] = dist[current] + weight[current][j];
				}
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName = "graph6.txt";  // ���� �̸� ����
		Scanner s = null;
		try {
			s = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {     // ���� �ҷ����⸦ �������� ���
			System.out.println(e.getMessage());
		}
		int numOfNode = s.nextInt();    // ���Ϸκ��� ����� ���� ������ ����
		int start = s.nextInt();        // ���Ϸκ��� ���� ��� ������ ����
		// ���� ��� �ʱ�ȭ
		int[][] weight = new int[numOfNode][numOfNode];
		for (int i = 0; i < numOfNode; i++) {
			for (int j = 0; j < numOfNode; j++) {
				weight[i][j] = s.nextInt();
			}
		}
		int[] visited = new int[numOfNode];  // �湮�ߴ� ��带 �����ϱ� ���� �迭
		int[] dist = new int[numOfNode];     // �� ���������� �ִ� ��θ� �����ϱ� ���� �迭
		visited[start - 1] = 1;
		// ���� ���κ��� �� �������� �Ÿ��� dist �迭�� ������ �ʱ�ȭ�Ѵ�.
		for (int i = 0; i < dist.length; i++)
			dist[i] = weight[start - 1][i];

		dijkstra(start, visited, dist, weight);
		// ��� ���
		System.out.println("�Է����� " + fileName);
		System.out.print("���� v" + start + "(��)�κ��� �� ���������� �ִܰ�� = ( ");
		for (int i = 0; i < dist.length; i++)
			System.out.print(dist[i] + " ");
		System.out.println(")");

	}

}
