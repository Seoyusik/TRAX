package action;

import javax.swing.JOptionPane;

import defs.*;
import view.*;

public class WinGame {
	public static Tile u,p;
	public static tColor color=null;
	private static int hMin,hMax,sMin,sMax; 
	private static int hMin_x,hMax_x,sMin_y,sMax_y; //직선승리 조건을 위한 타일의 상하좌우의 최대 최소를 저장하는 변수선언
	
	public static boolean GameEnd(Tile t) {//승리 검사를 위해 놓인타일을 기준으로 이어진색을 따라감 
		
		//현재턴에 놓여진타일의 상하좌우의 타일 상태 저장
		int up = t.getAdjTile(tDirection.UP);
		int down = t.getAdjTile(tDirection.DOWN);
		int left = t.getAdjTile(tDirection.LEFT);
		int right = t.getAdjTile(tDirection.RIGHT);
		boolean x=false;
		
		
		u=t;
		hMin=t.y; //제일 높이 있는타일의 y좌표
		hMax=t.y; //제일 아래 있는 타일의 y좌표
		sMin=t.x; //제일 왼쪽에 있는타일의 x좌표
		sMax=t.x; //네일 오른쪽에 있는 타일의 x좌표
		
		hMin_x=t.x; //제일 높이 있는타일의 x좌표
		hMax_x=t.x; //제일 아래 있는 타일의 x좌표
		sMin_y=t.y; //제일 왼쪽에 있는타일의 y좌표
		sMax_y=t.y; //네일 오른쪽에 있는 타일의 y좌표
		
		//색이이어진 타일을 따라감
		//system.out.println(""+t.x+","+t.y+"");
		if(TileJudge.colors[t.status][0] == TileJudge.colors[up][2]) {
			//system.out.println("up");
			p=t;
			x|=trace(t.map.tiles[t.y-1][t.x],TileJudge.colors[t.status][0]);
		}
		if(TileJudge.colors[t.status][2] == TileJudge.colors[down][0]) {
			//system.out.println("down");
			p=t;
			x|=trace(t.map.tiles[t.y+1][t.x],TileJudge.colors[t.status][2]);
		}
		if(TileJudge.colors[t.status][3] == TileJudge.colors[left][1]) {
			//system.out.println("left");
			p=t;
			x|=trace(t.map.tiles[t.y][t.x-1],TileJudge.colors[t.status][3]);
		}
		if(TileJudge.colors[t.status][1] == TileJudge.colors[right][3]) {
			//system.out.println("right");
			p=t;
			x|=trace(t.map.tiles[t.y][t.x+1],TileJudge.colors[t.status][1]);
		}
		//system.out.println("sMin="+sMin+" sMin_y="+sMin_y+","+" sMax="+sMax+" sMax_y="+sMax_y);
		//system.out.println("hMin="+hMin+" hMin_x="+hMin_x+","+" hMax="+hMax+" hMax_x="+hMax_x);
		//system.out.println("---------");
		
		return x;
	}


