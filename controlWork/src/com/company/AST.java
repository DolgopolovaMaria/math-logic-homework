package com.company;

public class AST {

    static public Formula createAST(String str) {
        if (str.length() == 1)
            return new Variable(str.charAt(0));
        if(str.length() == 3)
            return new Variable(str.charAt(1));
        int k = 0;
        for (int i = 0; i < str.length(); i++)
            if (k == 1) {
                switch (str.charAt(i)) {
                    case '(':
                        k++;
                        break;
                    case ')':
                        k--;
                        break;
                    case '>':
                        return new Operator(OperationType.Implication, createAST(str.substring(1, i)), createAST(str.substring(i + 1, str.length() - 1)));
                    case '|':
                        return new Operator(OperationType.Disjunction, createAST(str.substring(1, i)), createAST(str.substring(i + 1, str.length() - 1)));
                    case '&':
                        return new Operator(OperationType.Conjunction, createAST(str.substring(1, i)), createAST(str.substring(i + 1, str.length() - 1)));
                    case '-':
                        return new Operator(createAST(str.substring(i + 1, str.length() - 1)));
                    default:
                        continue;
                }
            } else {
                if (str.charAt(i) == '(')
                    k++;
                else if (str.charAt(i) == ')')
                    k--;
            }
        return null;
    }
}
