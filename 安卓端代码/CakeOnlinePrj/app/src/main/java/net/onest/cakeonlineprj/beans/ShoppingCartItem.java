package net.onest.cakeonlineprj.beans;

public class ShoppingCartItem {
    private int id;
    private int cakeId;
    private String customerPhone;
    private int count;


    public ShoppingCartItem() {
    }

    public ShoppingCartItem(int cakeId, String customerPhone, int count) {
        this.cakeId = cakeId;
        this.customerPhone = customerPhone;
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
}
