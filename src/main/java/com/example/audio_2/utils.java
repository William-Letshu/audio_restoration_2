package com.example.audio_2;
import javafx.scene.control.Alert;

public class utils {

    public static void pop_msg() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Please select a audio file first");
        alert.showAndWait();
    }

}
