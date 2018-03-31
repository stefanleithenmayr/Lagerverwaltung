package model;

public class Item {
    private int  itemnr;
    private int productnr;
    private String eancode;
    private int refItemnr;

    public Item (int itemnr, int productnr, String eancode, int refItemnr){
        this.itemnr = itemnr;
        this.productnr = productnr;
        this.eancode = eancode;
        this.refItemnr = refItemnr;
    }

    public int getItemnr() {
        return itemnr;
    }

    public void setItemnr(int itemnr) {
        this.itemnr = itemnr;
    }

    public int getProductnr() {
        return productnr;
    }

    public void setProductnr(int productnr) {
        this.productnr = productnr;
    }

    public String getEancode() {
        return eancode;
    }

    public void setEancode(String eancode) {
        this.eancode = eancode;
    }

    public int getRefItemnr() {
        return refItemnr;
    }

    public void setRefItemnr(int refItemnr) {
        this.refItemnr = refItemnr;
    }
}
