package co.solinx.forestserial.test.data;

import java.io.Serializable;

public class Response extends BaseSuperClass implements Serializable {

	/**
	 * 
	 */
	private static long serialVersionUID = -5544929078118767872L;
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

	public void setSuperSn(int sn){
		this.sn=sn;
	}

	@Override
	public String toString() {
		return "Response{" +
				"Result=" + Result +
				", sn=" + sn +
				"} " + super.toString();
	}

}
