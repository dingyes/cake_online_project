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
			// �������ڱ����û����ݵļ���
			cakes = new CakeService().getCakes("select * from cakeinfo");
			// ��ȡ��ͼƬ�ڷ���˵�·��
			// ��ȡ��ǰվ��ĸ�Ŀ¼
			String path = getServletContext().getContextPath();
			// �������ݣ��޸�·����Ϣ
			for (Cake cake : cakes) {
				String oPath = cake.getCakeImg();
				// ƴ����ͼƬ��վ���Ŀ¼
				cake.setCakeImg(path + "/" + oPath);
			}
			String json = convertToJson(cakes);
			// ��Json�����ظ��ͻ���
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
	 * �������Ķ���ת����json��
	 * 
	 * @param cakes ���⼯��
	 * @return json��
	 */
	private String convertToJson(List<Cake> cakes) {
		String json = null;
		// �������⹹��json��
		JSONObject jObj = new JSONObject();
		// ����JSONArray����
		JSONArray jArray = new JSONArray();
		for (Cake cake : cakes) {
			// ÿ��Cake�������һ��JsonObject
			JSONObject jCake = new JSONObject();
			// ��jCake���������
			jCake.put("id", cake.getId());
			jCake.put("cakeName", cake.getCakeName());
			jCake.put("sellerPhone", cake.getSellerPhone());
			jCake.put("description", cake.getDescription());
			jCake.put("cakeImg", cake.getCakeImg());
			jCake.put("cakeSize", cake.getSize());
			jCake.put("cakePrice", cake.getPrice());
			// �ѵ�ǰ��JSONObject��ӵ�JSONArray��
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
