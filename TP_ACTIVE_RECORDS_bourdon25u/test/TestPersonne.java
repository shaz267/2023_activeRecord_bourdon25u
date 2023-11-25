import activeRecord.DBConnection;
import activeRecord.Personne;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestPersonne {

    @BeforeEach
    public void init(){

        try{
            Personne.createTable();
            Personne p1 = new Personne("Spielberg", "Steven");
            Personne p2 = new Personne("Kubrick", "Stanley");
            Personne p3 = new Personne("Fincher", "David");
            Personne p4 = new Personne("Scott", "Ridley");
            p1.save();
            p2.save();
            p3.save();
            p4.save();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void end() throws SQLException {
        Personne.deleteTable();
    }

    @Test
    public void testSingleton_01_OK() throws SQLException {
        Connection dbConnection = DBConnection.getConnection();
        Connection dbConnection2 = DBConnection.getConnection();
        assertEquals("Les objets ne sont pas les même", dbConnection, dbConnection2);
    }

    @Test
    public void testDBName_02_OK() throws SQLException {
        DBConnection.setNomDB("testpersonne");
        Connection dbConnection = DBConnection.getConnection();
        DBConnection.setNomDB("tp_activerecord");
        Connection dbConnection2 = DBConnection.getConnection();
        assertFalse("Les deux objets sont les même", dbConnection == dbConnection2);
    }

    @Test
    public void testFindAll_03_OK() throws SQLException{

        ArrayList<Personne> personnes = Personne.findAll();
        // On crée deux personnes pour verif
        Personne p1 = new Personne("Kubrick", "Stanley");
        p1.setId(3);
        Personne p2 = new Personne("Scott", "Ridley");
        p2.setId(6);

        // On verif donc
        assertNotSame("Les personnes ne sont pas les même", p1, personnes.get(2));
        assertNotSame("Les personnes ne sont pas les même", p2, personnes.get(5));
    }

    @Test
    public void testFindById_04_OK() throws SQLException{

        // Préparation des données
        Personne p = new Personne("S_c_o_t_t", "R_i_d_l_e_y");
        p.setId(2);

        // On vérifie
        assertNotSame("Les personnes ne sont pas les même", p, Personne.findById(2));
    }

    @Test
    public void testFindByName_05_OK() throws SQLException{

        // Préparation des données
        ArrayList<Personne> actual = Personne.findByName("Spielberg");
        Personne p1 = new Personne("Spielberg", "Steven");
        p1.setId(1);
        Personne p2 = new Personne("Spielberg", "Steven");
        p2.setId(5);

        // on verifie
        assertEquals("Les deux personnes doivent être les même", actual.get(0).toString(), p1.toString());
        assertEquals("Les deux personnes doivent être les même", actual.get(1).toString(), p2.toString());
    }
}
