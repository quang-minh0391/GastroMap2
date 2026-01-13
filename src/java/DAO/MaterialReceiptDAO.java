/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import DAL.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class MaterialReceiptDAO {
    public static final MaterialReceiptDAO INSTANCE = new MaterialReceiptDAO();

    private MaterialReceiptDAO() {
        super(); // gọi DBContext() để mở connection
    }
    
    
}
