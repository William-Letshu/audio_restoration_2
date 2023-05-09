import sys
import numpy as np
import librosa

def has_clipping(audio_data, threshold=0.95):
    clipped_indices = np.where(np.abs(audio_data) > threshold)
    return len(clipped_indices[0]) > 0

if __name__ == '__main__':
    input_audio_file_path = sys.argv[1]
    audio_data, sampling_rate = librosa.load(input_audio_file_path, sr=None, mono=True)

    result = has_clipping(audio_data)
    print(result)