	private static boolean trace(Tile t,tColor c) { //이어진 타일의 색을 따라가면서 처음시작한 타일을 만나면 승리함
		//직선승리조건은 타일을 따라가며 상하, 좌우의 차이가 7이상일때 승리
		
		
		int up = t.getAdjTile(tDirection.UP);
		int down = t.getAdjTile(tDirection.DOWN);
		int left = t.getAdjTile(tDirection.LEFT);
		int right = t.getAdjTile(tDirection.RIGHT);
		int y_zeroCnt=0,x_zeroCnt=0; //가장 끝타일인지 검사를 위한 변수
		
		boolean x=false;
		
		tDirection direc;
		
		direc=dSeach(t,c);
		
		
		if(t==u) { //타일이 만나고 처음 시작한 색과 같으면 승리
			color=c;
			return true;
		}
		
		//타일의 상하좌우의 최대 최소 위치 갱신
		if(t.x>=sMax) {
			sMax=t.x;
			sMax_y=t.y;
		}
		if(t.x<=sMin) {
			sMin=t.x;
			sMin_y=t.y;
		}
		if(t.y>=hMax) {
			hMax=t.y;
			hMax_x=t.x;
		}
		if(t.y<=hMin) {
			hMin=t.y;
			hMin_x=t.x;
		}
		
		//이어진타일을 계속 따라감
		if(TileJudge.colors[t.status][0]==TileJudge.colors[up][2]&&direc==tDirection.UP) {
			//system.out.println("up");
			p=t;
			x=trace(t.map.tiles[t.y-1][t.x],TileJudge.colors[t.status][0]);
		}
		else if( TileJudge.colors[t.status][2]==TileJudge.colors[down][0]&&direc==tDirection.DOWN) {
			//system.out.println("down");
			p=t;
			x=trace(t.map.tiles[t.y+1][t.x],TileJudge.colors[t.status][2]);
		}
		else if(TileJudge.colors[t.status][3]==TileJudge.colors[left][1]&&direc==tDirection.LEFT) {
			//system.out.println("left");
			p=t;
			x=trace(t.map.tiles[t.y][t.x-1],TileJudge.colors[t.status][3]);
		}
		else if( TileJudge.colors[t.status][1]==TileJudge.colors[right][3]&&direc==tDirection.RIGHT) {
			//system.out.println("right");
			p=t;
			x=trace(t.map.tiles[t.y][t.x+1],TileJudge.colors[t.status][1]);
		}
		
		if(sMax-sMin>=7) { //가로 직선승리
			if(Map.last_x==sMin) {
				sMin_y=Map.last_y;
			}
			else if(Map.last_x==sMax) {
				sMax_y=Map.last_y;
			}
			//system.out.printf("gamdData[%d][%d]=%d gamdData[%d][%d]=%d\n",sMin_y,sMin,Map.gameData[sMin_y][sMin],sMax_y,sMax,Map.gameData[sMax_y][sMax]);
			
			for(int i=0;i<64;i++) { //맨끝타일인지 확인하기위해 한칸 전줄의 타일이 0번타일이 64개인지 검사함
				if(sMin!=0||sMax!=63) {
					if((Map.gameData[i][sMin-1]==0)&&(Map.gameData[i][sMax+1]==0)) {
						y_zeroCnt++;
					}
				}
				else {
					//맨끝타일은 다음줄이 없으므로 따로 검사함
					if(sMin==0) {
						if((Map.gameData[i][sMax+1]==0)) {
							x_zeroCnt++;
						}
					}
					else if(sMax==63) {
						if((Map.gameData[i][sMin-1]==0)) {
							x_zeroCnt++;
						}
					}
				}
			}
			if((c==tColor.BLACK)
					&&(y_zeroCnt==64)//맨끝타일이고
					//열린방향 타일일때
					&&(Map.gameData[sMin_y][sMin]==1||Map.gameData[sMin_y][sMin]==4||Map.gameData[sMin_y][sMin]==5)
					&&(Map.gameData[sMax_y][sMax]==2||Map.gameData[sMax_y][sMax]==3||Map.gameData[sMax_y][sMax]==5)) {
				//system.out.printf("gamdData[%d][%d]=%d gamdData[%d][%d]=%d\n",sMin_y,sMin,Map.gameData[sMin_y][sMin],sMax_y,sMax,Map.gameData[sMax_y][sMax]);
				color=c;
				//system.out.println(c);
				return true;
			}
			else if((c==tColor.WHITE)
					&&(y_zeroCnt==64)//맨끝타일이고
					//열린방향 타일일때
					&&(Map.gameData[sMin_y][sMin]==2||Map.gameData[sMin_y][sMin]==3||Map.gameData[sMin_y][sMin]==6)
					&&(Map.gameData[sMax_y][sMax]==1||Map.gameData[sMax_y][sMax]==4||Map.gameData[sMax_y][sMax]==6)) {
				//system.out.printf("gamdData[%d][%d]=%d gamdData[%d][%d]=%d\n",sMin_y,sMin,Map.gameData[sMin_y][sMin],sMax_y,sMax,Map.gameData[sMax_y][sMax]);
				color=c;
				//system.out.println(c);
				return true;
			}
		}
		else if(hMax-hMin>=7) { //세로직선 승리
			if(Map.last_y==hMin) {
				hMin_x=Map.last_x;
			}
			else if(Map.last_y==hMax) {
				hMax_x=Map.last_x;
			}
			//system.out.printf("gamdData[%d][%d]=%d gamdData[%d][%d]=%d\n",hMin,hMin_x,Map.gameData[hMin][hMin_x],hMax,hMax_x,Map.gameData[hMax][hMax_x]);
			for(int i=0;i<64;i++) {
				if(hMin!=0||hMax!=63) {//맨끝타일인지 확인하기위해 한칸 전줄의 타일이 0번타일이 64개인지 검사함
					if((Map.gameData[hMin-1][i]==0)&&(Map.gameData[hMax+1][i]==0)) {
						x_zeroCnt++;
					}
				}
				else {//맨끝타일은 다음줄이 없으므로 따로 검사함
					if(hMin==0) {
						if((Map.gameData[hMax+1][i]==0)) {
							x_zeroCnt++;
						}
					}
					else if(hMax==63) {
						if((Map.gameData[hMin-1][i]==0)) {
							x_zeroCnt++;
						}
					}
				}
			}
			if((c==tColor.BLACK)
					&&(x_zeroCnt==64)//맨끝타일이고
									//열린방향 타일일때
					&&(Map.gameData[hMin][hMin_x]==1||Map.gameData[hMin][hMin_x]==2||Map.gameData[hMin][hMin_x]==6)
					&&(Map.gameData[hMax][hMax_x]==3||Map.gameData[hMax][hMax_x]==4||Map.gameData[hMax][hMax_x]==6)) {
			//system.out.printf("gamdData[%d][%d]=%d gamdData[%d][%d]=%d\n",sMin_y,sMin,Map.gameData[sMin_y][sMin],sMax_y,sMax,Map.gameData[sMax_y][sMax]);
			color=c;
			
			//system.out.println(c);
			return true;
			}
			else if((c==tColor.WHITE)
					&&(x_zeroCnt==64)//맨끝타일이고
					//열린방향 타일일때
					&&(Map.gameData[hMin][hMin_x]==3||Map.gameData[hMin][hMin_x]==4||Map.gameData[hMin][hMin_x]==5)
					&&(Map.gameData[hMax][hMax_x]==1||Map.gameData[hMax][hMax_x]==2||Map.gameData[hMax][hMax_x]==5)) {
				//system.out.printf("gamdData[%d][%d]=%d gamdData[%d][%d]=%d\n",sMin_y,sMin,Map.gameData[sMin_y][sMin],sMax_y,sMax,Map.gameData[sMax_y][sMax]);
				color=c;
				//system.out.println(c);
				return true;
			}
		}
		return x;
	}
	
