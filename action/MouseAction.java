package action;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

import defs.*;
import view.*;

public class MouseAction implements MouseListener {
	static int tmp_status = 0;
	static int tmp_x = -1, tmp_y = -1;//정적이니깐 함수가 유지 되는거고, 마우스의 클릭 위치를 저장하는 변수
	
	public void mouseClicked(MouseEvent e) {
		Tile t = (Tile) e.getSource();
		int next=0;
		
		switch(e.getButton()) {		//어떤 버튼인지 받아주는 것
		case MouseEvent.BUTTON3: // right Click
			if(tmp_x != t.x || tmp_y != t.y)	break;//이전위치가 같않을때는 그냥 나가야
			
			t.setStatus(tmp_status);//이때 확정
			
			
			//로그창에 타일의 위치와 상태를 출력
			if(MainWindow.turn%2==0) {
				if(t.status==5||t.status==6) {
					MainWindow.textArea.append("White User Set Location : "+"("+t.x+","+t.y+")"+"  Value = + (0)\n");
				}
				else if(t.status==1||t.status==3) {
					MainWindow.textArea.append("White User Set Location : "+"("+t.x+","+t.y+")"+"  Value = / (1)\n");
				}
				else if(t.status==2||t.status==4) {
					MainWindow.textArea.append("White User Set Location : "+"("+t.x+","+t.y+")"+"  Value = \\ (2)\n");
				}
			}
			else if(MainWindow.turn%2==1) {
				if(t.status==5||t.status==6) {
					MainWindow.textArea.append("Black User Set Location : "+"("+t.x+","+t.y+")"+"  Value = + (0)\n");
				}
				else if(t.status==1||t.status==3) {
					MainWindow.textArea.append("Black User Set Location : "+"("+t.x+","+t.y+")"+"  Value = / (1)\n");
				}
				else if(t.status==2||t.status==4) {
					MainWindow.textArea.append("Black User Set Location : "+"("+t.x+","+t.y+")"+"  Value = \\ (2)\n");
				}
			}
			
			

			MainWindow.turn++;
			
			//놓은 타일을 기준으로 승리판별 호출
			try {
				WinGame.winner(t);
			}
			catch(Exception ex) {
				System.out.println("<!> Win Judge Error...");
				return;
			}
			
			//첫턴인지 기록하는 변수를 0으로 바꿈
			TileJudge.first=0;
			
			try {
				TileJudge.autoJudge(t);
				
			}catch(Exception ex) {
				System.out.println("<!> AutoJudge Error...");
				return;
			}

			//타일을 놓았으므로 놓인자리의 가중치를 0으로 바꿈
			for(int i=0;i<63;i++) {
				for(int j=0;j<63;j++) {
					if(Map.gameData[i][j]!=0) {
						Map.cost[i][j]=0;
					}
				//	System.out.printf("%2d", Map.cost[i][j]);
				}
			//	  System.out.printf("\n");

			}
			
			tmp_x = -1;
			tmp_y = -1;//이전에 눌린 버튼의 위치
			
			break;
		case MouseEvent.BUTTON1: // left Click
			if(t.status != 0)	break;//타일이 있는것 그러면 무시
					//t.x현재위치
	
			if((t.x!=32||t.y!=32)&&TileJudge.first==1)
				break;
			//if(t.x<=1||t.y<=1||t.x>=66||t.y>=66)
		   		//break;
			if(t.x == tmp_x && t.y == tmp_y) {//어디를 눌렀을때  다음꺼를 보고 싶은거면 이전꺼눌리면 다음것을 찾는다
				next = TileJudge.nextTile(tmp_status, //이미 한번 했으면 임시값을 넣어준다
						t.getAdjTile(tDirection.UP), 
						t.getAdjTile(tDirection.DOWN), 
						t.getAdjTile(tDirection.LEFT), 
						t.getAdjTile(tDirection.RIGHT));
			}else{
				if(tmp_x != -1 && tmp_y != -1) {//맨처음이 아닌경우 완전 눌린경우아닌경우 0, 그전이 확정이 아닌상태
					
					t.map.tiles[tmp_y][tmp_x].setIcon(new ImageIcon("./res/0.png"));
					
				}
				next = TileJudge.nextTile(0,		//시작값이 0이니깐 0으로 설정 
						t.getAdjTile(tDirection.UP), 
						t.getAdjTile(tDirection.DOWN), 
						t.getAdjTile(tDirection.LEFT), 
						t.getAdjTile(tDirection.RIGHT));
			}
			
			if(next == -1)	return;
			
			tmp_status = next; 
			tmp_x = t.x;
			tmp_y = t.y;
			t.setIcon(new ImageIcon("./res/" + tmp_status + "_tmp.png"));
			break;
			
		default:
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		//Tile t = (Tile) e.getSource();
		
		//System.out.println("Mouse Pos : ("+t.x+","+t.y+")");
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

