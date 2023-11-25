package activeRecord;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * classe de test
 */
public class PersonneTest {

	@BeforeEach
	/**
	 * prepare la base de donnees
	 */
	public void creerDonnees() throws SQLException {
		// lien vers la base de test
		Personne.createTable();

		// ajoute
		new Personne("Spielberg", "Steven").save(); // id 1
		new Personne("Scott", "Ridley").save(); // id 2
		new Personne("Kubrick", "Stanley").save(); // id 3
		new Personne("Spielberg", "George").save(); // id 4

	}

	/**
	 * permet de supprimer les donnees � la fin de chaque test
	 * 
	 * @throws SQLException
	 */
	@AfterEach
	public void supprimerDonnees() throws SQLException {
		Personne.deleteTable();
	}

	@Test
	/**
	 * test constructeur Personne
	 */
	public void testConstruct() {
		Personne p = new Personne("toto", "tot");
		assertEquals(p.getId(), -1,"objet pas dans la base");
	}

	@Test
	/**
	 * test find avec un nom existant
	 * @throws SQLException
	 */
	public void testFindByNameExiste() throws SQLException {
		ArrayList<Personne> p = Personne.findByName("Kubrick");

		assertEquals(1, p.size(),"une seule reponse");
		Personne pers = p.get(0);
		assertEquals(pers.getNom(), "Kubrick");
		assertEquals(pers.getPrenom(), "Stanley");
	}

	@Test
	/**
	 * test find avec un nom dans deux personnes
	 * @throws SQLException
	 */
	public void testFindByName2Existe() throws SQLException {
		ArrayList<Personne> p = Personne.findByName("Spielberg");

		assertEquals(2, p.size(),"deux reponses dans personne");
		Personne pers = p.get(0);
		assertEquals(pers.getNom(), "Spielberg");
		pers = p.get(1);
		assertEquals(pers.getNom(), "Spielberg");
	}

	@Test
	/**
	 * test findName avec personne n ayant le nom
	 * @throws SQLException
	 */
	public void testFindByNameNon() throws SQLException {
		ArrayList<Personne> p = Personne.findByName("ee");
		assertEquals(0, p.size(),"pas de reponse");
	}

	@Test
	/**
	 * test findall de Personne
	 * @throws SQLException
	 */
	public void testFindAll() throws SQLException {
		ArrayList<Personne> p = Personne.findAll();
		assertEquals(4, p.size());
	}

	@Test
	/**
	 * on test sauvegarde d'une nouvelle personne inexistante
	 */
	public void testSaveNew() throws SQLException {
		Personne p = new Personne("toto", "titi");
		p.save();

		assertEquals(5, p.getId(),"id de la personne vient autoincrement");
		Personne pers = Personne.findById(5);
		assertEquals(pers.getNom(), "toto");
		assertEquals(pers.getPrenom(), "titi");
		assertEquals(pers.getId(), 5);
	}

	@Test
	/**
	 * on test mise a jour d'une personne deja dans la table
	 */
	public void testSaveExistant() throws SQLException {
		// modifie la personne
		Personne p = Personne.findById(2);
		p.setNom("Bertrand");
		p.save();
		assertEquals(2, p.getId(),"id ne devrait pas bouger");

		// on la recherche
		ArrayList<Personne> pers = Personne.findByName("Bertrand");
		assertEquals(1, pers.size());
		Personne p2 = pers.get(0);
		assertEquals("Bertrand", p2.getNom());
		assertEquals("Ridley", p2.getPrenom());
		assertEquals(p2.getId(), 2,"ide devrait etre le meme");
	}

	@Test
	/**
	 * on test la suppression d'une personne existante 
	 */
	public void testDelete() throws SQLException {
		Personne p = Personne.findById(2);
		p.delete();
		Personne p2 = Personne.findById(2);

		assertEquals( -1, p.getId(),"id devrait etre revenue a -1");
		assertEquals( null, p2,"le supprime n'existe plus");
	}

}
