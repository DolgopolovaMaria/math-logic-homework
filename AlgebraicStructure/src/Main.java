import java.io.File;

public class Main {
    public final static String inputFileName = "Group.txt";

    public static TypeOfStructure checkTypeOfStructure(Structure structure){
        if(structure.closure()){
            if(structure.associativity()){
                if(structure.hasNeutralElement()){
                    if(structure.reversibility()){
                        if(structure.commutativity()){
                            return TypeOfStructure.AbelianGroup;
                        }
                        return TypeOfStructure.Group;
                    }
                    if(structure.commutativity()){
                        return TypeOfStructure.CommutativeMonoid;
                    }
                    return TypeOfStructure.Monoid;
                }
                return TypeOfStructure.Semigroup;
            }
            return TypeOfStructure.Magma;
        }
        return TypeOfStructure.Nothing;
    }

    public static boolean test(){
        File file = new File("Nothing.txt");
        Structure nothing = new Structure(file);
        if(checkTypeOfStructure(nothing) != TypeOfStructure.Nothing){
            return false;
        }

        file = new File("Magma.txt");
        Structure magma = new Structure(file);
        if(checkTypeOfStructure(magma) != TypeOfStructure.Magma){
            return false;
        }

        file = new File("Group.txt");
        Structure group = new Structure(file);
        if(checkTypeOfStructure(group) != TypeOfStructure.Group){
            return false;
        }

        file = new File("Semigroup.txt");
        Structure semiGroup = new Structure(file);
        if(checkTypeOfStructure(semiGroup) != TypeOfStructure.Semigroup){
            return false;
        }

        file = new File("AbelianGroup.txt");
        Structure abelianGroup = new Structure(file);
        if(checkTypeOfStructure(abelianGroup) != TypeOfStructure.AbelianGroup){
            return false;
        }

        file = new File("CommMonoid.txt");
        Structure commMonoid = new Structure(file);
        if(checkTypeOfStructure(commMonoid) != TypeOfStructure.CommutativeMonoid){
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        if(test()){
            System.out.println("ok");
        }

        File file = new File(inputFileName);
        Structure structure = new Structure(file);
        System.out.println(checkTypeOfStructure(structure));
    }
}
