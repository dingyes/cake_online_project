package net.onest.cakeonlineprj.beans;

public class Seller {
    private static String currentSeller = null;
    private String sellerPhone;
    private String sellerPassword;
    private String sellerName;

    public static String getCurrentSeller() {
        return currentSeller;
    }

    public static void setCurrentSeller(String currentSeller) {
        Seller.currentSeller = currentSeller;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerPassword() {
        return sellerPassword;
    }

    public void setSellerPassword(String sellerPassword) {
        this.sellerPassword = sellerPassword;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    @Override
    public String toString() {
        return "Seller{" +
                ", sellerPhone='" + sellerPhone + '\'' +
                ", sellerPassword='" + sellerPassword + '\'' +
                ", sellerName='" + sellerName + '\'' +
                '}';
    }
}
