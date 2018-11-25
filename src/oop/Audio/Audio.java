package oop.Audio;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.List;

public class Audio {

    private List<Clip> list = new ArrayList<>();
    private String path;
    private boolean loop;

    public Audio(String path, boolean loop) {
        this.path = path;
        this.loop = loop;
    }

    public void play() {
        if (loop && !list.isEmpty()) {
            return;
        }

        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(Audio.class.getResource(path)));
            list.add(clip);
            if (!loop) {
                clip.addLineListener(new LineListener() {
                    @Override
                    public void update(LineEvent event) {
                        if (event.getType() == LineEvent.Type.STOP) {
                            clip.close();
                            list.remove(clip);
                        }
                    }
                });
                clip.start();
            } else {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (!list.isEmpty()) {
            list.get(0).close();
            list.clear();
        }
    }
}