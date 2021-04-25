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

import onest.dy.service.OrderService;

/**
 * Servlet implementation class UpdateOrderStatusServlet
 */
@WebServlet("/UpdateOrderStatus")
public class UpdateOrderStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateOrderStatusServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取网络输入流与输出流
		InputStream in = request.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
		String orderInfo = reader.readLine();
		JSONObject jObj = new JSONObject(orderInfo);
		int orderId = jObj.getInt("orderId");
		int status = jObj.getInt("status");
		PrintWriter writer = response.getWriter();
		try {
			boolean flag = new OrderService().updateOrderStatus(orderId, status);
			if(flag) {
				writer.write("success");
			} else {
				writer.write("false");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		writer.write("\n");
		writer.flush();
		writer.close();
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
