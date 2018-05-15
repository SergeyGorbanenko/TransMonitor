import dbService.DBService;
import dbService.HBUtil;
import hba.PointEntity;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public class PointCUDController implements IController {

    private Stage stage = null;
    @Override
    public void initStage(Stage stage) {
        this.stage = stage;
    }

    @FXML public Label lblTitle;
    @FXML public Hyperlink linkAddNew;
    @FXML public Hyperlink linkDelete;
    @FXML public Hyperlink linkSave;
    @FXML public Hyperlink linkShowPointOnMap;
    //
    @FXML public Label lbloldvalueName;
    @FXML public Label lbloldvalueXcoord;
    @FXML public Label lbloldvalueYcoord;
    //
    @FXML public TextField txtName;
    @FXML public TextField txtXcoord;
    @FXML public TextField txtYcoord;

    private PointEntity pointEntity = null;
    public void setPointEntity(PointEntity pointEntity) {
        this.pointEntity = pointEntity;
    }

    private MonitorController monitorController;
    public void setMonitorController(MonitorController monitorController) {
        this.monitorController = monitorController;
    }

    private ObjectListController objectListController;
    public void setObjectListController(ObjectListController objectListController) {
        this.objectListController = objectListController;
    }

    @FXML
    private void showEditPointOnMap() {
        //if (monitorController != null)
        monitorController.showOnePointOneMap(pointEntity);
    }

    @FXML       //Добавление новой точки
    private void performAddNewPoint() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (txtName.getText().isEmpty() || txtName.getText() == null) throw new NullPointerException();
            if (txtXcoord.getText().isEmpty() || txtXcoord.getText() == null) throw new NullPointerException();
            if (txtYcoord.getText().isEmpty() || txtYcoord.getText() == null) throw new NullPointerException();
            if (Double.valueOf(txtXcoord.getText()) > 815 || Double.valueOf(txtXcoord.getText()) < 4) throw new Exception();
            if (Double.valueOf(txtYcoord.getText()) > 595 || Double.valueOf(txtYcoord.getText()) < 29) throw new Exception();
            PointEntity pointEntity = new PointEntity();
            pointEntity.setName(txtName.getText());
            pointEntity.setxCoord(Double.valueOf(txtXcoord.getText()));
            pointEntity.setyCoord(Double.valueOf(txtYcoord.getText()));
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.save(pointEntity);
            transaction.commit();
            //
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Добавлена новая точка");
            alert.setHeaderText(null);
            alert.setContentText("Точка \"" + txtName.getText() + "\" была успешно добавлена");
            alert.showAndWait();
            this.stage.close();
            if (monitorController != null) {
                monitorController.clearMap();
                monitorController.showAllPointOnMap();
            }
            if (objectListController != null)
                objectListController.viewPoints(DBService.loadPointList());
        } catch (NumberFormatException nfe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- поля координат точки могут содержать только цифры");
            alert.showAndWait();
            if (transaction != null) {
                transaction.rollback();
            }
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- поле \"Название\" не может быть пустым\n" +
                                    "- поля координат точки не могут быть пустыми");
            alert.showAndWait();
            if (transaction != null) {
                transaction.rollback();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- поля координат точки не могут выходить за пределы карты");
            alert.showAndWait();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //Редактирование точки
    private void performEditPoint() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (txtName.getText().isEmpty() || txtName.getText() == null) throw new NullPointerException();
            if (txtXcoord.getText().isEmpty() || txtXcoord.getText() == null) throw new NullPointerException();
            if (txtYcoord.getText().isEmpty() || txtYcoord.getText() == null) throw new NullPointerException();
            if (Double.valueOf(txtXcoord.getText()) > 815 || Double.valueOf(txtXcoord.getText()) < 4) throw new Exception();
            if (Double.valueOf(txtYcoord.getText()) > 595 || Double.valueOf(txtYcoord.getText()) < 29) throw new Exception();
            pointEntity.setName(txtName.getText());
            pointEntity.setxCoord(Double.valueOf(txtXcoord.getText()));
            pointEntity.setyCoord(Double.valueOf(txtYcoord.getText()));
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.update(pointEntity);
            transaction.commit();
            //
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Изменена точка");
            alert.setHeaderText(null);
            alert.setContentText("Точка \"" + txtName.getText() + "\" была успешно отредактирована");
            alert.showAndWait();
            this.stage.close();
            if (monitorController != null) {
                monitorController.clearMap();
                monitorController.showAllPointOnMap();
            }
            if (objectListController != null)
                objectListController.viewPoints(DBService.loadPointList());
        } catch (NumberFormatException nfe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- поля координат точки могут содержать только цифры");
            alert.showAndWait();
            if (transaction != null) {
                transaction.rollback();
            }
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- поле \"Название\" не может быть пустым\n" +
                    "- поля координат точки не могут быть пустыми");
            alert.showAndWait();
            if (transaction != null) {
                transaction.rollback();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- поля координат точки не могут выходить за пределы карты");
            alert.showAndWait();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //Удаление точки
    private void performDeletePoint() {
        String strName = pointEntity.getName();
        String strXcoord = pointEntity.getxCoord().toString();
        String strYcoord = pointEntity.getyCoord().toString();
        Alert alertSure = new Alert(Alert.AlertType.CONFIRMATION);
        alertSure.setTitle("Удаление точки");
        alertSure.setHeaderText("Удалить точку " + "[" + strName + " x: " + strXcoord + " y: " + strYcoord + "]" + "?");
        alertSure.setContentText("Вы уверены, что хотите удалить точку " + "[" + strName + " x: " + strXcoord + " y: " + strYcoord + "]" + "?");
        Optional<ButtonType> result = alertSure.showAndWait();
        if (result.get() == ButtonType.OK){
            Transaction transaction = null;
            Session session = HBUtil.getSessionFactory().openSession();
            try {
                if (!pointEntity.getRoutepointsByIdPoint().isEmpty()) throw new Exception();
                transaction = session.beginTransaction();
                session.delete(this.pointEntity);
                transaction.commit();
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Удалена точка");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("Точка" + "[" + strName + " x: " + strXcoord + " y: " + strYcoord + "]" + " была успешно удалена!");
                alertSuccess.showAndWait();
                this.stage.close();
                if (monitorController != null) {
                    monitorController.clearMap();
                    monitorController.showAllPointOnMap();
                }
                if (objectListController != null)
                    objectListController.viewPoints(DBService.loadPointList());
            } catch (Exception e) {
                e.printStackTrace();
                if (transaction != null) {
                    transaction.rollback();
                }
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Ошибка");
                alertError.setHeaderText("Удаление невозможно");
                alertError.setContentText("Вы не можете изменить точку, через которую проходит маршрут");
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
