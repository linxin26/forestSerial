package co.solinx.forestserial.data;

import java.io.Serializable;

public class Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5544929078118767872L;
	int sn;
	Object Result;

	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public Object getResult() {
		return Result;
	}

	public void setResult(Object result) {
		Result = result;
	}

	@Override
	public String toString() {
		return "Response [sn=" + sn + ", Result=" + Result + "]";
	}

}
