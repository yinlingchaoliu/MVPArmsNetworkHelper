package com.network.helper.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;

public class ToastUtil {
    private static void showNotification(Project project, MessageType type, String text) {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(text, type, null)
                .setFadeoutTime(7500)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
    }

    public static void showError(Project project, String msg) {
        showNotification(project, MessageType.ERROR, msg);
    }

    public static void showInfo(Project project, String msg) {
        showNotification(project, MessageType.INFO, msg);
    }

    public static void show(Project project, String msg) {
        showNotification(project, MessageType.INFO, msg);
    }

    public static void show( String msg) {
        System.out.println(msg);
    }

}
