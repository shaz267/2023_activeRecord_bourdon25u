package activeRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class PrincipaleJDBC {

    // IL FAUT PENSER A AJOUTER MYSQLCONNECTOR AU CLASSPATH

    public static void main(String[] args) throws SQLException {

        // On récupère les informations de connexion à la base de données
        Connection connect = DBConnection.getConnection();

        // variables a modifier en fonction de la base
        String userName = DBConnection.userName;
        String password = DBConnection.password;
        String serverName = DBConnection.serverName;
        //Attention, sous MAMP, le port est 8889
        String portNumber = DBConnection.portNumber;
        String tableName = "personne";

        // iL faut une base nommee testPersonne !
        String dbName = DBConnection.dbName;

        /*// creation de la table Personne
        {
            String createString = "CREATE TABLE Personne ( " + "ID INTEGER  AUTO_INCREMENT, "
                    + "NOM varchar(40) NOT NULL, " + "PRENOM varchar(40) NOT NULL, " + "PRIMARY KEY (ID))";
            Statement stmt = connect.createStatement();
            stmt.executeUpdate(createString);
            System.out.println("1) creation table Personne\n");
        }*/

        // ajout de personne avec requete preparee
        {
            String SQLPrep = "INSERT INTO Personne (nom, prenom) VALUES (?,?);";
            PreparedStatement prep;
            // l'option RETURN_GENERATED_KEYS permet de recuperer l'id (car
            // auto-increment)
            prep = connect.prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, "Spielberg");
            prep.setString(2, "Steven");
            prep.executeUpdate();
            System.out.println("2) ajout Steven Spielberg\n");
        }

        // ajout second personne
        {
            String SQLPrep = "INSERT INTO Personne (nom, prenom) VALUES (?,?);";
            PreparedStatement prep = connect.prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, "Scott");
            prep.setString(2, "Ridley");
            prep.executeUpdate();
            System.out.println("3) ajout Ridley Scott");

            // recuperation de la derniere ligne ajoutee (auto increment)
            // recupere le nouvel id
            int autoInc = -1;
            ResultSet rs = prep.getGeneratedKeys();
            if (rs.next()) {
                autoInc = rs.getInt(1);
            }
            System.out.print("  ->  id utilise lors de l'ajout : ");
            System.out.println(autoInc);
            System.out.println();
        }

        // recuperation de toutes les personnes + affichage
        {
            System.out.println("4) Recupere les personnes de la table Personne");
            String SQLPrep = "SELECT * FROM Personne;";
            PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
            prep1.execute();
            ResultSet rs = prep1.getResultSet();
            // s'il y a un resultat
            while (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int id = rs.getInt("id");
                System.out.println("  -> (" + id + ") " + nom + ", " + prenom);
            }
            System.out.println();
        }

        /*// suppression de la personne 1
        {
            PreparedStatement prep = connect.prepareStatement("DELETE FROM Personne WHERE id=?");
            prep.setInt(1, 1);
            prep.execute();
            System.out.println("5) Suppression personne id 1 (Spielberg)");
            System.out.println();
        }*/

        // recuperation de la seconde personne + affichage
        {
            System.out.println("6) Recupere personne d'id 2");
            String SQLPrep = "SELECT * FROM Personne WHERE id=?;";
            PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
            prep1.setInt(1, 2);
            prep1.execute();
            ResultSet rs = prep1.getResultSet();
            // s'il y a un resultat
            if (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int id = rs.getInt("id");
                System.out.println("  -> (" + id + ") " + nom + ", " + prenom);
            }
            System.out.println();
        }

        // met a jour personne 2
        {
            String SQLprep = "update Personne set nom=?, prenom=? where id=?;";
            PreparedStatement prep = connect.prepareStatement(SQLprep);
            prep.setString(1, "S_c_o_t_t");
            prep.setString(2, "R_i_d_l_e_y");
            prep.setInt(3, 2);
            prep.execute();
            System.out.println("7) Effectue modification Personne id 2");
            System.out.println();
        }

        // recuperation de la seconde personne + affichage
        {
            System.out.println("8) Affiche Personne id 2 apres modification");
            String SQLPrep = "SELECT * FROM Personne WHERE id=?;";
            PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
            prep1.setInt(1, 2);
            prep1.execute();
            ResultSet rs = prep1.getResultSet();
            // s'il y a un resultat
            if (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int id = rs.getInt("id");
                System.out.println("  -> (" + id + ") " + nom + ", " + prenom);
            }
            System.out.println();
        }

        /*// suppression de la table personne
        {
            String drop = "DROP TABLE Personne";
            Statement stmt = connect.createStatement();
            stmt.executeUpdate(drop);
            System.out.println("9) Supprime table Personne");
        }*/

    }

}