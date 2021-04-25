package onest.dy.entity;

public class ShoppingCartItem {
	private int id;
	private String customerPhone;
	private int cakeId;
	private int count;

	public ShoppingCartItem() {
	}

	public ShoppingCartItem(int id, String customerPhone, int cakeId, int count) {
		super();
		this.id = id;
		this.customerPhone = customerPhone;
		this.cakeId = cakeId;
		this.count = count;
	}

	public ShoppingCartItem(String customerPhone, int cakeId, int count) {
		this.customerPhone = customerPhone;
		this.cakeId = cakeId;
		this.count = count;
	}

	public int getId() {
		return id;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public int getCakeId() {
		return cakeId;
	}

	public void setCakeId(int cakeId) {
		this.cakeId = cakeId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "ShoppingCardGoods [id=" + id + ", customerPhone=" + customerPhone + ", cakeId=" + cakeId + ", count="
				+ count + "]";
	}

}
