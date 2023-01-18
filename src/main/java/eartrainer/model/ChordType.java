package eartrainer.model;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.sound.midi.ShortMessage.NOTE_OFF;
import static javax.sound.midi.ShortMessage.NOTE_ON;

public class ChordType {
    private final int[] intervals;

    public static ChordType MAJOR_DYAD = new ChordType(4);
    public static ChordType MINOR_DYAD = new ChordType(3);
    public static ChordType MAJOR = MAJOR_DYAD.extend(3);
    public static ChordType MINOR = MINOR_DYAD.extend(4);
    public static ChordType POWER = new ChordType(7);

    public static ChordType DOMINANT_7TH = MAJOR.extend(3);
    public static ChordType MAJOR_7TH = MAJOR.extend(4);
    public static ChordType MINOR_7TH = MINOR.extend(3);

    public static ChordType SUS2 = new ChordType(2, 5);
    public static ChordType SUS4 = new ChordType(5, 2);

    public ChordType(int... intervals) {
        this.intervals = intervals;
    }

    public ChordType extend(int... ext) {
        int[] newInts = Arrays.copyOf(intervals, intervals.length + ext.length);
        System.arraycopy(ext, 0, newInts, intervals.length, ext.length);
        return new ChordType(newInts);
    }

    public int[] getIntervals() {
        return Arrays.copyOf(intervals, intervals.length);
    }

    public int[] notesFromRoot(final int root) {
        final int[] notes = new int[intervals.length + 1];
        notes[0] = root;

        int note = root;
        for(int i = 1; i <= intervals.length; i++) {
            note += intervals[i-1];
            notes[i] = note;
        }
        return notes;
    }

    public List<ShortMessage> onMessages(final int root, final int channel, final int velocity) throws InvalidMidiDataException {
        return createMessages(root, channel, velocity, NOTE_ON);
    }

    public List<ShortMessage> offMessages(final int root, final int channel, final int velocity) throws InvalidMidiDataException {
        return createMessages(root, channel, velocity, NOTE_OFF);
    }

    private List<ShortMessage> createMessages(final int root, final int channel, final int velocity, final int command) throws InvalidMidiDataException {
        final int[] notes = notesFromRoot(root);
        final List<ShortMessage> messages = new ArrayList<>();
        for (int i: notes) {
            messages.add(new ShortMessage(command, channel, i, velocity));
        }
        return messages;
    }

//    public int[] getIntervalsWithRoot() {
//        int[] vals = new int[intervals.length + 1];
//        vals[0] = 0;
//        Arrays.
//        return Arrays.copyOf(intervals, intervals.length);
//    }
}
