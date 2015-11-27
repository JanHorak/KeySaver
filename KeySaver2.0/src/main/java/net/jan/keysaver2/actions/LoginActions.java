/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.actions;

import javafx.event.EventHandler;
import net.jan.keysaver2.sources.PageLoadHelper;

/**
 *
 * @author Jan
 */
public final class LoginActions extends GeneralActions {

    private LoginActions() {
    }

    public static EventHandler loginAction = (e) -> {
        System.out.println("t");
    };

    public static EventHandler openRegisterAction = (e) -> {
        new PageLoadHelper().loadRegisterDialog();
    };

}
