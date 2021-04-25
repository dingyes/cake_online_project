package onest.dy.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import onest.dy.entity.ShoppingCartItem;
import onest.dy.entity.ShoppingCartItemDetail;
import onest.dy.util.DBUtil;

public class ShoppingCartService {
	private DBUtil dbUtil;

	public ShoppingCartService() throws ClassNotFoundException, SQLException {
		dbUtil = DBUtil.getInstance();
	}

	/**
	 * ��ӵ����ﳵ
	 * 
	 * @param item
	 * @return
	 */
	public boolean addShoppingCartItem(ShoppingCartItem item) {
		boolean isExist = false;
		int id = -1;
		int num = -1;
		String sql = null;
		int n = -1;
		// ��ѯ���в���
		try {
			ResultSet rs = dbUtil.queryDate("select * from shoppingcart");
			while (rs.next()) {
				String customerPhone = rs.getString("customer_phone");
				int cakeId = rs.getInt("cake_id");
				// �ж��Ƿ��Ѵ��ڴ˵���
				if (customerPhone.equals(item.getCustomerPhone()) && (cakeId == item.getCakeId())) {
					isExist = true;
					id = rs.getInt("id");
					int count = rs.getInt("count");
					num = count + item.getCount();
					break;
				}
			}
			if (isExist) {
				sql = "update shoppingcart set count = " + num + " where id = " + id + "";
				System.out.println(sql);
			} else {
				sql = "insert into shoppingcart(customer_phone, cake_id, count) values('" + item.getCustomerPhone()
						+ "', " + item.getCakeId() + ", " + item.getCount() + ")";
				System.out.println(sql);
			}
			n = dbUtil.updateData(sql);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n > 0 ? true : false;
	}

	/**
	 * �õ�ĳһ�û��Ĺ��ﳵ����
	 * 
	 * @param customerPhone
	 * @return
	 */
	public List<ShoppingCartItemDetail> getSelfShoppingCartItems(String customerPhone) {
		List<ShoppingCartItemDetail> items = new ArrayList<>();
		try {
			ResultSet rs = dbUtil.queryDate(
					"select shoppingcart.id, shoppingcart.cake_id, cake_image, cake_size, cake_price, cake_name, seller_name, shoppingcart.customer_phone, shoppingcart.count from shoppingcart, cakeinfo, sellerinfo where sellerinfo.seller_phone = cakeinfo.seller_phone and shoppingcart.cake_id = cakeinfo.id and shoppingcart.customer_phone = '"
							+ customerPhone + "'");
			System.out.println(
					"select shoppingcart.id, shoppingcart.cake_id, cake_image, cake_size, cake_price, cake_name, seller_name,shoppingcart.customer_phone, shoppingcart.count from shoppingcart, cakeinfo, sellerinfo where sellerinfo.seller_phone = cakeinfo.seller_phone and shoppingcart.cake_id = cakeinfo.id and shoppingcart.customer_phone = '"
							+ customerPhone + "'");
			while (rs.next()) {
				int id = rs.getInt("shoppingcart.id");
				int cakeId = rs.getInt("shoppingcart.cake_id");
				String cakeImg = rs.getString("cake_image");
				int cakeSize = rs.getInt("cake_size");
				int cakePrice = rs.getInt("cake_price");
				String cakeName = rs.getString("cake_name");
				String sellerName = rs.getString("seller_name");
				int count = rs.getInt("shoppingcart.count");
				if ("".equals(cakeImg)) {
					cakeImg = "images/default.jpg";
				}
				ShoppingCartItemDetail detail = new ShoppingCartItemDetail(id, cakeId, cakeImg, cakeName, sellerName,
						cakeSize, cakePrice, count);
				items.add(detail);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	/**
	 * ɾ�����ﳵ���ض�����
	 * 
	 * @param itemId
	 * @return
	 */
	public boolean deleteShoppingCartItemById(String sql) {
		System.out.println(sql);
		int b = -1;
		try {
			b = dbUtil.updateData(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return b > 0 ? true : false;
	}

	/**
	 * �õ��û��Ĺ��ﳵ�б�
	 * 
	 * @param phone
	 * @return
	 */
	public List<ShoppingCartItem> getShoppingCartItems(String phone) {
		List<ShoppingCartItem> items = new ArrayList<>();
		try {
			ResultSet rs = dbUtil.queryDate("select * from shoppingcart");
			while (rs.next()) {
				String cusPhone = rs.getString("customer_phone");
				if (cusPhone.equals(phone)) {
					int id = rs.getInt("id");
					int cakeId = rs.getInt("cake_id");
					int count = rs.getInt("count");
					ShoppingCartItem item = new ShoppingCartItem(id, phone, cakeId, count);
					items.add(item);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

}
