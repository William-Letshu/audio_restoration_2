import sys
import numpy as np
import librosa
import scipy.signal as signal
import soundfile as sf

def parametric_eq(audio_file_path, output_audio_file_path, center_freq, Q, gain):
    y, sr = librosa.load(audio_file_path, sr=None)

    # Design peaking equalization filter
    b, a = signal.iirpeak(center_freq/(sr/2), Q, gain)

    # Apply the filter to the audio signal
    y_filtered = signal.lfilter(b, a, y)

    # Save the filtered audio
    sf.write(output_audio_file_path, y_filtered, sr)

if __name__ == '__main__':
    input_audio_file_path = sys.argv[1]
    output_audio_file_path = sys.argv[2]

    # Parameters for the equalizer
    center_freq = 1000.0  # Center frequency in Hz
    Q = 1.0  # Quality factor
    gain = 1.0  # Gain at the center frequency in dB

    parametric_eq(input_audio_file_path, output_audio_file_path, center_freq, Q, gain)
