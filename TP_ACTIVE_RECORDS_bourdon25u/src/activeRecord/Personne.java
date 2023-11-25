package activeRecord;

import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Classe Personne
 */
public class Personne {

    /**
     * Attributs de la classe Personne
     */
    private int id;
    private String nom;
    private String prenom;


    /**
     * Constructeur de la classe Personne
     * @param n, nom de la personne
     * @param p, prenom de la personne
     */
    public Personne(String n, String p){
        nom = n;
        prenom = p;
        id = -1;
    }

    /**
     * Méthode findAll qui permet de retourner une liste des personnes de la base
     * @return personnes, la liste des personnes de la base
     * @throws SQLException
     */
    public static ArrayList<Personne> findAll() throws SQLException {

        ArrayList<Personne> personnes = new ArrayList<Personne>();

        String query = "SELECT * FROM PERSONNE";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
        ps.execute();
        ResultSet rs = ps.getResultSet();

        // Pour chaque personne de la base
        while(rs.next()){
            // On récupère nom, prenom, id
            int idselect = rs.getInt("id");
            String nomselect = rs.getString("nom");
            String prenomselect = rs.getString("prenom");
            // On crée la personne
            Personne personne = new Personne(nomselect, prenomselect);
            personne.id = idselect;
            // On ajoute la personne à la liste
            personnes.add(personne);
        }

        return personnes;
    }

    /**
     * Méthode permettant de select une personne par son id
     * @param idPersonne
     * @return
     * @throws SQLException
     */
    public static Personne findById(int idPersonne) throws SQLException {

        // On prépare la requête
        String query = "SELECT * FROM Personne WHERE id=?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
        ps.setInt(1, idPersonne);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        // s'il y a un resultat
        if (rs.next()) {
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            int id = rs.getInt("id");
            Personne personne = new Personne(nom, prenom);
            personne.id = id;
            return personne;
        }
        else{
            return null;
        }
    }

    /**
     *
     * @param name
     * @return
     * @throws SQLException
     */
    public static ArrayList<Personne> findByName(String name) throws SQLException {

        // on crée la liste a retourner et on recup dans une autre liste toutes les personnes
        ArrayList<Personne> personnes = new ArrayList<Personne>();
        ArrayList<Personne> allPersonnes = Personne.findAll();

        // on verif qui a le bon nom name en parcourant allPersonnes
        for(int i = 0; i < allPersonnes.size(); i++){
            // si la personne a le bon nom
            if(allPersonnes.get(i).nom.equals(name)){
                // on ajoute alors la personne a personnes
                personnes.add(allPersonnes.get(i));
            }
        }

        // enfin, on retourne personnes
        return personnes;
    }

    /**
     * Méthode de création de la table Personne
     * @throws SQLException
     */
    public static void createTable() throws SQLException {

        String createString = "CREATE TABLE Personne ( " + "ID INTEGER  AUTO_INCREMENT, "
                + "NOM varchar(40) NOT NULL, " + "PRENOM varchar(40) NOT NULL, " + "PRIMARY KEY (ID))";
        Statement stmt = DBConnection.getConnection().createStatement();
        stmt.executeUpdate(createString);
    }

    /**
     * Méthode pour suppr la table personne
     * @throws SQLException
         */
        public static void deleteTable() throws SQLException{

            String query = "DROP TABLE PERSONNE";
            Statement st = DBConnection.getConnection().createStatement();
            st.executeUpdate(query);
        }

    /**
     * Méthode delete pour supprimer une personne de la table
     * @throws SQLException
     */
    public void delete() throws SQLException {

        // On récup l'id de la personne
        int id = this.id;

        // Si l'id est different de -1
        if(id != -1){
            // On supprime donc la personne qui a cet id dans la table
            PreparedStatement prep = DBConnection.getConnection().prepareStatement("DELETE FROM Personne WHERE id=?");
            prep.setInt(1, id);
            prep.execute();
            // On repasse donc l'id à -1 car il n'est plus dans la table
            this.id = -1;
        }
    }

    /**
     * Méthode update pour mettre à jour une personne dans la table déja existante
     * @throws SQLException
     */
    private void update() throws SQLException{

        String SQLprep = "update Personne set nom=?, prenom=? where id=?;";
        PreparedStatement prep = DBConnection.getConnection().prepareStatement(SQLprep);
        prep.setString(1, this.nom);
        prep.setString(2, this.prenom);
        prep.setInt(3, this.id);
        prep.execute();
    }

    /**
     * Méthode saveNew pour enregistrer une personne dans la table qui n'y est pas déja
     * @throws SQLException
     */
    private void saveNew() throws SQLException {

        String query = "INSERT INTO PERSONNE(nom, prenom) VALUES(?,?)";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
        ps.setString(1, this.nom);
        ps.setString(2, this.prenom);
        ps.execute();

    }

    /**
     * Méthode save, pour enregistrer un tuple Personne dans la table ou le maj si il y est déja
     * @throws SQLException
     */
    public void save() throws SQLException {
        // Si l'id est -1
        if(this.id == -1){
            // on met à jour
            this.saveNew();
            // on veut recup l'id pour le maj
            ArrayList<Personne> personnes = Personne.findAll();
            // on le maj
            this.id = personnes.size();
        }
        // Sinon
        else{
            this.update();
        }
    }

    /**
     * Méthode d'affichage de Personne
     * @return ch, chaîne à afficher
     */
    public String toString(){

        String ch = "";

        ch += "Personne :\nNom : " + nom + ", Prénom : " + prenom + ", Id : " + id;
        return ch;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }
}
