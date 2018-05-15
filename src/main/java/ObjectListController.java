import dbService.DBService;
import hba.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.List;

public class ObjectListController implements IController {

    private Stage stage = null;
    @Override
    public void initStage(Stage stage) {
        this.stage = stage;
    }

    @FXML public Label lblTitle;
    @FXML public ScrollPane scrollPane;
    @FXML public Hyperlink linkAddNewObject;
    //
    @FXML public Hyperlink linkShowAllPointOnMap;
    @FXML public Hyperlink linkShowAllRoutersOnMap;
    @FXML public Hyperlink linkRefresh;

    private MonitorController monitorController;
    public void setMonitorController(MonitorController monitorController) {
        this.monitorController = monitorController;
    }


    //Вывести список всех Транспортных средств
    public void viewVehicles(Collection<VehicleEntity> vehicleEntityCollection) {
        AnchorPane aPane = null;
        scrollPane.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        ColumnConstraints col4 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 10;
        Integer aPanePrefHeight = 50;
        for (VehicleEntity vehicleE : vehicleEntityCollection) {
            GridPane gridPane = new GridPane();
            gridPane.setStyle("-fx-background-color:  #f2e5ff;");
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            gridPane.setHgap(3);
            gridPane.setVgap(3);
            gridPane.setLayoutY(gridPaneLayoutY);
            gridPaneLayoutY += 65;
            gridPane.setLayoutX(gridPaneLayoutX);
            gridPane.setPrefWidth(325);
            //
            Label lblvalueModel = new Label(vehicleE.getModel());
            Label lblvalueRegnum = new Label(vehicleE.getRegnum());
            Label lblcaptionModel = new Label("Модель");
            Label lblcaptionRegnum = new Label("Рег. номер");
            Hyperlink linkShowMessagesList = new Hyperlink();
            Hyperlink linkIconEdit = new Hyperlink();
            //
            lblvalueModel.setFont(new Font("Arial Bold", 15));
            lblvalueRegnum.setFont(new Font("Arial Bold", 15));
            lblcaptionModel.setFont(new Font("Arial", 13));
            lblcaptionRegnum.setFont(new Font("Arial", 13));
            //
            linkShowMessagesList.setStyle("-fx-background-image: url('/icons/message/ic_rss_feed_black_24dp_1x.png')");
            linkShowMessagesList.setPrefHeight(24);
            linkShowMessagesList.setPrefWidth(24);
            linkIconEdit.setStyle("-fx-background-image: url('/zshestBlue.png')");
            linkIconEdit.setPrefHeight(25);
            linkIconEdit.setPrefWidth(25);
            //
            gridPane.add(lblvalueModel, 0, 0, 1, 1);
            gridPane.add(lblcaptionModel, 0, 1, 1, 1);
            gridPane.add(lblvalueRegnum, 1, 0, 1, 1);
            gridPane.add(lblcaptionRegnum, 1, 1, 1, 1);
            gridPane.add(linkShowMessagesList, 2, 0, 1, 2);
            gridPane.add(linkIconEdit, 3, 0, 1, 2);
            //
            col1.setPercentWidth(40);
            col2.setPercentWidth(37);
            col3.setPercentWidth(13);
            col4.setPercentWidth(10);
            gridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
            //
            linkShowMessagesList.setOnAction(event -> goShowMessageList(vehicleE));
            linkIconEdit.setOnAction(event -> goVehicleEdit(vehicleE));
            //
            aPane = (AnchorPane) scrollPane.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight);
            aPanePrefHeight += 65;
            scrollPane.setContent(aPane);
        }
    }

    private void goShowMessageList(VehicleEntity vehicleEntity) {
        MessageListController messageListController = (MessageListController) MonitorController.initChildStage("MessageList.fxml", "Сообщения от транспорта");
        messageListController.initCmbVehicle();
        messageListController.cmbVehicle.getSelectionModel().select(MessageListController.getIndexOfVehicle(vehicleEntity, FXCollections.observableArrayList(DBService.loadVehicleList())));
    }

    private void goVehicleEdit(VehicleEntity vehicleEntity) {
        VehicleCUDController vehicleCUDController = (VehicleCUDController) MonitorController.initChildStage("VehicleCUD.fxml", "Редактировать ТС");
        vehicleCUDController.linkAddNew.setVisible(false);
        vehicleCUDController.linkSave.setVisible(true);
        vehicleCUDController.linkDelete.setVisible(true);
        vehicleCUDController.lblTitle.setText(" Редактировать транспортное средство");
        vehicleCUDController.setVehicleEntity(vehicleEntity);
        //
        vehicleCUDController.lbloldvalueModel.setVisible(true);
        vehicleCUDController.lbloldvalueRegnum.setVisible(true);
        vehicleCUDController.lbloldvalueModel.setText(vehicleEntity.getModel());
        vehicleCUDController.lbloldvalueRegnum.setText(vehicleEntity.getRegnum());
        //
        vehicleCUDController.txtModel.setText(vehicleEntity.getModel());
        vehicleCUDController.txtRegnum.setText(vehicleEntity.getRegnum());
        //
        vehicleCUDController.setObjectListController(this);
    }


    //Вывести список всех Водителей
    public void viewDrivers(Collection<DriverEntity> driverEntityCollection) {
        AnchorPane aPane = null;
        scrollPane.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 10;
        Integer aPanePrefHeight = 50;
        for (DriverEntity driverE : driverEntityCollection) {
            GridPane gridPane = new GridPane();
            gridPane.setStyle("-fx-background-color:  #f2e5ff;");
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            gridPane.setHgap(3);
            gridPane.setVgap(3);
            gridPane.setLayoutY(gridPaneLayoutY);
            gridPaneLayoutY += 65;
            gridPane.setLayoutX(gridPaneLayoutX);
            gridPane.setPrefWidth(325);
            //
            Label lblvalueFIO = new Label(driverE.getFio());
            Label lblvalueVunum = new Label("№ ВУ: " + driverE.getVunum());
            Label lblvalueVehicle = new Label(driverE.getVehicleByIdVehicle().getModel());
            Label lblcaptionVehicle = new Label("Транспорт");
            Hyperlink linkIconEdit = new Hyperlink();
            //
            lblvalueFIO.setFont(new Font("Arial Bold", 15));
            lblvalueVunum.setFont(new Font("Arial", 13));
            lblvalueVehicle.setFont(new Font("Arial Bold", 15));
            lblcaptionVehicle.setFont(new Font("Arial", 13));
            //
            linkIconEdit.setStyle("-fx-background-image: url('/zshestBlue.png')");
            linkIconEdit.setPrefHeight(25);
            linkIconEdit.setPrefWidth(25);
            //
            gridPane.add(lblvalueFIO, 0, 0, 1, 1);
            gridPane.add(lblvalueVunum, 0, 1, 1, 1);
            gridPane.add(lblvalueVehicle, 1, 0, 1, 1);
            gridPane.add(lblcaptionVehicle, 1, 1, 1, 1);
            gridPane.add(linkIconEdit, 2, 0, 1, 2);
            //
            col1.setPercentWidth(60);
            col2.setPercentWidth(30);
            col3.setPercentWidth(10);
            gridPane.getColumnConstraints().addAll(col1, col2, col3);
            //
            linkIconEdit.setOnAction(event -> goDriverEdit(driverE));
            //
            aPane = (AnchorPane) scrollPane.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight);
            aPanePrefHeight += 65;
            scrollPane.setContent(aPane);
        }
    }

    private void goDriverEdit(DriverEntity driverEntity) {
        DriverCUDController driverCUDController = (DriverCUDController) MonitorController.initChildStage("DriverCUD.fxml", "Редактировать данные водителя");
        driverCUDController.linkAddNew.setVisible(false);
        driverCUDController.linkSave.setVisible(true);
        driverCUDController.linkDelete.setVisible(true);
        driverCUDController.lblTitle.setText("      Редактировать данные водителя");
        driverCUDController.setDriverEntity(driverEntity);
        //
        driverCUDController.lbloldvalueFIO.setVisible(true);
        driverCUDController.lbloldvalueVunum.setVisible(true);
        driverCUDController.lbloldvalueVehicle.setVisible(true);
        driverCUDController.lbloldvalueFIO.setText(driverEntity.getFio());
        driverCUDController.lbloldvalueVunum.setText("№ ВУ: " + driverEntity.getVunum());
        driverCUDController.lbloldvalueVehicle.setText(driverEntity.getVehicleByIdVehicle().getModel());
        //
        driverCUDController.txtFIO.setText(driverEntity.getFio());
        driverCUDController.txtVunum.setText(driverEntity.getVunum());
        //
        driverCUDController.initCmbVehicle();
        driverCUDController.cmbVehicle.getSelectionModel().select(DriverCUDController.getIndexOfVehicle(driverEntity, FXCollections.observableArrayList(DBService.loadVehicleList())));
        //
        driverCUDController.setObjectListController(this);
    }


    //Вывести список всех Точек
    public void viewPoints(Collection<PointEntity> pointEntityCollection) {
        AnchorPane aPane = null;
        scrollPane.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        ColumnConstraints col4 = new ColumnConstraints();
        ColumnConstraints col5 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 10;
        Integer aPanePrefHeight = 50;
        for (PointEntity pointE : pointEntityCollection) {
            GridPane gridPane = new GridPane();
            gridPane.setStyle("-fx-background-color:  #f2e5ff;");
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            gridPane.setHgap(3);
            gridPane.setVgap(3);
            gridPane.setLayoutY(gridPaneLayoutY);
            gridPaneLayoutY += 65;
            gridPane.setLayoutX(gridPaneLayoutX);
            gridPane.setPrefWidth(325);
            //
            Label lblvalueName = new Label(pointE.getName());
            Label lblvalueXcoord = new Label(String.valueOf(pointE.getxCoord()));
            Label lblvalueYcoord = new Label(String.valueOf(pointE.getyCoord()));
            Label lblcaptionName = new Label("Название");
            Label lblcaptionXcoord = new Label("X-коорд");
            Label lblcaptionYcoord = new Label("Y-коорд");
            Hyperlink linkIconShowOnMap = new Hyperlink();
            Hyperlink linkIconEdit = new Hyperlink();
            //
            lblvalueName.setFont(new Font("Arial Bold", 15));
            lblvalueXcoord.setFont(new Font("Arial Bold", 15));
            lblvalueYcoord.setFont(new Font("Arial Bold", 15));
            lblcaptionName.setFont(new Font("Arial", 13));
            lblcaptionXcoord.setFont(new Font("Arial", 13));
            lblcaptionYcoord.setFont(new Font("Arial", 13));
            //
            linkIconShowOnMap.setStyle("-fx-background-image: url('/icons/baseline_explore_black_24dp.png')");
            linkIconShowOnMap.setPrefHeight(24);
            linkIconShowOnMap.setPrefWidth(24);
            linkIconEdit.setStyle("-fx-background-image: url('/zshestBlue.png')");
            linkIconEdit.setPrefHeight(25);
            linkIconEdit.setPrefWidth(25);
            //
            gridPane.add(lblvalueName, 0, 0, 1, 1);
            gridPane.add(lblcaptionName, 0, 1, 1, 1);
            gridPane.add(lblvalueXcoord, 1, 0, 1, 1);
            gridPane.add(lblcaptionXcoord, 1, 1, 1, 1);
            gridPane.add(lblvalueYcoord, 2, 0, 1, 1);
            gridPane.add(lblcaptionYcoord, 2, 1, 1, 1);
            gridPane.add(linkIconShowOnMap, 3, 0, 1, 2);
            gridPane.add(linkIconEdit, 4, 0, 1, 2);
            //
            col1.setPercentWidth(40);
            col2.setPercentWidth(20);
            col3.setPercentWidth(20);
            col4.setPercentWidth(10);
            col5.setPercentWidth(10);
            gridPane.getColumnConstraints().addAll(col1, col2, col3, col4, col5);
            //
            linkIconShowOnMap.setOnAction(event -> monitorController.showOnePointOneMap(pointE));
            linkIconEdit.setOnAction(event -> goPointEdit(pointE));
            //
            aPane = (AnchorPane) scrollPane.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight);
            aPanePrefHeight += 65;
            scrollPane.setContent(aPane);
        }
    }

    private void goPointEdit(PointEntity pointEntity) {
        PointCUDController pointCUDController = (PointCUDController) MonitorController.initChildStage("PointCUD.fxml", "Редактировать точку маршрута");
        pointCUDController.linkAddNew.setVisible(false);
        pointCUDController.linkSave.setVisible(true);
        pointCUDController.linkDelete.setVisible(true);
        pointCUDController.linkShowPointOnMap.setVisible(true);
        pointCUDController.lblTitle.setText("      Редактировать точку маршрута");
        pointCUDController.setPointEntity(pointEntity);
        //
        pointCUDController.lbloldvalueName.setVisible(true);
        pointCUDController.lbloldvalueXcoord.setVisible(true);
        pointCUDController.lbloldvalueYcoord.setVisible(true);
        pointCUDController.lbloldvalueName.setText(pointEntity.getName());
        pointCUDController.lbloldvalueXcoord.setText("X: " + String.valueOf(pointEntity.getxCoord()));
        pointCUDController.lbloldvalueYcoord.setText("Y: " + String.valueOf(pointEntity.getyCoord()));
        //
        pointCUDController.txtName.setText(pointEntity.getName());
        pointCUDController.txtXcoord.setText(String.valueOf(pointEntity.getxCoord()));
        pointCUDController.txtYcoord.setText(String.valueOf(pointEntity.getyCoord()));
        //
        pointCUDController.setObjectListController(this);
        pointCUDController.setMonitorController(monitorController);
    }


    //Вывести список всех Маршрутов
    public void viewRouters(Collection<RouteEntity> routeEntityCollection) {
        AnchorPane aPane = null;
        scrollPane.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        ColumnConstraints col4 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 10;
        Integer aPanePrefHeight = 50;
        for (RouteEntity routeE : routeEntityCollection) {
            GridPane gridPane = new GridPane();
            gridPane.setStyle("-fx-background-color:  #f2e5ff;");
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            gridPane.setHgap(3);
            gridPane.setVgap(3);
            gridPane.setLayoutY(gridPaneLayoutY);
            gridPaneLayoutY += 65;
            gridPane.setLayoutX(gridPaneLayoutX);
            gridPane.setPrefWidth(325);
            //
            Label lblvalueName = new Label(routeE.getName());
            Label lblvaluePointsCount = new Label("Точек: " + String.valueOf(routeE.getRoutepointsByIdRoute().size()));
            Label lblcaptionName = new Label("Название");
            Hyperlink linkIconShowOnMap = new Hyperlink();
            Hyperlink linkIconEdit = new Hyperlink();
            //
            lblvalueName.setFont(new Font("Arial Bold", 15));
            lblvaluePointsCount.setFont(new Font("Arial Bold", 15));
            lblcaptionName.setFont(new Font("Arial", 13));
            //
            linkIconShowOnMap.setStyle("-fx-background-image: url('icons/baseline_explore_black_24dp.png')");
            linkIconShowOnMap.setPrefHeight(24);
            linkIconShowOnMap.setPrefWidth(24);
            linkIconEdit.setStyle("-fx-background-image: url('/zshestBlue.png')");
            linkIconEdit.setPrefHeight(25);
            linkIconEdit.setPrefWidth(25);
            //
            gridPane.add(lblvalueName, 0, 0, 1, 1);
            gridPane.add(lblcaptionName, 0, 1, 1, 1);
            gridPane.add(lblvaluePointsCount, 1, 0, 1, 2);
            gridPane.add(linkIconShowOnMap, 2, 0, 1, 2);
            gridPane.add(linkIconEdit, 3, 0, 1, 2);
            //
            col1.setPercentWidth(50);
            col2.setPercentWidth(30);
            col3.setPercentWidth(10);
            col4.setPercentWidth(10);
            gridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
            //
            linkIconShowOnMap.setOnAction(event -> monitorController.drawLineOfRoute((List<RoutepointEntity>) routeE.getRoutepointsByIdRoute(), Color.color(Math.random(), Math.random(), Math.random())));
            linkIconEdit.setOnAction(event -> goRouteEdit(routeE));
            //
            aPane = (AnchorPane) scrollPane.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight);
            aPanePrefHeight += 65;
            scrollPane.setContent(aPane);
        }
    }

    private void goRouteEdit(RouteEntity routeEntity) {
        RouteCUDController routeCUDController = (RouteCUDController) MonitorController.initChildStage("RouteCUD.fxml", "Редактировать маршрут");
        routeCUDController.linkAddNew.setVisible(false);
        routeCUDController.linkSave.setVisible(true);
        routeCUDController.linkDelete.setVisible(true);
        routeCUDController.lblTitle.setText("             Редактировать маршрут");
        routeCUDController.setRouteEntity(routeEntity);
        //
        routeCUDController.lbloldvalueName.setVisible(true);
        routeCUDController.lbloldvalueName.setText(routeEntity.getName());
        //
        routeCUDController.txtName.setText(routeEntity.getName());
        //
        routeCUDController.initCmbPoints();
        routeCUDController.associateCmbPoints();
        //
        routeCUDController.setObjectListController(this);
    }


    //Вывести список всех Путевых листов
    public void viewWaybills(Collection<WaybillEntity> waybillEntityCollection) {
        AnchorPane aPane = null;
        scrollPane.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        ColumnConstraints col4 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 10;
        Integer aPanePrefHeight = 50;
        for (WaybillEntity waybillE : waybillEntityCollection) {
            GridPane gridPane = new GridPane();
            gridPane.setStyle("-fx-background-color:  #f2e5ff;");
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            gridPane.setHgap(3);
            gridPane.setVgap(3);
            gridPane.setLayoutY(gridPaneLayoutY);
            gridPaneLayoutY += 65;
            gridPane.setLayoutX(gridPaneLayoutX);
            gridPane.setPrefWidth(325);
            //
            Label lblvalueIdList = new Label("№" + waybillE.getIdWaybill());
            Label lblvalueVehicle = new Label(waybillE.getVehicleByIdVehicle().getModel());
            Label lblvalueRoute = new Label(waybillE.getRouteByIdRoute().getName());
            Label lblcaptionVehicle = new Label("Транспорт");
            Label lblcaptionRoute = new Label("Маршрут");
            Hyperlink linkIconEdit = new Hyperlink();
            //
            lblvalueIdList.setFont(new Font("Arial Bold", 14));
            lblvalueIdList.setTextFill(Color.BLUE);
            lblvalueVehicle.setFont(new Font("Arial Bold", 14));
            lblvalueRoute.setFont(new Font("Arial Bold", 14));
            lblcaptionVehicle.setFont(new Font("Arial", 13));
            lblcaptionRoute.setFont(new Font("Arial", 13));
            //
            linkIconEdit.setStyle("-fx-background-image: url('/zshestBlue.png')");
            linkIconEdit.setPrefHeight(25);
            linkIconEdit.setPrefWidth(25);
            //
            gridPane.add(lblvalueIdList, 0, 0, 1, 2);
            gridPane.add(lblvalueVehicle, 1, 0, 1, 1);
            gridPane.add(lblcaptionVehicle, 1, 1, 1, 1);
            gridPane.add(lblvalueRoute, 2, 0, 1, 1);
            gridPane.add(lblcaptionRoute, 2, 1, 1, 1);
            gridPane.add(linkIconEdit, 3, 0, 1, 2);
            //
            col1.setPercentWidth(18);
            col2.setPercentWidth(27);
            col3.setPercentWidth(45);
            col4.setPercentWidth(10);
            gridPane.getColumnConstraints().addAll(col1, col2, col3);
            //
            linkIconEdit.setOnAction(event -> goWaybillEdit(waybillE));
            //цвет транспорта и линии
            Color color = new Color(Math.random(), Math.random(), Math.random(), 1);
            lblvalueIdList.setOnMouseClicked((MouseEvent event) -> {
                monitorController.playRoute(waybillE.getVehicleByIdVehicle(), waybillE.getRouteByIdRoute(), color);
            });
            //
            aPane = (AnchorPane) scrollPane.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight);
            aPanePrefHeight += 65;
            scrollPane.setContent(aPane);
        }
    }

    private void goWaybillEdit(WaybillEntity waybillEntity) {
        WaybillCUDController waybillCUDController = (WaybillCUDController) MonitorController.initChildStage("WaybillCUD.fxml", "Редактировать путевой лист");
        waybillCUDController.linkAddNew.setVisible(false);
        waybillCUDController.linkSave.setVisible(false);
        waybillCUDController.linkDelete.setVisible(true);
        waybillCUDController.linkDelete.setPrefWidth(350);
        waybillCUDController.linkDelete.setText("                         Удалить");
        waybillCUDController.lblTitle.setText("         Редактировать путевой лист");
        waybillCUDController.cmbVehicle.setDisable(true);
        waybillCUDController.cmbRoute.setDisable(true);
        waybillCUDController.setWaybillEntity(waybillEntity);
        //
        waybillCUDController.lbloldvalueVehicle.setVisible(true);
        waybillCUDController.lbloldvalueRoute.setVisible(true);
        waybillCUDController.lbloldvalueVehicle.setText(waybillEntity.getVehicleByIdVehicle().getModel());
        waybillCUDController.lbloldvalueRoute.setText(waybillEntity.getRouteByIdRoute().getName());
        //
        waybillCUDController.initCmbVehicle();
        waybillCUDController.initCmbRoute();
        waybillCUDController.cmbVehicle.getSelectionModel().select(waybillCUDController.getIndexOfVehicle(waybillEntity, FXCollections.observableArrayList(DBService.loadVehicleList())));
        waybillCUDController.cmbRoute.getSelectionModel().select(waybillCUDController.getIndexOfRoute(waybillEntity, FXCollections.observableArrayList(DBService.loadRouteList())));
        //
        waybillCUDController.setObjectListController(this);
    }


    //Показать все точки на карте
    @FXML
    private void showAllPointOnMap() {
        monitorController.showAllPointOnMap();
    }

    //Показать все маршруты на карте
    @FXML
    private void showAllRoutersOnMap() {
        monitorController.showAllRoutersOnMap();
    }

}
