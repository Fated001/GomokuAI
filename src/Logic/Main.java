package Logic;
import GUI.Painter;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static final double sq_size = 20;
	public static final int board_x = 19, board_y = 19;
	public static final int win_condition = 5;
	
	private Parent CreateContent() {
		Painter painter = new Painter();
		return painter.getPane();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene1 = new Scene(CreateContent());
		
		primaryStage.setScene(scene1);
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
