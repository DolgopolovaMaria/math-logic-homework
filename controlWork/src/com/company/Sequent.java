package com.company;

import java.util.ArrayList;

public class Sequent implements Cloneable{
    Sequent left, right;
    ArrayList<Formula> antecedent;
    ArrayList<Formula> succedent;
    Sequent() {
        left = null;
        right = null;
        succedent = new ArrayList<>();
        antecedent = new ArrayList<>();
    }
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < antecedent.size(); i++) {
            str.append(antecedent.get(i).print());
            if (antecedent.size() - 1 == i)
                continue;
            str.append(", ");
        }
        str.append(" |- ");

        for (int i = 0; i < succedent.size(); i++) {
            str.append(succedent.get(i).print());
            if (succedent.size() - 1 == i)
                continue;
            str.append(", ");
        }
        return str.toString();
    }

    @Override
    public Sequent clone() throws CloneNotSupportedException{
        Sequent newsequent = new Sequent();
        newsequent.antecedent = new ArrayList<Formula>(antecedent);
        newsequent.succedent = new ArrayList<Formula>(succedent);
        return newsequent;
    }
}
