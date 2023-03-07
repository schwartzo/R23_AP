package Team4450.Robot23;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class PlayThemeSong {
    String audioFilePath;
    File audioFile;
    AudioInputStream audioStream;

    AudioFormat format;
    DataLine.Info info;

    Clip audioClip;

    public PlayThemeSong() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioFile = new File(audioFilePath);
        audioStream = AudioSystem.getAudioInputStream(audioFile);
        format = audioStream.getFormat();
        info = new DataLine.Info(Clip.class, format);
        audioClip = (Clip) AudioSystem.getLine(info);

        audioClip.open(audioStream);
        audioClip.start();
    }
}