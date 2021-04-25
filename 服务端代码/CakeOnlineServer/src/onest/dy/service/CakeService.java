package onest.dy.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import onest.dy.entity.Cake;
import onest.dy.util.DBUtil;

public class CakeService {
	private DBUtil dbUtil;
	private List<Cake> cakes;

	public CakeService() throws ClassNotFoundException, SQLException {
		cakes = new ArrayList<>();
		dbUtil = DBUtil.getInstance();
	}

	/**
	 * ��ȡ���е�����Ϣ
	 * 
	 * @param sql ��ѯ�����sql���
	 * @return ���⼯��
	 */
	public List<Cake> getCakes(String sql) {
		try {
			// ��ѯ���в���
			ResultSet rs = dbUtil.queryDate(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String sellerPhone = rs.getString("seller_phone");
				String cakeName = rs.getString("cake_name");
				int cakePrice = rs.getInt("cake_price");
				String description = rs.getString("description");
				int cakeSize = rs.getInt("cake_size");
				String cakeImg = rs.getString("cake_image");
				if ("".equals(cakeImg) || null == cakeImg) {
					cakeImg = "images/default.jpg";
				}
				// ���ݻ�ȡ���ĵ�����Ϣ���쵰�����
				Cake cake = new Cake(id, sellerPhone, cakeName, cakePrice, description, cakeSize, cakeImg);
				cakes.add(cake);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return cakes;
	}

	/**
	 * ���ݹؼ��������ѯ���⼯��
	 * 
	 * @param keyword �û�����ؼ���
	 * @param size    �ߴ�
	 * @param price   ��߼۸�
	 * @return
	 */
	public List<Cake> findKeywordCakes(String keyword, int size, int price) {
		List<Cake> keyCakes = new ArrayList<>();
		try {
			if (size != 0) {
				if (price != 0) {
					// �û���ѡ�ߴ���۸�
					ResultSet rs = dbUtil.queryDate(
							"select * from cakeinfo where cake_size = " + size + " and cake_price <= " + price + "");
					while (rs.next()) {
						String cakeName = rs.getString("cake_name");
						if (cakeName.contains(keyword)) {
							int id = rs.getInt("id");
							String sellerPhone = rs.getString("seller_phone");
							int cakePrice = rs.getInt("cake_price");
							String description = rs.getString("description");
							int cakeSize = rs.getInt("cake_size");
							String cakeImg = rs.getString("cake_image");
							if ("".equals(cakeImg) || null == cakeImg) {
								cakeImg = "images/default.jpg";
							}
							Cake cake = new Cake(id, sellerPhone, cakeName, cakePrice, description, cakeSize, cakeImg);
							keyCakes.add(cake);
						}
					}
				} else {
					// �û���ѡ�ߴ磬δѡ�۸�
					ResultSet rs = dbUtil.queryDate("select * from cakeinfo where cake_size = " + size + "");
					while (rs.next()) {
						String cakeName = rs.getString("cake_name");
						if (cakeName.contains(keyword)) {
							int id = rs.getInt("id");
							String sellerPhone = rs.getString("seller_phone");
							int cakePrice = rs.getInt("cake_price");
							String description = rs.getString("description");
							int cakeSize = rs.getInt("cake_size");
							String cakeImg = rs.getString("cake_image");
							if ("".equals(cakeImg) || null == cakeImg) {
								cakeImg = "images/default.jpg";
							}
							Cake cake = new Cake(id, sellerPhone, cakeName, cakePrice, description, cakeSize, cakeImg);
							keyCakes.add(cake);
						}
					}
				}
			} else {
				if (price != 0) {
					// �û�δѡ�ߴ磬��ѡ���
					ResultSet rs = dbUtil.queryDate("select * from cakeinfo where cake_price <= " + price + "");
					while (rs.next()) {
						String cakeName = rs.getString("cake_name");
						if (cakeName.contains(keyword)) {
							int id = rs.getInt("id");
							String sellerPhone = rs.getString("seller_phone");
							int cakePrice = rs.getInt("cake_price");
							String description = rs.getString("description");
							int cakeSize = rs.getInt("cake_size");
							String cakeImg = rs.getString("cake_image");
							if ("".equals(cakeImg) || null == cakeImg) {
								cakeImg = "images/default.jpg";
							}
							Cake cake = new Cake(id, sellerPhone, cakeName, cakePrice, description, cakeSize, cakeImg);
							keyCakes.add(cake);
						}
					}
				} else {
					// �û�δѡ���
					ResultSet rs = dbUtil.queryDate("select * from cakeinfo");
					while (rs.next()) {
						String cakeName = rs.getString("cake_name");
						if (cakeName.contains(keyword)) {
							int id = rs.getInt("id");
							String sellerPhone = rs.getString("seller_phone");
							int cakePrice = rs.getInt("cake_price");
							String description = rs.getString("description");
							int cakeSize = rs.getInt("cake_size");
							String cakeImg = rs.getString("cake_image");
							if ("".equals(cakeImg) || null == cakeImg) {
								cakeImg = "images/default.jpg";
							}
							Cake cake = new Cake(id, sellerPhone, cakeName, cakePrice, description, cakeSize, cakeImg);
							keyCakes.add(cake);
						}
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return keyCakes;
	}

	/**
	 * ���ݹ���ѯ���⼯��
	 * 
	 * @param size  �ߴ�
	 * @param price ��߼۸�
	 * @return
	 */
	public List<Cake> findCakesBySpecifications(int size, int price) {
		List<Cake> keyCakes = new ArrayList<>();
		try {
			if (size != 0) {
				if (price != 0) {
					// �û���ѡ�ߴ���۸�
					ResultSet rs = dbUtil.queryDate(
							"select * from cakeinfo where cake_size = " + size + " and cake_price <= " + price + "");
					while (rs.next()) {
						String cakeName = rs.getString("cake_name");
						int id = rs.getInt("id");
						String sellerPhone = rs.getString("seller_phone");
						int cakePrice = rs.getInt("cake_price");
						String description = rs.getString("description");
						int cakeSize = rs.getInt("cake_size");
						String cakeImg = rs.getString("cake_image");
						if ("".equals(cakeImg) || null == cakeImg) {
							cakeImg = "images/default.jpg";
						}
						Cake cake = new Cake(id, sellerPhone, cakeName, cakePrice, description, cakeSize, cakeImg);
						keyCakes.add(cake);
					}
				} else {
					// �û���ѡ�ߴ磬δѡ�۸�
					ResultSet rs = dbUtil.queryDate("select * from cakeinfo where cake_size = " + size + "");
					while (rs.next()) {
						String cakeName = rs.getString("cake_name");
						int id = rs.getInt("id");
						String sellerPhone = rs.getString("seller_phone");
						int cakePrice = rs.getInt("cake_price");
						String description = rs.getString("description");
						int cakeSize = rs.getInt("cake_size");
						String cakeImg = rs.getString("cake_image");
						if ("".equals(cakeImg) || null == cakeImg) {
							cakeImg = "images/default.jpg";
						}
						Cake cake = new Cake(id, sellerPhone, cakeName, cakePrice, description, cakeSize, cakeImg);
						keyCakes.add(cake);
					}
				}
			} else {
				if (price != 0) {
					// �û�δѡ�ߴ磬��ѡ���
					ResultSet rs = dbUtil.queryDate("select * from cakeinfo where cake_price <= " + price + "");
					while (rs.next()) {
						String cakeName = rs.getString("cake_name");
						int id = rs.getInt("id");
						String sellerPhone = rs.getString("seller_phone");
						int cakePrice = rs.getInt("cake_price");
						String description = rs.getString("description");
						int cakeSize = rs.getInt("cake_size");
						String cakeImg = rs.getString("cake_image");
						if ("".equals(cakeImg) || null == cakeImg) {
							cakeImg = "images/default.jpg";
						}
						Cake cake = new Cake(id, sellerPhone, cakeName, cakePrice, description, cakeSize, cakeImg);
						keyCakes.add(cake);
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return keyCakes;
	}

	/**
	 * ͨ��id�õ�������Ϣ
	 * 
	 * @param id
	 * @return
	 */
	public Cake getCakeById(int id) {
		Cake cake = null;
		try {
			ResultSet rs = dbUtil.queryDate("select * from cakeinfo where id = " + id);
			while (rs.next()) {
				String cakeName = rs.getString("cake_name");
				String sellerPhone = rs.getString("seller_phone");
				int cakePrice = rs.getInt("cake_price");
				String description = rs.getString("description");
				int cakeSize = rs.getInt("cake_size");
				String cakeImg = rs.getString("cake_image");
				if ("".equals(cakeImg) || null == cakeImg) {
					cakeImg = "images/default.jpg";
				}
				cake = new Cake(id, sellerPhone, cakeName, cakePrice, description, cakeSize, cakeImg);
				break;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cake;
	}

	/**
	 * ���µ�����Ϣ
	 * 
	 * @param id
	 * @param cakeName
	 * @param cakePrice
	 * @param cakeSize
	 * @param cakeDescription
	 * @return
	 */
	public boolean updateCakeDatas(String sql) {
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
	 * ɾ������
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteCakeById(int id) {
		String sql = "delete from cakeinfo where id = " + id + "";
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

	public boolean updateCakeImageByCakeId(int cakeId, String imgPath) {
		String sql = "update cakeinfo set cake_image = '" + imgPath + "' where id = " + cakeId + "";
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
	 * ���ͼƬ
	 * 
	 * @param cake
	 * @return
	 */
	public boolean addCake(String sql) {
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

}
