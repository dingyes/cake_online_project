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
			// ��ȡ���˹��ﳵ����
			List<ShoppingCartItemDetail> items = new ShoppingCartService().getSelfShoppingCartItems(customerPhone);
			// ��ȡ��ͼƬ�ڷ���˵�·��
			// ��ȡ��ǰվ��ĸ�Ŀ¼
			String path = getServletContext().getContextPath();
			// �������ݣ��޸�·����Ϣ
			for (ShoppingCartItemDetail detail : items) {
				String oPath = detail.getCakeImg();
				// ƴ����ͼƬ��վ���Ŀ¼
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
		// �������⹹��json��
		JSONObject jObj = new JSONObject();
		// ����JSONArray����
		JSONArray jArray = new JSONArray();
		for (ShoppingCartItemDetail detail : items) {
			// ÿ��ShoppingCartItemDetail�������һ��JsonObject
			JSONObject jDetail = new JSONObject();
			// ��jDetail���������
			jDetail.put("id", detail.getId());
			jDetail.put("sellerName", detail.getSellerName());
			jDetail.put("cakePrice", detail.getCakePrice());
			jDetail.put("cakeSize", detail.getCakeSize());
			jDetail.put("cakeImg", detail.getCakeImg());
			jDetail.put("cakeName", detail.getCakeName());
			jDetail.put("cakeId", detail.getCakeId());
			jDetail.put("count", detail.getCount());
			// �ѵ�ǰ��JSONObject��ӵ�JSONArray��
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
