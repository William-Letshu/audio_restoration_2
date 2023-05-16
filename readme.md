# Audio Restoration

This is a JavaFX application that handles audio processing tasks.

## User Interface

The user interface of the application is designed using JavaFX and it provides a straightforward and user-friendly way to interact with the application. The design includes buttons for file selection, performing audio analysis and enhancement, and radio buttons for selecting specific audio processing options. Visualizations of the audio data (both pre and post-processing) are displayed for the user's convenience.

# Installation Instructions

This project requires Maven, Java, and Python with certain packages. Here's how you can set up everything:

## 1. Installing Maven

Maven is a build automation tool used primarily for Java projects. 

### On Ubuntu:

You can install Maven on Ubuntu using `apt` package manager:

```bash
sudo apt update
sudo apt install maven
```

### On MacOS:

You can install Maven on MacOS using `brew`:

```bash
brew install maven
```

### On Windows:

You can download the Maven zip file from the [official website](https://maven.apache.org/download.cgi). Extract it to a directory, and then add that directory's `bin` folder to your system path.

You can verify the installation by running:

```bash
mvn -v
```

## 2. Installing Java

Java is needed to compile and run the JavaFX application. You can download it from the [official Oracle website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or install via package managers like `apt` for Ubuntu, `brew` for MacOS. 

## 3. Installing Python

Python can be downloaded from the [official website](https://www.python.org/downloads/). You can also install it via package managers like `apt` for Ubuntu, `brew` for MacOS.

### On Ubuntu:

```bash
sudo apt update
sudo apt install python3
```

### On MacOS:

```bash
brew install python3
```

### On Windows:

Download Python from the official website and run the installer.

## 4. Installing Maven Dependencies

Maven automatically handles the project's dependencies. Simply navigate to the project's root directory (where the `pom.xml` file resides), and run the following command:

```bash
mvn install
```

Maven will download and install all the necessary dependencies defined in the `pom.xml` file.

## 5. Installing Python Packages

If your project uses Python as well, you'll need to install the necessary Python packages. This can be done using pip, which is a package manager for Python.

Firstly, it is recommended to create a virtual environment:

```bash
python3 -m venv env
source env/bin/activate  # On Windows use `env\Scripts\activate`
```

If the Python packages required by your project are listed in a `requirements.txt` file, you can install all of them with:

```bash
pip install -r requirements.txt
```

## Features

This application leverages several libraries to provide its functionality and offers the following features:

1. **Audio File Selection**: The application allows users to select an audio file for analysis and processing.

2. **Audio Analysis and Visualization**: The application visualizes audio data in three different ways: waveform, mel spectogram, and spectrum. This is done for both the original and the processed audio data.

3. **Audio Processing Options**: The application provides several processing options for the user, including noise reduction, equalization, loudness normalization, and de-clipping. These can be applied selectively based on the user's preference.

4. **Automatic Analysis**: There's a button for automatic audio analysis and another one for recommended enhancement procedures based on the analysis.

## Building

The project is set up to be built using Apache Maven. The source code is set to be compiled with JDK 20.

## Running

The project is configured with the JavaFX Maven Plugin to easily run the application using the Maven command:

```bash
mvn clean javafx:run
```

This command will launch the `HelloApplication` from the `com.example.audio_2` package.