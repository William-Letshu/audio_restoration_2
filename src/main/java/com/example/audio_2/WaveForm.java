package com.example.audio_2;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class WaveForm {

    public static double[] getWaveform(String audioFilePath) {
        double[] waveform = null;

        try {
            File audioFile = new File(audioFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat audioFormat = audioInputStream.getFormat();

            int numChannels = audioFormat.getChannels();
            int sampleSizeInBits = audioFormat.getSampleSizeInBits();
            int frameSize = audioFormat.getFrameSize();
            long numFrames = audioInputStream.getFrameLength();
            boolean isBigEndian = audioFormat.isBigEndian();

            byte[] audioDataBytes = new byte[(int) (numFrames * frameSize)];
            audioInputStream.read(audioDataBytes);

            waveform = new double[(int) numFrames];

            for (int frameIndex = 0; frameIndex < numFrames; frameIndex++) {
                double sum = 0.0;

                for (int channel = 0; channel < numChannels; channel++) {
                    int sampleIndex = frameIndex * frameSize + channel * (sampleSizeInBits / 8);

                    if (sampleSizeInBits == 8) {
                        sum += audioDataBytes[sampleIndex];
                    } else if (sampleSizeInBits == 16) {
                        int sample;
                        if (isBigEndian) {
                            sample = (audioDataBytes[sampleIndex] << 8) | (audioDataBytes[sampleIndex + 1] & 0xff);
                        } else {
                            sample = (audioDataBytes[sampleIndex] & 0xff) | (audioDataBytes[sampleIndex + 1] << 8);
                        }
                        sum += sample;
                    }
                }

                waveform[frameIndex] = sum / numChannels;
            }

        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }

        return waveform;
    }

    public static void drawWaveform(String audioFilePath, String outputImageFilePath) {
        double[] waveform = getWaveform(audioFilePath);

        if (waveform == null) {
            System.err.println("Error processing the audio file.");
            return;
        }

        int width = 800;
        int height = 400;
        BufferedImage waveformImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = waveformImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);

        int yOffset = height / 2;
        int prevX = 0;
        int prevY = yOffset;

        graphics.setColor(Color.BLUE);

        for (int i = 0; i < waveform.length; i++) {
            int x = (int) (((double) i / waveform.length) * width);
            int y = (int) (waveform[i] / Short.MAX_VALUE * yOffset + yOffset);

            graphics.drawLine(prevX, prevY, x, y);

            prevX = x;
            prevY = y;
        }

        // Set font, color, and draw title, x-axis, and y-axis labels
        graphics.setFont(new Font("Arial", Font.PLAIN, 14));
        graphics.setColor(Color.BLACK);

        String title = "Audio Waveform";
        String xAxisLabel = "Time";
        String yAxisLabel = "Amplitude";

        graphics.drawString(title, width / 2 - graphics.getFontMetrics().stringWidth(title) / 2, 20);
        graphics.drawString(xAxisLabel, width / 2 - graphics.getFontMetrics().stringWidth(xAxisLabel) / 2, height - 10);

        // Draw y-axis labels
        int numYLabels = 5;
        for (int i = 0; i <= numYLabels; i++) {
            double value = getYAxisValue(i, numYLabels);
            String label = String.format("%.2f", value);
            int labelWidth = graphics.getFontMetrics().stringWidth(label);
            graphics.drawString(label, 5, height - (i * (height - 20) / numYLabels) - 5);
        }
    }

    private static double getYAxisValue(int index, int numYLabels) {
        double range = 2.0; // Range of audio data from -1.0 to 1.0
        return -1.0 + (index * range / numYLabels);
    }


}
