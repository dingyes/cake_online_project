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

import org.json.JSONObject;

import onest.dy.entity.Order;
import onest.dy.service.OrderService;

/**
 * Servlet implementation class AddToOrder
 */
@WebServlet("/AddToOrder")
public class AddToOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddToOrderServlet() {
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
			// 获取网络输入流与输出流
			InputStream in = request.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String orderInfo = reader.readLine(); // 购物车商品Json串
			System.out.println(orderInfo);
			Order order = convertToObj(orderInfo);
			PrintWriter writer = response.getWriter();
			boolean flag = new OrderService().addToOrder(order);
			if (flag) {
				writer.write("success");
			} else {
				writer.write("fail");
			}
			writer.flush();
			writer.close();
			reader.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Order convertToObj(String orderInfo) {
		JSONObject jObj = new JSONObject(orderInfo);
		String customerPhone = jObj.getString("customerPhone");
		int cakeId = jObj.getInt("cakeId");
		int count = jObj.getInt("cakeCount");
		int status = jObj.getInt("status");
		Order order = new Order(customerPhone, cakeId, count, status);
		return order;
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
