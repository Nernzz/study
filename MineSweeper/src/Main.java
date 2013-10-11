import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.Timer;


/***
 *
 * @author s1023092
 */

class Main extends JFrame implements ActionListener{
	/**
	 *
	 */
	private static final long serialVersionUID = 1764198284798537056L;

	public static void main(String[] args){
		new Main();
	}

	private int time;
	Container container;
	JPanel panel;
	JPanel panel2;
	JLabel label;
	Timer timer;
	JButton b;

	Main() {
		/*--- MenuBar ---*/
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		JMenuItem menuOpen = new JMenuItem("Open");
		JMenuItem menuExit = new JMenuItem("Exit");
		JMenu menuView = new JMenu("View");
		JCheckBoxMenuItem menuTool = new JCheckBoxMenuItem("Tool Bar");
		JMenu menuSize = new JMenu("Size");
		JMenuItem menuSizeLarge = new JMenuItem("Large");
		JMenuItem menuSizeSmall = new JMenuItem("Small");

		menuFile.setMnemonic('F');
		menuOpen.setMnemonic('O');
		menuExit.setMnemonic('x');
		menuView.setMnemonic('V');
		menuTool.setMnemonic('T');
		menuSize.setMnemonic('S');
		menuSizeLarge.setMnemonic('L');
		menuSizeSmall.setMnemonic('S');

		menuOpen.addActionListener(this);
		menuExit.addActionListener(this);
		menuTool.addActionListener(this);
		menuSizeLarge.addActionListener(this);
		menuSizeSmall.addActionListener(this);

		getRootPane().setJMenuBar(menuBar);
		menuBar.add(menuFile);
		menuFile.add(menuOpen);
		menuFile.add(menuExit);
		menuBar.add(menuView);
		menuView.add(menuTool);
		menuView.add(menuSize);
		menuSize.add(menuSizeLarge);
		menuSize.add(menuSizeSmall);

		/*--- Minefield ---*/
		container = getContentPane();
		container.setLayout(null);
		container.setBounds(0, 0, 380, 440);
		panel = new JPanel(new GridLayout(9,9));
		panel.setBounds(0, 0, 380, 400);
	    panel2 = new JPanel();
		panel2.setBounds(0, 400, 380, 40);
		label = new JLabel("Time:0");
	    panel2.add(label);
		JButton b[][] = new JButton[9][9];
	    for(int j = 0; j < 9; j++){
			for(int i = 0; i < 9; i++){
				b[i][j] = new JButton("");
		    	b[i][j].addActionListener(new MapClick(i,j,b));
		    	b[i][j].addMouseListener(new MapClick(i,j,b));
	    		panel.add(b[i][j]);
		    }
	    }
	    container.add(panel);
	    container.add(panel2);

	    timer = new Timer(1000, this);
		timer.start();

		/*--- Property ---*/
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("MineSweeper");
		setSize(388, 490);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e){ // メニューバー
		int f = MapClick.getF();
		if(f == 1)
			label.setText("Time:"+ ++time);
		if(f == 2)
			timer.stop();
	}
}

class Map{
	/*--- property ---*/
	private static int map[][][];

	/*--- constructor ---*/
	Map(){

	}

	/*--- method ---*/
	void mapGenerate(){
		int map_x = 0,map_y = 0;
		switch(0){
		case 0:
			map_x = 9;	map_y = 9;	break;
		case 1:
			map_x = 16;	map_y = 16;	break;
		case 2:
			map_x = 30;	map_y = 16;	break;
		}
		map = new int[map_x][map_y][2];				// x → row(行)		y → column(列)		2 → 開いているか(booleanで別に作るべきかも)
	}

	void bombGenerate(int x, int y){
		int bomb = 0,bomb_x,bomb_y;
		switch(map.length){
		case 9:
			bomb = 10;	break;
		case 16:
			bomb = 40;	break;
		case 30:
			bomb = 99;	break;
		}
		Random rand = new Random();
		for(int i = 0;	i < bomb; i++){
			do{
				bomb_x = rand.nextInt(map.length);
				bomb_y = rand.nextInt(map[0].length);
			}while((bomb_x < x + 2 && bomb_x > x - 2) && (bomb_y < y + 2 && bomb_y > y - 2));		// 開始時の座標の周りには爆弾はない
			if(map[bomb_x][bomb_y][0] < 9){
				bombSetup(map,bomb_x,bomb_y);
			}else{
				i--;
			}
		}
	}