	private static tDirection dSeach(Tile t,tColor c) {//검사할 타음타일을 지정
		
		int up = t.getAdjTile(tDirection.UP);
		int down = t.getAdjTile(tDirection.DOWN);
		int left = t.getAdjTile(tDirection.LEFT);
		int right = t.getAdjTile(tDirection.RIGHT);
		
		//현재 타일과 위와 연결됐을때
		if(TileJudge.colors[t.status][0] == c &&t.map.tiles[t.y-1][t.x]!=p){
			return tDirection.UP;
		}
		//아래와 연결되었을 때
		else if(TileJudge.colors[t.status][2]==c&&t.map.tiles[t.y+1][t.x]!=p) {
			return tDirection.DOWN;
		}
		
		//왼쪽과 연결되었을 때
		else if(TileJudge.colors[t.status][3] ==c&&t.map.tiles[t.y][t.x-1]!=p) {
			return tDirection.LEFT;
		}
		//오른쪽과 연결되었을 때
		else if(TileJudge.colors[t.status][1] ==c&&t.map.tiles[t.y][t.x+1]!=p) {
			return tDirection.RIGHT;
		}
		return null;
	}
	
	public static void winner(Tile t) {
		//게임이 끝났을 경우 승리 창을 띄우고 종료
		if(GameEnd(t)) {
			if(color==tColor.WHITE) {
				JOptionPane.showMessageDialog(null, "White Win");
			}
			else if (color==tColor.BLACK) {
				JOptionPane.showMessageDialog(null, "Black Win");
			}
		}
	}
}
