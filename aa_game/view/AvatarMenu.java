package view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.User;

import java.io.File;
import java.net.URL;

public class AvatarMenu extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Pane avatarPane = FXMLLoader.load(
                new URL(EntryMenu.class.getResource("/FXML/AvatarMenu.fxml").toExternalForm()));
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        hBox.setSpacing(15);
        for(String imageAddress : User.getAvatarsMap().values()) {
            ImageView avatar = new ImageView(
                    new Image(imageAddress, 100, 100, false, false));
            EventHandler<MouseEvent> changeAvatarEvent = mouseEvent -> {
                try {
                    changeAvatar(imageAddress);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            avatar.setOnMouseClicked(changeAvatarEvent);
            hBox.getChildren().add(avatar);
        }

        FileChooser fileChooser = new FileChooser();

        Button chooseFile = new Button();
        chooseFile.setText("choose file");

        EventHandler<MouseEvent> chooseFileEvent = mouseEvent -> {
            File file = fileChooser.showOpenDialog(stage);
            try {
                changeAvatar(file.toURI().toURL().toExternalForm());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        chooseFile.setOnMouseClicked(chooseFileEvent);

        Button back = new Button();
        back.setText("back");

        EventHandler<MouseEvent> backEvent = mouseEvent -> {
            try {
                new ProfileMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        back.setOnMouseClicked(backEvent);

        vBox.getChildren().addAll(hBox, chooseFile, back);

        avatarPane.getChildren().add(vBox);
        vBox.setLayoutX(150);
        vBox.setSpacing(10);

        Scene scene = new Scene(avatarPane);
        stage.setScene(scene);
        stage.show();
    }



    private void changeAvatar(String imageAddress) throws Exception {
        User.getCurrentUser().setImage(imageAddress);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("changed avatar successfully");
        alert.setContentText("changed avatar successfully");
        alert.showAndWait();
        new ProfileMenu().start(EntryMenu.stage);
    }
}
