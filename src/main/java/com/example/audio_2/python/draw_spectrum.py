import os
import sys
import numpy as np
import librosa
import matplotlib.pyplot as plt

def draw_spectrum(audio_file_path, output_image_file_path):
    y, sr = librosa.load(audio_file_path)
    D = librosa.amplitude_to_db(np.abs(librosa.stft(y)), ref=np.max)
    
    plt.figure(figsize=(12, 4))
    librosa.display.specshow(D, sr=sr, x_axis='time', y_axis='log')
    plt.colorbar(format='%+2.0f dB')
    plt.title('Spectrum')
    plt.xlabel('Time')
    plt.ylabel('Frequency (log scale)')
    output_path = os.path.join('src', 'main', 'resources','com','example','audio_2','Images', output_image_file_path)
    plt.savefig(output_path)
    plt.close()

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python draw_spectrum.py <audio_file_path> <output_image_file_path>")
        sys.exit(1)

    audio_file_path = sys.argv[1]
    output_image_file_path = sys.argv[2]

    draw_spectrum(audio_file_path, output_image_file_path)
