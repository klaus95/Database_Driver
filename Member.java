import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Member extends DBMS {
    static String[] IDs;
    static String[] ID;

    public Member(String[] IDs) {
        this.IDs = IDs;
        createMemeberFile("Member");
    }
    private static void createMemeberFile(String filename) {
        ArrayList<String> temp = new ArrayList<>();
        Random randBool = new Random();

        for (int i = 0; i < IDs.length; i++) {
            if (randBool.nextBoolean()) {
                temp.add(IDs[i]);
            }
        }

        final int MAX_ENTRIES = temp.size();
        ID = temp.toArray(new String[MAX_ENTRIES]);

        String[] mem_id = new String[MAX_ENTRIES];
        int[] discount = new int[MAX_ENTRIES];
        String[] start_date = new String[MAX_ENTRIES];
        String[] status = new String[MAX_ENTRIES];
        String[] end_date = new String[MAX_ENTRIES];
        Random randNum = new Random();
        String domain = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int i = 0; i < MAX_ENTRIES; i++) {
            mem_id[i] = generateID(domain, 10);

            discount[i] = randNum.nextInt(11) + 20;

            StringBuilder date = new StringBuilder();
            date.append(randNum.nextInt(3) + 2015).append("-").append(randNum.nextInt(12)+ 1).append("-").append(randNum.nextInt(28)+ 1);
            start_date[i] = date.toString();
            date = new StringBuilder();
            int year = randNum.nextInt(3) + 2017;
            int month = randNum.nextInt(12)+ 1;
            int day = randNum.nextInt(28)+ 1;
            date.append(year).append("-").append(month).append("-").append(day);
            end_date[i] = date.toString();

            if (year < 2018) {
                status[i] = "Expired";
            } else if (year > 2018) {
                status[i] = "Active";
            } else {
                if (month < 12) {
                    status[i] = "Expired";
                } else {
                    status[i] = "Active";
                }
            }
        }

        System.out.println(filename + ".csv has " + MAX_ENTRIES + ".");

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename + ".csv"));
            for (int i = 0; i < MAX_ENTRIES; i++) {
                writer.write(ID[i] + "," + mem_id[i] + "," + discount[i] + "," + start_date[i] + "," + end_date[i] + "," + status[i] + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR: Unable to create " + filename + ".csv file!");
            System.exit(0);
        }
        System.out.println("GENERATE: " + filename + ".csv generated successfully!");
    }
    public String[] getID() { return ID; }
}
