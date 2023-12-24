package com.example.project;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextArea;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import  javafx.scene.layout.Pane;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.control.Button;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class GlobalKeyListener implements NativeKeyListener {

    private boolean flag = false;
    private ScrollPane scrollPane;

    String folderPath = "E:/Programms/Проект Тюрин/project/src/main/resources/images/screenshots";
    String folderPath2 = "E:/Programms/Проект Тюрин/project/src/main/resources/images/Text";
    private Stage stage;
    private List<ImageView> imageView_list = new ArrayList<>();
    private AnchorPane anchorPane;
    private boolean isControlPressed = false;
    private boolean isQPressed = false;
    private int quantity = 0;


    @Override
    public void nativeKeyPressed(NativeKeyEvent e)
    {

        if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
            isControlPressed = true;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_Q) {
            isQPressed = true;
        }

        // Проверка комбинации Ctrl + Q
        if (isControlPressed && isQPressed)
        {
            EventQueue.invokeLater(() -> {
                Main captureTool = new Main();
                captureTool.setVisible(true);
            });
        }
    }

    public GlobalKeyListener(Stage stage, ScrollPane scrollPane)
    {
        this.stage = stage;
        this.scrollPane = scrollPane;
        anchorPane = (AnchorPane) scrollPane.getContent();

    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
            isControlPressed = false;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_Q) {
            isQPressed = false;
        }
    }
    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {

    }

    public void Gallery()
    {
        Platform.runLater(() -> {
            anchorPane.getChildren().clear();
            imageView_list.clear();
            File folder = new File(folderPath);
            File[] files = folder.listFiles();
            int count = 0, height = 0;
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
                            quantity++;
                            String time_variable;
                            time_variable = fileName.replace(".jpg", ".txt").replace(".png", ".txt");
                            ImageView imageView = new ImageView();
                            Image image = new Image(file.toURI().toString());

                            imageView.setImage(image);
                           // imageView.setPreserveRatio(true);
                           // Rectangle2D viewportRect = new Rectangle2D(image.getWidth() / 2, image.getHeight() / 2, 200, 200);
                            //imageView.setViewport(viewportRect);
                            imageView_list.addLast(imageView);
                            imageView.setFitWidth(200);
                            imageView.setFitHeight(200);
                            anchorPane.getChildren().add(imageView);

                            if(count == 5)
                            {
                                count = 0;
                                height = height + 220;
                            }
                            imageView.setLayoutX(count * 220);
                            imageView.setLayoutY(height);
                            count++;
                            anchorPane.requestLayout();
                            imageView.setOnMouseClicked(event -> {
                                // Создаем новый Stage для отображения полноразмерного изображения
                                Stage imageStage = new Stage();
                                imageStage.setOnCloseRequest(events -> {
                                    stage.show();
                                });
                                ImageView fullImageView = new ImageView();
                                fullImageView.setImage(image);
                                Pane pane = new Pane();
                                ScrollPane scrollPane = new ScrollPane();
                                scrollPane.setContent(fullImageView);
                                pane.setPrefWidth(1900);
                                pane.setPrefHeight(1050);
                                scrollPane.setPrefWidth(1000);
                                scrollPane.setPrefHeight(1050);
                                pane.getChildren().add(scrollPane);
                                scrollPane.setLayoutX(0);
                                scrollPane.setLayoutY(0);

                                TextArea textArea = new TextArea();
                                textArea.setWrapText(true);
                                textArea.setPrefWidth(600);
                                textArea.setPrefHeight(500);
                                textArea.setLayoutX(1100);
                                textArea.setLayoutY(325);
                                textArea.setText("Привет");
                                pane.getChildren().add(textArea);

                                Button button = new Button("Готово");
                                button.setOnAction(event2 -> {
                                    try {
                                        FileWriter fileWriter = new FileWriter(folderPath2 + "/" + time_variable);
                                        BufferedWriter writer = new BufferedWriter(fileWriter);
                                        writer.write(textArea.getText());
                                        writer.close();

                                    } catch (IOException e) {
                                        System.out.println("Ошибка при создании и сохранении файла: " + e.getMessage());
                                    }
                                });
                                button.setLayoutX(1770);
                                button.setLayoutY(575);
                                pane.getChildren().add(button);

                                // Устанавливаем обработчик события нажатия мыши
                                textArea.setOnMouseClicked(event2 -> {
                                    if (event.getButton() == MouseButton.PRIMARY) { // Проверяем, что нажата левая кнопка мыши
                                        textArea.setEditable(true); // Устанавливаем поле редактируемым
                                    }
                                });

                                // Создаем новую Scene с StackPane и устанавливаем ее в Stage
                                Scene scene = new Scene(pane);
                                imageStage.setScene(scene);
                                imageStage.show();
                                stage.hide();
                            });
                        }
                    }
                }
            }
        });

    }


    public class Main extends JFrame {
        private Rectangle captureRect;

        public Main() {


            setUndecorated(true);
            setOpacity(0.3f);
            setSize(Toolkit.getDefaultToolkit().getScreenSize());
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

            final Point start = new Point();

            getContentPane().addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    start.setLocation(e.getPoint());
                    captureRect = new Rectangle(start);
                }

                public void mouseReleased(MouseEvent e) {
                    capture();
                }
            });

            getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    Point end = e.getPoint();
                    captureRect.setSize(end.x - captureRect.x, end.y - captureRect.y);
                    repaint();
                }
            });
        }

        public void capture() {
            try {
                Robot robot = new Robot();
                BufferedImage screenshot = robot.createScreenCapture(captureRect);
                dispose();


                File directory = new File(folderPath);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String format = "png";
                quantity++;
                String name = quantity +  "." + format;
                File file = new File(folderPath, name);
                ImageIO.write(screenshot, format, file);
                Platform.runLater(() -> Gallery());
            } catch (AWTException | IOException e) {
                e.printStackTrace();
            }
        }

        public void paint(Graphics g) {
            super.paint(g);
            if (captureRect != null) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.RED);
                g2d.draw(captureRect);
                g2d.setColor(new Color(35, 40, 45, 50));
                g2d.fill(captureRect);
            }
        }
    }
}