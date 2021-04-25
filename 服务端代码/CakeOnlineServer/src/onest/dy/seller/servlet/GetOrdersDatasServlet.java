package onest.dy.seller.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import onest.dy.entity.OrderDetail;
import onest.dy.service.OrderService;

/**
 * Servlet implementation class GetOrdersDatasServlet
 */
@WebServlet("/GetOrdersDatas")
public class GetOrdersDatasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetOrdersDatasServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获得买家电话号码
		String phone = request.getParameter("phone");
		PrintWriter writer = response.getWriter();
		try {
			// 获得数据
			List<OrderDetail> orders = new OrderService().getSellerOrders(phone);
			String json = convertToJson(orders);
			writer.write(json);
			writer.flush();
			writer.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String convertToJson(List<OrderDetail> orders) {
		String json = null;
		// 从内向外构造json串
		JSONObject jObj = new JSONObject();
		// 创建JSONArray对象
		JSONArray jArray = new JSONArray();
		for (OrderDetail detail : orders) {
			// 每个ShoppingCartItemDetail对象构造成一个JsonObject
			JSONObject jDetail = new JSONObject();
			// 向jDetail中添加数据
			jDetail.put("id", detail.getId());
			jDetail.put("cakeName", detail.getCakeName());
			jDetail.put("count", detail.getCount());
			jDetail.put("status", detail.getStatus());
			jDetail.put("customerPhone", detail.getCustomerPhone());
			// 把当前的JSONObject添加到JSONArray中
			jArray.put(jDetail);
		}
		jObj.put("orders", jArray);
		json = jObj.toString();
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
