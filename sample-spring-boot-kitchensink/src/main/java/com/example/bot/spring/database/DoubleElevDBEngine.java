package com.example.bot.spring.database;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DoubleElevDBEngine extends DBEngine {
	
	// functions for confirmation 
	// return all tour whose tourist number > min && not yet been confirmed; 
	public String getDiscountBookid(){ // only one tour is allowed to be discounted at the same time
		String discount_tours =  null;
		PreparedStatement nstmt = null;
		
		this.openConnection();
		String statement = "SELECT bootableid FROM double11 "
				+ "WHERE status = 'released' ";
		// choose the tours that haven't been broadcasted;  
		try {
			nstmt = connection.prepareStatement(statement);
			ResultSet rs = this.query(nstmt);
			
			if(rs.next()) {
				discount_tours = rs.getString(1);
			}
			nstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		this.close();
		
		return discount_tours;
	}

	public Set<String> getAllClient(){
		Set<String> clients = new HashSet<String>();
		PreparedStatement nstmt = null;	
		this.openConnection();
		
		String statement = "SELECT userid FROM line_user_info"
						 + " WHERE categorization = 'book'";

		try {
			nstmt = connection.prepareStatement(statement);			
			ResultSet rs = this.query(nstmt);
			
			while(rs.next()) {
				clients.add(rs.getString(1));
			}
			nstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.close();	
		
		return clients;
	}
	
	public void updateBroadcastedTours(String booktableid){	
		PreparedStatement nstmt = null;	
		this.openConnection();		
		String statement = "UPDATE double11 "
				+ "SET status = 'sent' "
				+ "WHERE bootableid = ?";
		try {
			nstmt = connection.prepareStatement(statement);			
			nstmt.setString(1,booktableid);		
			
			ResultSet rs = this.query(nstmt);
			
			nstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		this.close();
	}
}
