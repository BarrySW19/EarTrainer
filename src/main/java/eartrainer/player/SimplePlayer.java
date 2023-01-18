package eartrainer.player;

import eartrainer.model.ChordType;
import eartrainer.model.Notes;
import eartrainer.model.ScaleType;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static javax.sound.midi.Sequence.PPQ;

public class SimplePlayer {
    private static final int PULSES_PER_NOTE = 16;

    public static void play(Consumer<TrackBuilder> setUp) throws MidiUnavailableException, InterruptedException, InvalidMidiDataException {
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        Sequence sequence = new Sequence(PPQ, PULSES_PER_NOTE);
        Track track = sequence.createTrack();
        track.add(new MidiEvent(new ShortMessage(ShortMessage.PROGRAM_CHANGE, 0, 0, 0), 0));

        TrackBuilder trackBuilder = new TrackBuilder(track, 0, PULSES_PER_NOTE * 2, 80);

        setUp.accept(trackBuilder);

        sequencer.setSequence(sequence);
        AtomicBoolean b = new AtomicBoolean(false);
        sequencer.addMetaEventListener(meta -> {
            if(meta.getType() == 0x2F) {
                b.set(true);
            }
        });
        sequencer.start();
        while (!b.get()) {
            Thread.sleep(100);
        }
        sequencer.close();
    }

    public static void main(String[] args) throws Exception {
        playMajorChords();
    }

    private static void playMajorChords() throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        SimplePlayer.play(trackBuilder -> {
            for(int i = 0; i < 1000; i++) {
                int root = new Random().nextInt(40, 80);
                trackBuilder.playChord(new Notes(ChordType.MAJOR_DYAD, root), 2);
                trackBuilder.playChord(new Notes(ChordType.MAJOR_DYAD, root), 2);
                trackBuilder.arpeggiate(new Notes(ChordType.MAJOR_DYAD, root), 1);
                trackBuilder.playChord(new Notes(ChordType.MAJOR, root), 2);
                trackBuilder.rest();
            }
        });
    }

    private static void playMinorChords() throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        SimplePlayer.play(trackBuilder -> {
            for(int i = 0; i < 1000; i++) {
                int root = new Random().nextInt(40, 80);
                trackBuilder.playChord(new Notes(ChordType.MINOR_DYAD, root), 2);
                trackBuilder.playChord(new Notes(ChordType.MINOR_DYAD, root), 2);
                trackBuilder.arpeggiate(new Notes(ChordType.MINOR_DYAD, root), 1);
                trackBuilder.playChord(new Notes(ChordType.MINOR, root), 2);
                trackBuilder.rest();
            }
        });
    }

    private static void playProgressions() throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        SimplePlayer.play(trackBuilder -> {
            for(int i = 0; i < 20; i++) {
                int root = new Random().nextInt(40, 80);
                trackBuilder.playChord(new Notes(ChordType.MAJOR, root), 1);
                trackBuilder.playChord(new Notes(ChordType.MAJOR, root), 1);
                trackBuilder.playChord(new Notes(ChordType.MAJOR, root + 5), 1);
                trackBuilder.playChord(new Notes(ChordType.MAJOR, root + 5), 1);
                trackBuilder.playChord(new Notes(ChordType.MAJOR, root), 1);
                trackBuilder.rest();

                trackBuilder.playChord(new Notes(ChordType.MAJOR, root), 1);
                trackBuilder.playChord(new Notes(ChordType.MAJOR, root), 1);
                trackBuilder.playChord(new Notes(ChordType.MAJOR, root + 7), 1);
                trackBuilder.playChord(new Notes(ChordType.MAJOR, root + 7), 1);
                trackBuilder.playChord(new Notes(ChordType.MAJOR, root), 1);
                trackBuilder.rest();
            }
        });
    }

    private static void playScales() throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        SimplePlayer.play(trackBuilder -> {
            for(int i = 0; i < 20; i++) {
                int root = new Random().nextInt(40, 80);
                trackBuilder.playChord(new Notes(ChordType.MAJOR, root), 1);
                trackBuilder.playChord(new Notes(ChordType.MAJOR, root + 5), 1);
                trackBuilder.playChord(new Notes(ChordType.MAJOR, root + 7), 1);
                trackBuilder.playChord(new Notes(ChordType.MAJOR, root), 1);
                trackBuilder.rest();
                trackBuilder.arpeggiate(new Notes(ScaleType.MAJOR, root), 1);
                trackBuilder.rest(2);
            }
        });
    }
}
