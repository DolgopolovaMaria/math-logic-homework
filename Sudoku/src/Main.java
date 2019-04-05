import java.io.File;

public class Main {
    public static final String inputFileName = "in.txt";
    public static final String outputFileName = "out.txt";
    public static final String tempFileName = "temp.txt";

    public static void main(String[] args) {
        File inputFile = new File(inputFileName);
        SudokuTable sudoku = new SudokuTable(inputFile);
        if(sudoku.table == null){
            return;
        }
        sudoku.solve();
        if (sudoku.satisfies()) {
            File outputFile = new File(outputFileName);
            sudoku.writeToFile(outputFile);
            sudoku.print();
        }
        else{
            System.out.println("Unsatisfiable!");
        }
    }
}
