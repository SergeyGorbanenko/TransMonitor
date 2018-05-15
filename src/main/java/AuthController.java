import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AuthController {

    @FXML private TextField loginField;
    @FXML private TextField passwdField;
    @FXML private Hyperlink join;
    @FXML private Label redError;

    @FXML
    void initialize() {
        loginField.setText("admin");
        passwdField.setText("admin");
    }

    @FXML
    private void Join() {
        if (loginField.getText().equals("admin") && passwdField.getText().equals("admin"))
            Main.initMonitorLayout();
        else
            redError.setVisible(true);
    }

    @FXML
    private void HideRedError() {
        redError.setVisible(false);
    }

}
