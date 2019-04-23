package com.company;

public class Variable implements Formula{
    public char nameOfVarible;

    public Variable(char name){
        nameOfVarible = name;
    }

    @Override
    public String print() {
        return Character.toString(nameOfVarible);
    }
}
