package com.example.audio_2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DetectClipping {
    
    public static boolean hasClipping(String audioFilePath) throws IOException, InterruptedException {
        Path currentPath = Paths.get("").toAbsolutePath();
        
        String scriptPath = currentPath.resolve("src").resolve("main").resolve("java").resolve("com").
        resolve("example").resolve("audio_2").resolve("python").
        resolve("detect_clipping.py").toString();
        
        ProcessBuilder processBuilder = new ProcessBuilder("python",scriptPath, audioFilePath);
        Process process = processBuilder.start();
        BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String line;
        StringBuilder output = new StringBuilder();
        StringBuilder errorOutput = new StringBuilder();

        while ((line = outputReader.readLine()) != null) {
            output.append(line);
        }

        while ((line = errorReader.readLine()) != null) {
            errorOutput.append(line);
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Python script execution failed with exit code " + exitCode + "\nError output: " + errorOutput.toString());
        }

        return Boolean.parseBoolean(output.toString());
    }
}
