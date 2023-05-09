import sys
import os
import numpy as np
import matplotlib.pyplot as plt
from scipy.io import wavfile
from scipy.signal import stft
from scipy.fftpack import dct


def plot_mel_spectrogram(audio_path, output_filename):
    sample_rate, audio = wavfile.read(audio_path)

    # Short-Time Fourier Transform (STFT)
    _, _, Zxx = stft(audio, sample_rate, nperseg=400, noverlap=200)

    # Power spectrum
    power_spectrum = np.abs(Zxx) ** 2

    # Mel filter bank
    def mel_filter_bank(n_mels, sample_rate, n_fft):
        min_mel = 0
        max_mel = 2595 * np.log10(1 + sample_rate / 2 / 700)
        mels = np.linspace(min_mel, max_mel, n_mels + 2)
        freqs = 700 * (10 ** (mels / 2595) - 1)
        bins = np.floor((n_fft + 1) * freqs / sample_rate).astype(int)

        filter_bank = np.zeros((n_mels, int(np.floor(n_fft / 2)) + 1))
        for m in range(1, n_mels + 1):
            filter_bank[m - 1, bins[m - 1]: bins[m] + 1] = np.linspace(0, 1, bins[m] - bins[m - 1] + 1)
            filter_bank[m - 1, bins[m]: bins[m + 1] + 1] = np.linspace(1, 0, bins[m + 1] - bins[m] + 1)

        return filter_bank

    n_fft = 400
    mel_fb = mel_filter_bank(128, sample_rate, n_fft)

    # Mel spectrogram
    mel_spectrogram = np.dot(power_spectrum.T, mel_fb.T)

    # Log-Mel spectrogram
    log_mel_spectrogram = np.log10(mel_spectrogram + 1e-9)

    plt.figure(figsize=(12, 4))
    plt.imshow(log_mel_spectrogram.T, aspect='auto', origin='lower', cmap='jet')
    plt.colorbar(format='%+02.0f dB')
    plt.title('Mel spectrogram')
    plt.tight_layout()

    output_path = os.path.join('src', 'main', 'resources','com','example','audio_2','Images', output_filename)
    plt.savefig(output_path)
    print(f"Mel spectrogram saved to {output_path}")


if __name__ == '__main__':
    if len(sys.argv) < 3:
        print("Usage: python3 plot_mel_spectrogram.py <audio_path> <output_filename>")
        sys.exit(1)

    audio_path = sys.argv[1]
    output_filename = sys.argv[2]
    plot_mel_spectrogram(audio_path, output_filename)
