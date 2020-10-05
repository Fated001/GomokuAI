package GUI;

import Logic.AI;
import Logic.Game;
import Logic.Main;
import Logic.pair;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
public class Painter {
	private Game game = new Game();
	private AI ai = new AI(1);
	private Pane pane = new Pane();
	private static final int sq_size = 20;
	private class Tiles extends StackPane{
		pair<Integer, Integer> pos;
		private Rectangle rect = new Rectangle(Painter.sq_size, Painter.sq_size);
		private Circle cir = null;
		
		private void OnClick(MouseEvent e) {

			if(e.getButton() == MouseButton.PRIMARY) {
				if(!game.isValid(pos)) return;
//				System.out.println("(" + pos.first + " " + pos.second+ ")" + (turns%2));
				game.placeTile(pos);
				cir.setFill(((game.getTurns()-1) % 2 == 0) ? Color.BLACK : Color.WHITE);
				getChildren().add(cir);
				pair<Boolean, Integer> winner = game.Winner();
//				System.out.println("{" + winner.first + " " + winner.second + "}");
				if(winner.first) {
					endGame();
					return;
				}
				AIPlace();
			}
		}
		
		public Tiles(pair<Integer, Integer> coor) {
			pos = coor.clone();
			
			rect.setFill(Color.web("#F5B041"));
			rect.setStroke(Color.BLACK);
			rect.setWidth(Painter.sq_size);
			rect.setHeight(Painter.sq_size);
			rect.setOnMouseClicked(e -> {OnClick(e);});

			cir = new Circle(Painter.sq_size/2, Painter.sq_size/2, Painter.sq_size/2-2);
			cir.setFill(null);
			cir.setStroke(null);
			
			setTranslateX(pos.first * Painter.sq_size);
			setTranslateY(pos.second * Painter.sq_size);
			
			getChildren().add(rect);
		}
	}
	Tiles[][] board;
	
	private void AIPlace() {
		pair<Integer, Integer> pos2 = ai.calculate(game.clone());
		game.placeTile(pos2);
		board[pos2.second][pos2.first].cir.setFill(Color.WHITE);
		board[pos2.second][pos2.first].getChildren().add(board[pos2.second][pos2.first].cir);
		pair<Boolean, Integer> winner = game.Winner();
		if(winner.first) endGame();
	}
	
	private void initBoard() {
		board = new Tiles[game.board_y][game.board_x];
		for(int y = 0;y < game.board_y;y++) {
			for(int x = 0;x < game.board_x;x++) {
				board[y][x] = new Tiles(new pair<Integer, Integer>(x, y));
			}
		}
	}
	
	public Painter() {
		initBoard();
		pane.setPrefHeight(sq_size * game.board_y);
		pane.setPrefWidth(sq_size * game.board_x);
		for(int y = 0;y < game.board_y;y++) {
			for(int x = 0;x < game.board_x;x++) {
				pane.getChildren().add(board[y][x]);
			}
		}
		
	}
	
	public Pane getPane() {
		return pane;
	}
	
	private void endGame() {
		for(int y = 0;y < Main.board_y;y++) {
			for(int x = 0;x < Main.board_x;x++) {
				board[y][x].rect.setOnMouseClicked(null);
			}
		}
		Label label = new Label("Player " + ((game.getTurns()-1)%2+1) + " Wins!");
		label.setStyle("-fx-font: 24 arial; -fx-color: blue;");
		
		label.setTextAlignment(TextAlignment.CENTER);
		label.layoutXProperty().bind(pane.widthProperty().subtract(label.widthProperty()).divide(2));
		label.layoutYProperty().bind(pane.heightProperty().subtract(label.heightProperty()).divide(2));
		pane.getChildren().add(label);
		
	}
}
