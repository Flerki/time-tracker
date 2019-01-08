package time_tracker;

import javafx.application.Application;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class TimeTrackerApp extends Application {

    private VBox entriesTable;

    private Button addBtn;

    private DoubleProperty total = new SimpleDoubleProperty(0);
    private Text totalText = new Text();

    @Override
    public void start(Stage primaryStage) throws Exception {

        StringBinding totalBinding = new StringBinding() {
            {
                this.bind(total);
            }
            @Override
            protected String computeValue() {
                return "Total : " + total.getValue();
            }
        };

        totalText.textProperty().bind(totalBinding);

        Text startTitle = new Text("Start");
        Text endTitle = new Text("End");

        HBox entriesTitleRow = new HBox(startTitle, endTitle);
        entriesTable = new VBox(entriesTitleRow);

        addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            addCurrentRow();
            createTimeRow();
        });

        createTimeRow();

        VBox root = new VBox(totalText, entriesTable);
        Scene scene = new Scene(root, 300, 400);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addCurrentRow() {
        HBox current = retrieveCurrent();
        removeAddBtn(current);
        disableInputs(current);
        double diff = calculateTime(current);
        total.setValue(total.doubleValue() + diff);
    }

    private double calculateTime(HBox current) {
        List<Integer> values = current.getChildren()
                .stream()
                .map(node -> (TextField) node)
                .map(TextField::getText)
                .map(Integer::parseInt)
                .collect(toList());
        Integer startHour = values.get(0);
        Integer startMinute = values.get(1);
        Integer endHour = values.get(2);
        Integer endMinute = values.get(3);

        int startMinutes = startHour * 60 + startMinute;
        int endMinutes = endHour * 60 + endMinute;

        return (double)(endMinutes - startMinutes) / 60;
    }

    private void removeAddBtn(HBox row) {
        int ADD_BTN_POS = 4;
        row.getChildren().remove(ADD_BTN_POS);
    }

    private void disableInputs(HBox current) {
        current.getChildren().forEach(node -> node.setDisable(true));
    }

    private HBox retrieveCurrent() {
        ObservableList<Node> children = entriesTable.getChildren();
        int lastPos = children.size() - 1;
        return (HBox) children.get(lastPos);
    }

    void createTimeRow() {
        TextField startHour = new TextField();
        startHour.setPrefColumnCount(2);

        TextField startMinute = new TextField();
        startMinute.setPrefColumnCount(2);

        TextField endHour = new TextField();
        endHour.setPrefColumnCount(2);

        TextField endMinute = new TextField();
        endMinute.setPrefColumnCount(2);

        HBox timeRow = new HBox(startHour, startMinute, endHour, endMinute, addBtn);
        timeRow.setSpacing(5);

        entriesTable.getChildren().add(timeRow);
    }
}
