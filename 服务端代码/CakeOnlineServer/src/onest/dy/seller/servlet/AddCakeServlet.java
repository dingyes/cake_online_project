package onest.dy.seller.servlet;

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

import onest.dy.service.CakeService;

/**
 * Servlet implementation class AddCakeServlet
 */
@WebServlet("/AddCake")
public class AddCakeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddCakeServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取数据
		InputStream in = request.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
		String info = reader.readLine();
		System.out.println(info);
		JSONObject object = new JSONObject(info);
		String sellerPhone = object.getString("sellerPhone");
		String cakeName = object.getString("cakeName");
		int cakePrice = object.getInt("cakePrice");
		int cakeSize = object.getInt("cakeSize");
		String cakeDescription = object.getString("cakeDescription");
		String cakeImg = (String) this.getServletContext().getAttribute("img");
		PrintWriter writer = response.getWriter();
		boolean flag;
		try {
			if (null != cakeImg) {
				flag = new CakeService().addCake(
						"insert into cakeinfo(seller_phone, cake_name, cake_price, description, cake_size, cake_image) values('"
								+ sellerPhone + "','" + cakeName + "'," + cakePrice + ",'" + cakeDescription + "',"
								+ cakeSize + ",'" + cakeImg + "')");
			} else {
				flag = new CakeService().addCake(
						"insert into cakeinfo(seller_phone, cake_name, cake_price, description, cake_size) values('"
								+ sellerPhone + "','" + cakeName + "'," + cakePrice + ",'" + cakeDescription + "',"
								+ cakeSize + ")");
			}
			if (flag) {
				writer.write("success");
			} else {
				writer.write("false");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		writer.flush();
		writer.close();
		reader.close();
		this.getServletContext().removeAttribute("img");
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
