package com.example.crud_javaa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexao{
    private static final String URL = "jdbc:mysql://localhost:3306/java_mysql";
    private static final String USER = "root";
    private static final String SENHA = "";

    public static Connection obterconexao() throws SQLException {
        return DriverManager.getConnection(URL, USER, SENHA);
    }

    

}