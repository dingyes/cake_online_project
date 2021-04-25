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
 * Servlet implementation class UpdateCakeDatasServlet
 */
@WebServlet("/UpdateCakeDatas")
public class UpdateCakeDatasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateCakeDatasServlet() {
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
		String data = reader.readLine();
		JSONObject jObj = new JSONObject(data);
		int id = jObj.getInt("cakeId");
		String cakeName = jObj.getString("cakeName");
		int cakePrice = jObj.getInt("cakePrice");
		int cakeSize = jObj.getInt("cakeSize");
		String cakeDescription = jObj.getString("cakeDescription");
		String cakeImg = (String) this.getServletContext().getAttribute("image");
		PrintWriter writer = response.getWriter();
		try {
			boolean flag;
			if (null != cakeImg) {
				flag = new CakeService().updateCakeDatas("update cakeinfo set cake_name='" + cakeName
						+ "', cake_price = " + cakePrice + ", cake_size = " + cakeSize + ", description = '"
						+ cakeDescription + "', cake_image = '" + cakeImg + "' where id = " + id + "");
			} else {
				flag = new CakeService().updateCakeDatas("update cakeinfo set cake_name='" + cakeName
						+ "', cake_price = " + cakePrice + ", cake_size = " + cakeSize + ", description = '"
						+ cakeDescription + "' where id = " + id + "");
			}
			if (flag) {
				writer.write("success");
				writer.flush();
			} else {
				writer.write("false");
				writer.flush();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		writer.close();
		in.close();
		this.getServletContext().removeAttribute("image");
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
