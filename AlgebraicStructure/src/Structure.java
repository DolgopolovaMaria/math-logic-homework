import java.io.File;
import java.util.Scanner;

public class Structure {
    private int[][] table;
    private int amountOfElements;
    private int neutralElement;

    public Structure(File inputFile){
        try {
            Scanner scanner = new Scanner(inputFile);
            amountOfElements = scanner.nextInt();
            table = new int[amountOfElements][amountOfElements];
            neutralElement = -1;;
            for(int i = 0; i < amountOfElements; i++){
                for(int j = 0; j < amountOfElements; j++){
                    table[i][j] = scanner.nextInt();
                }
            }
            scanner.close();
        }
        catch (Exception e){
            table = null;
            e.printStackTrace();
        }
    }

    public void print(){
        for(int i = 0; i < amountOfElements; i++) {
            for (int j = 0; j < amountOfElements; j++) {
                System.out.print(table[i][j]);
            }
            System.out.println();
        }
    }

    //(xy)z == x(yz)
    public boolean associativity(){
        for(int i = 0; i < amountOfElements; i++){
            for(int j = 0; j < amountOfElements; j++){
                for(int k = 0; k < amountOfElements; k++) {
                    if(table[table[i][j]][k] != table[i][table[j][k]]) {
                        return false;
                    }
                }

            }
        }
        return true;
    }

    //x*e == x == e*x
    public boolean hasNeutralElement(){
        for(int i = 0; i < amountOfElements; i++){
            int j = 0;
            boolean ok = true;
            while((j < amountOfElements) && ok){
                if((table[i][j] != j) || (table[j][i] != j)){
                    //System.out.println(table[i][j]);
                    ok = false;
                }
                j++;
            }
            if(ok){
                neutralElement = i;
                return true;
            }
        }
        return false;
    }

    //xx^(-1) == e == x^(-1)x
    public boolean reversibility(){
        if(neutralElement == -1){
            return false;
        }
        for(int i = 0; i < amountOfElements; i++){
            boolean ok = false;
            int j = 0;
            while ((!ok) && (j < amountOfElements)){
                if((table[i][j] == neutralElement) &&
                        (table[j][i] == neutralElement)){
                    ok = true;
                }
                j++;
            }
            if(!ok){
                return false;
            }
        }
        return true;
    }

    //xy == yx
    public boolean commutativity(){
        for(int i = 0; i < amountOfElements; i++){
            for(int j = 0; j < amountOfElements; j++){
                if(table[i][j] != table[j][i]){
                    return false;
                }
            }
        }
        return true;
    }

    //xy < amount of elements
    public boolean closure(){
        for(int i = 0; i < amountOfElements; i++){
            for(int j = 0; j < amountOfElements; j++){
                if(table[i][j] >= amountOfElements){
                    return false;
                }
            }
        }
        return true;
    }
}
