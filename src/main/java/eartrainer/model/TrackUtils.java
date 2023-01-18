package eartrainer.model;

public class TrackUtils {

//    public static long createChordSequence(Track track, Chord chord, int length, int channel, int velocity, long from) throws InvalidMidiDataException {
//        List<ShortMessage> onMessages = MidiUtils.onMessages(chord, channel, velocity);
//        List<ShortMessage> offMessages = MidiUtils.offMessages(chord, channel, velocity);
//        return createChordSequence(track, onMessages, offMessages, length, from);
//    }
//
//    private static long createChordSequence(Track track, List<ShortMessage> onMessages, List<ShortMessage> offMessages,
//                                            int length, long from)
//    {
//        int idx = 1;
//        for (int i = 0; i < onMessages.size(); i++) {
//            track.add(new MidiEvent(onMessages.get(i), (long) idx * length + from));
//            track.add(new MidiEvent(offMessages.get(i), (long) (idx+1) * length + from));
//            idx++;
//        }
//
//        for (int i = 0; i < onMessages.size(); i++) {
//            track.add(new MidiEvent(onMessages.get(i), (long) idx * length + from));
//            track.add(new MidiEvent(offMessages.get(i), (long) (idx + 1) * length + from));
//        }
//
//        return (long) (idx + 1) * length + from;
//    }
}
