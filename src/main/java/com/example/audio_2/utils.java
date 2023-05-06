package com.example.audio_2;

import javafx.scene.image.*;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;

import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;



public class utils {

    public static ImageView bufferedImageToImageView(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        byte[] pixels = new byte[width * height * 4]; // 4 bytes per pixel (RGBA)
        int pixelIndex = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = bufferedImage.getRGB(x, y);

                pixels[pixelIndex++] = (byte) ((argb >> 16) & 0xFF); // Red
                pixels[pixelIndex++] = (byte) ((argb >> 8) & 0xFF);  // Green
                pixels[pixelIndex++] = (byte) (argb & 0xFF);         // Blue
                pixels[pixelIndex++] = (byte) ((argb >> 24) & 0xFF); // Alpha
            }
        }

        WritableImage fxImage = new WritableImage(width, height);
        fxImage.getPixelWriter().setPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), pixels, 0, width * 4);

        ImageView imageView = new ImageView(fxImage);
        return imageView;
    }

    /**
     * Retrieves the {@link AudioFormat} of a given audio file.
     *
     * @param audioFile A {@link File} object representing the audio file.
     * @return The {@link AudioFormat} of the audio file.
     * @throws IOException If an I/O error occurs when reading the audio file.
     * @throws UnsupportedAudioFileException If the provided audio file is not supported.
     */
    public static AudioFormat getAudioFormat(File audioFile) throws IOException, UnsupportedAudioFileException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat audioFormat = audioInputStream.getFormat();

        if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            audioFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    audioFormat.getSampleRate(),
                    audioFormat.getSampleSizeInBits(),
                    audioFormat.getChannels(),
                    audioFormat.getFrameSize(),
                    audioFormat.getFrameRate(),
                    audioFormat.isBigEndian());
            audioInputStream = AudioSystem.getAudioInputStream(audioFormat, audioInputStream);
        }

        audioInputStream.close();
        return audioFormat;
    }

    /**
     * Calculates the optimal buffer size and number of Mel bands for a given audio file, using default values for
     * desired time resolution, desired frequency resolution, and minimum frequency.
     *
     * @param audioFile The audio file for which the buffer size and number of Mel bands should be calculated.
     * @return An array containing the optimal buffer size and number of Mel bands.
     * @throws UnsupportedAudioFileException If the audio file format is not supported.
     * @throws IOException If there is an error reading the audio file.
     *
     * <p>The function estimates the maximum frequency based on the sample rate of the audio file, according to the
     * Nyquist-Shannon sampling theorem. It calculates the buffer size based on the desired time resolution and rounds
     * it to the nearest power of two. The number of Mel bands is calculated based on the desired frequency resolution,
     * minimum frequency, and estimated maximum frequency.</p>
     */
    public static int[] getOptimalBufferSizeAndMelBands(File audioFile) throws UnsupportedAudioFileException, IOException {
        // Default values
        int desiredTimeResolution = 20; // milliseconds
        int desiredFrequencyResolution = 100; // Hz
        int minFrequency = 300; // Hz

        AudioFormat audioFormat = getAudioFormat(audioFile);
        float sampleRate = audioFormat.getSampleRate();

        // Calculate the maximum frequency based on the sample rate
        int maxFrequency = (int) (sampleRate / 2);

        // Calculate buffer size based on desired time resolution
        int bufferSize = (int) ((sampleRate * desiredTimeResolution) / 1000);

        // Choose the closest power of 2 as the buffer size
        bufferSize = (int) Math.pow(2, Math.ceil(Math.log(bufferSize) / Math.log(2)));

        // Calculate number of Mel bands based on desired frequency resolution
        int numMelBands = (maxFrequency - minFrequency) / desiredFrequencyResolution;

        return new int[]{bufferSize, numMelBands};
    }

    public static BufferedImage plotMelSpectrogram(double[][] melSpectrogram) {
        int width = melSpectrogram.length;
        int height = melSpectrogram[0].length;

        // Create CategoryChart
        CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("Mel Spectrogram").xAxisTitle("Time").yAxisTitle("Mel Bands").theme(ChartTheme.Matlab).build();

        // Set data and styling
        chart.getStyler().setLegendPosition(LegendPosition.OutsideE);
        chart.getStyler().setPlotContentSize(0.99);
        chart.getStyler().setXAxisMax((double) width);
        chart.getStyler().setYAxisMax((double) height);

        // Add Mel Spectrogram data to the CategoryChart
        for (int i = 0; i < height; i++) {
            List<Double> xData = new ArrayList<>();
            List<Double> yData = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                xData.add((double) j);
                yData.add(melSpectrogram[j][i]);
            }
            chart.addSeries("Mel Band " + i, xData, yData);
        }

        // Return the BufferedImage
        BufferedImage image = BitmapEncoder.getBufferedImage(chart);
        return image;
    }

}
