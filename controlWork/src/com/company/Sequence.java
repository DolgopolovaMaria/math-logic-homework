package com.company;

import java.util.ArrayList;

public class Sequence implements Cloneable{
    Sequence left, right;
    ArrayList<Formula> anticedent;
    ArrayList<Formula> succedent;
    Sequence() {
        left = null;
        right = null;
        succedent = new ArrayList<>();
        anticedent = new ArrayList<>();
    }
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < anticedent.size(); i++) {
            str.append(anticedent.get(i).print());
            if (anticedent.size() - 1 == i)
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
    public Sequence clone() throws CloneNotSupportedException{
        Sequence newSequence = new Sequence();
        newSequence.anticedent = new ArrayList<Formula>(anticedent);
        newSequence.succedent = new ArrayList<Formula>(succedent);
        return newSequence;
    }
}
