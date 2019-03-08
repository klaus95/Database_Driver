import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Track extends DBMS{
    private static String[] purchased_ID;
    private static String[] tracking_number;

    public Track(String[] purchased_ID) {
        this.purchased_ID = purchased_ID;
        createTrackFile("Track");
    }
    private static void createTrackFile(String filename) {
        final int MAX_ENTRIES = purchased_ID.length;
        tracking_number = new String[MAX_ENTRIES];
        int[] eta = new int[MAX_ENTRIES];
        String domain = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new Random();

        tracking_number[0] = generateID(domain, 18);
        eta[0] = rand.nextInt(10);

        for (int i = 1; i < MAX_ENTRIES; i++) {
            if (purchased_ID[i - 1].equals(purchased_ID[i])) {
                tracking_number[i] = tracking_number[i-1];
                eta[i] = eta[i-1];
            } else {
                tracking_number[i] = generateID(domain, 18);
                eta[i] = rand.nextInt(10);
            }
        }
        System.out.println(filename + ".csv has " + MAX_ENTRIES + ".");

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename + ".csv"));
            for (int i = 0; i < MAX_ENTRIES; i++) {
                writer.write(purchased_ID[i] + "," + tracking_number[i] + "," + eta[i] + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR: Unable to create " + filename + ".csv file!");
            System.exit(0);
        }
        System.out.println("GENERATE: " + filename + ".csv generated successfully!");
    }
    public String[] getTracking_number() { return tracking_number; }
}
