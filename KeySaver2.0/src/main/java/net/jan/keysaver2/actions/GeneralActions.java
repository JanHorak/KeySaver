/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.actions;

import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Jan
 */
public class GeneralActions {
    
    public EventHandler closeWindow = (e) -> {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    };
    
    
    public static void fadeIn(Control c) {
        FadeTransition fadeTransition
                = new FadeTransition(Duration.millis(500), c);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    public static void fadeOut(Control c) {
        FadeTransition fadeTransition
                = new FadeTransition(Duration.millis(500), c);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
    }
    
}
