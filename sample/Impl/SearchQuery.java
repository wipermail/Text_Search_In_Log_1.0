package sample.Impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import sample.Inter.ISearchQuery;
import sample.ext.ExtFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SearchQuery implements ISearchQuery {

    // Создание ICon для файлов и папок в дереве каталогов
    private final Image nodeFolder = new Image("/Asset/folder.png");
    private final Image nodeFile = new Image("/Asset/file.png");

    // Переменная для хранения поискового текста
    private String SEARCHTEXT;

    //Переменная для хранения расширения файла
    private String EXTENCION;

    // Создание узлов дерева
    private TreeItem<File> createNode(final File filePth) throws IOException {
        TreeItem<File> rootItem = null; //Создаю корневой векту
        TreeItem<File> fileTreeItem; //Создаю дочернюю ветку

        if (filePth != null && filePth.isDirectory()){ //если filepth не пустой и директория

            rootItem = new TreeItem<File>(new ExtFile(filePth.toString())); //инициализирую крневую ветку, добавляя имя
            rootItem.setGraphic(new ImageView(nodeFolder));
            // через переопределенный toString

            //Пробегаюсь по файлам
            for (File file : filePth.listFiles()) {

                System.out.println("Длина листа файлов" + filePth.listFiles().length);

                //Инциальзирую дочернюю ветку
                fileTreeItem = new TreeItem<File>(new ExtFile(file.toString()));

                //Если file является директорией задим в него через рекурсию
                if (file !=null && file.isDirectory()){
                    rootItem.getChildren().addAll(createNode(file));
                }else if (file == null){ //если file пустой возращаем корневую ветку
                    return rootItem;
                }else { //Иначе добавляю в корень дочернюю ветку
                    String is = isExtension(fileTreeItem);
                    System.out.println(is + " " + EXTENCION);
                    if (EXTENCION.compareTo(is) == 0 || fileTreeItem.getValue().isDirectory()){
                        if (matchFound(fileTreeItem.getValue())){
                            fileTreeItem.setGraphic(new ImageView(nodeFile));
                            rootItem.getChildren().add(fileTreeItem);
                        }
                    }
//                    System.out.println(Files.probeContentType(fileTreeItem.getValue().toPath()).toString());
                    // Обнюляю Дочернюю ветку из памяти
                    fileTreeItem = null;
                }
            }
        }

        //Если вдруг не дай бог ветка нулевая напомним мне об этом
        if (rootItem == null) {
            return rootItem = new TreeItem<File>(new ExtFile("Пусто"));
        }
        //Вернули батя-ветка в вызывающую часть
        return rootItem;
    }

    // Выбор папка для поиска
    @Override
    public File addFolder(Stage primaryStage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выбор папки");
        File defaultDirectory = new File("c:/");
        directoryChooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = directoryChooser.showDialog(primaryStage);
        return  selectedDirectory;
    }

    //Метод для старта, поиска и построения дерева на основе заданных критериев
    @Override
    public TreeItem<File> startSearch(File path, String searchText, String extencion) throws IOException {
        SEARCHTEXT = searchText;
        EXTENCION = extencion;
        TreeItem<File> root = createNode(path);
        return root;
    }

    // Проверка рассширения файла
    @Override
    public String isExtension(TreeItem<File> treeItem) {
        String fileName = treeItem.getValue().getName();
        // если в имени файла есть точка и она не является первым символом в названии файла
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            // то вырезаем все знаки после последней точки в названии файла, то есть ХХХХХ.txt -> txt
            return fileName.substring(fileName.lastIndexOf(".")+1);
            // в противном случае возвращаем заглушку, то есть расширение не найдено
        else return "";
    }

    //Поиск совпадения в тексте по поисковому запросу
    private boolean matchFound(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] content = new byte[fileInputStream.available()];
        fileInputStream.read(content);
        fileInputStream.close();
        String[] lines = new String(content, "UTF-8").split("\n");
        for (String line : lines){
            if (line.contains(SEARCHTEXT)){
                return true;
            }
        }
        return false;
    }

    // Обнуление переменных когда не нужны
    public void destroy(){
        SEARCHTEXT = null;
        EXTENCION = null;
    }
}
