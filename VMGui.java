import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VMGui extends Application {

    private VirtualMachine vm;
    private Label accLabel;
    private TextArea outputArea;

    @Override
    public void start(Stage stage) {

        vm = new VirtualMachine();

        accLabel = new Label("Accumulator: 0");

        Button runButton = new Button("Run Program");
        Button resetButton = new Button("Reset");

        outputArea = new TextArea();
        outputArea.setEditable(false);

        runButton.setOnAction(e -> {
            vm.reset(); // reset accumulator before running
            outputArea.clear();

            ProgramLoader.loadAndExecute("program.vm", vm);

            accLabel.setText("Accumulator: " + vm.getAccumulator());
            outputArea.appendText("Program executed.\n");
        });

        resetButton.setOnAction(e -> {
            vm.reset();
            accLabel.setText("Accumulator: 0");
            outputArea.clear();
            outputArea.appendText("VM reset.\n");
        });

        VBox root = new VBox(10, accLabel, runButton, resetButton, outputArea);
        root.setPrefSize(450, 350);

        stage.setTitle("Secure Visual VM");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
