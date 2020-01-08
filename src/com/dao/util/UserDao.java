package com.dao.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.model.dao.LoginModel;
import com.dao.util.DBUtil;

import java.util.ArrayList;
import java.util.List;

public class UserDao {

	private Connection conn;
	public UserDao() {
		
		conn= DBUtil.getConnection();//connect to database
	}
	//Xác thực đăng nhập với đầu vào của người dùng
	public boolean validateLogin(String em,String pw)
	{
		boolean canLogin=false;
		
		// Tìm kiếm cơ sở dữ liệu cho email và mật khẩu và trả về true nếu tìm thấy
		try
		{
			PreparedStatement ps = conn.prepareStatement("select * from TheUser where email=? and psword=?");
			ps.setString(1, em);
			ps.setString(2, pw);
			
			ResultSet rs = ps.executeQuery();
			canLogin= rs.next(); 
			// đúng nếu tìm thấy sai
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return canLogin;
	}
	

	// trả về thông tin người dùng từ email được chỉ định cho phiên
	public LoginModel userSession(String em)
	{
		LoginModel user=new LoginModel(); 
		// tạo đối tượng người dùng mới
		try
		{
			PreparedStatement ps = conn
					.prepareStatement("select * from TheUser where email=?");
			// tìm kiếm cơ sở dữ liệu cho email
			ps.setString(1, em);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			{
				user.setUserID(rs.getInt("userID"));
				user.setUsername(rs.getString("username"));
				user.setPsword(rs.getString("psword"));
				user.setEmail(rs.getString("email"));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return user;
	}
	

	// Tạo người dùng mới với dữ liệu đầu vào
	public void createUser(LoginModel user)
	{
		try
		{
			PreparedStatement ps = conn
					.prepareStatement("insert into TheUser(username,psword,email) values (?,?,?)");
			// thêm người dùng vào cơ sở dữ liệu
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPsword());
			ps.setString(3, user.getEmail());
			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	//update user with input data
	public void editAccount(LoginModel user)
	{
		try
		{
			PreparedStatement ps = conn
					.prepareStatement("update TheUser set username=?, psword=?" + " where userID=?"); // tìm người dùng có id và cập nhật thông tin
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPsword());
			ps.setInt(3, user.getUserID());
			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	// Xóa người dùng khỏi cơ sở dữ liệu với id được chỉ định
	public void deleteAccount(int userid)
	{
		try
		{
			PreparedStatement ps = conn
					.prepareStatement("delete from TheUser where userID=?");
			ps.setInt(1, userid);
			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	

	// trả về danh sách tất cả người dùng
	public List<LoginModel> listUsers()
	{
		List<LoginModel> userList = new ArrayList<LoginModel>();
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select * from TheUser");
			while (rs.next())
			{
				LoginModel user = new LoginModel();
				user.setUserID(rs.getInt("userID"));
				user.setUsername(rs.getString("username"));
				user.setPsword(rs.getString("psword"));
				user.setEmail(rs.getString("email"));
				userList.add(user);
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return userList;
	}
	

	// trả về thông tin người dùng từ id được chỉ định
	public LoginModel getUserByID(int userid)
	{
		LoginModel user = new LoginModel();
		try
		{
			PreparedStatement ps = conn
					.prepareStatement("select * from TheUser where userID= ?");
			ps.setInt(1, userid);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			{
				user.setUserID(rs.getInt("userID"));
				user.setUsername(rs.getString("username"));
				user.setPsword(rs.getString("psword"));
				user.setEmail(rs.getString("email"));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return user;
	}
}
