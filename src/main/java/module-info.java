module com.example.cincuentazo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cincuentazo to javafx.fxml;
    opens com.example.cincuentazo.controller to javafx.fxml;
    opens com.example.cincuentazo.model to javafx.fxml;
    exports com.example.cincuentazo;
    exports com.example.cincuentazo.model;
    exports com.example.cincuentazo.model.threads;
    opens com.example.cincuentazo.model.threads to javafx.fxml;

}