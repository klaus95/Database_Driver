import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Invoice extends DBMS {
    private static String[] mem_IDs;
    private static String[] cus_IDs;
    private static String[] cusCC;
    private static String[] purchased_ID;
    private static int numberOfMemberships;

    public Invoice(String[] mem_IDs, String[] cusCC, String[] cus_IDs) {
        this.mem_IDs = mem_IDs;
        this.cus_IDs = cus_IDs;
        this.cusCC = cusCC;
        createInvoiceFile("Invoice");
    }
    private static void createInvoiceFile(String filename) {
        numberOfMemberships = mem_IDs.length;
        ArrayList<String> cus_temp = new ArrayList<>();
        ArrayList<String> cc_temp = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < mem_IDs.length; i++) {
            cus_temp.add(mem_IDs[i]);
            int index = findCCIndex(mem_IDs[i], cus_IDs);
            if (index >= 0) {
                cc_temp.add(cusCC[index]);
            } else {
                System.out.println("ERROR: Invalid CC number!");
            }
        }

        for (int i = 0; i < cus_IDs.length; i++) {
            for (int j = 0; j < rand.nextInt(50) + 20; j++) {
                cus_temp.add(cus_IDs[i]);
                cc_temp.add(cusCC[i]);
            }
        }

        final int MAX_ENTRIES = cus_temp.size();

        String[] ID = cus_temp.toArray(new String[MAX_ENTRIES]);
        String[] CC_num = cc_temp.toArray(new String[MAX_ENTRIES]);
        purchased_ID = new String[MAX_ENTRIES];
        String[] purchased_date = new String[MAX_ENTRIES];
        String domain = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int i = 0; i < MAX_ENTRIES; i++) {
            purchased_ID[i] = generateID(domain, 15);
            StringBuilder date = new StringBuilder();
            date.append(rand.nextInt(2) + 2017).append("-").append(rand.nextInt(12)+ 1).append("-").append(rand.nextInt(28)+ 1);
            purchased_date[i] = date.toString();
        }
        System.out.println(filename + ".csv has " + MAX_ENTRIES + ".");

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename + ".csv"));
            for (int i = 0; i < MAX_ENTRIES; i++) {
                writer.write(ID[i] + "," + purchased_ID[i] + "," + CC_num[i] + "," + purchased_date[i] + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR: Unable to create " + filename + ".csv file!");
            System.exit(0);
        }
        System.out.println("GENERATE: " + filename + ".csv generated successfully!");
    }
    private static void printCSV(String filename, int MAX_ENTRIES) {

    }
    private static int findCCIndex(String mem, String[] ID) {
        for (int i = 0; i < ID.length; i++) {
            if (mem.equals(ID[i])) {
                return i;
            }
        }
        return -1;
    }

    public String[] getPurchasedID() { return purchased_ID; }
    public int getNumberOfMemberships() { return numberOfMemberships; }
}
