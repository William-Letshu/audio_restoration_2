package com.example.audio_2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class HelloController {
    
    private boolean noise = false;
    private boolean equalize = false;
    private boolean loud = false;
    private boolean de_clip = false;
    File nextFile = null;
    File selected;
    File temp;
    
    @FXML
    private RadioButton radio_noise;
    @FXML
    private RadioButton radio_equalize;
    @FXML
    private RadioButton radio_loud;
    @FXML
    private RadioButton radio_clip;

    
    @FXML
    private Text clipping_text;
    @FXML
    private Text Clipping;
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
    private Text selected_step;

    @FXML
    private void update_noise(){
        if( this.noise){
            this.noise = false;
            String temp =this.selected_step.getText();
            temp = temp.replace("\nNoise Reduction","");
            selected_step.setText(temp);
        }else{
            this.noise = true;
            String temp =this.selected_step.getText();
            selected_step.setText(temp+"\nNoise Reduction");
        }
    }

    @FXML
    private void update_equalize(){
        if (this.equalize){
            this.equalize = false;
            String temp =this.selected_step.getText();
            temp = temp.replace("\nEqualization","");
            selected_step.setText(temp);
        }else{
            this.equalize = true;
            String temp =selected_step.getText();
            selected_step.setText(temp+"\nEqualization");
        }
    }

    
    @FXML
    private void update_de_clipping(){
        if (this.de_clip){
            this.de_clip = false;
            String temp =selected_step.getText();
            temp = temp.replace("\nDe_clip","");
            selected_step.setText(temp);
        }else{
            this.de_clip = true;
            String temp =selected_step.getText();
            selected_step.setText(temp+"\nDe_clip");
        }
    }

    @FXML
    private void update_loud(){
        if (this.loud){
            this.loud = false;
            String temp =selected_step.getText();
            temp = temp.replace("\nLoud","");
            selected_step.setText(temp);
        }else{
            this.loud = true;
            String temp =selected_step.getText();
            selected_step.setText(temp+"\nLoud");
        }
    }

    @FXML
    private void perform_all(){
        System.out.println("All procedures selected");
        if(this.selected != null){
            
            Map<String, String> scriptMap = new LinkedHashMap<>();
            scriptMap.put("Noise_Reduction_generated", "noise_reduction.py");
            scriptMap.put("De_clip_generated", "de_clip.py");
            scriptMap.put("Equalization_generated", "equalize_audio.py");
            scriptMap.put("Loud_generated", "loudness_normalization.py");
        
            for (Map.Entry<String, String> entry : scriptMap.entrySet()) {
                String audio_path = String.format("src/main/resources/com/example/audio_2/Audio/%s.wav", entry.getKey().toLowerCase());
                Path currentPath = Paths.get("").toAbsolutePath();
                String scriptPath = currentPath.resolve("src").resolve("main").resolve("java").resolve("com").
                        resolve("example").resolve("audio_2").resolve("python").
                        resolve(entry.getValue()).toString();
        
                if(nextFile == null){
                    RunPythonScript.runPythonScript(scriptPath,selected.getPath(),audio_path);
                }else{
                    RunPythonScript.runPythonScript(scriptPath,nextFile.getPath(),audio_path);
                }
        
                nextFile = new File(audio_path);
            }

            temp = this.selected;
            this.selected = nextFile;
            setMel_after();
            setSpectrum_after();
            setSpectrum_after();
            this.selected = temp;
            nextFile = null;
        }else{
            utils.pop_msg();
        }
    }

    @FXML
    private void selective(){
        System.out.println("Some selected");
    
        if(this.selected_step != null && this.selected_step.getText() != null){
            String[] temp_options = this.selected_step.getText().split("\n");
    
            System.out.println(this.selected_step.getText());
    
            if(this.selected != null){
                Map<String, String> scriptMap = new HashMap<>();
                scriptMap.put("De_clip", "de_clip.py");
                scriptMap.put("Loud", "loudness_normalization.py");
                scriptMap.put("Noise Reduction", "noise_reduction.py");
                scriptMap.put("Equalization", "equalize_audio.py");
    
                for (int i = 0; i < temp_options.length; i++) {
                    for(Map.Entry<String, String> entry : scriptMap.entrySet()) {
                        if(temp_options[i] != null && temp_options[i].contains(entry.getKey())){
                            String audio_path = String.format("src/main/resources/com/example/audio_2/Audio/%s.wav", entry.getKey().toLowerCase());
                            Path currentPath = Paths.get("").toAbsolutePath();
                            String scriptPath = currentPath.resolve("src").resolve("main").resolve("java").resolve("com").
                                    resolve("example").resolve("audio_2").resolve("python").
                                    resolve(entry.getValue()).toString();
    
                            if(nextFile == null){
                                RunPythonScript.runPythonScript(scriptPath,selected.getPath(),audio_path);
                            }else{
                                RunPythonScript.runPythonScript(scriptPath,nextFile.getPath(),audio_path);
                            }
    
                            nextFile = new File(audio_path);
                        }
                    }
                }
            } else {
                utils.pop_msg();
            }
        } else {
            System.out.println("selected_step or selected_step.getText() is null");
        }

        temp = selected;
        selected = nextFile;

        setMel_after();
        setWave_after();
        setSpectrum_after();
        nextFile = null; //resetting the process
        selected = temp;
    }
    

    @FXML
    private void perform_analysis() throws IOException, InterruptedException {
        
        if(this.selected != null){
            setMel_before();
            setWave_before();
            setSpectrum_before();
            audio_clipping();   
        }else{
            utils.pop_msg();
        }
        
    }

    public void audio_clipping() throws IOException, InterruptedException{
        if(selected != null){
            boolean clipping= DetectClipping.hasClipping(selected.getPath());
            
            if(clipping){
                update_text_clipping("Audio contains clipping");    
            }else{
                update_text_clipping("Audio doesn't have clipping");
            }
        }
    }

    public void setSpectrum_after(){

        if (selected != null){
            String imagePath = "src/main/resources/com/example/audio_2/Images/after_spectrum.png";
            Path currentPath = Paths.get("").toAbsolutePath();
            String scriptPath = currentPath.resolve("src").resolve("main").resolve("java").resolve("com").
                    resolve("example").resolve("audio_2").resolve("python").
                    resolve("draw_spectrum.py").toString();

            RunPythonScript.runPythonScript(scriptPath,selected.getPath(),"after_spectrum.png");
            this.spectrum_after.setImage(setImageViewFromPath(imagePath));
        }else{
            utils.pop_msg();
        }
        
    }

    public void setSpectrum_before() {
        if (selected != null){
            String imagePath = "src/main/resources/com/example/audio_2/Images/before_spectrum.png";
            Path currentPath = Paths.get("").toAbsolutePath();
            String scriptPath = currentPath.resolve("src").resolve("main").resolve("java").resolve("com").
                    resolve("example").resolve("audio_2").resolve("python").
                    resolve("draw_spectrum.py").toString();

            RunPythonScript.runPythonScript(scriptPath,selected.getPath(),"before_spectrum.png");
            this.spectrum_before.setImage(setImageViewFromPath(imagePath));
        }else{
            utils.pop_msg();
        }
    }


    public void setWave_after() {
        if (selected != null){
            String imagePath = "src/main/resources/com/example/audio_2/Images/after_waveform.png";
            Path currentPath = Paths.get("").toAbsolutePath();
            String scriptPath = currentPath.resolve("src").resolve("main").resolve("java").resolve("com").
            resolve("example").resolve("audio_2").resolve("python").resolve("draw_waveform.py").toString();

            RunPythonScript.runPythonScript(scriptPath,selected.getPath(),"after_waveform.png");
            this.wave_after.setImage(setImageViewFromPath(imagePath));
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
            resolve("example").resolve("audio_2").resolve("python").resolve("draw_waveform.py").toString();

            RunPythonScript.runPythonScript(scriptPath,selected.getPath(),"before_waveform.png");
            this.wave_before.setImage(setImageViewFromPath(imagePath));
        }else{
            System.out.println("File hasn't been selected");
        }
    }

    protected void setMel_after() {
        if (selected != null){
            Path currentPath = Paths.get("").toAbsolutePath();
            String scriptPath = currentPath.resolve("src").resolve("main").resolve("java").resolve("com").
            resolve("example").resolve("audio_2").resolve("python").resolve("script.py").toString();

            RunPythonScript.runPythonScript(scriptPath,selected.getPath(),"after_mel_spectrogram.png");
            
            String imagePath = "src/main/resources/com/example/audio_2/Images/after_mel_spectrogram.png";
            this.mel_after.setImage(setImageViewFromPath(imagePath));
        }else{
            System.out.println("File hasn't been selected");
        }
    }


    @FXML
    protected void setMel_before() {
        if (selected != null){
            Path currentPath = Paths.get("").toAbsolutePath();
            String scriptPath = currentPath.resolve("src").resolve("main").resolve("java").resolve("com").
            resolve("example").resolve("audio_2").resolve("python").resolve("script.py").toString();

            RunPythonScript.runPythonScript(scriptPath,selected.getPath(),"before_mel_spectrogram.png");
            
            String imagePath = "src/main/resources/com/example/audio_2/Images/before_mel_spectrogram.png";
            this.mel_before.setImage(setImageViewFromPath(imagePath));
        }else{
            System.out.println("File hasn't been selected");
        }
    }

    public static Image setImageViewFromPath(String imagePath) {
        File imageFile = new File(imagePath);
        String imageURL = imageFile.toURI().toString();
        Image image = new Image(imageURL);
        ImageView imageView = new ImageView();
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
        if (imageView != null) {
            enlargeImage(imageView.getImage());
            System.out.println("Image is not null"); 
        }
        
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

    public void update_text_clipping(String text) {        
        Random random = new Random();
        float hue = random.nextFloat() * 360;
        float saturation = 0.9f; // Keep saturation high for bright colors
        float brightness = 0.5f + random.nextFloat() * 0.5f; // Keep brightness between 0.5 and 1.0 for bright colors without black
        Color randomColor = Color.hsb(hue, saturation, brightness);
        clipping_text.setFill(randomColor);
        clipping_text.setText(text);
    }
}