package onest.dy.entity;

public class OrderDetail {
	private int id;
	private String customerPhone;
	private int count;
	private int status;
	private String cakeName;

	public OrderDetail() {
	}

	public OrderDetail(int id, int count, int status, String cakeName) {
		super();
		this.id = id;
		this.count = count;
		this.status = status;
		this.cakeName = cakeName;
	}

	public OrderDetail(int id, String customerPhone, int count, int status, String cakeName) {
		this.id = id;
		this.customerPhone = customerPhone;
		this.count = count;
		this.status = status;
		this.cakeName = cakeName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCakeName() {
		return cakeName;
	}

	public void setCakeName(String cakeName) {
		this.cakeName = cakeName;
	}
}
