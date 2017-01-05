package com.accounting.dao;

import com.accounting.data.Role;
import com.accounting.data.UserDataInfo;
import com.accounting.form.RegisterForm;


public class ManagementDao  extends DataAccessObject  {

	

  
  

	public UserDataInfo getLoginInfo(String login, String password) throws SQLException {
		ResultSet set = null;
		PreparedStatement statement = null;
		Connection con = null;
		UserDataInfo item = null;
		try {
			String SQL_INSERT_INTO_COMPANY =" select user.login, user.password, user.first_name, user.last_name, user.admin, role.role, user.store_id,role.desc " +
			" from user, role  , user_role "+
			" where user.login=?"+
			" and password=? "+
			" and user.role = user_role.login_role_id"+
			" and user_role.role = role.role" ;
			con = getConnection();
				int i=1;
				statement = con.prepareStatement(SQL_INSERT_INTO_COMPANY);
				statement.setString(i++,login);
				statement.setString(i++,password);
				set  = statement.executeQuery();
			ArrayList<String> roles = new ArrayList<String>();
			while (set.next()){
				item = new UserDataInfo();
				item.setLogin(set.getString("login"));
				item.setPassword(set.getString("password"));
				item.setFirstName(set.getString("first_name"));
				item.setLastName(set.getString("last_Name"));
				roles.add(""+set.getInt("role"));
				System.out.println("role added "+set.getInt("role"));
				item.setStoreId(set.getInt("store_id"));
				item.setAdmin(set.getString("admin"));
				item.setRoleDesc(set.getString("desc"));
			}
			if (roles.size()>0)
				item.setRoles(roles);
		}
		finally {
			close(set);
			close(statement);
			close(con);
		}

		return item;
	}
	
	
	public ArrayList<UserDataInfo> getRegisteredUsers(int store) throws SQLException {
		ResultSet set = null;
		PreparedStatement statement = null;
		Connection con = null;
		ArrayList<UserDataInfo> result = new ArrayList<UserDataInfo>();
		try {
			
			String query =" select user.login, user.password, user.first_name, user.last_name, user.admin, role.role, user.store_id,role.desc " +
			" from user, role  , user_role "+
			" where  store_id=? "+
			" and user.role = user_role.login_role_id"+
			" and user_role.role = role.role" ;
			con = getConnection();
				int i=1;
				statement = con.prepareStatement(query);
				statement.setInt(i++,store);
				set  = statement.executeQuery();
				while(set.next()){
					UserDataInfo item = new UserDataInfo();
					item.setLogin(set.getString("login"));
					item.setPassword(set.getString("password"));
					item.setFirstName(set.getString("first_name"));
					item.setLastName(set.getString("last_Name"));
					item.setRole(set.getInt("role"));
					item.setStoreId(set.getInt("store_id"));
					item.setAdmin(set.getString("admin"));
					item.setRoleDesc(set.getString("desc"));
					result.add(item);
			}
		}
		finally {
			close(set);
			close(statement);
			close(con);
		}

		return result;
	}
	
	
	public ArrayList<Role> getRoles(int store) throws SQLException {
		ResultSet set = null;
		PreparedStatement statement = null;
		Connection con = null;
		ArrayList<Role> result = new ArrayList<Role>();
		try {
			
			String SQL_INSERT_INTO_COMPANY ="select * from role " ;
			con = getConnection();
				statement = con.prepareStatement(SQL_INSERT_INTO_COMPANY);
				set  = statement.executeQuery();
				while(set.next()){
					Role item = new Role();
					item.setRole(set.getInt("role"));
					item.setRoleDesc(set.getString("desc"));
					result.add(item);
			}
		}
		finally {
			close(set);
			close(statement);
			close(con);
		}
		return result;
	}
	
	
	


