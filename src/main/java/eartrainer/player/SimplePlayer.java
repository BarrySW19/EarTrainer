package eartrainer.player;

import eartrainer.model.ChordType;
import eartrainer.model.Notes;
import eartrainer.model.ScaleType;

import javax.sound.midi.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static javax.sound.midi.Sequence.PPQ;

public class SimplePlayer {
    private static final int PULSES_PER_NOTE = 16;

    public static void play2() throws MidiUnavailableException, InterruptedException, InvalidMidiDataException {
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        Sequence sequence = new Sequence(PPQ, PULSES_PER_NOTE);

        Track track = sequence.createTrack();
        track.add(new MidiEvent(new ShortMessage(ShortMessage.PROGRAM_CHANGE, 0, 42, 0), 0));
        track.add(new MidiEvent(new ShortMessage(ShortMessage.PROGRAM_CHANGE, 1, 42, 0), 0));

        TrackBuilder trackBuilder1 = new TrackBuilder(track, 0, PULSES_PER_NOTE * 2, 70);
        TrackBuilder trackBuilder2 = new TrackBuilder(track, 1, PULSES_PER_NOTE * 2, 70);
        for(int i = 0; i < 10; i++) {
            int r = new Random().nextInt(45,66);
            ChordType ct; int inc;
            if(new Random().nextInt(0, 2) == 0) {
                ct = ChordType.MAJOR_DYAD;
                trackBuilder2.playChord(new Notes(ct, r), 20);
                trackBuilder1.rest(2);
                trackBuilder1.arpeggiate(new Notes(r, r+4, r+4), 1);
                trackBuilder1.rest(2);
                trackBuilder1.arpeggiate(new Notes(r, r+4, r+4), 1);
                trackBuilder1.rest(2);
                trackBuilder1.arpeggiate(new Notes(r, r+4, r+4), 1);
                trackBuilder1.rest(2);
                trackBuilder1.arpeggiate(new Notes(r, r+4, r+4), 1);
            } else {
                ct = ChordType.MINOR_DYAD;
                trackBuilder2.playChord(new Notes(ct, r), 20);
                trackBuilder1.rest(2);
                trackBuilder1.arpeggiate(new Notes(r, r+3, r+5), 1);
                trackBuilder1.rest(2);
                trackBuilder1.arpeggiate(new Notes(r, r+3, r+5), 1);
                trackBuilder1.rest(2);
                trackBuilder1.arpeggiate(new Notes(r, r+3, r+5), 1);
                trackBuilder1.rest(2);
                trackBuilder1.arpeggiate(new Notes(r, r+3, r+5), 1);
            }

            trackBuilder1.rest(3);
            trackBuilder2.rest(3);
        }

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

    public static void play(Consumer<TrackBuilder> setUp) throws MidiUnavailableException, InterruptedException, InvalidMidiDataException {
        for(Instrument i: MidiSystem.getSynthesizer().getAvailableInstruments()) {
            System.out.println("'" + i.getName() + "'");
        }
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
        play2();
    }

    private static void playMajMinChords() throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        SimplePlayer.play(trackBuilder -> {
            for(int i = 0; i < 1000; i++) {
                int root = new Random().nextInt(40, 80);
                if(new Random().nextInt(0, 2) == 0) {
                    trackBuilder.playChord(new Notes(ChordType.MAJOR, root), 1);
                    trackBuilder.playChord(new Notes(ChordType.MAJOR, root), 1);
                    trackBuilder.playChord(new Notes(ChordType.MAJOR, root), 2);
                    trackBuilder.rest();
                    trackBuilder.arpeggiate(new Notes(root), 1);
                    trackBuilder.arpeggiate(new Notes(root+4), 1);
                    trackBuilder.arpeggiate(new Notes(root+4), 1);
                } else {
                    trackBuilder.playChord(new Notes(ChordType.MINOR, root), 1);
                    trackBuilder.playChord(new Notes(ChordType.MINOR, root), 1);
                    trackBuilder.playChord(new Notes(ChordType.MINOR, root), 2);
                    trackBuilder.rest();
                    trackBuilder.arpeggiate(new Notes(root), 1);
                    trackBuilder.arpeggiate(new Notes(root+3), 2);
                    trackBuilder.arpeggiate(new Notes(root+5), 1);
                }
                trackBuilder.rest(2);
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
            for(int i = 0; i < 200; i++) {
                int root = new Random().nextInt(40, 80);
                if(new Random().nextInt(0, 2) == 0) {
                    trackBuilder.playChord(new Notes(ChordType.MAJOR, root), 2);
                    trackBuilder.playChord(new Notes(ChordType.MAJOR, root + 5), 2);
                    trackBuilder.rest();
                    trackBuilder.playChord(new Notes(ChordType.MAJOR, root + 5), 1);
                    trackBuilder.playChord(new Notes(ChordType.MAJOR, root + 7), 1);
                    trackBuilder.playChord(new Notes(ChordType.MAJOR, root), 1);
                    trackBuilder.rest(2);
                } else {
                    trackBuilder.playChord(new Notes(ChordType.MAJOR, root), 2);
                    trackBuilder.playChord(new Notes(ChordType.MAJOR, root + 7), 2);
                    trackBuilder.rest();
                    trackBuilder.playChord(new Notes(ChordType.MAJOR, root + 7), 1);
                    trackBuilder.playChord(new Notes(ChordType.MAJOR, root), 1);
                    trackBuilder.rest(2);
                }
            }
        });
    }

    private static void playScales() throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        SimplePlayer.play(trackBuilder -> {
            for(int i = 0; i < 100; i++) {
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
