package onest.dy.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import onest.dy.entity.Customer;
import onest.dy.util.DBUtil;

public class CustomerService {
	private DBUtil dbUtil;

	public CustomerService() throws ClassNotFoundException, SQLException {
		dbUtil = DBUtil.getInstance();
	}

	/**
	 * �ж��Ƿ�������
	 * 
	 * @param customer ���
	 * @return �����ڣ�����true�����򣬷���false
	 */
	public boolean isExistCustomer(Customer customer) {
		// ��ȡ���ҵ�¼���뼰����
		String customerPhone = customer.getCustomerPhone();
		String customerPwd = customer.getCustomerPassword();
		String sql = "select * from customerinfo where customer_phone = '" + customerPhone
				+ "' and customer_password = '" + customerPwd + "'";
		System.out.println(sql);
		boolean b = false;
		try {
			b = dbUtil.isExist(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * �õ������Ϣ
	 * 
	 * @param phone
	 * @return
	 */
	public Customer getSpecificCustomer(String phone) {
		try {
			// ��ѯ���в���
			ResultSet rs = dbUtil.queryDate("select * from customerinfo");
			while (rs.next()) {
				String cusPhone = rs.getString("customer_phone");
				if ((cusPhone.trim()).equals(phone)) {
					String cusPwd = rs.getString("customer_password");
					String nickname = rs.getString("nickname");
					String address = rs.getString("address");
					String cusPhoto = rs.getString("customer_photo");
					if ("".equals(cusPhoto) || null == cusPhoto) {
						cusPhoto = "images\\default.jpg";
					}
					// ���ݻ�ȡ������Ϣ��������
					Customer customer = new Customer(cusPhone, cusPwd, nickname, address, cusPhoto);
					return customer;
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * �����û�
	 * 
	 * @param customer
	 * @return
	 */
	public boolean addCustomer(Customer customer) {
		// ��ȡ��ҵ�¼���롢���롢����
		String name = customer.getNickname();
		String customerPhone = customer.getCustomerPhone();
		String customerPwd = customer.getCustomerPassword();
		String sql = "insert into customerinfo(customer_phone, customer_password, nickname) values ('" + customerPhone
				+ "','" + customerPwd + "', '" + name + "')";
		System.out.println(sql);
		int b = -1;
		try {
			b = dbUtil.updateData(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return b > 0 ? true : false;
	}

}
