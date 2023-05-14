import sys
import numpy as np
import librosa
import soundfile as sf
from scipy.signal import wiener

def de_clip(input_file_path, output_file_path, threshold=0.8):
    # Read the input audio file
    audio_data, sample_rate = librosa.load(input_file_path, sr=None)

    # Identify samples that exceed the threshold
    clipped_indexes = np.where(np.abs(audio_data) > threshold)

    # Reduce amplitude of those samples to the threshold
    audio_data[clipped_indexes] = np.sign(audio_data[clipped_indexes]) * threshold

    # Apply Wiener filter for noise reduction
    audio_data = wiener(audio_data)

    # Write the declipped and noise reduced audio to an output file
    sf.write(output_file_path, audio_data, sample_rate)


if __name__ == "__main__":
    audio_file_path = sys.argv[1]
    output_file_path = sys.argv[2]
    de_clip(audio_file_path, output_file_path)