package shared.model.ai;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Joel Bradley
 */
public class SheepFacts {

    private ArrayList<String> facts;

    public SheepFacts() {
        try {
            facts = new ArrayList<>();
            File input = new File("records/sheepFacts.txt");
            Scanner scan = new Scanner(input);
            scan = scan.useDelimiter("\\n+");
            while (scan.hasNext()) {
                facts.add(scan.next());
            }
            scan.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getFact() {
        String fact = facts.get(new Random().nextInt(facts.size()));
        if(fact.contains("?")) {
            fact = (fact.charAt(0) + "").toLowerCase() + fact.substring(1);
            return "Did you know that " + fact;
        } else {
            fact = (fact.charAt(0) + "").toLowerCase() + fact.substring(1, fact.length() - 1);
            return "Did you know that " + fact + "?";
        }
    }

}
