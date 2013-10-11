import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

/***
 *
 * @author s1023092
 * CUI環境での完成
 * 追加項目：勝利条件、(保留：周りを開ける機能、旗立て機能) → GUI実装 → リファクタリング(先生のアドバイス後)
 */

public class cMain{
	public static void main(String[] args){
		int[][][] map;									// [][][]	行 列 状態(開いているか)
		int mapSize = 0;
		int input_x = 0,input_y = 0;
		int retry = 0;
		Scanner stdIn = new Scanner(System.in);
		PrintStream ps = new PrintStream(System.out);

		do{
			/*--- Setup ---*/
			System.out.println("マインスイーパーを始めます。");
			System.out.print("難易度を選んでください(初級:0 中級:1 上級:2)：");
			mapSize = ScanInt(mapSize,0,2,stdIn);
			map = MapGenerate(mapSize);
			MapDisplay(map,ps);
			System.out.println("最初の座標を指定(爆発しません)");
			System.out.print("x座標:");		input_x = ScanInt(input_x,0,map.length,stdIn);	// locate か x,y をどちらかにすべき → 直した
			System.out.print("y座標:");		input_y = ScanInt(input_y,0,map[0].length,stdIn);
			map = BombGenerate(map,input_x,input_y);
			map = MapOpen(map,input_x,input_y);
			MapDisplay(map,ps);

			/*--- Game Main ---*/
			do{
				System.out.println("座標を指定");
				System.out.print("x座標:");		input_x = ScanInt(input_x,0,map.length,stdIn);
				System.out.print("y座標:");		input_y = ScanInt(input_y,0,map[0].length,stdIn);
				map = MapOpen(map,input_x,input_y);
				MapDisplay(map,ps);
			}while(Decision(map,input_x,input_y));

			/*--- Game End ---*/
			map = MapOpen(map,input_x,input_y);
			MapDisplayEnd(map,ps);
			do{
				System.out.print("もう一度遊びますか？(no:0 yes:1):");	retry = stdIn.nextInt();
			}while(retry < 1 && retry > 0);
		}while(retry == 1);
	}

	static int[][][] MapGenerate(int size){
		int x = 0,y = 0;
		switch(size){
		case 0:
			x = 9;	y = 9;	break;
		case 1:
			x = 16;	y = 16;	break;
		case 2:
			x = 30;	y = 16;	break;
		}
		int[][][] map = new int[x][y][2];				// x → row(行)		y → column(列)		2 → 開いているか(booleanで別に作るべきかも)
		return map;
	}

	static void MapDisplay(int[][][] map, PrintStream ps){
		System.out.print("  ");
		for(int i = 0; i < map.length; i++)
			ps.printf("%2d",i);
		System.out.println();
		for(int j = 0; j < map[0].length; j++){			// y(列)
			ps.printf("%2d",j);
			for(int i = 0; i < map.length; i++){		// x(行)
				if(map[i][j][1] == 0){					// マス目が開いていなければ
					System.out.print("■");
				}else{
					if(map[i][j][0] == 0){
						System.out.print("  ");
					}else{
						ps.printf("%2d",map[i][j][0]);
					}
				}
			}
			System.out.println(/*	map.length +" "+ map[0].length +" "+ map[0][0].length	*/);
		}
	}

	static void MapDisplayEnd(int[][][] map, PrintStream ps){	// 爆弾をすべて表示
		System.out.print("  ");
		for(int i = 0; i < map.length; i++)
			ps.printf("%2d",i);
		System.out.println();
		for(int j = 0; j < map[0].length; j++){			// y(列)
			ps.printf("%2d",j);
			for(int i = 0; i < map.length; i++){		// x(行)
				if(map[i][j][0] > 8){
					System.out.print(" *");
				}else{
					if(map[i][j][1] == 0){
						System.out.print("■");
					}else{
						if(map[i][j][0] == 0)
							System.out.print("  ");
						else
							ps.printf("%2d",map[i][j][0]);
					}
				}
			}
			System.out.println(/*	map.length +" "+ map[0].length +" "+ map[0][0].length	*/);
		}
	}

	static int[][][] BombGenerate(int[][][] map, int x, int y){
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
				BombSetup(map,bomb_x,bomb_y);
			}else{
				i--;
			}
		}
		return map;
	}

	static int[][][] BombSetup(int[][][] map,int x,int y){			// 周りの爆弾の数は8を超えることはありえないので,爆弾は9以上のもの
		if(x != 0 && y != 0)				map[x-1][y-1][0]++;		if(y != 0)					map[x][y-1][0]++;		if(x != map.length - 1 && y != 0)					map[x+1][y-1][0]++;
		if(x != 0)							map[x-1][y][0]++;									map[x][y][0] = 9;		if(x != map.length - 1)								map[x+1][y][0]++;
		if(x != 0 && y != map[0].length - 1)map[x-1][y+1][0]++;		if(y != map[0].length - 1)	map[x][y+1][0]++;		if(x != map.length - 1 && y != map[0].length - 1)	map[x+1][y+1][0]++;
		return map;
	}

	static int[][][] MapOpen(int[][][] map, int x, int y){
		map[x][y][1] = 1;
		if(map[x][y][0] == 0){
			if(x != 0 && y != 0 && map[x-1][y-1][1] == 0)								map = MapOpen(map,x-1,y-1);		// 左上
			if(y != 0 && map[x][y-1][1] == 0)											map = MapOpen(map,x,y-1);		// 上
			if(x != map.length - 1 && y != 0 && map[x+1][y-1][1] == 0)					map = MapOpen(map,x+1,y-1);		// 右上
			if(x != 0 && map[x-1][y][1] == 0 && map[x-1][y][1] == 0)					map = MapOpen(map,x-1,y);		// 左
			if(x != map.length - 1 && map[x+1][y][1] == 0)								map = MapOpen(map,x+1,y);		// 右
			if(x != 0 && y != map[0].length - 1 && map[x-1][y+1][1] == 0)				map = MapOpen(map,x-1,y+1);		// 左下
			if(y != map[0].length - 1 && map[x][y+1][1] == 0)							map = MapOpen(map,x,y+1);		// 下
			if(x != map.length - 1 && y != map[0].length - 1 && map[x+1][y+1][1] == 0)	map = MapOpen(map,x+1,y+1);		// 右下
		}
		return map;
	}

	static boolean Decision(int[][][] map, int x, int y){
		// 勝ち
		if(WinDecision(map)){
			System.out.println("あなたの勝ちです。");
			return false;
		}
		// 負け
		if(map[x][y][0] > 8){
			System.out.println("あなたの負けです。");
			return false;
		}
		return true;
	}

	static boolean WinDecision(int[][][] map){
		for(int j = 0; j < map[0].length; j++){
			for(int i = 0; i < map.length; i++){
				if(map[i][j][1] == 0 && map[i][j][0] < 9)
					return false;
			}
		}
		return true;
	}

	static int[][][] PutFlag(int[][][] map){
		return map;
	}

	static int[][][] AroundOpen(int[][][] map){
		return map;
	}

	static int ScanInt(int i, int min, int max, Scanner s){
		do{
			i = s.nextInt();
		}while(i < min && i > max);
		return i;
	}
}