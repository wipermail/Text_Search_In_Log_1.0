package sample.Inter;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public interface ISearchQuery {
    File addFolder(Stage primaryStage);
    TreeItem<File> startSearch(File path, String searchText, String exceptionFile) throws IOException;
    String isExtension(TreeItem<File> fileTreeItem);
}
