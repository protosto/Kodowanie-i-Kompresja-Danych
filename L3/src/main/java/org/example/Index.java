package org.example;

public class Index {
    private int index;

    public Index() {
        index = 0;
    }

    public Index(int n) {
        index = n;
    }

    public void incInd() {
        index++;
    }

    public void addInd(int n) {
        index += n;
    }

    public int getIndex(){
        return index;
    }

}
