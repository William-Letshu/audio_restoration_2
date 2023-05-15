import sys
import numpy as np
import librosa
import os
import soundfile as sf
from pyloudnorm import Meter

def loudness_normalization(audio_data, sampling_rate, target_loudness=-20.0):
    meter = Meter(sampling_rate) # create BS.1770 meter
    loudness = meter.integrated_loudness(audio_data)
    gain = target_loudness - loudness
    normalized_audio = librosa.util.normalize(audio_data, norm=np.inf) * np.power(10, gain/20)
    return normalized_audio

if __name__ == '__main__':
    input_audio_file_path = sys.argv[1]
    output_audio_file_path = sys.argv[2]
    audio_data, sampling_rate = librosa.load(input_audio_file_path, sr=None, mono=True)
    normalized_audio = loudness_normalization(audio_data, sampling_rate)
    sf.write(output_audio_file_path, normalized_audio, sampling_rate)
