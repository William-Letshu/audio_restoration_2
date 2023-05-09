import sys
import numpy as np
import librosa
import os

def loudness_normalization(audio_data, target_loudness=-20.0):
    loudness = librosa.lufs(audio_data)
    gain = target_loudness - loudness
    normalized_audio = librosa.util.normalize(audio_data, norm=np.inf) * np.power(10, gain/20)
    return normalized_audio

if __name__ == '__main__':
    input_audio_file_path = sys.argv[1]
    output_audio_file_path = sys.argv[2]
    output_audio_file_path = os.path.join('src', 'main', 'resources','com','example','audio_2','Audio', output_audio_file_path)
    audio_data, sampling_rate = librosa.load(input_audio_file_path, sr=None, mono=True)
    normalized_audio = loudness_normalization(audio_data)
    librosa.output.write_wav(output_audio_file_path, normalized_audio, sampling_rate)
