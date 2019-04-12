

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ShortestPath {
	static final int MAX_INT = 2147483647;

	// 각 정점까지의 최단 경로를 저장하는 배열 dist에서 가장 작은 값을 가진 요소의 index를 찾는 함수  
	static int getSmallIndex(int[] dist, int[] visited) {
		int min = MAX_INT;  // 먼저 MAX_INT 값으로  min 초기화
		int index = -1;     // 가장 작은 요소의 index를 저장할 변수 초기화
		for (int i = 0; i < dist.length; i++) {
			// min보다 작고 해당 노드가 방문했던 노드가 아닌 경우 min 값과 index 값 갱신
			if (min > dist[i] && visited[i] == 0) { 
				min = dist[i];
				index = i;
			}
		}
		return index;
	}

	static void dijkstra(int start, int[] visited, int[] dist, int[][] weight) {
		for (int i = 0; i < dist.length - 1; i++) {  // 노드의 수 - 1 만큼 반복
			int current = getSmallIndex(dist, visited);  // 가장 짧은 거리를 가진 요소의 인덱스를 current에 저장
			visited[current] = 1;
			for (int j = 0; j < dist.length; j++) {
				if (visited[j] == 0) {
					if (dist[current] + (long) weight[current][j] < dist[j])  // 오버플로우 발생을 막기 위해 (long)으로 타입 캐스팅해준다.
						dist[j] = dist[current] + weight[current][j];
				}
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName = "graph6.txt";  // 파일 이름 지정
		Scanner s = null;
		try {
			s = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {     // 파일 불러오기를 실패했을 경우
			System.out.println(e.getMessage());
		}
		int numOfNode = s.nextInt();    // 파일로부터 노드의 개수 가져와 저장
		int start = s.nextInt();        // 파일로부터 시작 노드 가져와 저장
		// 인접 행렬 초기화
		int[][] weight = new int[numOfNode][numOfNode];
		for (int i = 0; i < numOfNode; i++) {
			for (int j = 0; j < numOfNode; j++) {
				weight[i][j] = s.nextInt();
			}
		}
		int[] visited = new int[numOfNode];  // 방문했던 노드를 저장하기 위한 배열
		int[] dist = new int[numOfNode];     // 각 정점까지의 최단 경로를 저장하기 위한 배열
		visited[start - 1] = 1;
		// 시작 노드로부터 각 노드까지의 거리를 dist 배열에 저장해 초기화한다.
		for (int i = 0; i < dist.length; i++)
			dist[i] = weight[start - 1][i];

		dijkstra(start, visited, dist, weight);
		// 결과 출력
		System.out.println("입력파일 " + fileName);
		System.out.print("정점 v" + start + "(으)로부터 각 정점까지의 최단경로 = ( ");
		for (int i = 0; i < dist.length; i++)
			System.out.print(dist[i] + " ");
		System.out.println(")");

	}

}
