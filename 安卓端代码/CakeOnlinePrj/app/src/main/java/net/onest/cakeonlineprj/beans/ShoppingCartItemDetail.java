package net.onest.cakeonlineprj.beans;

public class ShoppingCartItemDetail {
    private int id;
    private int cakeId;
    private String cakeImg;
    private String cakeName;
    private String sellerName;
    private int cakeSize;
    private int cakePrice;
    private int cakeCount;
    private boolean selected = false;

    public ShoppingCartItemDetail(int id, int cakeId, String cakeImg, String cakeName, String sellerName, int cakeSize,
                                  int cakePrice, int cakeCount) {
        super();
        this.id = id;
        this.cakeId = cakeId;
        this.cakeImg = cakeImg;
        this.cakeName = cakeName;
        this.sellerName = sellerName;
        this.cakeSize = cakeSize;
        this.cakePrice = cakePrice;
        this.cakeCount = cakeCount;
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

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getCakeCount() {
        return cakeCount;
    }

    public void setCakeCount(int cakeCount) {
        this.cakeCount = cakeCount;
    }

    @Override
    public String toString() {
        return "ShoppingCartItemDetail{" +
                "id=" + id +
                ", cakeId=" + cakeId +
                ", cakeImg='" + cakeImg + '\'' +
                ", cakeName='" + cakeName + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", cakeSize=" + cakeSize +
                ", cakePrice=" + cakePrice +
                ", cakeCount=" + cakeCount +
                ", selected=" + selected +
                '}';
    }
}
