package eartrainer.model;

import java.util.Arrays;

public class Notes {
    private final int[] midiNotes;

    public Notes(ChordType chordType, int root) {
        this.midiNotes = chordType.notesFromRoot(root);
    }

    public Notes(ScaleType scaleType, int root) {
        this.midiNotes = scaleType.notesFromRoot(root);
    }

    public int[] getMidiNotes() {
        return Arrays.copyOf(midiNotes, midiNotes.length);
    }
}