package onest.dy.customer.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
 * ���տͻ��������Ϣ��������֤
 */
@WebServlet("/CustomerLoginVerification")
public class CustomerLoginVerificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CustomerLoginVerificationServlet() {
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
			// ͨ�����ķ�ʽ���տͻ��˷�����json��
			// ��ȡ�����������������
			InputStream in = request.getInputStream();
			OutputStream out = response.getOutputStream();
			// ��������
			byte[] data = new byte[256];
			in.read(data);
			String sJson = new String(data);
			System.out.println(sJson);
			Customer customer = convertToObj(sJson);
			boolean flag = new CustomerService().isExistCustomer(customer);
			if (flag) {
				out.write("success".getBytes());
			} else {
				out.write("fail".getBytes());
			}
			out.flush();
			in.close();
			out.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��������Json��ת��Ϊ����
	 *
	 * @param jStr
	 * @return
	 */
	private Customer convertToObj(String jStr) {
		Customer customer = null;
		try {
			// ����ָ����json������JSONObject����
			JSONObject jObj = new JSONObject(jStr);
			// ���������
			customer = new Customer();
			// ��ȡJSONObject��Ԫ��
			String phone = jObj.getString("customerPhone");
			String password = jObj.getString("customerPwd");
			customer.setCustomerPhone(phone);
			customer.setCustomerPassword(password);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return customer;
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