    public void close(Connection connection){
        if(connection != null){
            try {
                connection.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

	public void registerUser(RegisterForm form) {
		ResultSet set = null;
		PreparedStatement statement = null;
		Connection con = null;
		UserDataInfo item = null;
		String query=null;
		String admin="N";
		try {
			
			int uniqueId = getRandomNumber();
			
			for (int i=0; i<form.getRoles().length; i++ )
			{
				if ("1".equals(form.getRoles()[i]))
				{
					admin="Y";
					continue;
				}
			}
			
			System.out.println("Adding new User "+form.getLogin()+" uniqueId "+uniqueId);
			query ="INSERT   INTO  user  (login, password, first_name, last_Name, admin, store_id, role) VALUES (?,?,?,?,?,?,?)";
			
				con = getConnection();
				int i=1;
				statement = con.prepareStatement(query);
				statement.setString(i++,form.getLogin());
				statement.setString(i++,form.getPassword());
				statement.setString(i++,form.getFirstName());
				statement.setString(i++,form.getLastName());
				statement.setString(i++,admin);
				statement.setInt(i++,form.getStoreId());
				statement.setInt(i++,uniqueId);
				int status  = statement.executeUpdate();
				if (status > 0)
				{
					deleteUserRoles(form.getLogin());
					updateRoleTable(form,uniqueId,con);
				}
					
				System.out.println("new user added: "+form.getLogin());
			
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			close(set);
			close(statement);
			close(con);
		}
	}
	private void updateRoleTable(RegisterForm form,int uniqueId, Connection con ) {
		
		ResultSet set = null;
		PreparedStatement statement = null;
		String query=null;
		try {
			for (int i=0; i<form.getRoles().length; i++ )
			{
			String role = form.getRoles()[i];
			System.out.println("Adding new User "+role+" uniqueId "+uniqueId);
			query ="DELETE from user_role where login=?";
			query ="INSERT   INTO  user_role  (login, role, login_role_id) VALUES (?,?,?)";
				int j=1;
				statement = con.prepareStatement(query);
				statement.setString(j++,form.getLogin());
				statement.setString(j++,role);
				statement.setInt(j++,uniqueId);
				int status  = statement.executeUpdate();
				System.out.println("new role added: "+form.getLogin());
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			close(set);
			close(statement);
		}
	}

	public void updateUser(RegisterForm form) {
		ResultSet set = null;
		PreparedStatement statement = null;
		Connection con = null;
		String query=null;
		String admin="N";
		try {

			System.out.println("Adding new User "+form.getLogin());
			
			int uniqueId = getRandomNumber();
			for (int i=0; i<form.getRoles().length; i++ )
			{
				if ("1".equals(form.getRoles()[i]))
				{
					admin="Y";
					continue;
				}
			}
			query ="Update user  set password=?, first_name=?, last_Name=?, admin=?, role=? where login=? and store_id=? ";
				con = getConnection();
				int i=1;
				statement = con.prepareStatement(query);
				statement.setString(i++,form.getPassword());
				statement.setString(i++,form.getFirstName());
				statement.setString(i++,form.getLastName());
				statement.setString(i++,admin);
				statement.setInt(i++,uniqueId);
				statement.setString(i++,form.getLogin());
				statement.setInt(i++,form.getStoreId());
				statement.executeUpdate();
				deleteUserRoles(form.getLogin());
				updateRoleTable(form,uniqueId,con);
				System.out.println("user updated : "+form.getLogin());			
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			close(set);
			close(statement);
			close(con);
		}
	}
	
	private void deleteUserRoles(String login) {
		
		ResultSet set = null;
		PreparedStatement statement = null;
		Connection con = null;
		String query=null;
		try {
			con = getConnection();
			query ="DELETE from user_role where login=?";
				int j=1;
				statement = con.prepareStatement(query);
				statement.setString(j++,login);
				int status  = statement.executeUpdate();
				System.out.println("user roles deleted: "+status);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			close(set);
			close(statement);
			close(con);
		}
	}

	
	
	public void deleteUser(String login, int store) {
		ResultSet set = null;
		PreparedStatement statement = null;
		Connection con = null;
		String query=null;
		try {
			System.out.println("Adding new User "+login);
			query ="Delete from  user  where login=? and store_id=? ";
				con = getConnection();
				int i=1;
				statement = con.prepareStatement(query);
				statement.setString(i++,login);
				statement.setInt(i++,store);
				statement.executeUpdate();
				System.out.println("user deleted : "+login);			
				deleteUserRoles(login);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			close(set);
			close(statement);
			close(con);
		}
	}

	
	public boolean checkUserExist(String login) {
		boolean checkUserExist = false;
		PreparedStatement statement = null;
		Connection con = null;
		
		try {
			System.out.println("checkUser new User "+login);
			String query ="select * from  user  where login=? ";
				con = getConnection();
				int i=1;
				statement = con.prepareStatement(query);
				statement.setString(i++,login);
				ResultSet res = statement.executeQuery();
				if (res.next())
				{
					checkUserExist = true;
				}
				System.out.println("checkUser: "+login);			
				deleteUserRoles(login);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			close(statement);
			close(con);
		}
		return checkUserExist;
	}


	
	
	}