	void bombSetup(int[][][] map,int x,int y){			// 周りの爆弾の数は8を超えることはありえないので,爆弾は9以上のもの
		if(x != 0 && y != 0)				map[x-1][y-1][0]++;		if(y != 0)					map[x][y-1][0]++;		if(x != map.length - 1 && y != 0)					map[x+1][y-1][0]++;
		if(x != 0)							map[x-1][y][0]++;									map[x][y][0] = 9;		if(x != map.length - 1)								map[x+1][y][0]++;
		if(x != 0 && y != map[0].length - 1)map[x-1][y+1][0]++;		if(y != map[0].length - 1)	map[x][y+1][0]++;		if(x != map.length - 1 && y != map[0].length - 1)	map[x+1][y+1][0]++;
	}

	static void mapDisplay(int[][][] map, JButton[][] b){	// マップ更新
	    for(int j = 0; j < 9; j++){
			for(int i = 0; i < 9; i++){
				if(MapClick.getF() == 2 && map[i][j][1] == 0 && map[i][j][0] >= 9){
					b[i][j].setText("*");
					b[i][j].setEnabled(false);
				}
				if(map[i][j][1] == 1){
					if(map[i][j][0] == 0){
						b[i][j].setText("");
						b[i][j].setEnabled(false);
					}else if(map[i][j][0] < 9){
						b[i][j].setText(""+map[i][j][0]);
						b[i][j].setEnabled(false);
					}else if(b[i][j].getText() != "P"){
						b[i][j].setText("*");
						b[i][j].setEnabled(false);
					}
				}
			}
	    }
	}

	/*--- getter, setter ---*/
	static int[][][] getMap(){
		return map;
	}

	static void setMap(int[][][] map){
		Map.map = map;
	}
}

class MapClick extends MouseAdapter implements ActionListener{
	/*--- property ---*/
	private int x,y;
	private static int f = 0;
	static JButton b[][];

	/*--- constructor ---*/
	MapClick(int x, int y, JButton b[][]) {
		this.x = x;
		this.y = y;
		MapClick.b = b;
	}

	/*--- method ---*/
	public void actionPerformed(ActionEvent e){
		System.out.println(e.getActionCommand());
		if(e.getActionCommand() == ""){
			if(f == 0){
				Map m = new Map();
				m.mapGenerate();
				m.bombGenerate(x,y);
				f++;
			}
			int[][][] map = Map.getMap();
			mapOpen(map);
			decision(map);
		}
	}

	public void mouseClicked(MouseEvent e) {
		putFlag(e);
		aroundOpen(e);
	}

	void mapOpen(int[][][] map){
		map[x][y][1] = 1;
		if(map[x][y][0] == 0){
			if(x != 0 && y != 0 && map[x-1][y-1][1] == 0)								mapOpen(map,x-1,y-1);		// 左上
			if(y != 0 && map[x][y-1][1] == 0)											mapOpen(map,x,y-1);		// 上
			if(x != map.length - 1 && y != 0 && map[x+1][y-1][1] == 0)					mapOpen(map,x+1,y-1);		// 右上
			if(x != 0 && map[x-1][y][1] == 0 && map[x-1][y][1] == 0)					mapOpen(map,x-1,y);		// 左
			if(x != map.length - 1 && map[x+1][y][1] == 0)								mapOpen(map,x+1,y);		// 右
			if(x != 0 && y != map[0].length - 1 && map[x-1][y+1][1] == 0)				mapOpen(map,x-1,y+1);		// 左下
			if(y != map[0].length - 1 && map[x][y+1][1] == 0)							mapOpen(map,x,y+1);		// 下
			if(x != map.length - 1 && y != map[0].length - 1 && map[x+1][y+1][1] == 0)	mapOpen(map,x+1,y+1);		// 右下
		}
		Map.setMap(map);
	}

