import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class TrackingStatus extends DBMS {
    private static String[] trackingNumbers;
    private static String[] addresses;

    public TrackingStatus(String[] addresses, String[] trackingNumbers) {
        this.trackingNumbers = trackingNumbers;
        this.addresses = addresses;
        createTrackingStatusFile("TrackingStatus");
    }
    private static void createTrackingStatusFile(String filename) {
        ArrayList<String> track = new ArrayList<>();
        ArrayList<String> status = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < trackingNumbers.length; i++) {
            for (int j = 0; j < (rand.nextInt(4) + 2); j++) {
                track.add(trackingNumbers[i]);
                status.add(addresses[rand.nextInt(addresses.length)]);
            }
        }
        final int MAX_ENTRIES = track.size();
        System.out.println(filename + ".csv has " + MAX_ENTRIES + ".");

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename + ".csv"));
            for (int i = 0; i < MAX_ENTRIES; i++) {
                writer.write(track.get(i) + "," + status.get(i) + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR: Unable to create " + filename + ".csv file!");
            System.exit(0);
        }
        System.out.println("GENERATE: " + filename + ".csv generated successfully!");
    }
}
