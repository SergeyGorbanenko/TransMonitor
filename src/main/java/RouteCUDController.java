import dbService.DBService;
import dbService.HBUtil;
import hba.PointEntity;
import hba.RouteEntity;
import hba.RoutepointEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class RouteCUDController implements IController {

    private Stage stage = null;
    @Override
    public void initStage(Stage stage) {
        this.stage = stage;
    }

    @FXML public Label lblTitle;
    @FXML public Hyperlink linkAddNew;
    @FXML public Hyperlink linkDelete;
    @FXML public Hyperlink linkSave;
    //
    @FXML public Label lbloldvalueName;
    //
    @FXML public TextField txtName;
    @FXML public Label lblcapPoint1;
    @FXML public Label lblcapPoint2;
    @FXML public Label lblcapPoint3;
    @FXML public ComboBox<PointEntity> cmbPoint1;
    @FXML public ComboBox<PointEntity> cmbPoint2;
    @FXML public ComboBox<PointEntity> cmbPoint3;
    @FXML public CheckBox checkPoint1;
    @FXML public CheckBox checkPoint2;
    @FXML public CheckBox checkPoint3;

    private RouteEntity routeEntity = null;
    public void setRouteEntity(RouteEntity routeEntity) {
        this.routeEntity = routeEntity;
    }

    private ObjectListController objectListController;
    public void setObjectListController(ObjectListController objectListController) {
        this.objectListController = objectListController;
    }

    @FXML
    private void checkOnePoint() {
        checkThePoint(checkPoint1, lblcapPoint1, cmbPoint1);
        //
        if (!checkPoint1.isSelected()) {
            lblcapPoint2.setDisable(true);
            cmbPoint2.setDisable(true);
            cmbPoint2.setValue(null);
            checkPoint2.setSelected(false);
            checkPoint2.setDisable(true);
            lblcapPoint3.setDisable(true);
            cmbPoint3.setDisable(true);
            cmbPoint3.setValue(null);
            checkPoint3.setSelected(false);
            checkPoint3.setDisable(true);
        } else
            checkPoint2.setDisable(false);
    }

    @FXML
    private void checkSecondPoint() {
        checkThePoint(checkPoint2, lblcapPoint2, cmbPoint2);
        //
        if (!checkPoint2.isSelected()) {
            lblcapPoint3.setDisable(true);
            cmbPoint3.setDisable(true);
            cmbPoint3.setValue(null);
            checkPoint3.setSelected(false);
            checkPoint3.setDisable(true);
        } else
            checkPoint3.setDisable(false);
    }

    @FXML
    private void checkThirdPoint() {
        checkThePoint(checkPoint3, lblcapPoint3, cmbPoint3);
    }

    public void checkThePoint(CheckBox checkBox, Label label, ComboBox<PointEntity> comboBox) {
        if (checkBox.isSelected()) {
            label.setDisable(false);
            comboBox.setDisable(false);
        } else {
            label.setDisable(true);
            comboBox.setDisable(true);
            comboBox.setValue(null);
        }
    }


    private StringConverter<PointEntity> pointEntityStringConverter = new StringConverter<PointEntity>() {
        @Override
        public String toString(PointEntity pointEntity) {
            return pointEntity.getName();
        }
        @Override
        public PointEntity fromString(String string) {
            return null;
        }
    };

    //получить индекс Точки (Point) в obserList соответствующий редактируемому Маршруту (Route)
    public int getIndexOfPoint(PointEntity pointEntityChoose, ObservableList<PointEntity> ObservableList){
        int index = -1;
        for (PointEntity pointEntity : ObservableList)
            if (pointEntity.getIdPoint().equals(pointEntityChoose.getIdPoint()))
                index = ObservableList.indexOf(pointEntity);
        return index;
    }

    //инициализировать комбобоксы
    public void initCmbPoints() {
        cmbPoint1.setConverter(pointEntityStringConverter);
        cmbPoint1.getItems().clear();
        cmbPoint1.setItems(FXCollections.observableArrayList(DBService.loadPointList()));
        cmbPoint2.setConverter(pointEntityStringConverter);
        cmbPoint2.getItems().clear();
        cmbPoint2.setItems(FXCollections.observableArrayList(DBService.loadPointList()));
        cmbPoint3.setConverter(pointEntityStringConverter);
        cmbPoint3.getItems().clear();
        cmbPoint3.setItems(FXCollections.observableArrayList(DBService.loadPointList()));
    }

    //выбрать в комбобоксах соответственные точки
    public void associateCmbPoints() {
        List<RoutepointEntity> routepointEntityList = (List<RoutepointEntity>) routeEntity.getRoutepointsByIdRoute();
        if (routepointEntityList.size() >= 1)
            cmbPoint1.getSelectionModel().select(getIndexOfPoint(routepointEntityList.get(0).getPointByIdPoint(), FXCollections.observableArrayList(DBService.loadPointList())));
        else {
            checkPoint1.setSelected(false);
            checkOnePoint();
        }
        if (routepointEntityList.size() >= 2)
            cmbPoint2.getSelectionModel().select(getIndexOfPoint(routepointEntityList.get(1).getPointByIdPoint(), FXCollections.observableArrayList(DBService.loadPointList())));
        else {
            checkPoint2.setSelected(false);
            checkSecondPoint();
        }
        if (routepointEntityList.size() >= 3)
            cmbPoint3.getSelectionModel().select(getIndexOfPoint(routepointEntityList.get(2).getPointByIdPoint(), FXCollections.observableArrayList(DBService.loadPointList())));
        else {
            checkPoint3.setSelected(false);
            checkThirdPoint();
        }
    }


    @FXML       //Добавление нового маршрута
    private void performAddNewRoute() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (txtName.getText().isEmpty() || txtName.getText() == null) throw new NullPointerException();
            if (cmbPoint1.getValue() != null && cmbPoint2.getValue() == null && cmbPoint3.getValue() == null) throw new Exception();
            if (cmbPoint1.getValue() == null && checkPoint1.isSelected()) throw new Exception();
            if (cmbPoint2.getValue() == null && checkPoint2.isSelected()) throw new Exception();
            if (cmbPoint3.getValue() == null && checkPoint3.isSelected()) throw new Exception();
            RouteEntity routeEntity = new RouteEntity();
            routeEntity.setName(txtName.getText());
            transaction = session.getTransaction();
            transaction.begin();
            session.save(routeEntity);
            Collection<ComboBox<PointEntity>> boxCollection = FXCollections.observableArrayList(cmbPoint1, cmbPoint2, cmbPoint3);
            for (ComboBox<PointEntity> cmbPoint : boxCollection) {
                if (cmbPoint.getValue() != null) {
                    RoutepointEntity routepointEntity = new RoutepointEntity();
                    routepointEntity.setIdRoute(routeEntity.getIdRoute());
                    routepointEntity.setRouteByIdRoute(routeEntity);
                    routepointEntity.setIdPoint(cmbPoint.getValue().getIdPoint());
                    routepointEntity.setPointByIdPoint(cmbPoint.getValue());
                    session.save(routepointEntity);
                }
            }
            transaction.commit();
            //
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Добавлен новый маршрут");
            alert.setHeaderText(null);
            alert.setContentText("Маршрут \"" + txtName.getText() + "\" был успешно добавлен");
            alert.showAndWait();
            this.stage.close();
            if (objectListController != null)
                objectListController.viewRouters(DBService.loadRouteList());
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText("- поле \"Название\" не может быть пустым");
            alert.showAndWait();
            if (transaction != null) {
                transaction.rollback();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText("- требуется указать точки маршрута\n" +
                                 "- не может быть меньше двух точек");
            alert.showAndWait();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //Редактирование маршрута
    private void performEditRoute() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (txtName.getText().isEmpty() || txtName.getText() == null) throw new NullPointerException();
            if (cmbPoint1.getValue() != null && cmbPoint2.getValue() == null && cmbPoint3.getValue() == null) throw new Exception();
            if (cmbPoint1.getValue() == null && checkPoint1.isSelected()) throw new Exception();
            if (cmbPoint2.getValue() == null && checkPoint2.isSelected()) throw new Exception();
            if (cmbPoint3.getValue() == null && checkPoint3.isSelected()) throw new Exception();
            routeEntity.setName(txtName.getText());
            transaction = session.getTransaction();
            transaction.begin();
            session.update(routeEntity);
            Collection<RoutepointEntity> routepointEntityCollection = routeEntity.getRoutepointsByIdRoute();
            for (RoutepointEntity rpE : routepointEntityCollection) {
                session.delete(rpE);
            }
            Collection<ComboBox<PointEntity>> boxCollection = FXCollections.observableArrayList(cmbPoint1, cmbPoint2, cmbPoint3);
            for (ComboBox<PointEntity> cmbPoint : boxCollection) {
                if (cmbPoint.getValue() != null) {
                    RoutepointEntity routepointEntity = new RoutepointEntity();
                    routepointEntity.setIdRoute(routeEntity.getIdRoute());
                    routepointEntity.setRouteByIdRoute(routeEntity);
                    routepointEntity.setIdPoint(cmbPoint.getValue().getIdPoint());
                    routepointEntity.setPointByIdPoint(cmbPoint.getValue());
                    session.save(routepointEntity);
                }
            }
            transaction.commit();
            //
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Изменен маршрут");
            alert.setHeaderText(null);
            alert.setContentText("Маршрут \"" + txtName.getText() + "\" был успешно отредактирован");
            alert.showAndWait();
            this.stage.close();
            if (objectListController != null)
                objectListController.viewRouters(DBService.loadRouteList());
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText("- поле \"Название\" не может быть пустым");
            alert.showAndWait();
            if (transaction != null) {
                transaction.rollback();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText("- требуется указать точки маршрута\n" +
                                 "- не может быть меньше двух точек");
            alert.showAndWait();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //Удаление маршрута
    private void performDeleteRoute() {
        String strName = routeEntity.getName();
        Alert alertSure = new Alert(Alert.AlertType.CONFIRMATION);
        alertSure.setTitle("Удаление маршрута");
        alertSure.setHeaderText("Удалить маршрут " + "[" + strName + "]" + "?");
        alertSure.setContentText("Вы уверены, что хотите удалить маршрут " + "[" + strName + "]" + "?");
        Optional<ButtonType> result = alertSure.showAndWait();
        if (result.get() == ButtonType.OK){
            Transaction transaction = null;
            Session session = HBUtil.getSessionFactory().openSession();
            try {
                if (!routeEntity.getWaybillsByIdRoute().isEmpty()) throw new Exception();
                transaction = session.beginTransaction();
                Collection<RoutepointEntity> routepointEntityCollection = routeEntity.getRoutepointsByIdRoute();
                for (RoutepointEntity rpE : routepointEntityCollection) {
                    session.delete(rpE);
                }
                session.delete(routeEntity);
                transaction.commit();
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Удален маршрут");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("Маршрут " + "[" + strName + "]" + " был успешно удален!");
                alertSuccess.showAndWait();
                this.stage.close();
                if (objectListController != null)
                    objectListController.viewRouters(DBService.loadRouteList());
            } catch (Exception e) {
                e.printStackTrace();
                if (transaction != null) {
                    transaction.rollback();
                }
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Ошибка");
                alertError.setHeaderText("Удаление невозможно");
                alertError.setContentText("Вы не можете удалить маршрут, по которому есть путевые листы");
                alertError.showAndWait();
            } finally {
                if (session != null)
                    session.close();
            }
        } else {
            alertSure.hide();
        }
    }

}
