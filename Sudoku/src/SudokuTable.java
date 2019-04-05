import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;


public class SudokuTable {
    public static final String separator = System.getProperty("line.separator");
    public static final int numberOfVariables = 9*9*9;

    int[][] table;
    int amountOfNumbers;

    public SudokuTable(File inputFile){
        try {
            table = new int[9][9];
            amountOfNumbers = 0;
            Scanner scanner = new Scanner(inputFile);
            for(int i = 0; i < 9; i++) {
                String line = scanner.nextLine();
                for (int j = 0; j < 9; j++) {
                    if (line.charAt(j) == '.') {
                        table[i][j] = -1;
                    } else {
                        table[i][j] = line.charAt(j) - '0';
                        amountOfNumbers++;
                    }
                }
            }
            scanner.close();
        }
        catch (Exception e){
            table = null;
            e.printStackTrace();
        }
    }

    public void fromArray(int[] array){
        amountOfNumbers = numberOfVariables;
        if(array.length != numberOfVariables){
            table = null;
        }
        else{
            for (int i = 0; i < numberOfVariables; i++)
                if (array[i] > 0) {
                    table[i / 81][(i % 81) / 9] = (i % 9) + 1;
                }
        }
    }

    public boolean satisfies(){
        return (amountOfNumbers == numberOfVariables);
    }

    public void writeToFile(File outputFile){
        try {
            FileWriter writer = new FileWriter(outputFile);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if((i==2) || (i==5)){
                        writer.write("_");
                    }
                    else{
                        writer.write(" ");
                    }
                    if(table[i][j] < 0){
                        writer.write(".");
                    }
                    else {
                        writer.write(Integer.toString(table[i][j]));
                    }
                    if((i==2) || (i==5)){
                        writer.write("_");
                    }
                    else{
                        writer.write(" ");
                    }
                    if((j==2) || (j==5)){
                        writer.write("|");
                    }
                }
                writer.write(separator);
            }
            writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void print(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if((i==2) || (i==5)){
                    System.out.print("_");
                }
                else{
                    System.out.print(" ");
                }
                if(table[i][j] < 0){
                    System.out.print(".");
                }
                else {
                    System.out.print(Integer.toString(table[i][j]));
                }
                if((i==2) || (i==5)){
                    System.out.print("_");
                }
                else{
                    System.out.print(" ");
                }
                if((j==2) || (j==5)){
                    System.out.print("|");
                }
            }
            System.out.println();
        }
    }

    private int buildFormula(StringBuilder formula){
        formula.setLength(0);
        int numberOfLines = 0;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 1; k < 10; k++) {
                    for (int t = 1; t < 10; t++) {
                        if (t != k) {
                            formula.append("-" + number(i, j, k) + " -" + number(i, j, t) + " 0" + separator);
                            numberOfLines++;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++) {
                for (int k = 1; k < 10; k++) {
                    formula.append(number(i, j, k) + " ");
                }
                formula.append("0" + separator);
                numberOfLines++;
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    for (int t = 1; t < 10; t++) {
                        if (j != k) {
                            formula.append("-" + number(i, j, t) + " -" + number(i, k, t) + " 0 " + separator);
                            numberOfLines++;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    for (int t = 1; t < 10; t++) {
                        if (i != k) {
                            formula.append("-" + number(i, j, t) + " -" + number(k, j, t) + " 0" + separator);
                            numberOfLines++;
                        }
                    }
                }
            }
        }

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        for (int k = 0; k < 3; k++) {
                            for (int t = 0; t < 3; t++) {
                                for (int num = 1; num < 10; num++) {
                                    int x1, y1, x2, y2;
                                    x1 = r * 3 + i;
                                    y1 = c * 3 + j;
                                    x2 = r * 3 + k;
                                    y2 = c * 3 + t;
                                    if (!(x1 == x2 && y1 == y2)) {
                                        formula.append("-" + number(x1, y1, num) + " -" + number(x2, y2, num) + " 0" + separator);
                                        numberOfLines++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (table[i][j] != -1) {
                    formula.append(number(i, j, table[i][j]) + " 0" + separator);
                    numberOfLines++;
                }
            }
        }
        return numberOfLines;
    }

    private boolean writeTempFile(File tempFile){
        try {
            StringBuilder formula = new StringBuilder();
            int numberOfLines = buildFormula(formula);
            FileWriter writer = new FileWriter(tempFile);
            writer.write("p cnf " + Integer.toString(numberOfVariables) + " " + Integer.toString(numberOfLines) + separator);
            writer.write(formula.toString());
            writer.close();
            }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void solve(){
        File tempFile = new File(Main.tempFileName);
        writeTempFile(tempFile);

        ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        DimacsReader reader = new DimacsReader(solver);

        try {
            IProblem problem = reader.parseInstance(Main.tempFileName);
            if (problem.isSatisfiable()) {
                fromArray(problem.model());
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (ParseFormatException e) {
            System.out.println("format exception");
        } catch (IOException e) {
            System.out.println("io exception");
        } catch (ContradictionException e) {
            System.out.println("Unsatisfiable (trivial)!");
        } catch (TimeoutException e) {
            System.out.println("Timeout , sorry!");
        }

    }

    private int number(int a, int b, int c) {
        return a * 81 + b * 9 + c;
    }
}