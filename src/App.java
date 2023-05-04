import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        TaxCalculator taxCalculator = new TaxCalculator(new SalesTaxCalculator());
        
        Scanner sc =new Scanner(System.in);  

        System.out.print("How much was your sales?");  
        double sales = sc.nextDouble();  

        System.out.print("Which state will you be paying from? Please input the postal abbreviation (eg. 'NY' for New York).");  
        String state = sc.next();  

        double tax = taxCalculator.calculateTax(sales, state);

        System.out.println("You will need to pay a total amount of $" + tax + " to the state of " + state + "!");

        sc.close();
    }
}

// Strategy interface of the tax calculator behavior
interface TaxCalculationStrategy {
    public double calculateTax(double amount, String stateAbbr) throws Exception;
}


// An implementation of a tax calculation strategy that specifically computes the sales tax depending on the state.
class SalesTaxCalculator implements TaxCalculationStrategy {

    @Override
    public double calculateTax(double amount, String stateAbbr) throws Exception {
        double taxRate = USStateTaxRates.getStateTaxRate(stateAbbr);

        return amount * taxRate;
    }
}

// Main calculator class to compute the tax depending on what tax calculation strategy you pass down.
class TaxCalculator {
    public TaxCalculationStrategy calculationStrategy;

    TaxCalculator(TaxCalculationStrategy calculationStrategy) {
        this.calculationStrategy = calculationStrategy;
    }

    public double calculateTax(double amount, String stateAbbr) throws Exception {
        return calculationStrategy.calculateTax(amount, stateAbbr);
    }
}


// Class that holds the record of different tax rates of different states.

class USStateTaxRates {
    private static Map<String, Double> taxRateTable;

    static {
        taxRateTable = new HashMap<>();

        taxRateTable.put("MN", 0.0678);
        taxRateTable.put("CA", 0.065);
        taxRateTable.put("NY", 0.04);
        taxRateTable.put("NM", 0.05125);
        taxRateTable.put("TX", 0.0625);
    }

    public static void addState(String stateAbbr, double taxRate) {
        taxRateTable.putIfAbsent(stateAbbr, taxRate);
    }

    public static void updateTaxRateOfState(String stateAbbr, double newRate) throws Exception {
        if (!taxRateTable.containsKey(stateAbbr)) {
            throw new Exception("Can't update tax rate because state " + stateAbbr + " does not exist.");
        }

        taxRateTable.put(stateAbbr, newRate);
    }

    public static double getStateTaxRate(String stateAbbr) throws Exception {
        if (!taxRateTable.containsKey(stateAbbr)) {
            throw new Exception("No record of state " + stateAbbr);
        }

        return taxRateTable.get(stateAbbr);
    }

    public static void removeState(String stateAbbr) throws Exception {
        if (!taxRateTable.containsKey(stateAbbr)) {
            throw new Exception("Can't delete state. No record of state " + stateAbbr);
        }

        taxRateTable.remove(stateAbbr);
    }

    public static ArrayList<String> getAllStateAbbreviations() {
        return new ArrayList<>(taxRateTable.keySet());
    }

    public static ArrayList<Double> getAllStateTaxRates() {
        return new ArrayList<>(taxRateTable.values());
    }
}


