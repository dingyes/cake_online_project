package onest.dy.service;

import java.sql.SQLException;

import onest.dy.entity.Seller;
import onest.dy.util.DBUtil;

public class SellerService {
	private DBUtil dbUtil;

	public SellerService() throws ClassNotFoundException, SQLException {
		dbUtil = DBUtil.getInstance();
	}

	/**
	 * 判断是否存在卖家
	 * 
	 * @param seller 卖家
	 * @return 若存在，返回true；否则，返回false
	 */
	public boolean isExistSeller(Seller seller) {
		// 获取卖家登录号码及密码
		String sellerPhone = seller.getSellerPhone();
		String sellerPwd = seller.getSellerPassword();
		String sql = "select * from sellerinfo where seller_phone = '" + sellerPhone + "' and seller_password = '"
				+ sellerPwd + "'";
		System.out.println(sql);
		boolean b = false;
		try {
			b = dbUtil.isExist(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return b;
	}
}
