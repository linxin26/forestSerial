package co.solinx.forestserial.test.data;

/**
 * Created by linx on 2015/6/26.
 */
public class BaseSuperClass extends BaseClass{

    private int baseID;


    public int getBaseID() {
        return baseID;
    }

    public void setBaseID(int baseID) {
        this.baseID = baseID;
    }

    @Override
    public String toString() {
        return "BaseSuperClass{" +
                "baseID=" + baseID +
                "} " + super.toString();
    }
}
