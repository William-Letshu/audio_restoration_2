module com.example.audio_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.knowm.xchart;
    requires java.desktop;
    requires commons.math3;


    opens com.example.audio_2 to javafx.fxml;
    exports com.example.audio_2;
}