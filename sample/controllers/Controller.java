package sample.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Impl.SearchQuery;
import sample.Impl.TextTabView;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    //Получаю доступ к элементам для управления
    @FXML
    TextField inputSearchQuery;
    @FXML
    javafx.scene.control.Label resultLabel;
    @FXML
    ChoiceBox choiseBoxI;
    @FXML
    Label zaprosTest;
    @FXML
    TreeView<File> treeViewMy;
    @FXML
    TabPane tabPaneMy;
    @FXML
    Button goBack;
    @FXML
    Button goNext;
    @FXML
    Button onSelectedAll;
    @FXML
    Button searchButton;

    private Image loupeImage;

    // Массив раширений для выбора
    private final String[] EXTENCION = new String[]{"log", "txt"};

    private String searchText = ""; //Поисковый запрос
    private SearchQuery searchQuery = new SearchQuery(); // Создание обекта класса для работы с поиском файлов
    private File path; //Переменная для работы с абстрактын путем

    //Инициализация стандартных параметров и обработчиков
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Инициализация картинки и установка для кнопки поиска
        loupeImage = new Image("Asset/loupe.png");
        searchButton.setGraphic(new ImageView(loupeImage));
        //Добавление выбора расширения
        choiseBoxI.setItems(FXCollections.observableArrayList(".log", ".txt"));
        //Установка по умолчанию
        choiseBoxI.getSelectionModel().selectFirst();
        // Лбработка нажатия на дерево каталогов
        treeViewMy.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Если было 2 нажатия раскрыть узел или открыть файл
                if (event.getClickCount() == 2){
                    //Получаю выделенный узел
                    TreeItem<File> fileTreeItem = treeViewMy.getSelectionModel().getSelectedItem();
                    File getFile = fileTreeItem.getValue();
                    // Если файл не папка открыть его
                    if (!getFile.isDirectory()){
                        // Вызов и передача данных методу для открытия файла в TextArea
                        boolean isOpen = new TextTabView().addTabOnView(tabPaneMy, getFile);
                        // Если метод открытия не открыл файл из-за кодировки сообщить пользователю
                        if (!isOpen){
                            zaprosTest.setText("Кодировка не поддерживается");
                        }
                    }
                }


            }
        });
    }

    // Обработка кнопки для выбора локальной папка
    public void onFolder(ActionEvent actionEvent) throws IOException, URISyntaxException {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        path = searchQuery.addFolder(stage); // Вызов окна для выбора папки
    }

    // Получить индекс эелемнта списка для определения раширения
    public int getChoiseBoxI() {
        int extension = choiseBoxI.getSelectionModel().getSelectedIndex();
        return extension;
    }

    //Получение содержимого из TextField для поискового запроса
    public void searchQueryInput(ActionEvent actionEvent) {
        searchText = inputSearchQuery.getText();
        zaprosTest.setText(searchText);
    }

    // Вызов поиска по нажатию кнопки с указанными параметрами, если данные верны
    public void clickSearchButton(ActionEvent actionEvent) throws InterruptedException, IOException {
        searchQueryInput(actionEvent);
        // Если поисковый запрос не пуст
        if (!searchText.isEmpty()){
            zaprosTest.setText("Поисковый запрос введен");
            // Путь указан
            if (!(path == null)){
                zaprosTest.setText(path.toString());
                // Вызываю метод для поиска и отрисовки дерева
                treeViewMy.setRoot(searchQuery.startSearch(path, searchText, EXTENCION[getChoiseBoxI()]));
                treeViewMy.setShowRoot(true);
                //Обнуление внутренних переменных
                searchQuery.destroy();
            }else {
                zaprosTest.setText("Выберите папку");
            }
        } else {
            zaprosTest.setText("Введите поисковый запрос!");
        }
    }

    // Обработчики кнопок для управления текстовым окном
    public void goBack(ActionEvent actionEvent) {
       // Получаю выбранное окно и его TextArea
        TextArea textArea = onSelectTab();
        // Сдвигаю содержимое TextArea вниз на 30
        textArea.setScrollTop(textArea.getScrollTop() - 30.0);
    }

    public void goNext(ActionEvent actionEvent) {
        // Получаю выбранное окно и его TextArea
        TextArea textArea = onSelectTab();
        // Сдвигаю содержимое TextArea вверх на 30
        textArea.setScrollTop(textArea.getScrollTop() + 30.0);
    }

    public void onSelectedAll(ActionEvent actionEvent) {
        // Получаю выбранное окно и его TextArea
        TextArea textArea = onSelectTab();
        // Выделяю все сожержимое TextArea
        textArea.selectAll();
    }

    // Приватный метод для получения активного окнка Tab
    private TextArea onSelectTab(){
        // Получаю актинвный Tab
        Tab tab = tabPaneMy.getTabs().filtered(Tab::isSelected).get(0);

        //Получаю содержимое Tab в виде его ребенка AnchorPane
        AnchorPane anchorPane = (AnchorPane) tab.getContent();
        //Получаю нужный элемент TextArea из AnchorPane
        TextArea textArea = (TextArea) anchorPane.getChildren().get(0);
        //Возвращаю его
        return textArea;
    }
    // Конец обработчиков текста
}
