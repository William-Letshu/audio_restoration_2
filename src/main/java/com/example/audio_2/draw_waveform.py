import sys
import os
import librosa
import librosa.display
import matplotlib.pyplot as plt

def draw_waveform(audio_file_path, output_image_file_path):
    y, sr = librosa.load(audio_file_path)
    plt.figure(figsize=(12, 4))
    librosa.display.waveshow(y, sr=sr)
    plt.title('Waveform')
    plt.xlabel('Time')
    plt.ylabel('Amplitude')
    output_path = os.path.join('src', 'main', 'resources','com','example','audio_2','Images', output_image_file_path)
    plt.savefig(output_path)
    plt.close()

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python draw_waveform.py <audio_file_path> <output_image_file_path>")
        sys.exit(1)

    audio_file_path = sys.argv[1]
    output_image_file_path = sys.argv[2]

    draw_waveform(audio_file_path, output_image_file_path)
