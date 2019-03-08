import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.*;
import java.util.Random;

public class Product extends DBMS {
    private static String[] ID;
    private static float[] price;

    public Product() {
        createProductFile("Product");
    }

    private static void createProductFile(String filename) {
        final int MAX_ENTRIES = 165;
        String[] names = new String[MAX_ENTRIES]; //ATTRIBUTE [2]
        String queryPS4 = "https://api-endpoint.igdb.com/games/?fields=name&limit=50&order=popularity:desc&filter\\[release_dates.platform\\]\\[eq\\]=48";
        String[] PS4Games = gameNameParser(gameApiInterface(queryPS4), 50);
        String queryXbox = "https://api-endpoint.igdb.com/games/?fields=name&limit=50&order=popularity:desc&filter\\[release_dates.platform\\]\\[eq\\]=11";
        String[] XboxGames = gameNameParser(gameApiInterface(queryXbox), 50);
        String querySwitch = "https://api-endpoint.igdb.com/games/?fields=name&limit=50&order=popularity:desc&filter\\[release_dates.platform\\]\\[eq\\]=130";
        String[] SwitchGames = gameNameParser(gameApiInterface(querySwitch), 50);
        String[] PS4Accessories = {"PS4 - Mouse", "PS4 - Keyboard", "PS4 - Controller"};
        String[] XboxAccessories = {"Xbox - Mouse", "Xbox - Keyboard", "Xbox - Controller"};
        String[] SwitchAccessories = {"Switch - Mouse", "Switch - Keyboard", "Switch - Controller"};
        String[] consoles = {"PS4", "Xbox", "Switch"};
        String[] fees = {"1-year Subscription Lite", "1-year Subscription Premium", "1-year Subscription Ultimate"};

        String[] summary = new String[MAX_ENTRIES]; //ATTRIBUTE [3]
        String[] types = new String[MAX_ENTRIES]; //ATTRIBUTE [4]
        int[] quantity = new int[MAX_ENTRIES]; //ATTRIBUTE [5]
        price = new float[MAX_ENTRIES]; //ATTRIBUTE [6]
        Random rand = new Random();

        for (int i = 0; i < 50; i++) {
            names[i] = PS4Games[i];
                types[i] = "PS4 Game";
                    quantity[i] = rand.nextInt(250) + 35;
                        price[i] = (float) 19.95 + rand.nextInt(61);
                            summary[i] = "PS4";
            names[50 + i] = XboxGames[i];
                types[50 + i] = "Xbox Game";
                    quantity[50 + i] = rand.nextInt(100) + 35;
                        price[50 + i] = (float) 9.95 + rand.nextInt(61);
                            summary[50 + i] = "Xbox";
            names[100 + i] = SwitchGames[i];
                types[100 + i] = "Switch Game";
                    quantity[100+ i] = rand.nextInt(100) + 35;
                        price[100 + i] = (float) 19.95 + rand.nextInt(61);
                            summary[100 + i] = "Switch";
        }

        for (int i = 0; i < 3; i++) {
            names[150 + i] = PS4Accessories[i];
                types[150 + i] = "PS4 Acc";
                    quantity[150 + i] = rand.nextInt(30) + 5;
                        price[150 + i] = (float) 49.95 + rand.nextInt(31);
                            summary[150 + i] = "PS4 Accessories";
            names[153 + i] = XboxAccessories[i];
                types[153 + i] = "Xbox Acc";
                    quantity[153 + i] = rand.nextInt(20) + 5;
                        price[153 + i] = (float) 49.95 + rand.nextInt(21);
                            summary[153 + i] = "Xbox Accessories";
            names[156 + i] = SwitchAccessories[i];
                types[156 + i] = "Switch Acc";
                    quantity[156 + i] = rand.nextInt(20) + 5;
                        price[156 + i] = (float) 49.95 + rand.nextInt(21);
                            summary[156 + i] = "Switch Accessories";
        }

        for (int i = 0; i < 3; i++) {
            names[159 + i] = consoles[i];
                types[159 + i] = "Consoles";
                    quantity[159 + i] = 100;
                         price[159 + i] = (float) 169.95 + rand.nextInt( 50);
                            summary[159 + i] = "Consoles";
        }

        for (int i = 0; i < 3; i++) {
            names[162 + i] = fees[i];
                types[162 + i] = "Fees";
                    quantity[162 + i] = 9999;
                        price[162 + i] = (float) 49.95 + (i * 20);
                            summary[162 + i] = "Membership fees";
        }

        ID = new String[MAX_ENTRIES]; //ATTRIBUTE [1]
        String domainID = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String[] loc = new String[MAX_ENTRIES]; //ATTRIBUTE [7]
        String domainLet = "ABCDEF";
        String domainNum = "12345";
        for (int i = 0; i < MAX_ENTRIES; i++) {
            ID[i] = generateID(domainID, 10);
            loc[i] = generateID(domainLet, 1) + generateID(domainNum, 1);
        }

        loc[162] = "NA"; loc[163] = "NA"; loc[164] = "NA";
        System.out.println(filename + ".csv has " + MAX_ENTRIES + ".");

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename + ".csv"));
            for (int i = 0; i < MAX_ENTRIES; i++) {
                writer.write(ID[i] + "," + names[i].replace(',', ' ') + "," + summary[i] + "," + types[i] + "," + quantity[i] + "," + price[i] + "," + loc[i] + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR: Unable to create " + filename + ".csv file!");
            System.exit(0);
        }
        System.out.println("GENERATE: " + filename + ".csv generated successfully!");

    }
    private static String gameApiInterface(String query) {
        ProcessBuilder pb = new ProcessBuilder("./api.sh", query);
        Process p;
        String result;

        try {
            p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();

            String line = null;
            while ( (line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }

            result = builder.toString();
        } catch (IOException e) {
            result = null;
            System.out.println("ERROR: Unable to retrieve JSON information from Game API!");
            System.exit(0);
        }

        return result;
    }
    private static String[] gameNameParser(String JSONString, int MAX_ENTRIES) {
        String[] temp = new String[MAX_ENTRIES];

        int index = 0;
        final JsonParser parser = Json.createParser(new StringReader(JSONString));
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            if (event == JsonParser.Event.KEY_NAME && parser.getString().equals("name")) {
                parser.next();
                temp[index] = parser.getString();
                index++;
            }
        }
        parser.close();

        return temp;
    }
    public String[] getProductID() { return ID; }
    public float[] getPrice() { return price; }
}
