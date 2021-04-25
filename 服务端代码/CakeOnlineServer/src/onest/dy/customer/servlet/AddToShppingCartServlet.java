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

import onest.dy.entity.ShoppingCartItem;
import onest.dy.service.ShoppingCartService;

/**
 * Servlet implementation class addToShppingCartServlet
 */
@WebServlet("/AddToShppingCart")
public class AddToShppingCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddToShppingCartServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		try {
			// 获取网络输入流与输出流
			InputStream in = request.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String itemInfo = reader.readLine(); // 购物车商品Json串
			ShoppingCartItem item = convertToObj(itemInfo);
			boolean flag = new ShoppingCartService().addShoppingCartItem(item);
			if (flag) {
				writer.write("success");
			} else {
				writer.write("fail");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private ShoppingCartItem convertToObj(String itemInfo) {
		// 根据指定的json串创建JSONObject对象
		JSONObject jObj = new JSONObject(itemInfo);
		String customerPhone = jObj.getString("customerPhone");
		int cakeId = jObj.getInt("cakeId");
		int count = jObj.getInt("cakeCount");
		ShoppingCartItem item = new ShoppingCartItem(customerPhone, cakeId, count);
		return item;
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
