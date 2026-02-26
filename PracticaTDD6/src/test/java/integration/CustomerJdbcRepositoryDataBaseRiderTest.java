package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.arturojas32.practicatdd6.integration.Customer;
import com.arturojas32.practicatdd6.integration.CustomerJdbcRepository;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.junit5.DBUnitExtension;

@ExtendWith(DBUnitExtension.class)
@DataSet("customers.yml")
class CustomerJdbcRepositoryDataBaseRiderTest {

	private Connection conn;
	private CustomerJdbcRepository customerRepository;
	private ConnectionHolder connectionHolder;

	@BeforeEach
	void setUp() throws Exception {

		conn = DriverManager.getConnection("jdbc:h2:mem:test;INIT=runscript from 'classpath:customers-schema.sql'",
				"sa", "");
		customerRepository = new CustomerJdbcRepository(conn);
		connectionHolder = new ConnectionHolderImpl(conn);

	}


	@Test
	void shouldList() throws SQLException {

		// GIVEN
		List<Customer> expectedCustomers = List.of(createDefaultCustomer(1));

		// WHEN
		List<Customer> actualCustomers = customerRepository.list();
		// THEN
		assertEquals(expectedCustomers.size(), actualCustomers.size());
		assertEquals(expectedCustomers, actualCustomers);

	}

	@Test
	@ExpectedDataSet(value = "customers_expected_save.yml", ignoreCols = "id")
	void shouldSaveTest() {
		// GIVEN
		Customer cToSave = new Customer();

		cToSave.setEmail("michaeljordan@mail.comm");
		cToSave.setLegalIdentifier("12345679Z");
		cToSave.setLastName("Jordann");
		cToSave.setName("Michaell");
		cToSave.setPhoneNumber("6112223334");

		// WHEN
		Integer idGenerated = customerRepository.save(cToSave);

		// THEN
		// see anotation
	}

	@Test
	@ExpectedDataSet(value = "customers_after_delete.yml")
	void shouldDeleteTest() {

		// GIVEN
		Integer id = 1;
		// WHEN
		customerRepository.delete(id);
		// THEN
		// see anotation
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












