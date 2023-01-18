package eartrainer;

import eartrainer.model.Notes;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.List;

import static javax.sound.midi.ShortMessage.NOTE_OFF;
import static javax.sound.midi.ShortMessage.NOTE_ON;

public class MidiUtils {

    public static List<ShortMessage> onMessages(final Notes notes, final int channel, final int velocity) throws InvalidMidiDataException {
        return createMessages(notes.getMidiNotes(), channel, velocity, NOTE_ON);
    }

    public static List<ShortMessage> offMessages(final Notes notes, final int channel, final int velocity) throws InvalidMidiDataException {
        return createMessages(notes.getMidiNotes(), channel, velocity, NOTE_OFF);
    }

    private static List<ShortMessage> createMessages(final int[] notes, final int channel, final int velocity, final int command) throws InvalidMidiDataException {
        final List<ShortMessage> messages = new ArrayList<>();
        for (int i: notes) {
            messages.add(new ShortMessage(command, channel, i, velocity));
        }
        return messages;
    }
}
