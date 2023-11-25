import activeRecord.Film;
import activeRecord.Personne;
import activeRecord.RealisateurAbsentException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class TestFilm {

    private Personne p1 = new Personne("Spielberg", "Steven");
    private Personne p2 = new Personne("Scott", "Ridley");

    @BeforeEach
    public void init() throws SQLException {

        Personne.createTable();

        p1.save();
        p2.save();

        try {
            Film.createTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Film f1 = new Film("titanic", p1);
        Film f2 = new Film("la bete", p1);
        Film f3 = new Film("now Defense men", p2);
        Film f4 = new Film("les arbres", p2);

        try {
            f1.save();
            f2.save();
            f3.save();
            f4.save();
        } catch (SQLException | RealisateurAbsentException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void deleteTable() throws Exception {

        Film.deleteTable();
        Personne.deleteTable();
    }

    @Test
    public void testFindByRealisateur() throws Exception{

        ArrayList<Film> films = Film.findByRealisateur(p1);

        assertEquals("titanic", films.get(0).getTitre());
        assertEquals("la bete", films.get(1).getTitre());
    }


}