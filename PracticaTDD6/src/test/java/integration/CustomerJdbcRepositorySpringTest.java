package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.arturojas32.practicatdd6.integration.Customer;
import com.arturojas32.practicatdd6.integration.CustomerJdbcRepository;

class CustomerJdbcRepositorySpringTest {

	private CustomerJdbcRepository customerRepository;
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void setUp() throws Exception {

		EmbeddedDatabase db = new EmbeddedDatabaseBuilder().generateUniqueName(true).setType(EmbeddedDatabaseType.H2)
				.setScriptEncoding("UTF-8").addScripts("customers-schema.sql", "customer-dataset.sql").build();

		customerRepository = new CustomerJdbcRepository(db);
		jdbcTemplate = new JdbcTemplate(db);

	}


	@Test
	void shouldList() throws SQLException {

		// GIVEN
		List<Customer> expectedCustomers = List.of(createDefaultCustomer(1000));

		// WHEN
		List<Customer> actualCustomers = customerRepository.list();
		// THEN
		assertEquals(expectedCustomers.size(), actualCustomers.size());
		assertEquals(expectedCustomers, actualCustomers);

	}

	@Test
	void shouldSaveTest() {
		// GIVEN
		Customer cToSave = new Customer();

		cToSave.setEmail("pito@mail.com");
		cToSave.setLegalIdentifier("AbX16");
		cToSave.setLastName("Dominguez");
		cToSave.setName("Juan");


		int rowsBefore = countRowsInCustomerTable();

		// WHEN
		Integer idGenerated = customerRepository.save(cToSave);

		Map<String, Object> insertedCustomerMap = jdbcTemplate.queryForMap("SELECT * FROM CUSTOMER WHERE ID = ?",
				idGenerated);


		// THEN
		// assertan lo mismo


		assertEquals(cToSave.getEmail(), insertedCustomerMap.get("email"));
		assertEquals(rowsBefore + 1, customerRepository.list().size());
		assertEquals(rowsBefore + 1, countRowsInCustomerTable());


	}

	@Test
	void shouldGetCustomerByIdTest() {
		// GIVEN
		Customer cToSave = new Customer();

		cToSave.setEmail("pito@mail.com");
		cToSave.setLegalIdentifier("AbX16");
		cToSave.setLastName("Dominguez");
		cToSave.setName("Juan");

		int rowsBefore = countRowsInCustomerTable();

		// WHEN
		Integer idGenerated = customerRepository.save(cToSave);

		Map<String, Object> insertedCustomerMap = jdbcTemplate.queryForMap("SELECT * FROM CUSTOMER WHERE ID = ?",
				idGenerated);

		// THEN

		assertEquals("AbX16", customerRepository.get(idGenerated).getLegalIdentifier());
		assertEquals("pito@mail.com", customerRepository.get(idGenerated).getEmail());
		assertEquals("Dominguez", customerRepository.get(idGenerated).getLastName());
		assertEquals("Juan", customerRepository.get(idGenerated).getName());
		assertNotEquals("Juan", customerRepository.get(idGenerated).getLegalIdentifier());

	}

	@Test
	void shouldDeleteTest() {
		// GIVEN
		Customer cToSave = new Customer();

		cToSave.setEmail("pito@mail.com");
		cToSave.setLegalIdentifier("AbX16");
		cToSave.setLastName("Dominguez");
		cToSave.setName("Juan");

		// WHEN
		Integer idGenerated = customerRepository.save(cToSave);

		int sizeOfListWithCustomer = customerRepository.list().size();

		customerRepository.delete(idGenerated);

		int currentCustomerListSize = customerRepository.list().size();


		Executable e = () -> {
			Map<String, Object> insertedCustomerMap = jdbcTemplate.queryForMap("SELECT * FROM CUSTOMER WHERE ID = ?",
					idGenerated);
		};

		// THEN
		assertThrows(DataAccessException.class, e);
		assertEquals(sizeOfListWithCustomer - 1, currentCustomerListSize);
	}

	private int countRowsInCustomerTable() {
		return JdbcTestUtils.countRowsInTable(jdbcTemplate, "CUSTOMER");
	}



	private Customer createDefaultCustomer(Integer id) {

		Customer customer = new Customer();
		customer.setId(id);
		customer.setLegalIdentifier("12345678Z");
		customer.setName("Michael");
		customer.setLastName("Jordan");
		customer.setEmail("michaeljordan@mail.com");
		customer.setPhoneNumber("611222333");

		return customer;

	}

}












