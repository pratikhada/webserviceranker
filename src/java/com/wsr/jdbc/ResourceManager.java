package com.wsr.jdbc;

import java.sql.*;
import webservicerank.configuration.DBAttributes;
import webservicerank.configuration.ExecConfigHandler;

public class ResourceManager
{
    private static String JDBC_DRIVER   = null;//"com.mysql.jdbc.Driver";
    private static String JDBC_URL      = null;//"jdbc:mysql://localhost:3306/webservicerank";
    private static String JDBC_USER     = null;//"root";
    private static String JDBC_PASSWORD = null;//"";

    private static Driver driver = null;

    public static synchronized void initialize() throws Throwable{
        if (JDBC_DRIVER == null || JDBC_URL == null || JDBC_USER == null || JDBC_PASSWORD == null){
            try {
                DBAttributes db = ExecConfigHandler.getServiceDbAttributes();
                JDBC_DRIVER = db.getDriver();
                JDBC_URL = db.getHost() + ":" + db.getPort() + "/" + "webservicerank";
                JDBC_USER = db.getUsername();
                JDBC_PASSWORD = db.getPassword();
            } catch (Throwable ex) {
                String error = (ex.getMessage()!=null)?ex.getMessage():"Error in reading configuratin file!";
                throw new Throwable(error);
            }
        }
    }


    public static synchronized Connection getConnection() throws Throwable
    {
        if (driver == null)
        {
            try
            {
                initialize();
                Class jdbcDriverClass = Class.forName( JDBC_DRIVER );
                driver = (Driver) jdbcDriverClass.newInstance();
                DriverManager.registerDriver( driver );
            }
            catch (Exception e)
            {
                String error = (e.getMessage()!=null)?e.getMessage():"Failed to initialise JDBC driver";
                throw new Throwable(error);
            }
        }

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (SQLException ex) {
            String error = (ex.getMessage()!=null)?ex.getMessage():"Failed to connect to database";
            throw new Throwable(error);
        }
        return conn;
    }


	public static void close(Connection conn)
	{
		try {
			if (conn != null) conn.close();
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
	}

	public static void close(PreparedStatement stmt)
	{
		try {
			if (stmt != null) stmt.close();
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
	}

	public static void close(ResultSet rs)
	{
		try {
			if (rs != null) rs.close();
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}

	}

}
