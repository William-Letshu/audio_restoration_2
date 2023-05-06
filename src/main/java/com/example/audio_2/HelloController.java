package com.example.audio_2;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import static com.example.audio_2.WaveForm.drawWaveform;

public class HelloController {
    File selected;
    @FXML
    private ImageView mel_after;
    @FXML
    private ImageView mel_before;
    @FXML
    private ImageView spectrum_after;
    @FXML
    private ImageView spectrum_before;
    @FXML
    private ImageView wave_after;
    @FXML
    private ImageView wave_before;

    @FXML
    private void perform_analysis() {
        setMel_before();
        setWave_before();
        setSpectrum_before();
    }

    public void setSpectrum_before() {
        if (selected != null){
            String imagePath = "src/main/resources/com/example/audio_2/Images/before_spectrum.png";
            Path currentPath = Paths.get("").toAbsolutePath();
            String scriptPath = currentPath.resolve("src").resolve("main").resolve("java").resolve("com").
                    resolve("example").resolve("audio_2").resolve("draw_spectrum.py").toString();

            RunPythonScript.runPythonScript(scriptPath,selected.getPath(),"before_spectrum.png");
            this.spectrum_before.setImage(setImageViewFromPath(imagePath));
        }else{
            System.out.println("File hasn't been selected");
        }
    }

    @FXML
    public void setWave_before() {
        if (selected != null){
            String imagePath = "src/main/resources/com/example/audio_2/Images/before_waveform.png";
            Path currentPath = Paths.get("").toAbsolutePath();
            String scriptPath = currentPath.resolve("src").resolve("main").resolve("java").resolve("com").
            resolve("example").resolve("audio_2").resolve("draw_waveform.py").toString();

            RunPythonScript.runPythonScript(scriptPath,selected.getPath(),"before_waveform.png");
            this.wave_before.setImage(setImageViewFromPath(imagePath));
        }else{
            System.out.println("File hasn't been selected");
        }
    }



    @FXML
    protected void setMel_before() {
        if (selected != null){
            Path currentPath = Paths.get("").toAbsolutePath();
            String scriptPath = currentPath.resolve("src").resolve("main").resolve("java").resolve("com").
            resolve("example").resolve("audio_2").resolve("script.py").toString();

            RunPythonScript.runPythonScript(scriptPath,selected.getPath(),"before_mel_spectrogram.png");
            
            String imagePath = "src/main/resources/com/example/audio_2/Images/before_mel_spectrogram.png";
            this.mel_before.setImage(setImageViewFromPath(imagePath));
        }else{
            System.out.println("File hasn't been selected");
        }
    }

    public static Image setImageViewFromPath(String imagePath) {
        // Create a File object from the image path.
        File imageFile = new File(imagePath);
        
        // Create a proper URL from the File object.
        String imageURL = imageFile.toURI().toString();
        
        // Create an Image object from the image URL.
        Image image = new Image(imageURL);
        
        // Create an ImageView object.
        ImageView imageView = new ImageView();
        
        // Set the image to the ImageView object.
        imageView.setImage(image);
        
        return image;
    }

    /**
     * This method is used to choose a file
     * And store the selected file in variable called "selected"
     */
    @FXML
    protected void choose_audio_file(){
        System.out.println("Choosing file");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your audio file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files","*.wav"));
        selected = fileChooser.showOpenDialog(new Stage());
        if (selected != null){
            System.out.println("File has been successfully loaded");
        }
    }

    @FXML
    private void handleImageViewClick(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        enlargeImage(imageView.getImage());
    }

    private void enlargeImage(Image image) {
        Stage stage = new Stage();
        stage.setTitle("Enlarged Image");

        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(800); // Set the desired width of the enlarged image

        ScrollPane scrollPane = new ScrollPane(imageView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Scene scene = new Scene(scrollPane, 800, 600); // Set the desired dimensions for the new window
        stage.setScene(scene);
        stage.show();
    }
}