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
 * ���տͻ���������Ϣ��������֤
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
			// ͨ�����ķ�ʽ���տͻ��˷�����json��
			// ��ȡ�����������������
			InputStream in = request.getInputStream();
			OutputStream out = response.getOutputStream();
			// ��������
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
	 * ��������Json��ת��Ϊ����
	 *
	 * @param jStr
	 * @return
	 */
	private Seller convertToObj(String jStr) {
		Seller seller = null;
		try {
			// ����ָ����json������JSONObject����
			JSONObject jObj = new JSONObject(jStr);
			// ���������
			seller = new Seller();
			// ��ȡJSONObject��Ԫ��
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
