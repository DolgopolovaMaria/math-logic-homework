package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Evaluator {
    public static void run() throws Exception {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        while (!input.equals("0")) {
            Formula formula = AST.createAST(input);
            Sequent sequent = new Sequent();
            sequent.succedent.add(formula);
            createTreeSequent(sequent);
            removeDuplicatesFromSequent(sequent);
            findCounterexample(sequent);
            printTree(sequent, 0);
            input = sc.nextLine();
        }
    }

    private static boolean findCounterexample(Sequent sequent)
    {
        if (sequent == null)
            return false;
        if (sequent.left == null && sequent.right == null)
        {
            boolean ok = true;
            for (int i = 0; i < sequent.antecedent.size(); i++)
                for (int j = 0; j < sequent.succedent.size(); j++)
                    if (((Variable)sequent.antecedent.get(i)).nameOfVarible == ((Variable)sequent.succedent.get(j)).nameOfVarible)
                        ok = false;
            if (ok)
            {
                System.out.println("Counterexample:");
                System.out.print("1: ");
                for (int i = 0; i < sequent.antecedent.size(); i++)
                    System.out.print(((Variable)sequent.antecedent.get(i)).nameOfVarible + " ");
                System.out.println();
                System.out.print("0: ");
                for (int i = 0; i < sequent.succedent.size(); i++)
                    System.out.print(((Variable)sequent.succedent.get(i)).nameOfVarible + " ");
                System.out.println();
                System.out.println();
                return true;
            }
            return false;
        }
        else
        {
            if (!findCounterexample(sequent.left))
                return findCounterexample(sequent.right);
            return true;
        }
    }

    private static void printTree(Sequent sequent, int n)
    {
        if (sequent != null) {
            printTree(sequent.left, n+5);
            for (int i = 0; i < n; i++)
                System.out.print(" ");
            System.out.println(sequent);
            printTree(sequent.right, n+5);
        }
    }

    private static ArrayList<Formula> removeDuplicatesFromList(ArrayList<Formula> list){
        ArrayList<Formula> newList = new ArrayList<Formula>();
        for(int i = 0; i < list.size(); i++){
            char name = ((Variable)list.get(i)).nameOfVarible;
            int n = 0;
            while ((n < newList.size()) && (((Variable)newList.get(n)).nameOfVarible != name)){
                n++;
            }
            if(n == newList.size()){
                newList.add(new Variable(name));
            }
        }
        return newList;
    }

    private static void removeDuplicatesFromSequent(Sequent sequent){
        if (sequent == null)
            return;
        if (sequent.left == null && sequent.right == null){
            sequent.antecedent = removeDuplicatesFromList(sequent.antecedent);
            sequent.succedent = removeDuplicatesFromList(sequent.succedent);
            return;
        }
        else{
            removeDuplicatesFromSequent(sequent.left);
            removeDuplicatesFromSequent(sequent.right);
        }
    }

    private static void createTreeSequent(Sequent sequent) throws Exception{
        for (int i = 0; i < sequent.antecedent.size(); i++) {
            if (sequent.antecedent.get(i) instanceof Operator) {
                Operator elem = ((Operator) sequent.antecedent.get(i));
                switch (elem.operationType) {
                    case Implication: {
                        Sequent newSequnce1 = sequent.clone();
                        newSequnce1.antecedent.remove(i);
                        newSequnce1.antecedent.add(elem.right);
                        Sequent newSequnce2 = sequent.clone();
                        newSequnce2.antecedent.remove(i);
                        newSequnce2.succedent.add(elem.left);
                        sequent.left = newSequnce1;
                        sequent.right = newSequnce2;
                        createTreeSequent(sequent.left);
                        createTreeSequent(sequent.right);
                        return;
                    }
                    case Disjunction: {
                        Sequent newSequnce1 = sequent.clone();
                        newSequnce1.antecedent.remove(i);
                        newSequnce1.antecedent.add(elem.left);
                        Sequent newSequnce2 = sequent.clone();
                        newSequnce2.antecedent.remove(i);
                        newSequnce2.antecedent.add(elem.right);
                        sequent.left = newSequnce1;
                        sequent.right = newSequnce2;
                        createTreeSequent(sequent.left);
                        createTreeSequent(sequent.right);
                        return;
                    }
                    case Conjunction: {
                        Sequent newSequnce1 = sequent.clone();
                        newSequnce1.antecedent.remove(i);
                        newSequnce1.antecedent.add(elem.left);
                        newSequnce1.antecedent.add(elem.right);
                        sequent.left = newSequnce1;
                        createTreeSequent(sequent.left);
                        return;
                    }
                    case Negation: {
                        Sequent newSequnce1 = sequent.clone();
                        newSequnce1.antecedent.remove(i);
                        newSequnce1.succedent.add(elem.right);
                        sequent.right = newSequnce1;
                        createTreeSequent(sequent.right);
                        return;
                    }
                }
            }
        }

        for (int i = 0; i < sequent.succedent.size(); i++) {
            if (sequent.succedent.get(i) instanceof Operator) {
                Operator elem = ((Operator) sequent.succedent.get(i));
                switch (elem.operationType) {
                    case Implication: {
                        Sequent newSequnce1 = sequent.clone();
                        newSequnce1.succedent.remove(i);
                        newSequnce1.antecedent.add(elem.left);
                        newSequnce1.succedent.add(elem.right);
                        sequent.left = newSequnce1;
                        createTreeSequent(sequent.left);
                        return;
                    }
                    case Disjunction: {
                        Sequent newSequnce1 = sequent.clone();
                        newSequnce1.succedent.remove(i);
                        newSequnce1.succedent.add(elem.left);
                        newSequnce1.succedent.add(elem.right);
                        sequent.left = newSequnce1;
                        createTreeSequent(sequent.left);
                        return;
                    }
                    case Conjunction: {
                        Sequent newSequnce1 = sequent.clone();
                        newSequnce1.succedent.remove(i);
                        newSequnce1.succedent.add(elem.left);
                        Sequent newSequnce2 = sequent.clone();
                        newSequnce2.succedent.remove(i);
                        newSequnce2.succedent.add(elem.right);
                        sequent.left = newSequnce1;
                        sequent.right = newSequnce2;
                        createTreeSequent(sequent.left);
                        createTreeSequent(sequent.right);
                        return;
                    }
                    case Negation: {
                        Sequent newSequnce1 = sequent.clone();
                        newSequnce1.succedent.remove(i);
                        newSequnce1.antecedent.add(elem.right);
                        sequent.right = newSequnce1;
                        createTreeSequent(sequent.right);
                        return;
                    }
                }
            }
        }
    }

}
