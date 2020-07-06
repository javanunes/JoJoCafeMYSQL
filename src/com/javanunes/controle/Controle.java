/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javanunes.controle;

import com.javanunes.telas.JojocafemysqlTela;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricardo
 */
public class Controle {
    
    private static String user="root";
    private static String password="militaresgays";
    private static String database="mysql";
    private static String url="jdbc:mysql://localhost:3306/"+database;
    private static String useSSL="false";
    private static String query="";
    private static boolean emUso = false;
    public Connection conexao;
    public ResultSet rs = null;
    public Statement st = null;

    public String getUser() {
        return user;
    }

    public  boolean isEmUso() {
        return this.emUso;
    }

    public  void setEmUso(boolean emUso) {
        this.emUso = emUso;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUseSSL() {
        return useSSL;
    }

    public void setUseSSL(String useSSL) {
        this.useSSL = useSSL;
    }

    public String getQuery() {
        return query;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Connection getConexao() {
        return conexao;
    }

    public void setConexao(Connection conexao) {
        this.conexao = conexao;
    }

    public ResultSet getRs() {
        return rs;
    }

    public void setRs(ResultSet rs) {
        this.rs = rs;
    }

    public Statement getSt() {
        return st;
    }

    public void setSt(Statement st) {
        this.st = st;
    }
    
    /*public Connection conectaNoBancoDados(){
         try{
          return DriverManager.getConnection(this.url,this.user,this.password);
         }
         catch(SQLException e){
             //gui.messageBox("Erro estranho ao conectar no banco, veja se o driver mysql esta incluido em nos: "+e);
             return null;
         }
    }*/
    
     public Connection conectaNoBancoDados(String database){
         this.setDatabase(database);
         try{
          return DriverManager.getConnection(this.url,this.user,this.password);
         }
         catch(SQLException e){
             //gui.messageBox("Erro estranho ao conectar no banco, veja se o driver mysql esta incluido em nos: "+e);
             return null;
         }
    }
    
     public Connection conectaNoBancoDados2(String database){
         
         if(database.equals("") || database == null){
            return null; 
         }
         try{
          return DriverManager.getConnection("jdbc:mysql://localhost:3306/"+database,this.user,this.password);
         }
         catch(SQLException e){
             System.out.println("NÃO CHEGOU BANCO PRA ENTRAR ou teve erro: "+e);
             return null;
         }
    }
    
   
    public void mostraEstruturaTabela(String banco, String tabela){
        try{
            Connection conexao = conectaNoBancoDados2(banco);
            Statement st = conexao.createStatement();
            String query ="DESCRIBE "+tabela;
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData md = rs.getMetaData();
            int col = md.getColumnCount();
            for (int i = 1; i <= col; i++){
              String col_name = md.getColumnName(i);
              System.out.print(col_name+"\t");
            }
            System.out.println();
            DatabaseMetaData dbm = conexao.getMetaData();
            ResultSet rs1;
            rs1 = dbm.getColumns(null,"%",tabela,"%");

             while (rs1.next()){
               String col_name = rs1.getString("COLUMN_NAME");
               String data_type = rs1.getString("TYPE_NAME");
               int data_size = rs1.getInt("COLUMN_SIZE");
               int nullable = rs1.getInt("NULLABLE");
               System.out.print(col_name+"\t"+data_type+"("+data_size+")"+"\t");
             }
             rs1.close();
        }
        catch(SQLException e){
            System.out.println("ixi, não consegui não ver a estrutura nao---------------------------");
        }
        
    }  
     
     
    
    public int salvaFuncionario(String nome, String sobrenome, String nascimento, String email, String salario){
        conexao = conectaNoBancoDados("mysql");
        Double salarioDouble=0000d;
        // prepara a query
        
        if(nome.equals("") || sobrenome.equals("") || nascimento.equals("") || email.equals("") || salario.equals("")){
            //gui.messageBox("Tem campo em branco chegando, saindo!");
            return 0;
        }
        try{
           salarioDouble = Double.parseDouble(salario);
        }
        catch(NumberFormatException e){
           //gui.messageBox("Erro no seu salario");   
        }
        
        try{
        query="INSERT INTO funcionarios(nome,sobrenome,dataNascimento, email, salario) VALUES(?,?,?,?,?)";
        PreparedStatement prst  = conexao.prepareStatement(query);
        prst.setString(1, nome.toUpperCase());
        prst.setString(2, sobrenome.toUpperCase());
        prst.setString(3, nascimento);
        prst.setString(4, email);
        prst.setDouble(5, salarioDouble);
        // tenta salvar executar
        int linhas = prst.executeUpdate();
        //gui.messageBox("Salvo !");
        conexao.close();
        return linhas;
       }
       catch(SQLException e){
           //gui.messageBox("Erro ao salvar dados de " + nome + " em "+e);
           return 0;
           
       } 
    }
    
    public void escreve(){
         conexao = conectaNoBancoDados("mysql");
         String resultado;
         query="SHOW databases;";
         JojocafemysqlTela tela = new JojocafemysqlTela();
       
        try
        {
            st = conexao.createStatement();
            rs = st.executeQuery(query);
            while(rs.next()){
               resultado = rs.getString("Database"); 
               System.out.println(resultado); 
               // tela.adicionaItemNaArvore(resultado);
               
            }
        } catch (SQLException ex)
        {
            System.out.println("-------------------------------------------------------------------------ssss");
        }
        finally{
            //gui.messageBox("Deu merda Javinha");
        }   
    }
    
    
    public String[] retornaInformacoesArrayPorNome(String nome){
         conexao = conectaNoBancoDados("mysql");
         String[] arrays = new String[5];
         nome = nome.toUpperCase();
         query="SELECT * FROM funcionarios WHERE nome='"+nome+"'";
        try
        {
            st = conexao.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){
             arrays[0] = rs.getString("nome");
             arrays[1] = rs.getString("sobrenome");
             arrays[2] = rs.getString("dataNascimento");
             arrays[3] = rs.getString("email");
             arrays[4] = rs.getString("salario");
            }
            else{
                arrays[0] = "";
                arrays[1] = "";
                arrays[2] = "";
                arrays[3] = "";
                arrays[4] = "";                
            }
            conexao.close();
            return arrays;
            
        } catch (SQLException ex)
        {
            
            return arrays;
        }
         
    }
    
    public static void showDataBaseList(){
        
        
    }
   
    public static void main(String[] parametros){
       JojocafemysqlTela.ChamaJanelaPrincipal();
        
    }    

  
    
}

