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
	 * �ж��Ƿ��������
	 * 
	 * @param seller ����
	 * @return �����ڣ�����true�����򣬷���false
	 */
	public boolean isExistSeller(Seller seller) {
		// ��ȡ���ҵ�¼���뼰����
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
