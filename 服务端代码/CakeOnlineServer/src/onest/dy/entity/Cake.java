package onest.dy.entity;

public class Cake {
	private int id;
	private String sellerPhone;
	private String cakeName;
	private int price;
	private String description;
	private int size;
	private String cakeImg;

	public Cake() {
	}

	public Cake(String sellerPhone, String cakeName, int price, String description, int size, String cakeImg) {
		this.sellerPhone = sellerPhone;
		this.cakeName = cakeName;
		this.price = price;
		this.description = description;
		this.size = size;
		this.cakeImg = cakeImg;
	}

	public Cake(int id, String sellerPhone, String cakeName, int price, String description, int size, String cakeImg) {
		super();
		this.id = id;
		this.sellerPhone = sellerPhone;
		this.cakeName = cakeName;
		this.price = price;
		this.description = description;
		this.size = size;
		this.cakeImg = cakeImg;
	}

	public int getId() {
		return id;
	}

	public String getSellerPhone() {
		return sellerPhone;
	}

	public void setSellerPhone(String sellerPhone) {
		this.sellerPhone = sellerPhone;
	}

	public String getCakeName() {
		return cakeName;
	}

	public void setCakeName(String cakeName) {
		this.cakeName = cakeName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getCakeImg() {
		return cakeImg;
	}

	public void setCakeImg(String cakeImg) {
		this.cakeImg = cakeImg;
	}

	@Override
	public String toString() {
		return "Cake [id=" + id + ", sellerPhone=" + sellerPhone + ", cakeName=" + cakeName + ", price=" + price
				+ ", description=" + description + ", size=" + size + ", cakeImg=" + cakeImg + "]";
	}

}
