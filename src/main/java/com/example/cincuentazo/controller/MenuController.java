package com.example.cincuentazo.controller;

import com.example.cincuentazo.config.GameSettings;
import com.example.cincuentazo.view.Path;
import com.example.cincuentazo.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import java.io.IOException;

/**
 * Controller responsible for handling all interactions
 * in the main menu view. <p>
 *
 * This controller manages:
 * - Opponent selection (1, 2, or 3 AI players).
 * - Visual feedback for the selected option.
 * - Scene transitions to the game screen.
 * - Application termination. <p>
 *
 * The selected opponent count is maintained internally
 * and can later be passed to the game initialization logic.
 *
 * @author - Jose Manuel Cardona Gil. <p>
 *         - Jorge Luis Castro Scarpetta.
 * @version 1.2
 */
public class MenuController {

    @FXML
    private Button oneMachine;

    @FXML
    private Button twoMachine;

    @FXML
    private Button threeMachine;

    /**
     * Stores the number of AI opponents selected by the user. <p>
     *
     * Default value is 1, matching the initial active button
     * displayed in the menu interface.
     */
    private int selectedMachines = 1;

    /**
     * Initializes menu components and registers the event handlers
     * for the opponent selection buttons. <p>
     *
     * The method also establishes the default configuration
     * by selecting one AI opponent when the menu is loaded.
     */
    @FXML
    public void initialize() {

        oneMachine.setOnAction(e -> selectMachineCount(1));
        twoMachine.setOnAction(e -> selectMachineCount(2));
        threeMachine.setOnAction(e -> selectMachineCount(3));

        selectMachineCount(1);
    }

    /**
     * Updates the selected number of AI opponents and refreshes
     * the visual state of the selector buttons. <p>
     *
     * Only one button can remain active at a time.
     * The active button receives the "selector-active"
     * CSS class, while all others use the "selector-button"
     * CSS class.
     *
     * @param machines number of AI opponents selected
     */
    private void selectMachineCount(int machines) {

        selectedMachines = machines;

        GameSettings.setMachineCount(machines);

        oneMachine.getStyleClass().remove("selector-active");
        twoMachine.getStyleClass().remove("selector-active");
        threeMachine.getStyleClass().remove("selector-active");

        oneMachine.getStyleClass().remove("selector-button");
        twoMachine.getStyleClass().remove("selector-button");
        threeMachine.getStyleClass().remove("selector-button");

        oneMachine.getStyleClass().add("selector-button");
        twoMachine.getStyleClass().add("selector-button");
        threeMachine.getStyleClass().add("selector-button");

        switch (machines) {

            case 1:
                oneMachine.getStyleClass().remove("selector-button");
                oneMachine.getStyleClass().add("selector-active");
                break;

            case 2:
                twoMachine.getStyleClass().remove("selector-button");
                twoMachine.getStyleClass().add("selector-active");
                break;

            case 3:
                threeMachine.getStyleClass().remove("selector-button");
                threeMachine.getStyleClass().add("selector-active");
                break;
        }

        System.out.println("Máquinas seleccionadas: " + selectedMachines);
    }

    /**
     * Starts the game using the currently selected
     * number of AI opponents. <p>
     *
     * This method triggers the transition from the menu
     * view to the game view.
     *
     * @param event button action event
     * @throws IOException if the game view cannot be loaded
     */
    @FXML
    void onHandlePlay2(ActionEvent event) throws IOException {

        System.out.println("Jugar contra " + selectedMachines + " máquina(s)");

        GameSettings.setMachineCount(selectedMachines);
        
        SceneManager.changeScene(Path.GameView);
    }

    /**
     * Closes the application. <p>
     *
     * This action is triggered when the user presses
     * the exit button in the main menu.
     *
     * @param event button action event
     */
    @FXML
    void onHandleExit(ActionEvent event) {
        System.exit(0);
    }
}
