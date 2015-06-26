package co.solinx.forestserial.data;

/**
 * Created by linx on 2015/6/26.
 */
public class BaseClass {
    private int baseClassID;

    public int getBaseClassID() {
        return baseClassID;
    }

    public void setBaseClassID(int baseClassID) {
        this.baseClassID = baseClassID;
    }

    @Override
    public String toString() {
        return "BaseClass{" +
                "baseClassID=" + baseClassID +
                '}';
    }
}
