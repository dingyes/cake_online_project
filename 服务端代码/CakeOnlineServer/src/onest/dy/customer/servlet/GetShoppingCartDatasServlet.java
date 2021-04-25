package onest.dy.customer.servlet;

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

import onest.dy.entity.ShoppingCartItemDetail;
import onest.dy.service.ShoppingCartService;

/**
 * Servlet implementation class GetShoppingCartDatasServlet
 */
@WebServlet("/GetSelfShoppingCartItems")
public class GetShoppingCartDatasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetShoppingCartDatasServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String customerPhone = request.getParameter("phone");
		PrintWriter writer = response.getWriter();
		try {
			// 获取个人购物车详情
			List<ShoppingCartItemDetail> items = new ShoppingCartService().getSelfShoppingCartItems(customerPhone);
			// 获取到图片在服务端的路径
			// 获取当前站点的根目录
			String path = getServletContext().getContextPath();
			// 遍历数据，修改路径信息
			for (ShoppingCartItemDetail detail : items) {
				String oPath = detail.getCakeImg();
				// 拼接上图片的站点根目录
				detail.setCakeImg(path + "/" + oPath);
			}
			String result = convertToJson(items);
			writer.write(result);
			writer.flush();
			writer.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String convertToJson(List<ShoppingCartItemDetail> items) {
		String json = null;
		// 从内向外构造json串
		JSONObject jObj = new JSONObject();
		// 创建JSONArray对象
		JSONArray jArray = new JSONArray();
		for (ShoppingCartItemDetail detail : items) {
			// 每个ShoppingCartItemDetail对象构造成一个JsonObject
			JSONObject jDetail = new JSONObject();
			// 向jDetail中添加数据
			jDetail.put("id", detail.getId());
			jDetail.put("sellerName", detail.getSellerName());
			jDetail.put("cakePrice", detail.getCakePrice());
			jDetail.put("cakeSize", detail.getCakeSize());
			jDetail.put("cakeImg", detail.getCakeImg());
			jDetail.put("cakeName", detail.getCakeName());
			jDetail.put("cakeId", detail.getCakeId());
			jDetail.put("count", detail.getCount());
			// 把当前的JSONObject添加到JSONArray中
			jArray.put(jDetail);
		}
		jObj.put("self", jArray);
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
