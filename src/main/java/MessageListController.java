import dbService.DBService;
import hba.MessageEntity;
import hba.VehicleEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Collection;

public class MessageListController implements IController {

    private Stage stage = null;
    @Override
    public void initStage(Stage stage) {
        this.stage = stage;
    }

    @FXML public Label lblTitle;
    @FXML public ComboBox<VehicleEntity> cmbVehicle;
    @FXML public DatePicker dtpDate;
    @FXML public ScrollPane scrollPane;
    @FXML public Label lblNotFound;

    private MessageEntity messageEntity = null;
    public void setMessageEntity(MessageEntity messageEntity) {
        this.messageEntity = messageEntity;
    }

    //получить индекс ТС (Vehicle) в obserList соответствующий выбранному ТС (Vehicle)
    public static int getIndexOfVehicle(VehicleEntity vehicleEntityChoose, ObservableList<VehicleEntity> ObservableList){
        int index = -1;
        for (VehicleEntity vehicleEntity : ObservableList)
            if (vehicleEntity.getIdVehicle().equals(vehicleEntityChoose.getIdVehicle()))
                index = ObservableList.indexOf(vehicleEntity);
        return index;
    }

    //инициализировать комбобокс выбора ТС
    public void initCmbVehicle() {
        cmbVehicle.setConverter(DriverCUDController.vehicleEntityStringConverter);
        cmbVehicle.getItems().clear();
        cmbVehicle.setItems(FXCollections.observableArrayList(DBService.loadVehicleList()));
    }


    //Вывести список Сообщений от транспорта
    public void viewMessages(Collection<MessageEntity> messageEntityCollection) {
        AnchorPane aPane = null;
        scrollPane.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        ColumnConstraints col4 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 10;
        Integer aPanePrefHeight = 50;
        for (MessageEntity messageE : messageEntityCollection) {
            GridPane gridPane = new GridPane();
            gridPane.setStyle("-fx-background-color:  #f2e5ff;");
            gridPane.setPadding(new Insets(5, 5, 5, 5));
            gridPane.setHgap(3);
            gridPane.setVgap(3);
            gridPane.setLayoutY(gridPaneLayoutY);
            gridPaneLayoutY += 35;
            gridPane.setLayoutX(gridPaneLayoutX);
            gridPane.setPrefWidth(323);
            //
            Label lblXcoord = new Label("x: " + messageE.getxCoord().intValue() + " ");
            Label lblYcoord = new Label("y: " + messageE.getyCoord().intValue() + " ");
            Label lblSpeed = new Label(messageE.getSpeed().intValue() + " км/ч ");
            Label lblTime = new Label( messageE.getDatetime().getHour() + ":" + messageE.getDatetime().getMinute() + ":" + messageE.getDatetime().getSecond());
            //
            lblXcoord.setFont(new Font("Arial", 14));
            lblXcoord.setTextFill(Color.BLUE);
            lblYcoord.setFont(new Font("Arial", 14));
            lblYcoord.setTextFill(Color.BROWN);
            lblSpeed.setFont(new Font("Arial", 14));
            lblSpeed.setTextFill(Color.GREEN);
            lblTime.setFont(new Font("Arial", 14));
            lblTime.setTextFill(Color.DARKVIOLET);
            //
            gridPane.add(lblXcoord, 0, 0, 1, 1);
            gridPane.add(lblYcoord, 1, 0, 1, 1);
            gridPane.add(lblSpeed, 2, 0, 1, 1);
            gridPane.add(lblTime, 3, 0, 1, 1);
            //
            col1.setPercentWidth(25);
            col2.setPercentWidth(25);
            col3.setPercentWidth(25);
            col4.setPercentWidth(25);
            gridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
            //
            aPane = (AnchorPane) scrollPane.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight);
            aPanePrefHeight += 35;
            scrollPane.setContent(aPane);
        }
    }

    @FXML       //Вывести сообщения
    private void performShowMessages() {
        try {
            if (cmbVehicle.getValue() == null) throw new NullPointerException();
            if (dtpDate.getValue() == null) throw new Exception();
            //
            Collection<MessageEntity> messageEntityCollection = DBService.loadMessageList(cmbVehicle.getValue(), dtpDate.getValue().atStartOfDay());
            if (!messageEntityCollection.isEmpty())
                lblNotFound.setVisible(false);
            else
                lblNotFound.setVisible(true);
            viewMessages(messageEntityCollection);
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- требуется выбрать транспортное средство");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- требуется указать дату");
            alert.showAndWait();
        }
    }

    //Связать комбобокс с титулом
    public void associateCmbVehicleWithTitle() {
        lblTitle.setText("   Сообщения от: " + cmbVehicle.getValue().getModel() + " " + cmbVehicle.getValue().getRegnum());
    }

}
