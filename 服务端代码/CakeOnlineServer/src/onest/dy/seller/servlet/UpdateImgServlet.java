package onest.dy.seller.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UpdateImgServlet
 */
@WebServlet("/UpdateImg")
public class UpdateImgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateImgServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		InputStream in = request.getInputStream();
		String path = getServletContext().getRealPath("/");
		// 获取文件输出流
		String uuid = UUID.randomUUID().toString().replace("-", "");
		OutputStream out = new FileOutputStream(path + "images\\" + uuid + ".jpg");
		System.out.println(path + "images\\" + uuid + ".jpg");
		String cakeImgPath = "images/" + uuid + ".jpg";
		int n = -1;
		while ((n = in.read()) != -1) {
			out.write(n);
			out.flush();
		}
		this.getServletContext().setAttribute("image", cakeImgPath);
		System.out.println(cakeImgPath);
		PrintWriter write = response.getWriter();
		write.write("success");
		write.flush();
		in.close();
		out.close();
		write.close();
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
