package Logic;

import java.util.ArrayList;

public class AI {
	
	private int player_rpr;
	
	private class move_state{
		private int score;
		private pair<Integer, Integer> move_placed;
		public move_state(int sc, pair<Integer, Integer> p, Game game) {
			this.score = sc;
			this.move_placed = p;
		}
		
	}
	
	public AI(int r) {
		this.player_rpr = r;
	}
	
	public pair<Integer, Integer> findMove() {
		return null;
	}
	
	private Game tryPlace(pair<Integer, Integer> pos, Game game) {
		Game temp = game.clone();
		if(temp.isValid(pos)) {
			temp.placeTile(pos);
			return temp;
		}
		return null;
	}
	
	private ArrayList<pair<Integer, Integer> > getPossibleMoves(Game game) {
		int[][] game_stat = game.getStatus();
		ArrayList<pair<Integer, Integer> > out = new ArrayList<pair<Integer, Integer> >();
		boolean[][] added = new boolean[game.board_x][game.board_y];
		for(int x = 0;x < game.board_x;x++) {
			for(int y = 0;y < game.board_y;y++) {
				added[y][x] = false;
			}
		}
		for(int x = 0;x < game.board_x;x++) {
			for(int y = 0;y < game.board_y;y++) {
				pair<Integer, Integer> pos = pair.new_pair(x, y);
//				System.out.println(x + " " + y + " " + game_stat[y][x]);
				if(game_stat[pos.second][pos.first] == -1) continue;
				for(pair<Integer, Integer> dir: Directions.allDir) {
					for(int i = 1; i < 2;i++) {
						int xx = pos.first + i*dir.first;
						int yy = pos.second+ i*dir.second;
						pair<Integer, Integer> temp = pair.new_pair(xx, yy);
						if(game.isValid(temp)) {
//							System.out.println(temp.first + " " + temp.second);
							if(game_stat[temp.second][temp.first] == -1 && !added[y][x]) {
								out.add(new pair<Integer, Integer>(xx, yy));
								added[yy][xx] = true;
							}
						}
					}
				}
			}
		}
		return out;
	}
	
	private int countTiles(pair<Integer, Integer> start, pair<Integer, Integer> dir, Game game, int p) {
		int[][] game_stat = game.getStatus();
		int cnt = 0;
		for(int i = 0;i < 5;i++) {
			pair<Integer, Integer> temp = pair.new_pair(start.first + i*dir.first, start.second + i*dir.second);
			if(!game.CheckBound(temp)) return 0;
			else if(game_stat[temp.second][temp.first] != -1 && game_stat[temp.second][temp.first] != p) return 0;
			else if(game_stat[temp.second][temp.first] == p) cnt++;
		}
		return cnt;
	}
	
	private int CalcOffense(int p, Game game) {
		int mx_score = 0;
		int mx_cnt = 0;
		for(int y = 0;y < game.board_y;y++) {
			for(int x = 0;x < game.board_x;x++) {
				for(pair<Integer, Integer> dir: Directions.allDir) {
						mx_cnt = Math.max(mx_cnt, countTiles(new pair<Integer, Integer>(x, y), dir, game, p));
				}
			}
		}
		mx_score = mx_cnt;
		if(p != this.player_rpr) return -mx_score;
		return mx_score;
	}
	
	private boolean CheckWin(int p, Game game) {
		int mx_cnt = 0;
		for(int y = 0;y < game.board_y;y++) {
			for(int x = 0;x < game.board_x;x++) {
				for(pair<Integer, Integer> dir: Directions.allDir) {
						mx_cnt = Math.max(mx_cnt, countTiles(new pair<Integer, Integer>(x, y), dir, game, p));
				}
			}
		}
		if(mx_cnt >= 5) return true;
		return false;
	}
	
	private move_state bestMoves(Game game, int d, pair<Integer, Integer> p, int alpha, int beta) {
		boolean win = CheckWin((this.player_rpr+d-1)%2, game);
		if(win) {
			int sc;
			if(d %2 == 1) sc = 5;
			else sc = -5;
			return new move_state(sc, p, game);
		}
		else if(d == 5) 
			return new move_state(CalcOffense((this.player_rpr + d)%2, game), p, game);
		else {
			ArrayList<pair<Integer, Integer > > allMoves = getPossibleMoves(game);
			move_state out = null;
			for(pair<Integer, Integer> move: allMoves) {
				System.out.println(move.first+" "+ move.second+" "+d);
				Game newBoard = tryPlace(move, game);
				move_state nextMove = bestMoves(newBoard, d+1, move, alpha, beta);
				if(out == null) {
					out = nextMove;
					out.move_placed = move;
				}
				if(d%2 == 0) {
					alpha = Math.max(alpha, nextMove.score);
					if(out.score < nextMove.score) {
						out = nextMove;
						out.move_placed = move;
					}
					if(alpha >= beta) break;
				}
				if(d%2 == 1) {
					beta = Math.min(beta, nextMove.score);
					if(out.score < nextMove.score) {
						out = nextMove;
						out.move_placed = move;
					}
				}
			}
		return out;
		}
	}
	
	public pair<Integer, Integer> calculate(Game game){
		return bestMoves(game, 2, null, Integer.MIN_VALUE, Integer.MAX_VALUE).move_placed;
	}
}
