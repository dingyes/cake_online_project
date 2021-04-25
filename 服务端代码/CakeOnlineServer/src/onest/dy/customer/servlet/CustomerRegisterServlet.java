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
			// 通过流的方式接收客户端发来的json串
			// 获取网络输入流与输出流
			InputStream in = request.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			// 接收数据
			String sJson = reader.readLine();
			System.out.println(sJson);
			Customer customer = convertToObj(sJson);
			CustomerService service = new CustomerService();
			boolean flag1 = service.isExistCustomer(customer);
			if(flag1) { // 若已存在用户
				tip = "false&&&该电话号码已被注册";
			} else {
				boolean flag = new CustomerService().addCustomer(customer);
				if(flag) {
					tip = "success&&&注册成功";
				} else {
					tip = "false&&&注册失败";
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
	 * 将给定的Json串转换为对象
	 *
	 * @param jStr
	 * @return
	 */
	private Customer convertToObj(String jStr) {
		Customer customer = null;
		try {
			// 根据指定的json串创建JSONObject对象
			JSONObject jObj = new JSONObject(jStr);
			// 构造出对象
			customer = new Customer();
			// 获取JSONObject中元素
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
