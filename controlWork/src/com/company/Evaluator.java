package com.company;

import java.util.Scanner;

public class Evaluator {
    public static void run() throws Exception {
        Scanner sc = new Scanner(System.in);
        Formula formula = AST.createAST(sc.nextLine());
        Sequence sequence = new Sequence();
        sequence.succedent.add(formula);
        createTreeSequence(sequence);
        findCounterexample(sequence);
        printTree(sequence, 0);
    }

    public static boolean findCounterexample(Sequence sequence)
    {
        if (sequence == null)
            return false;
        if (sequence.left == null && sequence.right == null)
        {
            boolean ok = true;
            for (int i = 0; i < sequence.anticedent.size(); i++)
                for (int j = 0; j < sequence.succedent.size(); j++)
                    if (((Variable)sequence.anticedent.get(i)).nameOfVarible == ((Variable)sequence.succedent.get(j)).nameOfVarible)
                        ok = false;
            if (ok)
            {
                System.out.println("Counterexample:");
                System.out.print("1: ");
                for (int i = 0; i < sequence.anticedent.size(); i++)
                    System.out.print(((Variable)sequence.anticedent.get(i)).nameOfVarible + " ");
                System.out.println();
                System.out.print("0: ");
                for (int i = 0; i < sequence.succedent.size(); i++)
                    System.out.print(((Variable)sequence.succedent.get(i)).nameOfVarible + " ");
                System.out.println();
                System.out.println();
                return true;
            }
            return false;
        }
        else
        {
            if (!findCounterexample(sequence.left))
                return findCounterexample(sequence.right);
            return true;
        }
    }

    public static void printTree(Sequence sequence, int n)
    {
        if (sequence != null) {
            printTree(sequence.left, n+5);
            for (int i = 0; i < n; i++)
                System.out.print(" ");
            System.out.println(sequence);
            printTree(sequence.right, n+5);
        }
    }

    public static void createTreeSequence(Sequence sequence) throws Exception{
        for (int i = 0; i < sequence.anticedent.size(); i++) {
            if (sequence.anticedent.get(i) instanceof Operator) {
                Operator elem = ((Operator) sequence.anticedent.get(i));
                switch (elem.operationType) {
                    case Implication: {
                        Sequence newSequnce1 = sequence.clone();
                        newSequnce1.anticedent.remove(i);
                        newSequnce1.anticedent.add(elem.right);
                        Sequence newSequnce2 = sequence.clone();
                        newSequnce2.anticedent.remove(i);
                        newSequnce2.succedent.add(elem.left);
                        sequence.left = newSequnce1;
                        sequence.right = newSequnce2;
                        createTreeSequence(sequence.left);
                        createTreeSequence(sequence.right);
                        return;
                    }
                    case Disjunction: {
                        Sequence newSequnce1 = sequence.clone();
                        newSequnce1.anticedent.remove(i);
                        newSequnce1.anticedent.add(elem.left);
                        Sequence newSequnce2 = sequence.clone();
                        newSequnce2.anticedent.remove(i);
                        newSequnce2.anticedent.add(elem.right);
                        sequence.left = newSequnce1;
                        sequence.right = newSequnce2;
                        createTreeSequence(sequence.left);
                        createTreeSequence(sequence.right);
                        return;
                    }
                    case Conjunction: {
                        Sequence newSequnce1 = sequence.clone();
                        newSequnce1.anticedent.remove(i);
                        newSequnce1.anticedent.add(elem.left);
                        newSequnce1.anticedent.add(elem.right);
                        sequence.left = newSequnce1;
                        createTreeSequence(sequence.left);
                        return;
                    }
                    case Negation: {
                        Sequence newSequnce1 = sequence.clone();
                        newSequnce1.anticedent.remove(i);
                        newSequnce1.succedent.add(elem.right);
                        sequence.right = newSequnce1;
                        createTreeSequence(sequence.right);
                        return;
                    }
                }
            }
        }

        for (int i = 0; i < sequence.succedent.size(); i++) {
            if (sequence.succedent.get(i) instanceof Operator) {
                Operator elem = ((Operator) sequence.succedent.get(i));
                switch (elem.operationType) {
                    case Implication: {
                        Sequence newSequnce1 = sequence.clone();
                        newSequnce1.succedent.remove(i);
                        newSequnce1.anticedent.add(elem.left);
                        newSequnce1.succedent.add(elem.right);
                        sequence.left = newSequnce1;
                        createTreeSequence(sequence.left);
                        return;
                    }
                    case Disjunction: {
                        Sequence newSequnce1 = sequence.clone();
                        newSequnce1.succedent.remove(i);
                        newSequnce1.succedent.add(elem.left);
                        newSequnce1.succedent.add(elem.right);
                        sequence.left = newSequnce1;
                        createTreeSequence(sequence.left);
                        return;
                    }
                    case Conjunction: {
                        Sequence newSequnce1 = sequence.clone();
                        newSequnce1.succedent.remove(i);
                        newSequnce1.succedent.add(elem.left);
                        Sequence newSequnce2 = sequence.clone();
                        newSequnce2.succedent.remove(i);
                        newSequnce2.succedent.add(elem.right);
                        sequence.left = newSequnce1;
                        sequence.right = newSequnce2;
                        createTreeSequence(sequence.left);
                        createTreeSequence(sequence.right);
                        return;
                    }
                    case Negation: {
                        Sequence newSequnce1 = sequence.clone();
                        newSequnce1.succedent.remove(i);
                        newSequnce1.anticedent.add(elem.right);
                        sequence.right = newSequnce1;
                        createTreeSequence(sequence.right);
                        return;
                    }
                }
            }
        }
    }

}
