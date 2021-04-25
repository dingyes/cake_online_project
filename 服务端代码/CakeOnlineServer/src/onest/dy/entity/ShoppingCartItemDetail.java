package onest.dy.entity;

public class ShoppingCartItemDetail {
	private int id;
	private int cakeId; // µ°¸âid
	private String cakeImg; // µ°¸âÍ¼Æ¬
	private String cakeName; // µ°¸âÃû×Ö
	private String sellerName; // Âò¼ÒÃû³Æ
	private int cakeSize;
	private int cakePrice;
	private int count;

	public ShoppingCartItemDetail(int id, int cakeId, String cakeImg, String cakeName, String sellerName, int cakeSize,
			int cakePrice, int count) {
		super();
		this.id = id;
		this.cakeId = cakeId;
		this.cakeImg = cakeImg;
		this.cakeName = cakeName;
		this.sellerName = sellerName;
		this.cakeSize = cakeSize;
		this.cakePrice = cakePrice;
		this.count = count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCakeId() {
		return cakeId;
	}

	public void setCakeId(int cakeId) {
		this.cakeId = cakeId;
	}

	public String getCakeImg() {
		return cakeImg;
	}

	public void setCakeImg(String cakeImg) {
		this.cakeImg = cakeImg;
	}

	public String getCakeName() {
		return cakeName;
	}

	public void setCakeName(String cakeName) {
		this.cakeName = cakeName;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public int getCakeSize() {
		return cakeSize;
	}

	public void setCakeSize(int cakeSize) {
		this.cakeSize = cakeSize;
	}

	public int getCakePrice() {
		return cakePrice;
	}

	public void setCakePrice(int cakePrice) {
		this.cakePrice = cakePrice;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "ShoppingCartItemDetail [id=" + id + ", cakeId=" + cakeId + ", cakeImg=" + cakeImg + ", cakeName="
				+ cakeName + ", sellerName=" + sellerName + ", cakeSize=" + cakeSize + ", cakePrice=" + cakePrice
				+ ", count=" + count + "]";
	}

}
