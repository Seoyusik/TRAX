package action;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import defs.*;
import view.*;

public class TileJudge {
	public static int first=1; //첫턴인지 기록하는 변수 1:첫턴 , 0:첫턴아님
	private tColor jC;
	
	//타일 종류마다 상하좌우의 색을 저장하는 2차원 배열
	static final tColor[][] colors = new tColor[][]{
		{tColor.NONE, tColor.NONE, tColor.NONE, tColor.NONE},
		{tColor.BLACK, tColor.WHITE, tColor.WHITE, tColor.BLACK},
		{tColor.BLACK, tColor.BLACK, tColor.WHITE, tColor.WHITE},
		{tColor.WHITE, tColor.BLACK, tColor.BLACK, tColor.WHITE},
		{tColor.WHITE, tColor.WHITE, tColor.BLACK, tColor.BLACK},
		{tColor.WHITE, tColor.BLACK, tColor.WHITE, tColor.BLACK},
		{tColor.BLACK, tColor.WHITE, tColor.BLACK, tColor.WHITE}
	};	
	
	//타일 조건에 맞는 타일인지 검사하는 메소드
	public static boolean isMatch(int self, int up, int down, int left, int right) {
		boolean x = true;
		if(first==0&&up==0&&down==0&&left==0&&right==0) {
			return false;
		}
		x = x && (up == -1 || up == 0 || colors[self][0] == colors[up][2]);
		x = x && (down == -1 || down == 0 || colors[self][2] == colors[down][0]);
		x = x && (left == -1 || left == 0 || colors[self][3] == colors[left][1]);
		x = x && (right == -1 || right == 0 ||  colors[self][1] == colors[right][3]);	
		return x;
	}
	
	//클릭시 다음타일을 보여주기 위한 메소드
	public static int nextTile(int self, int up, int down, int left, int right) {
		int i, t;
		
		if(first==1) { //첫클릭엔 3번하고 5번만 놓임
			if(self==0||self==3)
				return 5;
			else if(self==5)
				return 3;
		}
		
		//타일의 생성조건에맞는 타일만 보여줌
		for(i=1; i<=6; i++) {
			t = (self + i) % 7;
			if(t != 0 && isMatch(t, up, down, left, right))	
				return t;
		}
		
		return -1;
	}
	
	//마우스 좌클릭시 바뀌는 타일을 조건에 맞는 타일만 보여줌
	public static int cntNext(Tile t) {
		int self = t.status; //현재 타일상태
	
		//선택된 위치 주변의 타일상태
		int up = t.getAdjTile(tDirection.UP);
		int down = t.getAdjTile(tDirection.DOWN);
		int left = t.getAdjTile(tDirection.LEFT);
		int right = t.getAdjTile(tDirection.RIGHT);
		int i, j, cnt = 0;
		
		for(i=1; i<=6; i++) {
			j = (self + i) % 7;
			if(j != 0 && isMatch(j, up, down, left, right))  
				cnt++;	
		}
		
		return cnt;
	}
	
	public static void autoJudge(Tile t) {
		
		//놓인 타일 주변의 타일상태
		int up = t.getAdjTile(tDirection.UP);
		int down = t.getAdjTile(tDirection.DOWN);
		int left = t.getAdjTile(tDirection.LEFT);
		int right = t.getAdjTile(tDirection.RIGHT);
		Tile u; //자동생성될 타일
		int next;
		
		//상하좌우 모든 방향 검사
		if(up == 0) {//아무것도 업는경우
			u = t.map.tiles[t.y-1][t.x];
			if(cntNext(u)==1) {//다음에 가능한 타일 개수 1개일때 자동생성
				next = TileJudge.nextTile(0, 
						u.getAdjTile(tDirection.UP), 
						u.getAdjTile(tDirection.DOWN), 
						u.getAdjTile(tDirection.LEFT), 
						u.getAdjTile(tDirection.RIGHT));
				u.setStatus(next);
				//로그창에 타일의 위치와 상태를 출력
				MainWindow.textArea.append("     AutoJudge Location : "+"("+u.x+","+u.y+")"+"  Value = "+u.status+"\n");
				
				//자동완성으로 놓은 타일을 기준으로 승리판별을 하고 또 자동완성할수 있는 타일이 있는지 검사
				WinGame.winner(u);
				autoJudge(u);
			}
		}
		
		if(down == 0) {
			u = t.map.tiles[t.y+1][t.x];
			if(cntNext(u)==1) {//다음에 가능한 타일 개수 1개일때 자동생성
				next = TileJudge.nextTile(0, 
						u.getAdjTile(tDirection.UP), 
						u.getAdjTile(tDirection.DOWN), 
						u.getAdjTile(tDirection.LEFT), 
						u.getAdjTile(tDirection.RIGHT));
				u.setStatus(next);
				//로그창에 타일의 위치와 상태를 출력
				MainWindow.textArea.append("     AutoJudge Location : "+"("+u.x+","+u.y+")"+"  Value = "+u.status+"\n");

				//자동완성으로 놓은 타일을 기준으로 승리판별을 하고 또 자동완성할수 있는 타일이 있는지 검사
				WinGame.winner(u);
				autoJudge(u);
			}
		}
		
		if(left == 0) {
			u = t.map.tiles[t.y][t.x-1];
			if(cntNext(u)==1) {//다음에 가능한 타일 개수 1개일때 자동생성
				next = TileJudge.nextTile(0, 
						u.getAdjTile(tDirection.UP), 
						u.getAdjTile(tDirection.DOWN), 
						u.getAdjTile(tDirection.LEFT), 
						u.getAdjTile(tDirection.RIGHT));
				u.setStatus(next);
				//로그창에 타일의 위치와 상태를 출력
				MainWindow.textArea.append("     AutoJudge Location : "+"("+u.x+","+u.y+")"+"  Value = "+u.status+"\n");
				
				//자동완성으로 놓은 타일을 기준으로 승리판별을 하고 또 자동완성할수 있는 타일이 있는지 검사
				WinGame.winner(u);
				autoJudge(u);
			}
		}
		
		if(right == 0) {
			u = t.map.tiles[t.y][t.x+1];
			if(cntNext(u)==1) {//다음에 가능한 타일 개수 1개일때 자동생성
				next = TileJudge.nextTile(0, 
						u.getAdjTile(tDirection.UP), 
						u.getAdjTile(tDirection.DOWN), 
						u.getAdjTile(tDirection.LEFT), 
						u.getAdjTile(tDirection.RIGHT));
				u.setStatus(next);
				//로그창에 타일의 위치와 상태를 출력
				MainWindow.textArea.append("     AutoJudge Location : "+"("+u.x+","+u.y+")"+"  Value = "+u.status+"\n");
				
				//자동완성으로 놓은 타일을 기준으로 승리판별을 하고 또 자동완성할수 있는 타일이 있는지 검사
				WinGame.winner(u);
				autoJudge(u);
			}
		}
		
	}
}


