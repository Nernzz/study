package standAlone;

import java.util.Random;

import javax.swing.JButton;

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

	static void mapDisplay(int[][][] map){	// マップ更新
		JButton b[][] = Main.getButton();
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
						b[i][j].setText("" + map[i][j][0]);
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