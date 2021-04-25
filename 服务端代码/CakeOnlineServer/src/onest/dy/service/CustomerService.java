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
	 * 判断是否存在买家
	 * 
	 * @param customer 买家
	 * @return 若存在，返回true；否则，返回false
	 */
	public boolean isExistCustomer(Customer customer) {
		// 获取卖家登录号码及密码
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
	 * 得到买家信息
	 * 
	 * @param phone
	 * @return
	 */
	public Customer getSpecificCustomer(String phone) {
		try {
			// 查询所有菜谱
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
					// 根据获取到的信息构建对象
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
	 * 创建用户
	 * 
	 * @param customer
	 * @return
	 */
	public boolean addCustomer(Customer customer) {
		// 获取买家登录号码、密码、名字
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
