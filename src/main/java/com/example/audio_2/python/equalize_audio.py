import os
import sys
from pydub import AudioSegment
from pydub.effects import equalize

def equalize_audio(audio_file_path, bands=None):
    if bands is None:
        bands = [(50, 100), (100, 200), (200, 400), (400, 800), (800, 1600), (1600, 3200), (3200, 6400), (6400, 12800), (12800, 16000)]

    audio = AudioSegment.from_file(audio_file_path)
    equalized_audio = equalize(audio, bands)
    return equalized_audio

if __name__ == '__main__':
    input_audio_file_path = sys.argv[1]
    output_audio_file_path = sys.argv[2]
    output_audio_file_path = os.path.join('src', 'main', 'resources','com','example','audio_2','Audio', output_audio_file_path)
    equalized_audio = equalize_audio(input_audio_file_path)
    equalized_audio.export(output_audio_file_path, format="wav")
