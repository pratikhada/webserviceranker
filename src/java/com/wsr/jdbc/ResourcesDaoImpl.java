/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.wsr.jdbc;

import com.wsr.dao.*;
import com.wsr.factory.*;
import com.wsr.dto.*;
import com.wsr.exceptions.*;
import java.sql.Connection;
import java.util.Collection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

public class ResourcesDaoImpl extends AbstractDAO implements ResourcesDao
{
	/** 
	 * The factory class for this DAO has two versions of the create() method - one that
takes no arguments and one that takes a Connection argument. If the Connection version
is chosen then the connection will be stored in this attribute and will be used by all
calls to this DAO, otherwise a new Connection will be allocated for each operation.
	 */
	protected java.sql.Connection userConn;

	/** 
	 * All finder methods in this class use this SELECT constant to build their queries
	 */
	protected final String SQL_SELECT = "SELECT res_id, property_ID, resource, ns_id FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( res_id, property_ID, resource, ns_id ) VALUES ( ?, ?, ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET res_id = ?, property_ID = ?, resource = ?, ns_id = ? WHERE res_id = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE res_id = ?";

	/** 
	 * Index of column res_id
	 */
	protected static final int COLUMN_RES_ID = 1;

	/** 
	 * Index of column property_ID
	 */
	protected static final int COLUMN_PROPERTY_ID = 2;

	/** 
	 * Index of column resource
	 */
	protected static final int COLUMN_RESOURCE = 3;

	/** 
	 * Index of column ns_id
	 */
	protected static final int COLUMN_NS_ID = 4;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 4;

	/** 
	 * Index of primary-key column res_id
	 */
	protected static final int PK_COLUMN_RES_ID = 1;

	/** 
	 * Inserts a new row in the resources table.
	 */
	public ResourcesPk insert(Resources dto) throws ResourcesDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			stmt = conn.prepareStatement( SQL_INSERT, Statement.RETURN_GENERATED_KEYS );
			int index = 1;
			stmt.setInt( index++, dto.getResId() );
			stmt.setInt( index++, dto.getPropertyId() );
			stmt.setString( index++, dto.getResource() );
			stmt.setInt( index++, dto.getNsId() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
		
			// retrieve values from auto-increment columns
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				dto.setResId( rs.getInt( 1 ) );
			}
		
			reset(dto);
			return dto.createPk();
		}
		catch (Throwable _e) {
			throw new ResourcesDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the resources table.
	 */
	public void update(ResourcesPk pk, Resources dto) throws ResourcesDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			stmt = conn.prepareStatement( SQL_UPDATE );
			int index=1;
			stmt.setInt( index++, dto.getResId() );
			stmt.setInt( index++, dto.getPropertyId() );
			stmt.setString( index++, dto.getResource() );
			stmt.setInt( index++, dto.getNsId() );
			stmt.setInt( 5, pk.getResId() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
		}
		catch (Throwable _e) {
			throw new ResourcesDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the resources table.
	 */
	public void delete(ResourcesPk pk) throws ResourcesDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			stmt = conn.prepareStatement( SQL_DELETE );
			stmt.setInt( 1, pk.getResId() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
		}
		catch (Throwable _e) {
			throw new ResourcesDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the resources table that matches the specified primary-key value.
	 */
	public Resources findByPrimaryKey(ResourcesPk pk) throws ResourcesDaoException
	{
		return findByPrimaryKey( pk.getResId() );
	}

	/** 
	 * Returns all rows from the resources table that match the criteria 'res_id = :resId'.
	 */
	public Resources findByPrimaryKey(int resId) throws ResourcesDaoException
	{
		Resources ret[] = findByDynamicSelect( SQL_SELECT + " WHERE res_id = ?", new Object[] {  new Integer(resId) } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the resources table that match the criteria ''.
	 */
	public Resources[] findAll() throws ResourcesDaoException
	{
		return findByDynamicSelect( SQL_SELECT, null );
	}

	/** 
	 * Returns all rows from the resources table that match the criteria 'property_ID = :propertyId'.
	 */
	public Resources[] findByPropertyId(int propertyId) throws ResourcesDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE property_ID = ?", new Object[] {  new Integer(propertyId) } );
	}

	/**
	 * Method 'ResourcesDaoImpl'
	 * 
	 */
	public ResourcesDaoImpl()
	{
	}

	/**
	 * Method 'ResourcesDaoImpl'
	 * 
	 * @param userConn
	 */
	public ResourcesDaoImpl(final java.sql.Connection userConn)
	{
		this.userConn = userConn;
	}

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows)
	{
		this.maxRows = maxRows;
	}

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows()
	{
		return maxRows;
	}

	/**
	 * Method 'getTableName'
	 * 
	 * @return String
	 */
	public String getTableName()
	{
		return "webservicerank.resources";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected Resources fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			Resources dto = new Resources();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected Resources[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			Resources dto = new Resources();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		Resources ret[] = new Resources[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(Resources dto, ResultSet rs) throws SQLException
	{
		dto.setResId( rs.getInt( COLUMN_RES_ID ) );
		dto.setPropertyId( rs.getInt( COLUMN_PROPERTY_ID ) );
		dto.setResource( rs.getString( COLUMN_RESOURCE ) );
		dto.setNsId( rs.getInt( COLUMN_NS_ID ) );
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(Resources dto)
	{
	}

	/** 
	 * Returns all rows from the resources table that match the specified arbitrary SQL statement
	 */
	public Resources[] findByDynamicSelect(String sql, Object[] sqlParams) throws ResourcesDaoException
	{
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			// construct the SQL statement
			final String SQL = sql;
		
		
			// prepare statement
			stmt = conn.prepareStatement( SQL );
			stmt.setMaxRows( maxRows );
		
			// bind parameters
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
		
		
			rs = stmt.executeQuery();
		
			// fetch the results
			return fetchMultiResults(rs);
		}
		catch (Throwable _e) {
			throw new ResourcesDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(rs);
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns all rows from the resources table that match the specified arbitrary SQL statement
	 */
	public Resources[] findByDynamicWhere(String sql, Object[] sqlParams) throws ResourcesDaoException
	{
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			// construct the SQL statement
			final String SQL = SQL_SELECT + " WHERE " + sql;
		
		
			// prepare statement
			stmt = conn.prepareStatement( SQL );
			stmt.setMaxRows( maxRows );
		
			// bind parameters
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
		
		
			rs = stmt.executeQuery();
		
			// fetch the results
			return fetchMultiResults(rs);
		}
		catch (Throwable _e) {
			throw new ResourcesDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(rs);
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

}