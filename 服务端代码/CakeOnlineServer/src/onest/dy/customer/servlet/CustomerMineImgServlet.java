package onest.dy.customer.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import onest.dy.entity.Customer;
import onest.dy.service.CustomerService;

/**
 * 获取用户图片
 */
@WebServlet("/CustomerMineImg")
public class CustomerMineImgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CustomerMineImgServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cusPhone = request.getParameter("phone");
		String root = getServletContext().getRealPath("/");
		OutputStream out = response.getOutputStream();
		try {
			Customer customer = new CustomerService().getSpecificCustomer(cusPhone);
			String path = customer.getCustomerPhoto();
			int n = -1;
			InputStream in = new FileInputStream(root + path);
			while ((n = in.read()) != -1) {
				out.write(n);
			}
			in.close();
			out.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
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
