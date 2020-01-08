package com.controllers.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.util.UserDao;
import com.model.dao.LoginModel;

@WebServlet(name = "Login", urlPatterns = { "/Login" }) 
public class LoginController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static String EDITPG= "/edit.jsp";
	private static String ADMINPG= "/admin.jsp";
	private static String WELCMPG= "/welcome.jsp";
	//private static String REGISTPG= "/registration.jsp";
	private UserDao dao;	
	
	public LoginController()
	{
		super();
		dao = new UserDao();
		// tạo đối tượng dữ liệu mới để tương tác với cơ sở dữ liệu
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException
	{
		String redirectPage="/index.jsp"; //default page
		String action = request.getParameter("action");
		

		// Xóa tài khoản người dùng khỏi bảng và tải lại bảng
		if(action.equalsIgnoreCase("remove"))
		{
			int userid = Integer.parseInt(request.getParameter("userid"));
			dao.deleteAccount(userid);
			redirectPage= ADMINPG;
			request.setAttribute("users", dao.listUsers());
		}
		

		// Tải trang quản trị với dữ liệu cơ sở dữ liệu trong bảng
		else if(action.equalsIgnoreCase("listUsers"))
		{
			redirectPage= ADMINPG;
			request.setAttribute("users", dao.listUsers());
		}
		

		// Tìm người dùng bằng ID và cập nhật cơ sở dữ liệu và bảng với dữ liệu mới.
		else if(action.equalsIgnoreCase("edit"))
		{
			redirectPage= EDITPG;
			int userid = Integer.parseInt(request.getParameter("userid")); 
			// lấy id đối tượng này
			LoginModel user = dao.getUserByID(userid); 
			// đối tượng người dùng
			request.setAttribute("user", user); // gửi dữ liệu người dùng đến jsp
		}
		
		RequestDispatcher view = request.getRequestDispatcher(redirectPage);
		view.forward(request, response); // chuyển tiếp phản hồi cho yêu cầu
	}
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter pwOut = response.getWriter(); 
		//get input from jsp		
		String em=request.getParameter("email");
		String pw =request.getParameter("psword");
		
		
		// Xác thực Đăng nhập với đầu vào
		if(dao.validateLogin(em, pw))
		{

			// tạo session
			LoginModel user= dao.userSession(em);
			HttpSession session = request.getSession();
	        session.setAttribute("username", user.getUsername());
	        session.setAttribute("email", em);
	        // lấy dữ liệu từ session
			RequestDispatcher view = request.getRequestDispatcher(WELCMPG);		
			view.forward(request, response);
			
		}
		// nếu đầu vào không được lưu trong thông báo lỗi in cơ sở dữ liệu và tải lại trang
		else
		{
			pwOut.print("<p style=\"color:red\">Incorrect Username or Password!</p>");
			RequestDispatcher view = request.getRequestDispatcher("/index.jsp");		
			view.include(request, response);
		
		}
	
		pwOut.close();
		
	}
}
