package view;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;
import action.*;
public class MainWindow {
	
	private static JFrame mainFrame;
	private static JScrollPane mapPanel;
	private static Map map;
	public static JTextArea textArea = new JTextArea();
	public static JScrollPane record = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		         JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	public static JPanel logPanel=new JPanel();
	public static JButton btn = new JButton("AI");
	public static int turn=0;
	
	//메인 윈도우를 만듬
	private static JFrame initWindow() {
		JFrame frame = new JFrame();
		
		frame.setSize(new Dimension(1100,800));
		frame.setTitle("Trax Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mapPanel = initMap();
		logPanel=initLog();
		logPanel.setPreferredSize(new Dimension(300, 800));

		
		frame.getContentPane().add(mapPanel);
		frame.add(logPanel, BorderLayout.EAST);
		frame.setVisible(true);
		
		return frame;
	}
	
	
	//스크롤바와 맵에 타일을 만듬
	private static JScrollPane initMap() {
		JScrollPane sp = new JScrollPane();
		JPanel pn = new JPanel();
		int x, y;
		
		map = new Map();
		pn.setLayout(new GridLayout(map.height, map.width));
		for(y=0; y<map.height; y++) {
			for(x=0; x<map.width; x++) {
				pn.add(map.tiles[y][x]);
			}
		}
		
		sp.setViewportView(pn);
		
		return sp;
	}

	//로그창을 설정한다.
	private static JPanel initLog(){
		
		JPanel logPanel=new JPanel();
		textArea.setEditable(false);
		record.setPreferredSize(new Dimension(300, 600));
		
		//AI실행을위한 버튼과 리스너 등록
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { 
				AI.setAI(map, turn);
				
				turn++;
				TileJudge.first=0;
				
				
				//AI가 놓은 타일을 기준으로 승리검사와 자동생성할수 있는 타일이 있는지 검사함
				if(turn!=1) {
					WinGame.winner(map.tiles[AI.tmp_y][AI.tmp_x]);
				}
				
				
				try {
					TileJudge.autoJudge(map.tiles[AI.tmp_y][AI.tmp_x]);
					
				}catch(Exception ex) {
					System.out.println("<!> AutoJudge Error...");
					return;
				}
				

				for(int i=0;i<63;i++) { //Ai로 놓인 타일 위치의 가중치를 0으로 변경
					for(int j=0;j<63;j++) {
						if(Map.gameData[i][j]!=0) {
							Map.cost[i][j]=0;
						}
					//	System.out.printf("%2d", Map.cost[i][j]);
					}
				//	  System.out.printf("\n");

				}
			}
			
		});
		
		logPanel.add(record);
		logPanel.add(btn);
	
		return logPanel;
		
		
	}
	
	public static void main (String[] args) {
		
		mainFrame = initWindow(); //메인 프레임을 만듬
		//스크롤바의 초기  위치
		mapPanel.getVerticalScrollBar().setValue(1030);
		mapPanel.getHorizontalScrollBar().setValue(1005);
	}
	
}