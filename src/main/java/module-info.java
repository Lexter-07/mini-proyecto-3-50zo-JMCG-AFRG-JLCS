/**
 * Defines the Java module for the Cincuentazo application. <p>
 *
 * This module declares the required JavaFX dependencies, exports the public
 * packages that compose the application's API, and opens the necessary
 * packages to the JavaFX runtime for FXML loading and reflection-based
 * controller injection.
 */
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
    exports com.example.cincuentazo.model.intefaces;
    opens com.example.cincuentazo.model.intefaces to javafx.fxml;

}