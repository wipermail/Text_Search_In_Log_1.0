package sample.Impl;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import sample.Inter.TextView;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class TextTabView implements TextView {

    // Метод зодания вкладки и открытия текстового файла
    @Override
    public boolean addTabOnView(TabPane tabPane, File file) {

        //Создание новой вкладки
        Tab tab = new Tab(file.getName());
        //Создание полотна для текстового поля
        AnchorPane anchorPane = new AnchorPane();
        //Создание переменной для текстового поля
        TextArea textArea = null;
        try {
            //Инициализация текствого поля и заполнение его
            textArea = new TextArea(readFile(file));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


        //Сделать текстовое полотно растягивающимся по всему AnchorPane
        AnchorPane.setBottomAnchor(textArea, 0.0);
        AnchorPane.setTopAnchor(textArea, 0.0);
        AnchorPane.setLeftAnchor(textArea, 0.0);
        AnchorPane.setRightAnchor(textArea, 0.0);
        //Добавить TextArea в AnchorPane
        anchorPane.getChildren().add(textArea);
        // Добавить AnchorPane на вкладку
        tab.setContent(anchorPane);
        //Добавить вкладку на полотно вкладок
        tabPane.getTabs().add(tab);
        return true;
    }

    ArrayList<String> stringArrayList = new ArrayList<String>();
    //Чтение и запись файла
    String readFile(File file) throws IOException {
        Files.lines(file.toPath(), StandardCharsets.UTF_8).forEach(a -> stringArrayList.add(a));
        String string = "";
        for (String str:stringArrayList) {
            string += str;
            string += "\n";
        }
        stringArrayList = null;
        return string;
    }
}
