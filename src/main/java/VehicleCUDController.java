import dbService.DBService;
import dbService.HBUtil;
import hba.VehicleEntity;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public class VehicleCUDController implements IController {

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
    @FXML public Label lbloldvalueModel;
    @FXML public Label lbloldvalueRegnum;
    //
    @FXML public TextField txtModel;
    @FXML public TextField txtRegnum;

    private VehicleEntity vehicleEntity = null;
    public void setVehicleEntity(VehicleEntity vehicleEntity) {
        this.vehicleEntity = vehicleEntity;
    }

    private ObjectListController objectListController;
    public void setObjectListController(ObjectListController objectListController) {
        this.objectListController = objectListController;
    }

    @FXML       //Добавление нового ТС
    private void performAddNewVehicle() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (txtModel.getText().isEmpty() || txtModel.getText() == null) throw new NullPointerException();
            if (txtRegnum.getText().isEmpty() || txtRegnum.getText() == null) throw new NullPointerException();
            VehicleEntity vehicleEntity = new VehicleEntity();
            vehicleEntity.setModel(txtModel.getText());
            vehicleEntity.setRegnum(txtRegnum.getText());
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.save(vehicleEntity);
            transaction.commit();
            //

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Добавлено новое транспортное средство");
            alert.setHeaderText(null);
            alert.setContentText("Транспорт \"" + txtModel.getText() + "\" был успешно добавлен");
            alert.showAndWait();
            this.stage.close();
            if (objectListController != null)
                objectListController.viewVehicles(DBService.loadVehicleList());
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- поля \"Модель\" и \"Рег. номер\" не могут быть пустыми\n");
            alert.showAndWait();
            if (transaction != null) {
                transaction.rollback();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //Редактирование ТС
    private void performEditVehicle() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (txtModel.getText().isEmpty() || txtModel.getText() == null) throw new NullPointerException();
            if (txtRegnum.getText().isEmpty() || txtRegnum.getText() == null) throw new NullPointerException();
            vehicleEntity.setModel(txtModel.getText());
            vehicleEntity.setRegnum(txtRegnum.getText());
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.update(vehicleEntity);
            transaction.commit();
            //
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Изменено транспортное средство");
            alert.setHeaderText(null);
            alert.setContentText("Транспорт \"" + txtModel.getText() + "\" был успешно отредактирован");
            alert.showAndWait();
            this.stage.close();
            if (objectListController != null)
                objectListController.viewVehicles(DBService.loadVehicleList());
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- поля \"Модель\" и \"Рег. номер\" не могут быть пустыми\n");
            alert.showAndWait();
            if (transaction != null) {
                transaction.rollback();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //Удаление ТС
    private void performDeleteVehicle() {
        String strModel = vehicleEntity.getModel();
        String strRegnum = vehicleEntity.getRegnum();
        Alert alertSure = new Alert(Alert.AlertType.CONFIRMATION);
        alertSure.setTitle("Удаление транспортного средства");
        alertSure.setHeaderText("Удалить транспорт " + "[" + strModel + " " + strRegnum + "]" + "?");
        alertSure.setContentText("Вы уверены, что хотите удалить транспорт " + "[" + strModel + " " + strRegnum + "]" + "?");
        Optional<ButtonType> result = alertSure.showAndWait();
        if (result.get() == ButtonType.OK){
            Transaction transaction = null;
            Session session = HBUtil.getSessionFactory().openSession();
            try {
                if (!vehicleEntity.getDriversByIdVehicle().isEmpty()) throw new Exception();
                if (!vehicleEntity.getWaybillsByIdVehicle().isEmpty()) throw new Exception();
                if (!vehicleEntity.getMessagesByIdVehicle().isEmpty()) throw new Exception();
                transaction = session.beginTransaction();
                session.delete(this.vehicleEntity);
                transaction.commit();
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Удалено транспортное средство");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("Транспорт " + "[" + strModel + " " + strRegnum + "]" + " был успешно удален!");
                alertSuccess.showAndWait();
                this.stage.close();
                if (objectListController != null)
                    objectListController.viewVehicles(DBService.loadVehicleList());
            } catch (Exception e) {
                e.printStackTrace();
                if (transaction != null) {
                    transaction.rollback();
                }
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Ошибка");
                alertError.setHeaderText("Удаление невозможно");
                alertError.setContentText("- у ТС не должно быть водителей\n" +
                                          "- у ТС не должно быть путевых листов\n" +
                                          "- у ТС не должно быть истории сообщений");
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
