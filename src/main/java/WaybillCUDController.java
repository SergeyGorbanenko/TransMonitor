import dbService.DBService;
import dbService.HBUtil;
import hba.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public class WaybillCUDController implements IController {

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
    @FXML public Label lbloldvalueVehicle;
    @FXML public Label lbloldvalueRoute;
    //
    @FXML public ComboBox<VehicleEntity> cmbVehicle;
    @FXML public ComboBox<RouteEntity> cmbRoute;

    private WaybillEntity waybillEntity = null;
    public void setWaybillEntity(WaybillEntity waybillEntity) {
        this.waybillEntity = waybillEntity;
    }

    private ObjectListController objectListController;
    public void setObjectListController(ObjectListController objectListController) {
        this.objectListController = objectListController;
    }

    private StringConverter<VehicleEntity> vehicleEntityStringConverter = new StringConverter<VehicleEntity>() {
        @Override
        public String toString(VehicleEntity vehicleEntity) {
            return vehicleEntity.getModel() + " " + vehicleEntity.getRegnum();
        }
        @Override
        public VehicleEntity fromString(String string) {
            return null;
        }
    };

    private StringConverter<RouteEntity> routeEntityStringConverter = new StringConverter<RouteEntity>() {
        @Override
        public String toString(RouteEntity routeEntity) {
            return routeEntity.getName();
        }
        @Override
        public RouteEntity fromString(String string) {
            return null;
        }
    };

    //получить индекс ТС (Vehicle) в obserList соответствующий редактируемому Путевому листу (Waybill)
    public int getIndexOfVehicle(WaybillEntity waybillEntityChoose, ObservableList<VehicleEntity> ObservableList){
        int index = -1;
        for (VehicleEntity vehicleEntity : ObservableList)
            if (vehicleEntity.getIdVehicle().equals(waybillEntityChoose.getIdVehicle()))
                index = ObservableList.indexOf(vehicleEntity);
        return index;
    }

    //получить индекс Маршрута (Route) в obserList соответствующий редактируемому Путевому листу (Waybill)
    public int getIndexOfRoute(WaybillEntity waybillEntityChoose, ObservableList<RouteEntity> ObservableList){
        int index = -1;
        for (RouteEntity routeEntity : ObservableList)
            if (routeEntity.getIdRoute().equals(waybillEntityChoose.getIdRoute()))
                index = ObservableList.indexOf(routeEntity);
        return index;
    }

    //инициализировать комбобоксы
    public void initCmbVehicle() {
        cmbVehicle.setConverter(vehicleEntityStringConverter);
        cmbVehicle.getItems().clear();
        cmbVehicle.setItems(FXCollections.observableArrayList(DBService.loadVehicleList()));
    }

    public void initCmbRoute() {
        cmbRoute.setConverter(routeEntityStringConverter);
        cmbRoute.getItems().clear();
        cmbRoute.setItems(FXCollections.observableArrayList(DBService.loadRouteList()));
    }


    @FXML       //Добавление нового путевого листа
    private void performAddNewWaybill() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (cmbVehicle.getValue() == null || cmbRoute.getValue() == null) throw new NullPointerException();
            WaybillEntity waybillEntity = new WaybillEntity();
            waybillEntity.setIdVehicle(cmbVehicle.getValue().getIdVehicle());
            waybillEntity.setIdRoute(cmbRoute.getValue().getIdRoute());
            waybillEntity.setVehicleByIdVehicle(cmbVehicle.getValue());
            waybillEntity.setRouteByIdRoute(cmbRoute.getValue());
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.save(waybillEntity);
            transaction.commit();
            //
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Добавлен новый путевой лист");
            alert.setHeaderText(null);
            alert.setContentText("Путевой лист был успешно добавлен");
            alert.showAndWait();
            this.stage.close();
            if (objectListController != null)
                objectListController.viewWaybills(DBService.loadWaybillList());
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность указанных данных");
            alert.setContentText(   "- требуется выбрать транспорт и его маршрут");
            alert.showAndWait();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //Удаление путевого листа
    private void performDeleteWaybill() {
        String strIdWaybill = waybillEntity.getIdWaybill().toString();
        Alert alertSure = new Alert(Alert.AlertType.CONFIRMATION);
        alertSure.setTitle("Удаление путевого листа");
        alertSure.setHeaderText("Удалить путевой лист № " + strIdWaybill + "?");
        alertSure.setContentText("Вы уверены, что хотите удалить путевой лист № " + strIdWaybill + "?");
        Optional<ButtonType> result = alertSure.showAndWait();
        if (result.get() == ButtonType.OK){
            Transaction transaction = null;
            Session session = HBUtil.getSessionFactory().openSession();
            try {
                transaction = session.beginTransaction();
                session.delete(this.waybillEntity);
                transaction.commit();
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Удалён путевой лист");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("Путевой лист № " + strIdWaybill + " был успешно удален!");
                alertSuccess.showAndWait();
                this.stage.close();
                if (objectListController != null)
                    objectListController.viewWaybills(DBService.loadWaybillList());
            } catch (Exception e) {
                e.printStackTrace();
                if (transaction != null) {
                    transaction.rollback();
                }
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Ошибка");
                alertError.setHeaderText(null);
                alertError.setContentText("Что-то пошло не так...");
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
