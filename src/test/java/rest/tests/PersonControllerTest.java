package rest.tests;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.nio.charset.Charset;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import ru.polis.interfaces.PersonDAO;
import ru.polis.main.Main;
import ru.polis.objects.Person;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@WebAppConfiguration
public class PersonControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;
    @Autowired
	private PersonDAO personDB;

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Before
	public void setup() {

		personDB.create();

		Person person = new Person();
		person.setFirstName("Evgeny");
		person.setLastName("Timoshchuk");
		person.setAge(20);
		personDB.insert(person);

		person.setFirstName("Andrey");
		person.setLastName("Varlamov");
		person.setAge(30);
		personDB.insert(person);

	}

	@After
	public void tearDown() {
		personDB.drop();
	}

	@Test
	public void getPerson() throws Exception {
        MockMvc mockMvc = webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/get_person").param("id", "1").contentType(contentType)).andExpect(status().isOk());
	}

	@Test
	public void getPersonParamNull() throws Exception {
	    MockMvc mockMvc = webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/get_person").param("id", "").contentType(contentType)).andExpect(status().isIAmATeapot());
	}

	@Test
	public void personNotFound() throws Exception {
        MockMvc mockMvc = webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/get_person").param("id", "999").contentType(contentType))
				.andExpect(status().isNotFound());
	}

	@Test
	public void getAllPerson() throws Exception {
        MockMvc mockMvc = webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/get_all")).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(content().contentType(contentType)).andExpect(status().isOk());

	}

	@Test
	public void postPerson() throws Exception {
        MockMvc mockMvc = webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(post("/post_person").content("{\"firstName\":\"Albert\",\"lastName\":\"Popkov\",\"age\":45}")
				.contentType(contentType)).andExpect(status().isCreated());

	}

}
