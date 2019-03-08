import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

public class Costumer extends DBMS{
    private static String[] IDs;

    public Costumer() {
        createCustomerFile("Customer");
    }

    private static void createCustomerFile(String filename) {
        final int MAX_ENTRIES = 400;
        IDs = new String[MAX_ENTRIES];
        String nameQuery = "https://randomuser.me/api/1.2/?results=" + MAX_ENTRIES + "&inc=name,email&nat=us";
        String[] name = fullNameParser(userApiInterface(nameQuery), MAX_ENTRIES);
        String[] email = new String[MAX_ENTRIES];
        String phoneQuery = "https://randomuser.me/api/1.2/?results=" + MAX_ENTRIES + "&inc=phone&nat=us";
        String[] phone = phoneParser(userApiInterface(phoneQuery), MAX_ENTRIES);
        String[] password = new String[MAX_ENTRIES];

        String IDDomain = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String passwordDomain = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%&-_";
        for (int i = 0; i < MAX_ENTRIES; i++) {
            IDs[i] = generateID(IDDomain,10);
            email[i] = name[i].replace(' ', '.') + "@gmail.com";
            password[i] = generateID(passwordDomain,15);
        }

        System.out.println(filename + ".csv has " + MAX_ENTRIES + ".");

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename + ".csv"));
            for (int i = 0; i < MAX_ENTRIES; i++) {
                writer.write(IDs[i] + "," + name[i] + "," + email[i] + "," + phone[i] + "," + password[i] + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR: Unable to create " + filename + ".csv file!");
            System.exit(0);
        }
        System.out.println("GENERATE: " + filename + ".csv generated successfully!");
    }

    private static String[] fullNameParser(String JSONString, int MAX_ENTRIES) {
        String[] temp = new String[MAX_ENTRIES];

        int index = 0;
        final JsonParser parser = Json.createParser(new StringReader(JSONString));
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            if (event == JsonParser.Event.KEY_NAME && parser.getString().equals("first")) {
                event = parser.next();
                StringBuilder fullName = new StringBuilder();
                fullName.append(parser.getString());
                fullName.append(" ");
                while (event != JsonParser.Event.KEY_NAME && !parser.getString().equals("last")) {
                    event = parser.next();
                }
                parser.next();
                fullName.append(parser.getString());

                temp[index] = fullName.toString();
                index++;
            }
        }
        parser.close();

        return temp;
    }

    private static String[] phoneParser(String JSONString, int MAX_ENTRIES) {
        String[] temp = new String[MAX_ENTRIES];

        int index = 0;
        final JsonParser parser = Json.createParser(new StringReader(JSONString));
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            if (event == JsonParser.Event.KEY_NAME && parser.getString().equals("phone")) {
                parser.next();
                temp[index] = parser.getString();
                index++;
            }
        }
        parser.close();

        return temp;
    }

    public String[] getCostumerIDs() { return IDs; }
}
