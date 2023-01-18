package eartrainer.player;

import eartrainer.MidiUtils;
import eartrainer.model.Notes;
import eartrainer.model.ScaleType;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import java.util.List;

public class TrackBuilder {
    private final Track track;
    private final int channel;
    private final int velocity;
    private final int noteLength;

    private long trackEnd = 0;

    public TrackBuilder(Track track, int channel, int noteLength, int velocity) {
        this.track = track;
        this.channel = channel;
        this.velocity = velocity;
        this.noteLength = noteLength;
    }

    public void createChordSequence(Notes notes) {
        arpeggiate(notes, 1);
        playChord(notes, 1);
    }

    public void playScale(ScaleType scaleType, int root) {

    }

    public void playChord(Notes notes, int length) {
        try {
            List<ShortMessage> onMsg = MidiUtils.onMessages(notes, channel, velocity);
            List<ShortMessage> offMsg = MidiUtils.offMessages(notes, channel, velocity);
            for(int i = 0; i < onMsg.size(); i++) {
                track.add(new MidiEvent(onMsg.get(i), trackEnd));
                track.add(new MidiEvent(offMsg.get(i), trackEnd + ((long) noteLength * length)));
            }
            trackEnd += (long) noteLength * length;
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }
    }

    public void arpeggiate(Notes notes, int length) {
        try {
            List<ShortMessage> onMsg = MidiUtils.onMessages(notes, channel, velocity);
            List<ShortMessage> offMsg = MidiUtils.offMessages(notes, channel, velocity);
            for(int i = 0; i < onMsg.size(); i++) {
                track.add(new MidiEvent(onMsg.get(i), trackEnd));
                track.add(new MidiEvent(offMsg.get(i), trackEnd + ((long) noteLength * length)));
                trackEnd += (long) noteLength * length;
            }
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }
    }

    public void rest() {
        trackEnd += noteLength;
    }

    public void rest(int n) {
        trackEnd += ((long) noteLength * n);
    }
}
