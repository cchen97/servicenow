import java.io.FileNotFoundException;
import java.util.TreeMap;
import java.util.Scanner;
import java.util.ArrayList;
import javax.naming.ldap.PagedResultsControl;

import java.util.List;
import java.io.File;

// CompanyTreeNode keeps track the name of the company, all of its products, and 
// total amount of spend
class CompanyTreeNode {
    String label;
    TreeMap<String, ProductTreeNode> products;
    int totalAmount;
    
    CompanyTreeNode(String label, TreeMap<String, ProductTreeNode> products, int totalAmount) {
        this.label = label;
        this.products = products;
        this.totalAmount = totalAmount;
    }
}

// ProductTreeNode keeps track of the name of the product and its spend
class ProductTreeNode {
    String label;
    int amount;

    ProductTreeNode(String label, int amount) {
        this.label = label;
        this.amount = amount;
    }
}

// SoftwareSpendReporter helps IT departments make sense of their
// software spend. It's a console application that reads in software spend data and outputs a report
// summarizing that data.
public class SoftwareSpendReporter {
    
    // spendReporter takes in a tree, a company name, a product name and the spend as parameters and store the product
    // into the tree then returns the updated tree
    static TreeMap<String, CompanyTreeNode> spendReporter(TreeMap<String, CompanyTreeNode> tree, String companyName, String productName, int productAmount) throws FileNotFoundException {
        if (!tree.containsKey((companyName))){
            TreeMap<String, ProductTreeNode> productsList = new TreeMap<String, ProductTreeNode>(); //empty list for products
            CompanyTreeNode company = new CompanyTreeNode(companyName, productsList, productAmount);
            ProductTreeNode product = new ProductTreeNode(productName, productAmount);
            company.products.put(productName, product);
            tree.put(companyName, company);
        } else {
            CompanyTreeNode company = tree.get(companyName);
            ProductTreeNode product = new ProductTreeNode(productName, productAmount);
            if (company.products.containsKey(productName)){
                company.products.get(productName).amount += productAmount;
            } else {
                company.products.put(productName, product);
            }
            company.totalAmount += productAmount;
        }
        return tree;
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Enter input file path: ");
        Scanner userInput = new Scanner(System.in);
        String filepath = userInput.nextLine().trim();
        userInput.close();
        
        Scanner scanner = new Scanner(new File(filepath));
        TreeMap<String, CompanyTreeNode> tree = new TreeMap<String, CompanyTreeNode>();
        scanner.useDelimiter(",");
        String colNames = scanner.nextLine(); // gets rid of the column names of the csv file
        
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] vals = line.split(",");
            String companyName = vals[1];
            String productName = vals[2];
            int productAmount = Integer.parseInt(vals[3]);
            tree = spendReporter(tree, companyName, productName, productAmount);
        }
        
        for (String i : tree.keySet()){
            System.out.println(i + " $" + String.format("%,d", tree.get(i).totalAmount));
            for (String p: tree.get(i).products.keySet()){
                System.out.println("  " + p + " $" + String.format("%,d", tree.get(i).products.get(p).amount));
            }
        }
        scanner.close();
    }
}

