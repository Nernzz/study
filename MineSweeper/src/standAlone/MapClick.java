package standAlone;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;

class MapClick extends MouseAdapter implements ActionListener{
	/*--- property ---*/
	private int x,y;
	private static int f = 0;

	/*--- constructor ---*/
	MapClick(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/*--- method ---*/
	@Override
	public void actionPerformed(ActionEvent e){
		if(f == 0){
			Map m = new Map();
			m.mapGenerate();
			m.bombGenerate(x,y);
			f++;
			Main.timer.start();
		}
		int[][][] map = Map.getMap();
		mapOpen(map);
		decision(map);
	}

	@Override
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
		JButton b[][] = Main.getButton();
		JLabel label = Main.getLabel();
	    for(int j = 0; j < 9; j++){
			for(int i = 0; i < 9; i++){
				b[i][j].setText("");
				b[i][j].setEnabled(true);
			}
		}
		MapClick.setF(0);
		Time.setTime(0);
		label.setText("Time:"+ Time.getTime());
	}

	void decision(int[][][] map){
		if(map[x][y][0] > 8){			// 負け
			f = 2;
			Map.mapDisplay(map);
			Dialog dlg = new Dialog(null,"あなたは負けました");
			dlg.setVisible(true);
		}else if(winDecision(map)){		// 勝ち
			f = 2;
			Map.mapDisplay(map);
			Dialog dlg = new Dialog(null,"あなたは勝ちました");
			dlg.setVisible(true);
		}else
			Map.mapDisplay(map);
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
        		JButton b[][] = Main.getButton();
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
		JButton b[][] = Main.getButton();
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