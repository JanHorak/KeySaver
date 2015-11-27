/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.actions;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 *
 * @author Jan
 */
public abstract class GeneralActions {
    
    public EventHandler closeWindow = (e) -> {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    };
    
}
