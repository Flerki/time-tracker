package time_tracker.component;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class Interval extends HBox {

    private final static int START_HOUR_POS = 0;
    private final static int START_MINUTE_POS = 1;
    private final static int END_HOUR_POS = 2;
    private final static int END_MINUTE_POS = 3;

    public Interval() {
        super();

        TextField startHour = new TextField();
        startHour.setPrefColumnCount(2);

        TextField startMinute = new TextField();
        startMinute.setPrefColumnCount(2);

        TextField endHour = new TextField();
        endHour.setPrefColumnCount(2);

        TextField endMinute = new TextField();
        endMinute.setPrefColumnCount(2);

        getChildren().addAll(startHour, startMinute, endHour, endMinute);
        setSpacing(5);
    }

    public double calculateTime() {
        List<Integer> values = getChildren()
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

        return (double) (endMinutes - startMinutes) / 60;
    }

    public void disableInputs() {
        getChildren().forEach(node -> node.setDisable(true));
    }

    public String asStringForExcel() {
        return String.format("%s:%s\t%s:%s",
                retrieveInputValue(START_HOUR_POS),
                retrieveInputValue(START_MINUTE_POS),
                retrieveInputValue(END_HOUR_POS),
                retrieveInputValue(END_MINUTE_POS)
        );
    }

    private String retrieveInputValue(int pos) {
        return ((TextField) getChildren().get(pos)).getText();
    }
}
