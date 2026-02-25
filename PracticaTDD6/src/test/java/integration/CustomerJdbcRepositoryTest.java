package integration;

import static org.junit.jupiter.api.Assertions.*;

import com.arturojas32.practicatdd6.integration.Customer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.arturojas32.practicatdd6.integration.CustomerJdbcRepository;

class CustomerJdbcRepositoryTest {

	private Connection conn;
	private CustomerJdbcRepository customerRepository;

	@BeforeEach
	void setUp() throws Exception {

		conn = DriverManager.getConnection("jdbc:h2:C:\\Users\\artur\\Desktop\\Java Testing\\Herramientas\\db", "sa",
				"");
		customerRepository = new CustomerJdbcRepository(conn);
	}


	@Test
	@Disabled
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

		// WHEN
		Integer idGenerated = customerRepository.save(cToSave);

		// THEN
		cToSave.setId(idGenerated);
		Customer insertedCustomer = customerRepository.get(idGenerated);
		assertEquals(cToSave, insertedCustomer);

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












