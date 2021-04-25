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
 * 根据传输信息，查找对应的买家信息
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
			// 通过流的方式接收客户端发来的用户电话
			// 获取网络输入流与输出流
			InputStream in = request.getInputStream();
			PrintWriter out = response.getWriter();
			// 接收数据
			byte[] phone = new byte[256];
			in.read(phone);
			String cusPhone = new String(phone).trim();
			System.out.println(cusPhone);
			// 查询对应用户的信息
			Customer customer = new CustomerService().getSpecificCustomer(cusPhone);
			if (null != customer) {
				// 将用户信息转换成Json串
				String customerJson = convertToJson(customer);
				// 传输Json串
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
	 * 把指定的对象转换为Json对象
	 *
	 * @param Customer
	 * @return
	 */
	private String convertToJson(Customer customer) {
		String json = null;
		try {
			// 创建JSONObject对象
			JSONObject jObj = new JSONObject();
			// 向JSONObject中添加数据
			jObj.put("customerPhone", customer.getCustomerPhone());
			jObj.put("nickname", customer.getNickname());
			jObj.put("address", customer.getAddress());
			// 由JSONObject生成Json字符串
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
