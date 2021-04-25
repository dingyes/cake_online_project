package onest.dy.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import onest.dy.entity.Order;
import onest.dy.entity.OrderDetail;
import onest.dy.entity.ShoppingCartItem;
import onest.dy.util.DBUtil;

public class OrderService {
	private DBUtil dbUtil;
	private final int TOBESENTOUT = 0; // ������
	private final int DELIVERED = 1; // �ѷ���
	private final int HANDLED = 2; // �����

	public OrderService() throws ClassNotFoundException, SQLException {
		dbUtil = DBUtil.getInstance();
	}

	/**
	 * ���붩��
	 * 
	 * @param order
	 * @return
	 */
	public boolean addToOrder(Order order) {
		String customerPhone = order.getCustomerPhone();
		int cakeId = order.getCakeId();
		int count = order.getCount();
		int status = order.getStatus();
		String sql = "insert into orderinfo(cake_id, customer_phone, count, status) values(" + cakeId + ",'"
				+ customerPhone + "'," + count + "," + status + ")";
		System.out.println(sql);
		int n = -1;
		try {
			n = dbUtil.updateData(sql);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n > 0 ? true : false;
	}

	/**
	 * �õ�ĳһ�û�����ʷ����
	 * 
	 * @param phone
	 * @return
	 */
	public List<OrderDetail> getHistoryOrders(String phone) {
		List<OrderDetail> orders = new ArrayList<>();
		try {
			ResultSet rs = dbUtil.queryDate(
					"select orderinfo.id, cake_name, count, status from cakeinfo, orderinfo where orderinfo.cake_id = cakeinfo.id and customer_phone ='"
							+ phone + "' and flag = 0");
			while (rs.next()) {
				int id = rs.getInt("orderinfo.id");
				String cakeName = rs.getString("cake_name");
				int count = rs.getInt("count");
				int status = rs.getInt("status");
				OrderDetail de = new OrderDetail(id, count, status, cakeName);
				orders.add(de);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	/**
	 * �õ�ĳһ���ҵĶ�����Ϣ
	 * 
	 * @param phone
	 * @return
	 */
	public List<OrderDetail> getSellerOrders(String phone) {
		List<OrderDetail> orders = new ArrayList<>();
		try {
			ResultSet rs = dbUtil.queryDate(
					"select orderinfo.id, customer_phone, count, status, cake_name from orderinfo, cakeinfo where orderinfo.cake_id = cakeinfo.id and seller_phone = '"
							+ phone + "'");
			System.out.println(
					"select orderinfo.id, customer_phone, count, status, cake_name from orderinfo, cakeinfo where orderinfo.cake_id = cakeinfo.id and seller_phone = '"
							+ phone + "'");
			while (rs.next()) {
				int id = rs.getInt("orderinfo.id");
				String cakeName = rs.getString("cake_name");
				int count = rs.getInt("count");
				int status = rs.getInt("status");
				String customerPhone = rs.getString("customer_phone");
				OrderDetail de = new OrderDetail(id, customerPhone, count, status, cakeName);
				orders.add(de);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	/**
	 * �޸Ķ���״̬
	 * 
	 * @param orderId
	 * @param status
	 * @return
	 */
	public boolean updateOrderStatus(int orderId, int status) {
		if (status == TOBESENTOUT) { // ������
			status = DELIVERED;
		} else if (status == DELIVERED) { // �ѷ���
			status = HANDLED;
		}
		String sql = "update orderinfo set status = " + status + " where id = " + orderId + "";
		int n = -1;
		try {
			n = dbUtil.updateData(sql);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n > 0 ? true : false;
	}

	public boolean deleteCustomerOrder(int id) {
		String sql = "update orderinfo set flag = 1 where id = " + id;
		int n = -1;
		try {
			n = dbUtil.updateData(sql);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n > 0 ? true : false;
	}

	public boolean addAllToOrder(List<ShoppingCartItem> items) {
		String sql = null;
		boolean flag = true;
		int n;
		for (ShoppingCartItem item : items) {
			n = -1;
			sql = "insert into orderinfo(cake_id, customer_phone, count, status) values(" + item.getCakeId() + ", '"
					+ item.getCustomerPhone() + "', " + item.getCount() + ", 0)";
			System.out.println(sql);
			try {
				n = dbUtil.updateData(sql);
				if (n == -1) {
					flag = false;
					break;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

}
