import dbService.DBService;
import dbService.HBUtil;
import hba.DriverEntity;
import hba.VehicleEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public class DriverCUDController implements IController {

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
    @FXML public Label lbloldvalueFIO;
    @FXML public Label lbloldvalueVunum;
    @FXML public Label lbloldvalueVehicle;
    //
    @FXML public TextField txtFIO;
    @FXML public TextField txtVunum;
    @FXML public ComboBox<VehicleEntity> cmbVehicle;

    private DriverEntity driverEntity = null;
    public void setDriverEntity(DriverEntity driverEntity) {
        this.driverEntity = driverEntity;
    }

    private ObjectListController objectListController;
    public void setObjectListController(ObjectListController objectListController) {
        this.objectListController = objectListController;
    }

    public static StringConverter<VehicleEntity> vehicleEntityStringConverter = new StringConverter<VehicleEntity>() {
        @Override
        public String toString(VehicleEntity vehicleEntity) {
            return vehicleEntity.getModel() + " " + vehicleEntity.getRegnum();
        }
        @Override
        public VehicleEntity fromString(String string) {
            return null;
        }
    };

    //получить индекс ТС (Vehicle) в obserList соответствующий редактируемому Водителю (Driver)
    public static int getIndexOfVehicle(DriverEntity driverEntityChoose, ObservableList<VehicleEntity> ObservableList){
        int index = -1;
        for (VehicleEntity vehicleEntity : ObservableList)
            if (vehicleEntity.getIdVehicle().equals(driverEntityChoose.getIdVehicle()))
                index = ObservableList.indexOf(vehicleEntity);
        return index;
    }

    //инициализировать комбобокс
    public void initCmbVehicle() {
        cmbVehicle.setConverter(vehicleEntityStringConverter);
        cmbVehicle.getItems().clear();
        cmbVehicle.setItems(FXCollections.observableArrayList(DBService.loadVehicleList()));
    }


    @FXML       //Добавление нового водителя
    private void performAddNewDriver() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (txtFIO.getText().isEmpty() || txtFIO.getText() == null) throw new NullPointerException();
            if (txtVunum.getText().isEmpty() || txtVunum.getText() == null) throw new NullPointerException();
            if (cmbVehicle.getValue() == null) throw new Exception();
            DriverEntity driverEntity = new DriverEntity();
            driverEntity.setFio(txtFIO.getText());
            driverEntity.setVunum(txtVunum.getText());
            driverEntity.setIdVehicle(cmbVehicle.getValue().getIdVehicle());
            driverEntity.setVehicleByIdVehicle(cmbVehicle.getValue());
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.save(driverEntity);
            transaction.commit();
            //
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Добавлен новый водитель");
            alert.setHeaderText(null);
            alert.setContentText("Водитель \"" + txtFIO.getText() + "\" был успешно добавлен");
            alert.showAndWait();
            this.stage.close();
            if (objectListController != null)
                objectListController.viewDrivers(DBService.loadDriverList());
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText("- поля \"ФИО\" и \"Номер ВУ\" не могут быть пустыми");
            alert.showAndWait();
            if (transaction != null) {
                transaction.rollback();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText("- требуется указать транспортное средство");
            alert.showAndWait();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //Редактирование водителя
    private void performEditDriver() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (txtFIO.getText().isEmpty() || txtFIO.getText() == null) throw new NullPointerException();
            if (txtVunum.getText().isEmpty() || txtVunum.getText() == null) throw new NullPointerException();
            if (cmbVehicle.getValue() == null) throw new Exception();
            driverEntity.setFio(txtFIO.getText());
            driverEntity.setVunum(txtVunum.getText());
            driverEntity.setIdVehicle(cmbVehicle.getValue().getIdVehicle());
            driverEntity.setVehicleByIdVehicle(cmbVehicle.getValue());
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.update(driverEntity);
            transaction.commit();
            //
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Изменен водитель");
            alert.setHeaderText(null);
            alert.setContentText("Водитель \"" + txtFIO.getText() + "\" был успешно отредактирован");
            alert.showAndWait();
            this.stage.close();
            if (objectListController != null)
                objectListController.viewDrivers(DBService.loadDriverList());
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText("- поля \"ФИО\" и \"Номер ВУ\" не могут быть пустыми");
            alert.showAndWait();
            if (transaction != null) {
                transaction.rollback();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText("- требуется указать транспортное средство");
            alert.showAndWait();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //Удаление водителя
    private void performDeleteDriver() {
        String strFIO = driverEntity.getFio();
        Alert alertSure = new Alert(Alert.AlertType.CONFIRMATION);
        alertSure.setTitle("Удаление водителя");
        alertSure.setHeaderText("Удалить водителя " + "[" + strFIO + "]" + "?");
        alertSure.setContentText("Вы уверены, что хотите удалить водителя " + "[" + strFIO + "]" + "?");
        Optional<ButtonType> result = alertSure.showAndWait();
        if (result.get() == ButtonType.OK){
            Transaction transaction = null;
            Session session = HBUtil.getSessionFactory().openSession();
            try {
                transaction = session.beginTransaction();
                session.delete(this.driverEntity);
                transaction.commit();
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Удален водитель");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("Водитель " + "[" + strFIO + "]" + " был успешно удален!");
                alertSuccess.showAndWait();
                this.stage.close();
                if (objectListController != null)
                    objectListController.viewDrivers(DBService.loadDriverList());
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
