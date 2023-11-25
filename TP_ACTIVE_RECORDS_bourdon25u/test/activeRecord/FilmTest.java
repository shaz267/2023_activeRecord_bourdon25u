package activeRecord;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

public class FilmTest {

    /**
     * attributs reutilisables dans les tests
     */
    Personne spielberg, scott, kubrick, fincher;

    @BeforeEach
    /**
     * prepare la base de donnees de test
     */
    public void creerDonnees() throws SQLException, RealisateurAbsentException {
        // lien vers la base de test
        Personne.createTable();
        Film.createTable();

        // creation des personnes
        spielberg = new Personne("Spielberg", "Steven");
        spielberg.save(); // id 1
        scott = new Personne("Scott", "Ridley");
        scott.save(); // id 2
        kubrick = new Personne("Kubrick", "Stanley");
        kubrick.save(); // id 3
        fincher = new Personne("Fincher", "David");
        fincher.save(); // id 4

        // creation des films
        new Film("Arche perdue", spielberg).save(); // 1
        new Film("Alien", scott).save(); // 2
        new Film("Temple Maudit", spielberg).save(); // 3
        new Film("Blade Runner", scott).save(); // 4
        new Film("Alien3", fincher).save(); // 5
        new Film("Fight Club", fincher).save(); // 6
        new Film("Orange Mecanique", kubrick).save(); // 7

    }

    @AfterEach
    /**
     * destruction de la base de test
     */
    public void detruireDonnees() throws SQLException {
        // lien vers la base de test
        Film.deleteTable();
        Personne.deleteTable();
    }

    @Test
    /**
     * test constructeur de film
     */
    public void testConstructFilm() {
        Film f = new Film("derniere croisade", spielberg);
        assertEquals(f.getId(), -1, "id nouvel objet a -1");
    }

    @Test
    /**
     * test de recherche par id
     * @throws SQLException
     */
    public void testfindById() throws SQLException {
        Film f = Film.findById(1);
        assertEquals("Arche perdue", f.getTitre());
        assertEquals("Spielberg", f.getRealisateur().getNom());
    }

    @Test
    /**
     * deuxieme test de recherche par id
     * @throws SQLException
     */
    public void testfindByIdBis() throws SQLException {
        Film f = Film.findById(5);
        assertEquals("Alien3", f.getTitre());
        assertEquals("Fincher", f.getRealisateur().getNom());
    }

    @Test
    /**
     * deuxieme test de recherche id inexistant
     * @throws SQLException
     */
    public void testfindByIdInexistant() throws SQLException {
        Film f = Film.findById(8);
        Film res = null;
        assertEquals(res, f, "pas de film correspondant");
    }

    @Test
    /**
     * ajout dans la table d'un nouveau film avec un realisateur existant
     * @throws SQLException
     * @throws RealisateurAbsentException
     */
    public void testSaveAvecRealisateur() throws SQLException,
            RealisateurAbsentException {
        new Film("Panic Room", fincher).save();

        Film f2 = Film.findById(8);
        assertEquals("Panic Room", f2.getTitre());
        assertEquals("Fincher", f2.getRealisateur().getNom());
        // verifie qu'on ne duplique pas les realisateur (normalement deja teste
        // dans Personne)
        assertEquals(4, f2.getRealisateur().getId());

    }

    @Test
    /**
     * Ajout d'un avec film avec un realisateur recupere dans la base
     * @throws SQLException
     * @throws RealisateurAbsentException
     */
    public void testSaveAvecRealisateurRecupere() throws SQLException,
            RealisateurAbsentException {
        Personne p = Personne.findByName("Spielberg").get(0);
        new Film("ET", p).save();

        Film f2 = Film.findById(8);
        assertEquals("ET", f2.getTitre());
        assertEquals("Spielberg", f2.getRealisateur().getNom());
        // verifie qu'on ne duplique pas les realisateur (normalement deja teste
        // dans Personne)
        assertEquals(1, f2.getRealisateur().getId());

    }

    @Test
    /**
     * test deux ajout successifs de film
     * @throws SQLException
     * @throws RealisateurAbsentException
     */
    public void test2Saves() throws SQLException, RealisateurAbsentException {
        Film f = new Film("Panic Room", fincher);
        f.save();
        Film f2 = new Film("ET", spielberg);
        f2.save();

        Film f3 = Film.findById(9);
        assertEquals("ET", f3.getTitre());
        assertEquals("Spielberg", f3.getRealisateur().getNom());
        // verifie qu'on ne duplique pas les realisateur (normalement deja teste
        // dans Personne)
        assertEquals(1, f3.getRealisateur().getId());
    }

    @Test
    /**
     * test creation de film avec un realisateur qu'on vient d'ajouter
     *
     * @throws SQLException
     * @throws RealisateurAbsentException
     */
    public void testNouveauRealSauve() throws SQLException,
            RealisateurAbsentException {
        Personne p = new Personne("Zemeckis", "Robert");
        p.save();
        new Film("Retour vers le futur", p).save();

        Film f = Film.findById(8);
        assertEquals("Retour vers le futur", f.getTitre());
        assertEquals("Zemeckis", f.getRealisateur().getNom());
        // verifie qu'on ne duplique pas les realisateur (normalement deja teste
        // dans Personne)
        assertEquals(5, f.getRealisateur().getId());
    }

    @Test
    /**
     * test de sauvegarde d'un film avec un realisateur non sauve dans la base.
     * doit lever une exception realisateur absent
     *
     * @throws SQLException
     * @throws RealisateurAbsentException
     */
    public void testNouveauRealInconnu() throws SQLException,
            RealisateurAbsentException {
        Personne p = new Personne("Zemeckis", "Robert");
        assertThrows(RealisateurAbsentException.class, () -> {
                    new Film("Retour vers le futur", p).save();
                }
        );
    }

    @Test
    /**
     * test de modification de realisateur dan un film et mise a jour
     * @throws SQLException
     * @throws RealisateurAbsentException
     */
    public void testChangeNomReal() throws SQLException,
            RealisateurAbsentException {
        Film f = new Film("Retour vers le futur", fincher);
        f.save();

        fincher.setNom("bincher");
        fincher.save();

        Film f2 = Film.findById(8);
        assertEquals("Retour vers le futur", f2.getTitre());
        assertEquals("bincher", f2.getRealisateur().getNom());
        // verifie qu'on ne duplique pas les realisateur
        assertEquals(4, f2.getRealisateur().getId());
    }
}
