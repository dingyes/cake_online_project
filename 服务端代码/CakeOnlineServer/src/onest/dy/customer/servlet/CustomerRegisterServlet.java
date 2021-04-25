package onest.dy.customer.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
 * Servlet implementation class CustomerRegisterServlet
 */
@WebServlet("/CustomerRegister")
public class CustomerRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CustomerRegisterServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String tip = null;
		try {
			// ͨ�����ķ�ʽ���տͻ��˷�����json��
			// ��ȡ�����������������
			InputStream in = request.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			// ��������
			String sJson = reader.readLine();
			System.out.println(sJson);
			Customer customer = convertToObj(sJson);
			CustomerService service = new CustomerService();
			boolean flag1 = service.isExistCustomer(customer);
			if(flag1) { // ���Ѵ����û�
				tip = "false&&&�õ绰�����ѱ�ע��";
			} else {
				boolean flag = new CustomerService().addCustomer(customer);
				if(flag) {
					tip = "success&&&ע��ɹ�";
				} else {
					tip = "false&&&ע��ʧ��";
				}
			}
			PrintWriter out = response.getWriter();
			out.write(tip);
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
			String phone = jObj.getString("phone");
			String password = jObj.getString("password");
			String name = jObj.getString("name");
			customer.setNickname(name);
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
