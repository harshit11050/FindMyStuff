package com.springapp.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shayan on 02-05-2015.
 */
public class UserDAOImpl implements UserDAO
{

    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://0.0.0.0:3306/project");
        dataSource.setUsername("root");
        dataSource.setPassword("shayan");

        return dataSource;
    }

    @Override
    public void getCount(User user)
    {
        Connection conn = null;
        Statement statement=null;

        String query = "select count(*)from User";
        try {

            conn = this.getDataSource().getConnection();

            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next());
                UserCounter.setCounter(rs.getInt(1));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {

                }
            }
        }
    }

    @Override
    public int checkUser(User user)
    {
        Connection conn = null;
        Statement statement=null;

        String query = "select UserID from User where emailID="+"\""+user.getEmail_id()+"\"";
        System.out.println(query);
        try {

            conn = this.getDataSource().getConnection();

            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            System.out.println(rs);

            if(rs.next()){
                int id =rs.getInt(1);
                System.out.println(id);
                return id;
            }else{
                return 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {

                }
            }
        }
    }

    @Override
    public void addUser(User user, int userid)
    {
        System.out.println(user.getEmail_id()+" "+user.getUsername());
        String query = "insert into User"
                +"(UserID, UserName, emailID) values (?, ?, ?)";

        Connection conn = null;

        try {

            conn = this.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(query);


            ps.setString(1, Integer.toString(userid));
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail_id());
            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {

                }
            }
        }
    }


}
