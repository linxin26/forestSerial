package co.solinx.forestserial.data;

import java.io.Serializable;

public class Request implements Serializable {
	/**
	 * 
	 */
//	private static long serialVersionUID = 6776150053843966262L;
	int id;
	Integer sn;
	long longv;
	Long longL;
	Object data;
	String version;
	Response response;
	boolean bool;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public static long getSerialversionuid() {
		return 8888;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public long getLongv() {
		return longv;
	}

	public void setLongv(long longv) {
		this.longv = longv;
	}


	public Long getLongL() {
		return longL;
	}

	public void setLongL(Long longL) {
		this.longL = longL;
	}

	public boolean isBool() {
		return bool;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}

	@Override
	public String toString() {
		return "Request{" +
				"id=" + id +
				", sn=" + sn +
				", longv=" + longv +
				", longL=" + longL +
				", data=" + data +
				", version='" + version + '\'' +
				", response=" + response +
				", bool=" + bool +
				'}';
	}
}
