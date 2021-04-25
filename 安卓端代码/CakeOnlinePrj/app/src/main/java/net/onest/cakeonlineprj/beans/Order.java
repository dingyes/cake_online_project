package net.onest.cakeonlineprj.beans;

public class Order {
    private int id;
    private String customerPhone;
    private int cakeId;
    private int count;
    private int status;

    public Order() {
    }

    public Order(String customerPhone, int cakeId, int count, int status) {
        this.customerPhone = customerPhone;
        this.cakeId = cakeId;
        this.count = count;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerPhone=" + customerPhone +
                ", cakeId=" + cakeId +
                ", count=" + count +
                ", status=" + status +
                '}';
    }
}
