package co.solinx.forestserial.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by linx on 2015/6/19.
 */
public class Test extends Response{

//    private color backage;
    private int b;
    private Integer inte;
    private Long longNum;
    private Short shortNum;
    private Character charNum;
    private Float floatNum;
    private Double doubleNum;
    private Boolean booleanNum;
    private Byte byteNum;

    private int a;
    private long cc;
    private String bb;
    private String ba;
    private boolean bl=false;
    private float fl;
    private double dl;
    private byte bt;
    private char cr;
    private short st;
    private String stringString;
    private Long zzzz;
    private Object obj;
    private Response response;

    private List<Integer> integerList;
    private List<String> stringList;
    private ArrayList<String> arrayList;
    private ArrayList<Long> longList;
    private ArrayList<Character> charList;
    private Map<String,Integer> map;
    private Map<Short,Short> integerMap;


    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public long getCc() {
        return cc;
    }

    public void setCc(long cc) {
        this.cc = cc;
    }

    public String getBb() {
        return bb;
    }

    public void setBb(String bb) {
        this.bb = bb;
    }

    public boolean isBl() {
        return bl;
    }

    public void setBl(boolean bl) {
        this.bl = bl;
    }

    public float getFl() {
        return fl;
    }

    public void setFl(float fl) {
        this.fl = fl;
    }

    public double getDl() {
        return dl;
    }

    public void setDl(double dl) {
        this.dl = dl;
    }

    public byte getBt() {
        return bt;
    }

    public void setBt(byte bt) {
        this.bt = bt;
    }

    public char getCr() {
        return cr;
    }

    public void setCr(char cr) {
        this.cr = cr;
    }

    public short getSt() {
        return st;
    }

    public void setSt(short st) {
        this.st = st;
    }

    public String getBa() {
        return ba;
    }

    public void setBa(String ba) {
        this.ba = ba;
    }


    public Integer getInte() {
        return inte;
    }

    public void setInte(Integer inte) {
        this.inte = inte;
    }

    public Long getLongNum() {
        return longNum;
    }

    public void setLongNum(Long longNum) {
        this.longNum = longNum;
    }

    public Short getShortNum() {
        return shortNum;
    }

    public void setShortNum(Short shortNum) {
        this.shortNum = shortNum;
    }

    public Character getCharNum() {
        return charNum;
    }

    public void setCharNum(Character charNum) {
        this.charNum = charNum;
    }

    public Float getFloatNum() {
        return floatNum;
    }

    public void setFloatNum(Float floatNum) {
        this.floatNum = floatNum;
    }

    public Double getDoubleNum() {
        return doubleNum;
    }

    public void setDoubleNum(Double doubleNum) {
        this.doubleNum = doubleNum;
    }

    public Boolean getBooleanNum() {
        return booleanNum;
    }

    public void setBooleanNum(Boolean booleanNum) {
        this.booleanNum = booleanNum;
    }

    public Byte getByteNum() {
        return byteNum;
    }

    public void setByteNum(Byte byteNum) {
        this.byteNum = byteNum;
    }

    public String getStringString() {
        return stringString;
    }

    public void setStringString(String stringString) {
        this.stringString = stringString;
    }

    public Long getZzzz() {
        return zzzz;
    }

    public void setZzzz(Long zzzz) {
        this.zzzz = zzzz;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void setIntegerList(List<Integer> integerList) {
        this.integerList = integerList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    public ArrayList<Long> getLongList() {
        return longList;
    }

    public void setLongList(ArrayList<Long> longList) {
        this.longList = longList;
    }

    public ArrayList<Character> getCharList() {
        return charList;
    }

    public void setCharList(ArrayList<Character> charList) {
        this.charList = charList;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    public Map<Short, Short> getIntegerMap() {
        return integerMap;
    }

    public void setIntegerMap(Map<Short, Short> integerMap) {
        this.integerMap = integerMap;
    }

//    public color getBackage() {
//        return backage;
//    }
//
//    public void setBackage(color backage) {
//        this.backage = backage;
//    }

    @Override
    public String toString() {
        return "Test{" +
                "b=" + b +
                ", inte=" + inte +
                ", longNum=" + longNum +
                ", shortNum=" + shortNum +
                ", charNum=" + charNum +
                ", floatNum=" + floatNum +
                ", doubleNum=" + doubleNum +
                ", booleanNum=" + booleanNum +
                ", byteNum=" + byteNum +
                ", a=" + a +
                ", cc=" + cc +
                ", bb='" + bb + '\'' +
                ", ba='" + ba + '\'' +
                ", bl=" + bl +
                ", fl=" + fl +
                ", dl=" + dl +
                ", bt=" + bt +
                ", cr=" + cr +
                ", st=" + st +
                ", stringString='" + stringString + '\'' +
                ", zzzz=" + zzzz +
                ", obj=" + obj +
                ", response=" + response +
                ", integerList=" + integerList +
                ", stringList=" + stringList +
                ", arrayList=" + arrayList +
                ", longList=" + longList +
                ", charList=" + charList +
                ", map=" + map +
                ", integerMap=" + integerMap +
                "} " + super.toString();
    }

    private String toStirng(List<String> list){
        String value = null;
        for (int i=0;i<list.size();i++){
            System.out.println(list.get(i));
            value+=list.get(i);
        }
        return value;
    }

}
