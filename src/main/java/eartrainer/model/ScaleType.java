package eartrainer.model;

public class ScaleType {
    private final int[] intervals;

    public static final ScaleType MAJOR = new ScaleType(2, 2, 1, 2, 2, 2, 1);
    public static final ScaleType DORIAN = new ScaleType(2, 1, 2, 2, 2, 1, 2);
    public static final ScaleType PHRYGIAN = new ScaleType(1, 2, 2, 2, 1, 2, 2);
    public static final ScaleType LYDIAN = new ScaleType(2, 2, 2, 1, 2, 2, 1);
    public static final ScaleType MIXOLYDIAN = new ScaleType(2, 2, 1, 2, 2, 1, 2);
    public static final ScaleType AOLIAN = new ScaleType(2, 1, 2, 2, 1, 2, 2);
    public static final ScaleType LOCRIAN = new ScaleType(1, 2, 2, 1, 2, 2, 2);

    public static final ScaleType MINOR_PENTATONIC = new ScaleType(3, 2, 2, 3, 2);
    public static final ScaleType BLUES = new ScaleType(3, 2, 1, 1, 3, 2);

    public ScaleType(int... intervals) {
        this.intervals = intervals;
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
}
