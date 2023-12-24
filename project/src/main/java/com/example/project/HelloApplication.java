package com.example.project;

import javafx.application.Platform;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import org.jnativehook.dispatcher.SwingDispatchService;

import java.io.File;
import java.io.IOException;


public class HelloApplication extends Application {


    public static int image()
    {

        String folderPath = "C:/Users/User/Desktop/Учёба/Java/project/src/main/resources/images/screenshots";

        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        int count = 0;

        if (files != null) {
            for (File file : files)
            {
                if (file.isFile())
                {
                    String fileName = file.getName();
                    String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
                    // Проверяем расширение файла
                    if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png"))
                    {
                        count++;
                    }
                }
            }
        }
        if(count > 20)
        {
            int count2 = count % 5;
            count = (count - 20) / 5;
            if(count2 != 0)
            {
                return count + 1;
            }
            else
            {
                return count;
            }
        }
        return 0;
    }

    @Override
    public void start(Stage stage) throws IOException
    {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        ScrollPane scrollPane = fxmlLoader.load();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);


        AnchorPane anchorPane = (AnchorPane) scrollPane.getContent();

        int count = image();
        anchorPane.setPrefWidth(1100);
        anchorPane.setPrefHeight(900 + 220 * count);
        try {

            GlobalKeyListener keyListener = new GlobalKeyListener(stage, scrollPane);
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(keyListener);
            keyListener.Gallery();
        } catch (NativeHookException ex) {
            System.err.println("Ошибка при регистрации слушателя: " + ex.getMessage());
        }
        Scene scene = new Scene(scrollPane);
        stage.setTitle("ScreenReader");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args)
    {
        launch();
    }
}

