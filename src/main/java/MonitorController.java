import dbService.DBService;
import dbService.HBUtil;
import hba.*;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class MonitorController {

    @FXML public Hyperlink linkShowAllPointOnMap;
    @FXML public Hyperlink linkShowAllRoutersOnMap;
    @FXML public Hyperlink linkClearMap;
    //
    @FXML public VBox messageVBox;
    @FXML public Hyperlink linkClearMessageVBox;

    @FXML
    private void Logout() {
        Main.initAuthorizationLayout();
    }

    public static IController initChildStage(String FXMLpath, String TitleStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(FXMLpath));
            BorderPane objectlistLayout = (BorderPane) loader.load();
            Stage objectListStage = new Stage();
            objectListStage.setTitle(TitleStage);
            objectListStage.initModality(Modality.NONE);
            objectListStage.initOwner(Main.primaryStage);
            Scene objectlistScene = new Scene(objectlistLayout);
            objectListStage.setScene(objectlistScene);
            objectListStage.show();
            IController iController = loader.getController();
            iController.initStage(objectListStage);
            return iController;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Stage retStag(Stage stg) {
        return stg;
    }

    /** О программе **/

    @FXML
    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("О программе");
        alert.setHeaderText("Монитор транспорта v.0.1");
        alert.setContentText("Разработано в рамках курсовой работы\n" +
                "\"Проектирование интерфейса программного обеспечения навигационного мониторинга грузовых перевозок\"\n" +
                "© 2018 АлтГТУ · Горбаненко Сергей · ПИ-41");
        alert.showAndWait();
    }

    /** Просмотр списка объектов **/

    @FXML
    private void showVehicleList() {
        ObjectListController objectListController = (ObjectListController) initChildStage("ObjectList.fxml", "Список транспортных средств");
        objectListController.lblTitle.setText("              Транспортные средства");
        objectListController.linkAddNewObject.setText("     Добавить транспортное средство");
        objectListController.linkAddNewObject.setOnAction(event -> {
            VehicleCUDController vehicleCUDController = goVehicleAdd();
            vehicleCUDController.setObjectListController(objectListController);
        });
        objectListController.viewVehicles(DBService.loadVehicleList());
        objectListController.setMonitorController(this);
        objectListController.linkShowAllPointOnMap.setVisible(false);
        objectListController.linkShowAllRoutersOnMap.setVisible(false);
        objectListController.linkRefresh.setOnAction(event -> {
            objectListController.viewVehicles(DBService.loadVehicleList());
        });
    }

    @FXML
    private void showDriverList() {
        ObjectListController objectListController = (ObjectListController) initChildStage("ObjectList.fxml", "Список водителей");
        objectListController.lblTitle.setText("                         Водители");
        objectListController.linkAddNewObject.setText("                Добавить водителя");
        objectListController.linkAddNewObject.setOnAction(event -> {
            DriverCUDController driverCUDController = goDriverAdd();
            driverCUDController.setObjectListController(objectListController);
        });
        objectListController.viewDrivers(DBService.loadDriverList());
        objectListController.setMonitorController(this);
        objectListController.linkShowAllPointOnMap.setVisible(false);
        objectListController.linkShowAllRoutersOnMap.setVisible(false);
        objectListController.linkRefresh.setOnAction(event -> {
            objectListController.viewDrivers(DBService.loadDriverList());
        });
    }

    @FXML
    private void showRouteList() {
        ObjectListController objectListController = (ObjectListController) initChildStage("ObjectList.fxml", "Список маршрутов");
        objectListController.lblTitle.setText("                         Маршруты");
        objectListController.linkAddNewObject.setText("                Добавить маршрут");
        objectListController.linkAddNewObject.setOnAction(event -> {
            RouteCUDController routeCUDController = goRouteAdd();
            routeCUDController.setObjectListController(objectListController);
        });
        objectListController.viewRouters(DBService.loadRouteList());
        objectListController.setMonitorController(this);
        objectListController.linkShowAllPointOnMap.setVisible(false);
        objectListController.linkShowAllRoutersOnMap.setVisible(true);
        objectListController.linkRefresh.setOnAction(event -> {
            objectListController.viewRouters(DBService.loadRouteList());
        });
    }

    @FXML
    private void showPointList() {
        ObjectListController objectListController = (ObjectListController) initChildStage("ObjectList.fxml", "Список точек маршрута");
        objectListController.lblTitle.setText("                    Точки маршрута");
        objectListController.linkAddNewObject.setText("           Добавить точку маршрута");
        objectListController.linkAddNewObject.setOnAction(event -> {
            PointCUDController pointCUDController = goPointAdd();
            pointCUDController.setObjectListController(objectListController);
        });
        objectListController.viewPoints(DBService.loadPointList());
        objectListController.setMonitorController(this);
        objectListController.linkShowAllPointOnMap.setVisible(true);
        objectListController.linkShowAllRoutersOnMap.setVisible(false);
        objectListController.linkRefresh.setOnAction(event -> {
            objectListController.viewPoints(DBService.loadPointList());
        });
    }

    @FXML
    private void showWaybillList() {
        ObjectListController objectListController = (ObjectListController) initChildStage("ObjectList.fxml", "Список путевых листов");
        objectListController.lblTitle.setText("                     Путевые листы");
        objectListController.linkAddNewObject.setText("            Добавить путевой лист");
        objectListController.linkAddNewObject.setOnAction(event -> {
            WaybillCUDController waybillCUDController = goWaybillAdd();
            waybillCUDController.setObjectListController(objectListController);
        });
        objectListController.viewWaybills(DBService.loadWaybillList());
        objectListController.setMonitorController(this);
        objectListController.linkShowAllPointOnMap.setVisible(false);
        objectListController.linkShowAllRoutersOnMap.setVisible(false);
        objectListController.linkRefresh.setOnAction(event -> {
            objectListController.viewWaybills(DBService.loadWaybillList());
        });
    }

    @FXML
    private void showMessagesList() {
        MessageListController messageListController = (MessageListController) initChildStage("MessageList.fxml", "Сообщения от транспорта");
        messageListController.initCmbVehicle();
    }

    /** Добавление / редактирование / удаление объекта **/

    @FXML
    private VehicleCUDController goVehicleAdd() {
        VehicleCUDController vehicleCUDController = (VehicleCUDController) initChildStage("VehicleCUD.fxml", "Добавить ТС");
        vehicleCUDController.linkAddNew.setVisible(true);
        vehicleCUDController.linkSave.setVisible(false);
        vehicleCUDController.linkDelete.setVisible(false);
        //
        vehicleCUDController.lbloldvalueModel.setVisible(false);
        vehicleCUDController.lbloldvalueRegnum.setVisible(false);
        return vehicleCUDController;
    }

    @FXML
    private DriverCUDController goDriverAdd() {
        DriverCUDController driverCUDController = (DriverCUDController) initChildStage("DriverCUD.fxml", "Добавить водителя");
        driverCUDController.linkAddNew.setVisible(true);
        driverCUDController.linkSave.setVisible(false);
        driverCUDController.linkDelete.setVisible(false);
        //
        driverCUDController.lbloldvalueFIO.setVisible(false);
        driverCUDController.lbloldvalueVunum.setVisible(false);
        driverCUDController.lbloldvalueVehicle.setVisible(false);
        //
        driverCUDController.initCmbVehicle();
        return driverCUDController;
    }

    @FXML
    private RouteCUDController goRouteAdd() {
        RouteCUDController routeCUDController = (RouteCUDController) initChildStage("RouteCUD.fxml", "Добавить маршрут");
        routeCUDController.linkAddNew.setVisible(true);
        routeCUDController.linkSave.setVisible(false);
        routeCUDController.linkDelete.setVisible(false);
        //
        routeCUDController.lbloldvalueName.setVisible(false);
        //
        routeCUDController.initCmbPoints();
        return routeCUDController;
    }

    @FXML
    private PointCUDController goPointAdd() {
        PointCUDController pointCUDController = (PointCUDController) initChildStage("PointCUD.fxml", "Добавить точку маршрута");
        pointCUDController.linkAddNew.setVisible(true);
        pointCUDController.linkSave.setVisible(false);
        pointCUDController.linkDelete.setVisible(false);
        pointCUDController.linkShowPointOnMap.setVisible(false);
        //
        pointCUDController.lbloldvalueName.setVisible(false);
        pointCUDController.lbloldvalueXcoord.setVisible(false);
        pointCUDController.lbloldvalueYcoord.setVisible(false);
        //
        pointCUDController.setMonitorController(this);
        return pointCUDController;
    }

    @FXML
    private WaybillCUDController goWaybillAdd() {
        WaybillCUDController waybillCUDController = (WaybillCUDController) initChildStage("WaybillCUD.fxml", "Добавить путевой лист");
        waybillCUDController.linkAddNew.setVisible(true);
        waybillCUDController.linkSave.setVisible(false);
        waybillCUDController.linkDelete.setVisible(false);
        //
        waybillCUDController.lbloldvalueVehicle.setVisible(false);
        waybillCUDController.lbloldvalueRoute.setVisible(false);
        //
        waybillCUDController.initCmbVehicle();
        waybillCUDController.initCmbRoute();
        return waybillCUDController;
    }

    /** Карта **/

    @FXML protected Pane mapPane;
    public void goPointAddFromMap() {
        mapPane.setOnMouseClicked((MouseEvent e) -> {
            PointCUDController pointCUDController = (PointCUDController) initChildStage("PointCUD.fxml", "Добавить точку маршрута");
            pointCUDController.linkAddNew.setVisible(true);
            pointCUDController.linkSave.setVisible(false);
            pointCUDController.linkDelete.setVisible(false);
            pointCUDController.linkShowPointOnMap.setVisible(false);
            //
            pointCUDController.lbloldvalueName.setVisible(false);
            pointCUDController.lbloldvalueXcoord.setVisible(false);
            pointCUDController.lbloldvalueYcoord.setVisible(false);
            //
            pointCUDController.txtXcoord.setText(String.valueOf(e.getSceneX()));
            pointCUDController.txtYcoord.setText(String.valueOf(e.getSceneY()));
            //
            pointCUDController.setMonitorController(this);
        });
        /*mapPane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            PointCUDController pointCUDController = (PointCUDController) initChildStage("PointCUD.fxml", "Добавить точку маршрута");
            pointCUDController.linkAddNew.setVisible(true);
            pointCUDController.linkSave.setVisible(false);
            pointCUDController.linkDelete.setVisible(false);
            pointCUDController.linkShowPointOnMap.setVisible(false);
            //
            pointCUDController.lbloldvalueName.setVisible(false);
            pointCUDController.lbloldvalueXcoord.setVisible(false);
            pointCUDController.lbloldvalueYcoord.setVisible(false);
            //
            pointCUDController.txtXcoord.setText(String.valueOf(e.getSceneX()));
            pointCUDController.txtYcoord.setText(String.valueOf(e.getSceneY()));
            //
            pointCUDController.setMonitorController(this);
        });*/
    }


    /** Визуализация **/

    //Воспроизвести маршрут на карте
    public void playRoute(VehicleEntity vehicleEntity, RouteEntity routeEntity, Color color) {
        List<RoutepointEntity> routePointList = (List<RoutepointEntity>) routeEntity.getRoutepointsByIdRoute();
        //транспорт
        Circle vehicleCircle = new Circle();
        vehicleCircle.setRadius(7);
        vehicleCircle.setFill(color);
        vehicleCircle.setTranslateX(routePointList.get(0).getPointByIdPoint().getxCoord());
        vehicleCircle.setTranslateY(routePointList.get(0).getPointByIdPoint().getyCoord() - 25);
        //маршрут
        TranslateTransition tT1 = new TranslateTransition();
        tT1.setNode(vehicleCircle);
        tT1.setToX(routePointList.get(1).getPointByIdPoint().getxCoord());
        tT1.setToY(routePointList.get(1).getPointByIdPoint().getyCoord() - 25);
        tT1.setDuration(Duration.seconds(calcStepDuration(vehicleCircle.getTranslateX(), vehicleCircle.getTranslateY(), routePointList.get(1).getPointByIdPoint())));
        tT1.play();
        tT1.setOnFinished(event1 -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.play();
            pause.setOnFinished(event2 -> {
                TranslateTransition tT2 = new TranslateTransition();
                tT2.setNode(vehicleCircle);
                tT2.setToX(routePointList.get(2).getPointByIdPoint().getxCoord());
                tT2.setToY(routePointList.get(2).getPointByIdPoint().getyCoord() - 25);
                tT2.setDuration(Duration.seconds(calcStepDuration(vehicleCircle.getTranslateX(), vehicleCircle.getTranslateY(), routePointList.get(2).getPointByIdPoint())));
                tT2.play();
            });
        });
        drawLineOfRoute(routePointList, color);
        drawRectOnPoint(routePointList);
        mapPane.getChildren().add(vehicleCircle);
        //Когда точка перемещается, она отправляет сообщения
        Bindings.bindBidirectional(linkClearMap.textProperty(), vehicleCircle.translateXProperty(), new StringConverter<Number>() {
            int i = 0;
            @Override
            public String toString(Number object) {
                if (i % 30 == 0)
                    sendMessage(vehicleEntity, vehicleCircle.getTranslateX(), vehicleCircle.getTranslateY());
                i++;
                return "";
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });
    }

    //Рисует линию маршрута на карте
    public void drawLineOfRoute(List<RoutepointEntity> routePointList, Color color) {
        Line line1 = new Line();
        line1.setStartX(routePointList.get(0).getPointByIdPoint().getxCoord());
        line1.setStartY(routePointList.get(0).getPointByIdPoint().getyCoord() - 25);
        line1.setEndX(routePointList.get(1).getPointByIdPoint().getxCoord());
        line1.setEndY(routePointList.get(1).getPointByIdPoint().getyCoord() - 25);
        line1.setStroke(color);
        line1.setStrokeWidth(3);
        mapPane.getChildren().add(line1);
        if (routePointList.size() >= 3) {
            Line line2 = new Line();
            line2.setStartX(routePointList.get(1).getPointByIdPoint().getxCoord());
            line2.setStartY(routePointList.get(1).getPointByIdPoint().getyCoord() - 25);
            line2.setEndX(routePointList.get(2).getPointByIdPoint().getxCoord());
            line2.setEndY(routePointList.get(2).getPointByIdPoint().getyCoord() - 25);
            line2.setStroke(color);
            line2.setStrokeWidth(3);
            mapPane.getChildren().add(line2);
        }
    }

    //Рисует точки маршрута на карте
    public void drawRectOnPoint(List<RoutepointEntity> routePointList) {
        for (RoutepointEntity rpE : routePointList) {
            Hyperlink linkPoint = new Hyperlink();
            linkPoint.setPrefWidth(24);
            linkPoint.setPrefHeight(24);
            linkPoint.setStyle("-fx-underline: false; -fx-background-image: url('icons/ic_place_black_24dp_1x.png');");
            linkPoint.setLayoutX(rpE.getPointByIdPoint().getxCoord() - 12);
            linkPoint.setLayoutY(rpE.getPointByIdPoint().getyCoord() - 49);
            linkPoint.setOnAction(event -> {
                PointCUDController pointCUDController = (PointCUDController) initChildStage("PointCUD.fxml", "Редактировать точку маршрута");
                pointCUDController.linkAddNew.setVisible(false);
                pointCUDController.linkSave.setVisible(true);
                pointCUDController.linkDelete.setVisible(true);
                pointCUDController.lblTitle.setText("      Редактировать точку маршрута");
                pointCUDController.setPointEntity(rpE.getPointByIdPoint());
                //
                pointCUDController.lbloldvalueName.setVisible(true);
                pointCUDController.lbloldvalueXcoord.setVisible(true);
                pointCUDController.lbloldvalueYcoord.setVisible(true);
                pointCUDController.lbloldvalueName.setText(rpE.getPointByIdPoint().getName());
                pointCUDController.lbloldvalueXcoord.setText("X: " + String.valueOf(rpE.getPointByIdPoint().getxCoord()));
                pointCUDController.lbloldvalueYcoord.setText("Y: " + String.valueOf(rpE.getPointByIdPoint().getyCoord()));
                //
                pointCUDController.txtName.setText(rpE.getPointByIdPoint().getName());
                pointCUDController.txtXcoord.setText(String.valueOf(rpE.getPointByIdPoint().getxCoord()));
                pointCUDController.txtYcoord.setText(String.valueOf(rpE.getPointByIdPoint().getyCoord()));
            });
            mapPane.getChildren().add(linkPoint);
        }
    }

    //Вычисляет суммарную длительность воспроизведения всего маршрута
    public double calcFullDuration(List<RoutepointEntity> routePointList) {
        Point2D p1 = new Point2D(routePointList.get(0).getPointByIdPoint().getxCoord(), routePointList.get(0).getPointByIdPoint().getyCoord() - 25);
        Point2D p2 = new Point2D(routePointList.get(1).getPointByIdPoint().getxCoord(), routePointList.get(1).getPointByIdPoint().getyCoord() - 25);
        Double dur1 = p1.distance(p2);
        Double dur2 = null;
        if (routePointList.size() >= 3) {
            Point2D p3 = new Point2D(routePointList.get(2).getPointByIdPoint().getxCoord(), routePointList.get(2).getPointByIdPoint().getyCoord() - 25);
            dur2 = p2.distance(p3);
        }
        if (dur2 != null)
            return (dur1 + dur2)/50;
        else
            return dur1/30;
    }

    //Вычисляет длительность воспроизведения участка маршрута
    public double calcStepDuration(Double pointX, Double pointY, PointEntity pointEntity) {
        Point2D p1 = new Point2D(pointX, pointY - 25);
        Point2D p2 = new Point2D(pointEntity.getxCoord(), pointEntity.getyCoord() - 25);
        return p1.distance(p2)/40;
    }


    /** Генерация сообщений от транспорта**/

    //Генерация сообщения и запись его в базу
    public void sendMessage(VehicleEntity vehicleEntity, Double pointX, Double pointY) {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            MessageEntity message = new MessageEntity();
            message.setIdVehicle(vehicleEntity.getIdVehicle());
            message.setVehicleByIdVehicle(vehicleEntity);
            message.setxCoord(pointX);
            message.setyCoord(pointY);
            message.setDatetime(LocalDateTime.now());
            message.setSpeed((Math.random()*20)+40);
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.save(message);
            transaction.commit();
            //
            showMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
    }
    //Вывод сгенерированного сообщения
    public void showMessage(MessageEntity messageEntity) {
        Label lblVehicle = new Label(messageEntity.getVehicleByIdVehicle().getModel() + " ");
        Label lblXcoord = new Label("x" + messageEntity.getxCoord().intValue() + " ");
        Label lblYcoord = new Label("y" + messageEntity.getyCoord().intValue() + " ");
        Label lblSpeed = new Label("s" + messageEntity.getSpeed().intValue() + " ");
        Label lblTime = new Label(messageEntity.getDatetime().getMinute() + ":" + messageEntity.getDatetime().getSecond());
        lblVehicle.setFont(new Font("Arial Bold", 12));
        lblXcoord.setFont(new Font("Arial", 12));
        lblXcoord.setTextFill(Color.BLUE);
        lblYcoord.setFont(new Font("Arial", 12));
        lblYcoord.setTextFill(Color.ORANGE);
        lblSpeed.setFont(new Font("Arial", 12));
        lblSpeed.setTextFill(Color.GREEN);
        lblTime.setFont(new Font("Arial", 12));
        lblTime.setTextFill(Color.VIOLET);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(lblVehicle, lblXcoord, lblYcoord, lblSpeed, lblTime);
        messageVBox.getChildren().add(hBox);
        if (messageVBox.getChildren().size() > 15) {
            messageVBox.getChildren().remove(0, 1);
        }
    }
    //Очистка messageVBox
    public void clearMessageBox() {
        messageVBox.getChildren().clear();
    }


    /** Кнопки карты **/

    @FXML       //Очистить карту
    public void clearMap() {
        mapPane.getChildren().clear();
    }

    @FXML       //Показать все точки на карте
    public void showAllPointOnMap() {
        Collection<PointEntity> pointEntityCollection = DBService.loadPointList();
        for (PointEntity pointEntity : pointEntityCollection) {
            showOnePointOneMap(pointEntity);
        }
    }

    public void showOnePointOneMap(PointEntity pointEntity) {
        Hyperlink linkPoint = new Hyperlink();
        linkPoint.setPrefWidth(24);
        linkPoint.setPrefHeight(24);
        linkPoint.setStyle("-fx-underline: false; -fx-background-image: url('icons/ic_place_black_24dp_1x.png');");
        linkPoint.setLayoutX(pointEntity.getxCoord() - 12);
        linkPoint.setLayoutY(pointEntity.getyCoord() - 49);
        linkPoint.setOnAction(event -> {
            PointCUDController pointCUDController = (PointCUDController) initChildStage("PointCUD.fxml", "Редактировать точку маршрута");
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
            pointCUDController.setMonitorController(this);

        });
        mapPane.getChildren().add(linkPoint);
        //
        ScaleTransition scale = new ScaleTransition(new Duration(1000));
        scale.setNode(linkPoint);
        scale.setByX(3);
        scale.setByY(3);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }

    @FXML       //Показать все маршруты на карте
    public void showAllRoutersOnMap() {
        Collection<RouteEntity> routeEntityCollection = DBService.loadRouteList();
        for (RouteEntity routeEntity : routeEntityCollection) {
            List<RoutepointEntity> routepointEntityList = (List<RoutepointEntity>) routeEntity.getRoutepointsByIdRoute();
            drawLineOfRoute(routepointEntityList, Color.color(Math.random(), Math.random(), Math.random()));
        }
    }

}
