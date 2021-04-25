package onest.dy.customer.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import onest.dy.entity.Customer;
import onest.dy.service.CustomerService;

/**
 * ���ݴ�����Ϣ�����Ҷ�Ӧ�������Ϣ
 */
@WebServlet("/CustomerMineData")
public class CustomerMineDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CustomerMineDataServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// ͨ�����ķ�ʽ���տͻ��˷������û��绰
			// ��ȡ�����������������
			InputStream in = request.getInputStream();
			PrintWriter out = response.getWriter();
			// ��������
			byte[] phone = new byte[256];
			in.read(phone);
			String cusPhone = new String(phone).trim();
			System.out.println(cusPhone);
			// ��ѯ��Ӧ�û�����Ϣ
			Customer customer = new CustomerService().getSpecificCustomer(cusPhone);
			if (null != customer) {
				// ���û���Ϣת����Json��
				String customerJson = convertToJson(customer);
				// ����Json��
				out.write(customerJson);
				System.out.println(customerJson);
				out.flush();
				in.close();
				out.close();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ָ���Ķ���ת��ΪJson����
	 *
	 * @param Customer
	 * @return
	 */
	private String convertToJson(Customer customer) {
		String json = null;
		try {
			// ����JSONObject����
			JSONObject jObj = new JSONObject();
			// ��JSONObject���������
			jObj.put("customerPhone", customer.getCustomerPhone());
			jObj.put("nickname", customer.getNickname());
			jObj.put("address", customer.getAddress());
			// ��JSONObject����Json�ַ���
			json = jObj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
