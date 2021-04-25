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
import onest.dy.service.ShoppingCartService;

/**
 * Servlet implementation class ConvertItemToOrderServlet
 */
@WebServlet("/ConvertItemToOrder")
public class ConvertItemToOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ConvertItemToOrderServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		InputStream in = request.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
		String info = reader.readLine();
		JSONObject jObj = new JSONObject(info);
		String customerPhone = jObj.getString("customerPhone");
		int itemId = jObj.getInt("itemId");
		int cakeId = jObj.getInt("cakeId");
		int count = jObj.getInt("count");
		try {
			// 购物车删除
			boolean item = new ShoppingCartService().deleteShoppingCartItemById("delete from shoppingcart where id = " + itemId);
			if (item) {
				// 订单下单
				Order order = new Order(customerPhone, cakeId, count, 0);
				System.out.println(order.toString());
				boolean flag = new OrderService().addToOrder(order);
				if (flag) {
					// 结算成功
					out.write("success");
					out.flush();
				} else {
					// 返回数据
					out.write("false");
					out.flush();
				}
			} else {
				// 返回数据
				out.write("false");
				out.flush();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.close();
		reader.close();
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
