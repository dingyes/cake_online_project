package onest.dy.entity;

public class Customer {
	private String customerPhone;
	private String customerPassword;
	private String nickname;
	private String address;
	private String customerPhoto;

	public Customer() {
	}

	public Customer(String customerPhone, String customerPassword, String nickname, String address,
			String customerPhoto) {
		this.customerPhone = customerPhone;
		this.customerPassword = customerPassword;
		this.nickname = nickname;
		this.address = address;
		this.customerPhoto = customerPhoto;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getCustomerPassword() {
		return customerPassword;
	}

	public void setCustomerPassword(String customerPassword) {
		this.customerPassword = customerPassword;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCustomerPhoto() {
		return customerPhoto;
	}

	public void setCustomerPhoto(String customerPhoto) {
		this.customerPhoto = customerPhoto;
	}

	@Override
	public String toString() {
		return "Customer [customerPhone=" + customerPhone + ", customerPassword=" + customerPassword + ", nickname="
				+ nickname + ", address=" + address + ", customerPhoto=" + customerPhoto + "]";
	}

}
