package com.example.tobi.billingapp2.Utility;

import java.io.Serializable;

public class Row implements Serializable {
    Integer index;
    String[] row;
    public void apply(Integer index, String[] row) {
        this.index = index;
        this.row = row;
    }

    public String[] getRow() {
        return row;
    }

}
