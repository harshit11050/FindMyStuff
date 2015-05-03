package com.springapp.mvc;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shayan on 02-05-2015.
 */
public class FoundItemDaoImpl
{
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://0.0.0.0:3306/project");
        dataSource.setUsername("root");
        dataSource.setPassword("shayan");

        return dataSource;
    }

    public void create(FoundItem item, int foundItemID,int userid)
    {
        String query = "insert into found_item"
                +"(itemID, itemName, itemDescription, itemLocation, date,userid) values (?, ?, ?, ?, ?,?)";

        Connection conn = null;

        try {

            conn = this.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1,Integer.toString(foundItemID));
            ps.setString(2, item.getItem_name());
            ps.setString(3, item.getItem_details());
            ps.setString(4, item.getItem_location());
            ps.setString(5, item.getDate());
            ps.setString(6,Integer.toString(userid));
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

    public List<FoundItem> getItemList(String query) {
        Connection conn = null;
        Statement statement=null;
        List<FoundItem> events = new ArrayList<FoundItem>();

        try {

            conn = this.getDataSource().getConnection();

            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next())
            {

                //Integer itemID = rs.getInt("itemID");
                String  itemName= rs.getString("itemName");
                String itemDescription = rs.getString("itemDescription");
                String itemLocation = rs.getString("itemLocation");
                String date= rs.getString("date");
                FoundItem item = new FoundItem();

                //item.setItemID(itemID);
                item.setItem_name(itemName);
                item.setItem_details(itemDescription);
                item.setItem_location(itemLocation);
                item.setDate(date);

                events.add(item);
            }

            return events;


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

    public int getUserID(FoundItem item)
    {
        String name = item.getItem_name();
        String description = item.getItem_details();
        String location = item.getItem_location();
        String query = "select userid from found_item where itemName="+name+" AND itemDescription="+description
                +" AND itemLocation="+location;
        Connection conn = null;
        Statement statement=null;

        try {

            conn = this.getDataSource().getConnection();

            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);

            if (rs.next()) {
                System.out.println("Working");
                return rs.getInt(1);
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
        return 0;
    }

    public String getEmailID(int ID)
    {
        String query = "select emailID from User where UserID="+ID;
        Connection conn = null;
        Statement statement=null;

        try {

            conn = this.getDataSource().getConnection();

            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);

            if (rs.next()) {
                System.out.println("User ID Working");
                return rs.getString(1);
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
        return null;
    }
}
