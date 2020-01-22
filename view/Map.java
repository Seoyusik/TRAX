package view;
import java.awt.Dimension;
import javax.swing.*;
import action.*;

public class Map {
	public final int width = 64;
	public final int height = 64;
	public Tile[][] tiles; 
	// 	public static Tile[][] tiles; //for reset

	public static int gameData[][]=new int[64][64];//타일의 상태만 기록하는 맵
	public static int cost[][]=new int[64][64];//위치지정을 위한 가중치를 저장하는 배열
	public static int last_x=32, last_y=32;
	
	public int end_lx, end_rx, end_uy,end_dy;
	
	//맵에 타일을 추가함
	public Map() {
		int i, j;
		this.tiles = new Tile[this.height][this.width];
		
		for(i=0; i<this.height; i++) {
			for(j=0; j<this.width; j++) {
				this.tiles[i][j] = new Tile(j, i, this);
				this.tiles[i][j].status = 0; //타일의 초기 상태
				this.tiles[i][j].addMouseListener(new MouseAction()); //마우스 리스너를 등록함
				this.tiles[i][j].setIcon(new ImageIcon("./res/0.png")); //초기 잔디 이미지
				this.tiles[i][j].setPreferredSize(new Dimension(44,44));
				this.tiles[i][j].setBorderPainted(false);
			}
		}

		if(TileJudge.first==1) {//아직 아무것도 놓이지 않았을경우 32,32위치른 다른색으로 표현
			this.tiles[32][32].setIcon(new ImageIcon("./res/0_tmp.png")); 
		}
	}
}
