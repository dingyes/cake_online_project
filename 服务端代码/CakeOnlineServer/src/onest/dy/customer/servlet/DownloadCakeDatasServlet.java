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

import onest.dy.entity.Cake;
import onest.dy.service.CakeService;

/**
 * Servlet implementation class DownloadCakeImagesServlet
 */
@WebServlet("/DownloadCakeDatas")
public class DownloadCakeDatasServlet extends HttpServlet {
	private List<Cake> cakes;
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownloadCakeDatasServlet() {
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
			// 创建用于保存用户数据的集合
			cakes = new CakeService().getCakes("select * from cakeinfo");
			// 获取到图片在服务端的路径
			// 获取当前站点的根目录
			String path = getServletContext().getContextPath();
			// 遍历数据，修改路径信息
			for (Cake cake : cakes) {
				String oPath = cake.getCakeImg();
				// 拼接上图片的站点根目录
				cake.setCakeImg(path + "/" + oPath);
			}
			String json = convertToJson(cakes);
			// 把Json串返回给客户端
			PrintWriter out = response.getWriter();
			out.write(json);
			out.flush();
			out.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将给定的对象转换成json串
	 * 
	 * @param cakes 蛋糕集合
	 * @return json串
	 */
	private String convertToJson(List<Cake> cakes) {
		String json = null;
		// 从内向外构造json串
		JSONObject jObj = new JSONObject();
		// 创建JSONArray对象
		JSONArray jArray = new JSONArray();
		for (Cake cake : cakes) {
			// 每个Cake对象构造成一个JsonObject
			JSONObject jCake = new JSONObject();
			// 向jCake中添加数据
			jCake.put("id", cake.getId());
			jCake.put("cakeName", cake.getCakeName());
			jCake.put("sellerPhone", cake.getSellerPhone());
			jCake.put("description", cake.getDescription());
			jCake.put("cakeImg", cake.getCakeImg());
			jCake.put("cakeSize", cake.getSize());
			jCake.put("cakePrice", cake.getPrice());
			// 把当前的JSONObject添加到JSONArray中
			jArray.put(jCake);
		}
		jObj.put("cakes", jArray);
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
