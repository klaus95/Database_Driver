import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;

public class ShippingAddress extends DBMS {
    private static String[] IDs;
    private static String[] address;

    public ShippingAddress(String[] IDs) {
        this.IDs = IDs;
        createShippingFile("ShippingAddress");
    }
    private static void createShippingFile(String filename) {
        Integer[] randomNumberAdresses = generateRandomNumbers();
        int sum = 0;
        for (Integer num : randomNumberAdresses) {
            sum += num;
        }
        String[] ID = generateIDArray(sum, randomNumberAdresses);
        String addressQuery = "https://randomuser.me/api/1.2/?results=" + sum + "&inc=location&nat=us";
        address = generateAddresses(userApiInterface(addressQuery), sum);
        System.out.println(filename + ".csv has " + sum + ".");

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename + ".csv"));
            for (int i = 0; i < sum; i++) {
                writer.write(ID[i] + "," + address[i] + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR: Unable to create " + filename + ".csv file!");
            System.exit(0);
        }
        System.out.println("GENERATE: " + filename + ".csv generated successfully!");
    }
    private static Integer[] generateRandomNumbers() {
        ArrayList<Integer> temp = new ArrayList<>();
        Random length = new Random();
        for (int i = 0; i < 400; i++) {
            temp.add(length.nextInt(2) + 1);
        }
        return temp.toArray(new Integer[temp.size()]);
    }
    private static String[] generateIDArray(int sum, Integer[] rand) {
        String[] temp = new String[sum];
        int index = 0;
        int indexID = 0;
        for (Integer i : rand) {
            for(int j = 0; j < i; j++) {
                temp[index] = IDs[indexID];
                index++;
            }
            indexID++;
        }
        return temp;
    }
    public static String[] generateAddresses(String json, int sum) {
        String[] temp = new String[sum];

        int index = 0;
        final JsonParser parser = Json.createParser(new StringReader(json));
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            if (event == JsonParser.Event.KEY_NAME && parser.getString().equals("street")) {
                parser.next();
                StringBuilder address = new StringBuilder(parser.getString());
                parser.next(); parser.next();
                address.append(" ").append(parser.getString());
                parser.next(); parser.next();
                address.append(" ").append(parser.getString());
                parser.next(); parser.next();
                address.append(" ").append(parser.getString());
                temp[index] = address.toString();
                index++;
            }
        }
        parser.close();

        return temp;
    }
    public String[] getAddress() { return address; }
}
