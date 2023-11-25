package activeRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Film {

    private String titre;

    private int id;

    private int id_rea;

    public Film(String titre, Personne real){

        this.titre = titre;
        this.id = -1;
        this.id_rea = real.getId();
    }

    private Film(int idreal, int id2real, String titre){

    }

    /**
     * Méthode qui permet de trouver un film par son id
     * @param idfilm
     * @return
     * @throws SQLException
     */
    public static Film findById(int idfilm) throws SQLException {

        // On prépare la requête
        String query = "SELECT * FROM Film WHERE id=?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
        ps.setInt(1, idfilm);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        // s'il y a un resultat
        if (rs.next()) {
            String titre = rs.getString("titre");
            String titre1 = rs.getString("titre");
            int id1 = rs.getInt("id");
            Personne real = Personne.findById(rs.getInt("id_rea"));

            Film film = new Film(titre1, real);
            film.id = id1;
            return film;
        }
        else{
            return null;
        }
    }

    /**
     * Méthode findAll qui permet de retourner une liste des films de la base
     * @return films, la liste des films de la base
     * @throws SQLException
     */
    public static ArrayList<Film> findAll() throws SQLException {

        ArrayList<Film> films = new ArrayList<Film>();

        String query = "SELECT * FROM FILM";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
        ps.execute();
        ResultSet rs = ps.getResultSet();

        // Pour chaque personne de la base
        while(rs.next()){
            // On récupère id, titre, id_rea
            int idselect = rs.getInt("id");
            String titre = rs.getString("titre");
            Personne real = Personne.findById(rs.getInt("id_rea"));
            // On crée le film
            Film film = new Film(titre, real);
            film.id = idselect;
            // On ajoute la personne à la liste
            films.add(film);
        }

        return films;
    }

    public static ArrayList<Film> findByRealisateur(Personne p) throws SQLException {

        ArrayList<Film> films = new ArrayList<Film>();
        // On récupère l'id du réalisateur
        int id_rea_select = p.getId();
        Personne real = Personne.findById(id_rea_select);

        // On prépare la requête
        String query = "SELECT * FROM FILM WHERE id_rea=?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
        ps.setInt(1, id_rea_select);
        ps.execute();
        ResultSet rs = ps.getResultSet();

        // On crée la liste de films qui sont réalisés par le réalisateur
        while(rs.next()){
            // On récupère titre, id
            int idselect = rs.getInt("id");
            String titre = rs.getString("titre");
            // On crée le film
            Film film = new Film(titre, real);
            film.id = idselect;
            // On ajoute le film à la liste
            films.add(film);
        }

        return films;
    }

    /**
     * Méthode de création de la table Film
     * @throws SQLException
     */
    public static void createTable() throws SQLException {

        String createString = "CREATE TABLE film ( " + "ID int(11)  AUTO_INCREMENT, "
                + "titre varchar(40) NOT NULL, " + "id_rea int(11) NOT NULL, " + "PRIMARY KEY (ID))";
        Statement st = DBConnection.getConnection().createStatement();
        st.executeUpdate(createString);
    }

    /**
     * Méthode pour suppr la table personne
     * @throws SQLException
     */
    public static void deleteTable() throws SQLException{

        String query = "DROP TABLE FILM";
        Statement st = DBConnection.getConnection().createStatement();
        st.executeUpdate(query);
    }

    /**
     * Méthode delete pour supprimer un film de la table
     * @throws SQLException
     */
    public void delete() throws SQLException {

        // On récup l'id du film
        int id = this.id;

        // Si l'id est different de -1
        if(id != -1){
            // On supprime donc le film qui a cet id dans la table
            PreparedStatement prep = DBConnection.getConnection().prepareStatement("DELETE FROM FILM WHERE id=?");
            prep.setInt(1, id);
            prep.execute();
            // On repasse donc l'id à -1 car il n'est plus dans la table
            this.id = -1;
        }
    }

    /**
     * Méthode update pour mettre à jour un film déja existant dans la table
     * @throws SQLException
     */
    private void update() throws SQLException, RealisateurAbsentException {

        String SQLprep = "update FILM set titre=?, id_rea=? where id=?;";
        PreparedStatement prep = DBConnection.getConnection().prepareStatement(SQLprep);
        prep.setString(1, this.titre);
        prep.setInt(2, this.id_rea);
        prep.setInt(3, this.id);
        prep.execute();
        if(this.id_rea == -1){
            throw new RealisateurAbsentException("Le réalisateur n'est pas dans la base");
        }
    }

    /**
     * Méthode saveNew pour enregistrer un film dans la table qui n'y est pas déja
     * @throws SQLException
     */
    private void saveNew() throws SQLException, RealisateurAbsentException {

        String query = "INSERT INTO FILM(titre, id_rea) VALUES(?,?)";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
        ps.setString(1, this.titre);
        ps.setInt(2, this.id_rea);
        ps.execute();
        if(this.id_rea == -1){
            throw new RealisateurAbsentException("Le réalisateur n'est pas dans la base");
        }
    }

    /**
     * Méthode save, pour enregistrer un tuple Film dans la table ou le maj si il y est déja
     * @throws SQLException
     */
    public void save() throws SQLException, RealisateurAbsentException {
        // Si l'id est -1
        if(this.id == -1){
            // on met à jour
            this.saveNew();
            // on veut recup l'id pour le maj
            ArrayList<Film> films = Film.findAll();
            // on le maj
            this.id = films.size() - 1;
        }
        // Sinon
        else{
            this.update();
        }
    }

    /**
     * Méthode pour récupérer l'id du réalisateur du Film this
     */
    public Personne getRealisateur() throws SQLException {

        return Personne.findById(this.id_rea);
    }

    public String getTitre() {
        return titre;
    }

    public int getId() {
        return id;
    }

    /**
     * Méthode d'affichage d'un film
     * @return
     */
    public String toString(){
        String ch = "";
        ch += "Film : " + this.titre + ", Id Réalisateur: " + this.id_rea + ", Id Film: " + this.id;
        return ch;
    }
}
