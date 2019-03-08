import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Purchased extends DBMS {
    private static String[] purchased_ID;
    private static String[] fullPurchased_ID;
    private static String[] prod_ID;
    private static int numberOfMemberships;
    private static float[] prices;

    public Purchased(String[] purchased_ID, String[] prod_ID, float[] prices, int numberOfMemberships) {
        this.purchased_ID = purchased_ID;
        this.prod_ID = prod_ID;
        this.prices = prices;
        this.numberOfMemberships = numberOfMemberships;
        createPurchasesFile("purchased");
    }
    private static void createPurchasesFile(String filename) {
        ArrayList<String> purch_temp = new ArrayList<>();
        ArrayList<String> prod_temp = new ArrayList<>();
        ArrayList<String> quantity_temp = new ArrayList<>();
        ArrayList<String> totalPrice_temp = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < numberOfMemberships; i++) {
            purch_temp.add(purchased_ID[i]);
            prod_temp.add(prod_ID[162]);
            quantity_temp.add("1");
            totalPrice_temp.add(prices[162] + "");
        }

        for (int i = numberOfMemberships; i < purchased_ID.length; i++) {
            float sum = 0;
            for (int j = 0; j < (rand.nextInt(3) + 1); j++) {
                purch_temp.add(purchased_ID[i]);
                int randomProduct = rand.nextInt(162);
                prod_temp.add(prod_ID[randomProduct]);
                int productBought = rand.nextInt(3) + 1;
                sum += (productBought * prices[randomProduct]);
                quantity_temp.add(productBought + "");
                totalPrice_temp.add(sum + "");
            }
        }
        final int MAX_ENTRIES = purch_temp.size();
        System.out.println(filename + ".csv has " + MAX_ENTRIES + ".");
        fullPurchased_ID = purch_temp.toArray(new String[MAX_ENTRIES]);

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename + ".csv"));
            for (int i = 0; i < MAX_ENTRIES; i++) {
                writer.write(purch_temp.get(i) + "," + prod_temp.get(i) + "," + quantity_temp.get(i) + "," + totalPrice_temp.get(i) + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR: Unable to create " + filename + ".csv file!");
            System.exit(0);
        }
        System.out.println("GENERATE: " + filename + ".csv generated successfully!");
    }
    public String[] getPurchased_ID() { return fullPurchased_ID; }
}
