package com.dao.util;



import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class DBUtil {

	private static Connection conn=null;
	public static Connection getConnection()
	{
		if(conn !=null) //nếu có một kết nối đã trả về nó, nếu không thì thiết lập một kết nối
			return conn;
		else
		{
			try
			{
				String url = "jdbc:mysql://localhost:3306/ccfa";
		     
				String driver = "com.mysql.jdbc.Driver";
				String username= "root";
				String password= "123456abc";
				Class.forName(driver);
				conn= (Connection) DriverManager.getConnection(url,username,password);
			}
			catch (ClassNotFoundException e) 
			{
                e.printStackTrace();
            }
			catch (SQLException e) 
			{
                e.printStackTrace();
		
			}
			return conn;
		}
		
	}
}