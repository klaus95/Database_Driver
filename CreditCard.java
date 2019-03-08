import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

public class CreditCard extends DBMS {
    static String[] IDs;
    static String[] CC;
    static String[] street;
    static String[] city;
    static String[] state;
    static String[] postcode;

    public CreditCard(String[] IDs) {
        this.IDs = IDs;
        createCCFile("CreditCard");
    }
    private static void createCCFile(String filename) {
        final int MAX_ENTRIES = 400;
        CC = new String[MAX_ENTRIES];
        String[] EXP = new String[MAX_ENTRIES];
        String[] CVV = new String[MAX_ENTRIES];

        String numString = "123456789";
        for (int i = 0; i < MAX_ENTRIES; i++) {
            CC[i] = generateID(numString, 20);
            EXP[i] = generateID(numString, 4);
            CVV[i] = generateID(numString, 3);
        }

        String addressQuery = "https://randomuser.me/api/1.2/?results=" + MAX_ENTRIES + "&inc=location&nat=us";
        generateAddresses(userApiInterface(addressQuery), MAX_ENTRIES);
        System.out.println(filename + ".csv has " + MAX_ENTRIES + ".");

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename + ".csv"));
            for (int i = 0; i < 400; i++) {
                writer.write(IDs[i] + "," + CC[i] + "," + EXP[i] + "," + street[i] + "," + city[i] + "," + state[i] + "," + postcode[i] + "," + CVV[i] + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR: Unable to create " + filename + ".csv file!");
            System.exit(0);
        }
        System.out.println("GENERATE: " + filename + ".csv generated successfully!");
    }
    private static void generateAddresses(String json, int sum) {
        street = new String[sum];
        city = new String[sum];
        state = new String[sum];
        postcode = new String[sum];

        int index = 0;
        final JsonParser parser = Json.createParser(new StringReader(json));
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            if (event == JsonParser.Event.KEY_NAME && parser.getString().equals("street")) {
                parser.next();
                street[index] = parser.getString();
                parser.next(); parser.next();
                city[index] = parser.getString();
                parser.next(); parser.next();
                state[index] = parser.getString();
                parser.next(); parser.next();
                postcode[index] = parser.getString();
                index++;
            }
        }
        parser.close();
    }
    public String[] getCC() { return CC; }
}