	void mapOpen(int[][][] map,int x, int y){
		map[x][y][1] = 1;
		if(map[x][y][0] == 0){
			if(x != 0 && y != 0 && map[x-1][y-1][1] == 0)								mapOpen(map,x-1,y-1);		// 左上
			if(y != 0 && map[x][y-1][1] == 0)											mapOpen(map,x,y-1);		// 上
			if(x != map.length - 1 && y != 0 && map[x+1][y-1][1] == 0)					mapOpen(map,x+1,y-1);		// 右上
			if(x != 0 && map[x-1][y][1] == 0 && map[x-1][y][1] == 0)					mapOpen(map,x-1,y);		// 左
			if(x != map.length - 1 && map[x+1][y][1] == 0)								mapOpen(map,x+1,y);		// 右
			if(x != 0 && y != map[0].length - 1 && map[x-1][y+1][1] == 0)				mapOpen(map,x-1,y+1);		// 左下
			if(y != map[0].length - 1 && map[x][y+1][1] == 0)							mapOpen(map,x,y+1);		// 下
			if(x != map.length - 1 && y != map[0].length - 1 && map[x+1][y+1][1] == 0)	mapOpen(map,x+1,y+1);		// 右下
		}
	}

	static void mapInitialize(){
	    for(int j = 0; j < 9; j++){
			for(int i = 0; i < 9; i++){
				b[i][j].setText("");
				b[i][j].setEnabled(true);
			}
		}
		MapClick.setF(0);
	}

	void decision(int[][][] map){
		if(map[x][y][0] > 8){			// 負け
			f = 2;
			Map.mapDisplay(map,b);
			Dialog dlg = new Dialog(null,"あなたは負けました");
			dlg.setVisible(true);
		}else if(winDecision(map)){		// 勝ち
			f = 2;
			Map.mapDisplay(map,b);
			Dialog dlg = new Dialog(null,"あなたは勝ちました");
			dlg.setVisible(true);
		}else
			Map.mapDisplay(map,b);
	}

	boolean winDecision(int[][][] map){
		for(int j = 0; j < map[0].length; j++){
			for(int i = 0; i < map.length; i++){
				if(map[i][j][1] == 0 && map[i][j][0] < 9)
					return false;
			}
		}
		return true;
	}

	void putFlag(MouseEvent e){
        if ( e.getModifiers() == MouseEvent.BUTTON3_MASK ){
        	if(f != 0){
        		int[][][] map = Map.getMap();
	        	if(map[x][y][1] == 0){
	        		map[x][y][1] = 1;
					b[x][y].setText("P");
					b[x][y].setEnabled(false);
	        	}else if(b[x][y].getText() == "P"){
	        		map[x][y][1] = 0;
					b[x][y].setText("");
					b[x][y].setEnabled(true);
	        	}
        	}
        }
	}

	void aroundOpen(MouseEvent e){
        if ( e.getModifiers() == MouseEvent.BUTTON2_MASK ){
        	if(f != 0){
        		int[][][] map = Map.getMap();
	        	if(aroundBomb(map) != 0 && aroundFlag(map) != 0 && aroundBomb(map) == aroundFlag(map)){
	        		for(int j = y-1; j <= y+1; j++){
	        			for(int i = x-1; i <= x+1; i++){
	        				if(i >= 0 && i < map.length && j >= 0 && j < map[0].length){
		        				mapOpen(map,i,j);
		        			}
	        			}
	        		}
	        		decision(map);
	        	}
        	}
        }
	}

	int aroundBomb(int[][][] map){
		int bomb = 0;
		for(int j = y-1; j <= y+1; j++){
			for(int i = x-1; i <= x+1; i++){
				if(i >= 0 && i < map.length && j >= 0 && j < map[0].length){
					if(map[i][j][0] >= 9)
						bomb++;
				}
			}
		}
		return bomb;
	}


	int aroundFlag(int[][][] map){
		int flag = 0;
		for(int j = y-1; j <= y+1; j++){
			for(int i = x-1; i <= x+1; i++){
				if(i >= 0 && i < map.length && j >= 0 && j < map[0].length){
					if(b[i][j].getText() == "P")
						flag++;
				}
			}
		}
		return flag;
	}

	/*--- getter, setter ---*/
	static int getF(){
		return f;
	}

	static void setF(int f){
		MapClick.f = f;
	}
}

class Dialog extends JDialog implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = -6125220581096443687L;

	Dialog(Frame owner, String s){
		super(owner);
		getContentPane().setLayout(new FlowLayout());

		JButton btn = new JButton("終了");
		JButton btn2 = new JButton("もう一度プレイ");
		btn.addActionListener(this);
		btn2.addActionListener(this);
		getContentPane().add(btn);
		getContentPane().add(btn2);

		setTitle(s);
		setSize(200, 140);
		setLocationRelativeTo(null);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand() == "終了"){
			System.exit(0);
		}else if(e.getActionCommand() == "もう一度プレイ"){
			setVisible(false);
		}
		MapClick.mapInitialize();
	}
}