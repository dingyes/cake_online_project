package onest.dy.customer.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import onest.dy.entity.ShoppingCartItem;
import onest.dy.service.OrderService;
import onest.dy.service.ShoppingCartService;

/**
 * Servlet implementation class AddAllToOrderServlet
 */
@WebServlet("/AddAllToOrder")
public class AddAllToOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddAllToOrderServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("执行了");
		// 删除全部订单
		// 1、查询并返回购物车列表
		// 获取网络输入流与输出流
		InputStream in = request.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
		String info = reader.readLine(); // 关键词搜索蛋糕
		System.out.println(info);
		JSONObject object = new JSONObject(info);
		String phone = object.getString("phone");
		JSONArray array = object.getJSONArray("cakeCount");
		List<ShoppingCartItem> items = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			// 获取当前的JSONObject对象
			JSONObject jOrder = array.getJSONObject(i);
			int id = jOrder.getInt("id");
			int cakeId = jOrder.getInt("cakeId");
			int count = jOrder.getInt("count");
			ShoppingCartItem item = new ShoppingCartItem(id, phone, cakeId, count);
			items.add(item);
		}
		PrintWriter writer = response.getWriter();
		// 先生成订单
		try {
			boolean flag = new OrderService().addAllToOrder(items);
			// 删除购物车
			if (flag) {
				boolean flag2 = new ShoppingCartService()
						.deleteShoppingCartItemById("delete from shoppingcart where customer_phone = " + phone);
				if(flag2) {
					writer.write("success");
				} else {
					writer.write("false");
				}
			} else {
				writer.write("false");
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
