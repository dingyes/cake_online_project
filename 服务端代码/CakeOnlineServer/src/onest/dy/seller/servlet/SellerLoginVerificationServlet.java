package onest.dy.seller.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import onest.dy.entity.Seller;
import onest.dy.service.SellerService;

/**
 * 接收客户端卖家信息，进行验证
 */
@WebServlet("/SellerLoginVerification")
public class SellerLoginVerificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public SellerLoginVerificationServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// 通过流的方式接收客户端发来的json串
			// 获取网络输入流与输出流
			InputStream in = request.getInputStream();
			OutputStream out = response.getOutputStream();
			// 接收数据
			byte[] data = new byte[256];
			in.read(data);
			String sJson = new String(data);
			Seller seller = convertToObj(sJson);
			boolean flag = new SellerService().isExistSeller(seller);
			if (flag) {
				out.write("success".getBytes());
			} else {
				out.write("fail".getBytes());
			}
			out.flush();
			in.close();
			out.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将给定的Json串转换为对象
	 *
	 * @param jStr
	 * @return
	 */
	private Seller convertToObj(String jStr) {
		Seller seller = null;
		try {
			// 根据指定的json串创建JSONObject对象
			JSONObject jObj = new JSONObject(jStr);
			// 构造出对象
			seller = new Seller();
			// 获取JSONObject中元素
			String phone = jObj.getString("sellerPhone");
			String password = jObj.getString("sellerPwd");
			seller.setSellerPhone(phone);
			seller.setSellerPassword(password);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return seller;
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
