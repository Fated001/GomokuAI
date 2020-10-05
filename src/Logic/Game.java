package Logic;

import java.util.ArrayList; 

public class Game {
	private int turns = 0;
	private int[][] status;
	public int board_y = 19, board_x = 19;
	
	public Game() {
		turns = 0;
		status = new int[board_y][board_x];
		for(int y = 0;y < board_y;y++) {
			for(int x = 0;x < board_x;x++) {
				status[y][x] = -1;
			}
		}
	}
	
	private Game(int t, int[][] st) {
		turns = t;
		status = st.clone();
	}
	
	public int getTurns() {
		return turns;
	}
	
	public void setTurns(int t) {
		turns = t;
	}
	
	public int[][] getStatus() {
		return status;
	}
	
	public boolean CheckBound(pair<Integer, Integer> pos) {
		if(pos.first < 0 || pos.first >= Main.board_x) return false;
		else if(pos.second < 0 || pos.second >= Main.board_y) return false;
		return true;
	}
	
	private ArrayList<pair<Integer, Integer> > GetStartPoints(pair<Integer, Integer> pos, int num, int player){
		ArrayList<pair<Integer, Integer> > output = new ArrayList<pair<Integer, Integer> >();
		output.add(pos);
		for(pair<Integer, Integer> dir: Directions.allDir) {
			for(int i = 1;i <= num;i++) {
				pair<Integer, Integer> coor = new pair<Integer, Integer>(pos.first + i * dir.first, pos.second + i * dir.second);
				if(CheckBound(coor) && status[coor.second][coor.first] == player) output.add(coor);
			}
		}
		return output;
	}
	
	private boolean CheckDirContains(pair<Integer, Integer> start, pair<Integer, Integer> pos, pair<Integer, Integer> dir, int cnt) {
//		Check that from the start point contains the tile placed by the player
		for(int i = 0;i <= cnt;i++) {
			pair<Integer, Integer> temp = new pair<Integer, Integer>(start.first + dir.first * i, start.second + dir.second * i);
			if(temp.equals(pos)) return true;
		}
		return false;
	}
	
	private int CountConsecRange(pair<Integer, Integer> start, pair<Integer, Integer> dir, int player, int range) {
//		Count Consecutive Placed Tiles by the Same Player in Range range from the start point
		int cnt = 0;
		pair<Integer, Integer> temp = start.clone();
		for(int i = 0;i <= range;i++) {
			if(!CheckBound(temp)) return cnt;
			if(status[temp.second][temp.first] == player) cnt++;
			else return cnt;
			temp = pair.new_pair(temp.first + dir.first, temp.second + dir.second);
		}
		return cnt;
	}
	
	private boolean CheckRule(pair<Integer, Integer> pos) {
		int player = turns % 2;
//		Check 3-3 and 4-4 rule
		status[pos.second][pos.first] = player;
		for(int i = 3;i <= 4;i++) {
			ArrayList<pair<Integer, Integer> > possibleStartPoints = GetStartPoints(pos, i, player);
			for(pair<Integer, Integer> point: possibleStartPoints) {
				for(pair<Integer, Integer> dir: Directions.allDir) {
					if(CheckDirContains(point, pos, dir, i) && CountConsecRange(point, dir, player, i) == i) {
						for(pair<Integer, Integer> dir2: Directions.allDir) {
							if(dir2.compareTo(dir) == 0 || pair.new_pair(-dir2.first, -dir2.second).compareTo(dir) == 0) continue;
							if(CountConsecRange(point, dir2, player, i) == i) {
								status[pos.second][pos.first] = -1;
								return false;
							}
						}
					}
				}
			}
		}
		status[pos.second][pos.first] = -1;
		return true;
	}
	
	public boolean isValid(pair<Integer, Integer> pos) {
		if(!CheckBound(pos)) return false;
		if(status[pos.second][pos.first] != -1) return false;
		if(!CheckRule(pos)) return false;
		return true;
	}
	
	public void placeTile(pair<Integer, Integer> pos) {
		if(isValid(pos)) status[pos.second][pos.first] = turns % 2;
		turns++;
	}
	
	public pair<Boolean, Integer> Winner(){
		for(int y = 0; y < Main.board_y; y++) {
			for(int x = 0; x < Main.board_x; x++) {
				if(status[y][x] != -1) {
					for(pair<Integer, Integer> dir: Directions.allDir) {
						if(CountConsecRange(new pair<Integer, Integer>(x, y), dir, status[y][x], Main.win_condition) == Main.win_condition)
							return new pair<Boolean, Integer>(true, status[y][x]);
					}
				}
			}
		}
		return new pair<Boolean, Integer>(false, -1);
	}
	@Override
	public Game clone() {
		int[][] copy_status = new int[board_y][board_x];
		for(int i = 0;i < board_y;i++) {
			copy_status[i] = status[i].clone();
		}
		return new Game(turns, copy_status);
	}
	
}
