package model;

import javax.xml.bind.annotation.XmlAttribute;

public class CustomerBean {
	@XmlAttribute
	private String account;
	private String name;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "CustomerBean [account=" + account + ", name=" + name + "]";
	}
}
