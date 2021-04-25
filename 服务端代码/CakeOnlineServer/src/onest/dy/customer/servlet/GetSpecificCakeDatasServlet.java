package onest.dy.customer.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import onest.dy.entity.Cake;
import onest.dy.service.CakeService;

/**
 * Servlet implementation class GetSpecificCakeDatas
 */
@WebServlet("/GetSpecificCakeDatas")
public class GetSpecificCakeDatasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetSpecificCakeDatasServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		try {
			Cake cake = new CakeService().getCakeById(id);
			String path = getServletContext().getContextPath();
			String oPath = cake.getCakeImg();
			// 拼接上图片的站点根目录
			cake.setCakeImg(path + "/" + oPath);
			String cakeInfo = convertToJson(cake);
			PrintWriter out = response.getWriter();
			out.write(cakeInfo);
			out.flush();
			out.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String convertToJson(Cake cake) {
		JSONObject jCake = new JSONObject();
		jCake.put("id", cake.getId());
		jCake.put("cakeName", cake.getCakeName());
		jCake.put("sellerPhone", cake.getSellerPhone());
		jCake.put("description", cake.getDescription());
		jCake.put("cakeImg", cake.getCakeImg());
		jCake.put("cakeSize", cake.getSize());
		jCake.put("cakePrice", cake.getPrice());
		return jCake.toString();
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
