
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class Start extends Application {
	protected static final Server server = new Server(10);
	@Override
	public void start(Stage primaryStage) {		
		primaryStage.setTitle("Parking System Server");
		GridPane grid = new GridPane();
		grid.setStyle("-fx-background-color: #336DFF;");
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(5, 5, 5, 5));

		
		
		Button Run = new Button("Run");
		grid.add(Run, 1, 0);

		Button Stop = new Button("Stop");
		grid.add(Stop, 0, 0); 

		final TextField Consol = new TextField();
		Consol.setDisable(true);
		grid.addRow(2, Consol);
		
		Run.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				try {
					server.start();
					Consol.setText("Connected ! ");
				} catch (Exception e1) {
					Consol.setText(e1.getMessage());
				}
			}
		});
		Stop.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				try {
					server.Stop();
					Consol.setText("Not Connected ! ");
				} catch (Exception e1) {
					Consol.setText(e1.getMessage());
				}
			}
		});
		Scene scene = new Scene(grid, 300, 300);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
