package com.api;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@TestPropertySource(locations="classpath:application-test.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTests {
	
	@Autowired
	protected MockMvc mockMvc;
	
	@Sql({
		"/insert_user_data.sql", "/insert_address_data.sql", "/insert_answer_data.sql",
		"/insert_book_data.sql", "/insert_course_data.sql", "/insert_questionnaire_data.sql",
		"/insert_question_data.sql", "/insert_survey_data.sql", "/insert_surveyChoice_data.sql"
		})
	@Test
	public void testAuthenticate() throws Exception {
		
		String token = null;
		JSONObject userRegisterObject = new JSONObject();      
		JSONObject tokenObject;
		
		userRegisterObject.put("username", "NewAcc");
		userRegisterObject.put("password", "password");
		
		String userRegister = userRegisterObject.toJSONString();
		
		// Test 1 Create new account
		this.mockMvc
		.perform(MockMvcRequestBuilders
				.post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userRegister))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isCreated())
		.andReturn();
		
		// Test 2 Create new account with same credentials
		this.mockMvc
		.perform(MockMvcRequestBuilders
				.post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userRegister))
		.andDo(MockMvcResultHandlers.print())
        .andExpect(status().isConflict())
        .andReturn();

		// Test 3 Authenticate created account
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userRegister))
				.andDo(MockMvcResultHandlers.print())
	            .andExpect(status().isOk())
	            .andReturn();
		
		token = result.getResponse().getContentAsString();
		tokenObject = (JSONObject) new JSONParser().parse(token);
		token = tokenObject.get("token").toString();
		
		// Test 4 Get logged account informations
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/me").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isOk());	
	}
	
	@Test
	public void testUser() throws Exception {
		String token = null;
		JSONObject userObject = new JSONObject();
		JSONObject adminObject = new JSONObject();
		JSONObject tokenObject;
		
		userObject.put("username", "NewAcc");
		userObject.put("password", "password");
		adminObject.put("username", "Salim");
		adminObject.put("password", "password");
		
		String user = userObject.toJSONString();
		String admin = adminObject.toJSONString();
		
		// Authenticate to user
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(user))
				.andDo(MockMvcResultHandlers.print())
	            .andExpect(status().isOk())
	            .andReturn();
		token = result.getResponse().getContentAsString();
		tokenObject = (JSONObject) new JSONParser().parse(token);
		token = tokenObject.get("token").toString();
				
		// Test 1 get all users without token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/user"))
		.andDo(print())
		.andExpect(status().isUnauthorized());
		
		// Test 2 get all users with token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/user").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isOk());
		
		// Test 3 delete another account with user privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/user/5").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isForbidden());
		
		// Authenticate to admin
		result = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(admin))
				.andDo(MockMvcResultHandlers.print())
	            .andExpect(status().isOk())
	            .andReturn();
		
		token = result.getResponse().getContentAsString();
		tokenObject = (JSONObject) new JSONParser().parse(token);
		token = tokenObject.get("token").toString();
		
		// Test 4 delete another account with admin privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/user/5").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void testAddress() throws Exception {
		String token = null;
		String adminToken = null;
		JSONObject userObject = new JSONObject();
		JSONObject adminObject = new JSONObject();
		JSONObject addressObject = new JSONObject();
		JSONObject tokenObject;
		JSONObject adminTokenObject;
		
		userObject.put("username", "NewAcc");
		userObject.put("password", "password");
		adminObject.put("username", "Salim");
		adminObject.put("password", "password");
		
		addressObject.put("city", "Marseille");
		addressObject.put("country", "France");
		addressObject.put("street", "10 Avenue de Marseille");
		addressObject.put("postalCode", "69200");

		String user = userObject.toJSONString();
		String admin = adminObject.toJSONString();
		String address = addressObject.toJSONString();
		
		// Authenticate to user
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(user))
				.andDo(MockMvcResultHandlers.print())
	            .andExpect(status().isOk())
	            .andReturn();
		
		token = result.getResponse().getContentAsString();
		tokenObject = (JSONObject) new JSONParser().parse(token);
		token = tokenObject.get("token").toString();
		
		// Authenticate to admin
				
		MvcResult adminResult = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(admin))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn();
		
		adminToken = adminResult.getResponse().getContentAsString();
		adminTokenObject = (JSONObject) new JSONParser().parse(adminToken);
		adminToken = adminTokenObject.get("token").toString();
		
		// Test 1 get all address without token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/address"))
		.andDo(print())
		.andExpect(status().isUnauthorized());
		
		// Test 2 get all address with token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/address").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isOk());
		
		// Test 3 Create new address
		this.mockMvc
		.perform(MockMvcRequestBuilders
				.post("/address")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(address))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isCreated())
		.andReturn();
		
		// Test 4 Delete another address with user privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/address/10").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isForbidden());
		
		// Authenticate to admin
		result = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(admin))
				.andDo(MockMvcResultHandlers.print())
	            .andExpect(status().isOk())
	            .andReturn();
		
		token = result.getResponse().getContentAsString();
		tokenObject = (JSONObject) new JSONParser().parse(token);
		token = tokenObject.get("token").toString();
		
		// Test 5 Delete another address with admin privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/address/10").header("Authorization", "Bearer " + adminToken))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void testAnswer() throws Exception {
		String token = null;
		String adminToken = null;
		JSONObject userObject = new JSONObject();
		JSONObject adminObject = new JSONObject();
		JSONObject answerObject = new JSONObject();
		JSONObject tokenObject;
		JSONObject adminTokenObject;
		
		userObject.put("username", "NewAcc");
		userObject.put("password", "password");
		adminObject.put("username", "Salim");
		adminObject.put("password", "password");
		
		answerObject.put("content", "Réponse 1");
		answerObject.put("placement", 1);
		answerObject.put("success", 1);
		answerObject.put("question_id", 2);

		String user = userObject.toJSONString();
		String admin = adminObject.toJSONString();
		String answer = answerObject.toJSONString();
		
		// Authenticate to user
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(user))
				.andDo(MockMvcResultHandlers.print())
	            .andExpect(status().isOk())
	            .andReturn();
		
		token = result.getResponse().getContentAsString();
		tokenObject = (JSONObject) new JSONParser().parse(token);
		token = tokenObject.get("token").toString();
		
		// Authenticate to admin
		MvcResult adminResult = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(admin))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn();
				
		adminToken = adminResult.getResponse().getContentAsString();
		adminTokenObject = (JSONObject) new JSONParser().parse(adminToken);
		adminToken = adminTokenObject.get("token").toString();
		
		// Test 1 get all answer without token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/answer"))
		.andDo(print())
		.andExpect(status().isUnauthorized());
		
		// Test 2 get all answer with token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/answer").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isOk());
		
		// Test 3 Create new answer
		this.mockMvc
		.perform(MockMvcRequestBuilders
				.post("/answer")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(answer))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isCreated())
		.andReturn();
		
		// Test 4 Delete another answer with user privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/answer/10").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isForbidden());
		
		// Test 5 Delete another answer with admin privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/answer/10").header("Authorization", "Bearer " + adminToken))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void testBook() throws Exception {
		String token = null;
		String adminToken = null;
		JSONObject userObject = new JSONObject();
		JSONObject adminObject = new JSONObject();
		JSONObject bookObject = new JSONObject();
		JSONObject tokenObject;
		JSONObject adminTokenObject;
		
		userObject.put("username", "NewAcc");
		userObject.put("password", "password");
		adminObject.put("username", "Salim");
		adminObject.put("password", "password");
		
		bookObject.put("title", "Livre de test");
		bookObject.put("author", "Salim");
		bookObject.put("description", "Un livre permettant les tests unitaires");
		bookObject.put("premium", 0);
		bookObject.put("price", 424.42);

		String user = userObject.toJSONString();
		String admin = adminObject.toJSONString();
		String book = bookObject.toJSONString();
		
		// Authenticate to user
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(user))
				.andDo(MockMvcResultHandlers.print())
	            .andExpect(status().isOk())
	            .andReturn();
		
		token = result.getResponse().getContentAsString();
		tokenObject = (JSONObject) new JSONParser().parse(token);
		token = tokenObject.get("token").toString();
		
		// Authenticate to admin
		MvcResult adminResult = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(admin))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn();
				
		adminToken = adminResult.getResponse().getContentAsString();
		adminTokenObject = (JSONObject) new JSONParser().parse(adminToken);
		adminToken = adminTokenObject.get("token").toString();
		
		// Test 1 get all book without token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/book"))
		.andDo(print())
		.andExpect(status().isUnauthorized());
		
		// Test 2 get all book with token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/book").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isOk());
		
		// Test 3 Create new book
		this.mockMvc
		.perform(MockMvcRequestBuilders
				.post("/book")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(book))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isCreated())
		.andReturn();
		
		// Test 4 Delete another book with user privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/book/5").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isForbidden());
		
		// Test 5 Delete another book with admin privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/book/5").header("Authorization", "Bearer " + adminToken))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void testCourse() throws Exception {
		String token = null;
		String adminToken = null;
		JSONObject userObject = new JSONObject();
		JSONObject adminObject = new JSONObject();
		JSONObject courseObject = new JSONObject();
		JSONObject tokenObject;
		JSONObject adminTokenObject;
		
		userObject.put("username", "NewAcc");
		userObject.put("password", "password");
		adminObject.put("username", "Salim");
		adminObject.put("password", "password");
		
		courseObject.put("title", "Première course");
		courseObject.put("difficulty", "MEDIUM");
		courseObject.put("suggestedHours", 20);
		courseObject.put("premium", 0);

		String user = userObject.toJSONString();
		String admin = adminObject.toJSONString();
		String course = courseObject.toJSONString();
		
		// Authenticate to user
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(user))
				.andDo(MockMvcResultHandlers.print())
	            .andExpect(status().isOk())
	            .andReturn();
		
		token = result.getResponse().getContentAsString();
		tokenObject = (JSONObject) new JSONParser().parse(token);
		token = tokenObject.get("token").toString();
		
		// Authenticate to admin
		MvcResult adminResult = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(admin))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn();
				
		adminToken = adminResult.getResponse().getContentAsString();
		adminTokenObject = (JSONObject) new JSONParser().parse(adminToken);
		adminToken = adminTokenObject.get("token").toString();
		
		// Test 1 get all course without token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/course"))
		.andDo(print())
		.andExpect(status().isUnauthorized());
		
		// Test 2 get all course with token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/course").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isOk());
		
		// Test 3 Create new course
		this.mockMvc
		.perform(MockMvcRequestBuilders
				.post("/course")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(course))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isCreated())
		.andReturn();
		
		// Test 4 Delete another course with user privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/course/2").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isForbidden());
		
		// Test 5 Delete another course with admin privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/course/2").header("Authorization", "Bearer " + adminToken))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void testDiploma() throws Exception {
		String token = null;
		String adminToken = null;
		JSONObject userObject = new JSONObject();
		JSONObject adminObject = new JSONObject();
		JSONObject tokenObject;
		JSONObject adminTokenObject;
		
		userObject.put("username", "NewAcc");
		userObject.put("password", "password");
		adminObject.put("username", "Salim");
		adminObject.put("password", "password");

		String user = userObject.toJSONString();
		String admin = adminObject.toJSONString();
		
		String diploma = "{ 'user' : { 'id': 1}, 'course' : { 'id' : 1 } }";
		diploma = diploma.replaceAll("'", "\"");
		
		
		// Authenticate to user
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(user))
				.andDo(MockMvcResultHandlers.print())
	            .andExpect(status().isOk())
	            .andReturn();
		
		token = result.getResponse().getContentAsString();
		tokenObject = (JSONObject) new JSONParser().parse(token);
		token = tokenObject.get("token").toString();
		
		// Authenticate to admin
		MvcResult adminResult = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(admin))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn();
				
		adminToken = adminResult.getResponse().getContentAsString();
		adminTokenObject = (JSONObject) new JSONParser().parse(adminToken);
		adminToken = adminTokenObject.get("token").toString();
		
		// Test 1 get all diploma without token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/diploma"))
		.andDo(print())
		.andExpect(status().isUnauthorized());
		
		// Test 2 get all diploma with token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/diploma").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isOk());
		
		// Test 3 Create new diploma
		this.mockMvc
		.perform(MockMvcRequestBuilders
				.post("/diploma")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(diploma))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isCreated())
		.andReturn();
		
		// Test 4 Delete another diploma with user privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/diploma/1").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isForbidden());
		
		// Test 5 Delete another diploma with admin privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/diploma/1").header("Authorization", "Bearer " + adminToken))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void testQuestionnaire() throws Exception {
		String token = null;
		String adminToken = null;
		JSONObject userObject = new JSONObject();
		JSONObject adminObject = new JSONObject();
		JSONObject questionnaireObject = new JSONObject();
		JSONObject tokenObject;
		JSONObject adminTokenObject;
		
		userObject.put("username", "NewAcc");
		userObject.put("password", "password");
		adminObject.put("username", "Salim");
		adminObject.put("password", "password");
		
		questionnaireObject.put("title", "Nouveau Questionnaire");
		questionnaireObject.put("description", "Premier questionnaire de la deuxieme course");
		questionnaireObject.put("placement", 1);
		questionnaireObject.put("course_id", 2);

		String user = userObject.toJSONString();
		String admin = adminObject.toJSONString();
		String questionnaire = questionnaireObject.toJSONString();
		
		// Authenticate to user
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(user))
				.andDo(MockMvcResultHandlers.print())
	            .andExpect(status().isOk())
	            .andReturn();
		
		token = result.getResponse().getContentAsString();
		tokenObject = (JSONObject) new JSONParser().parse(token);
		token = tokenObject.get("token").toString();
		
		// Authenticate to admin
		MvcResult adminResult = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(admin))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn();
				
		adminToken = adminResult.getResponse().getContentAsString();
		adminTokenObject = (JSONObject) new JSONParser().parse(adminToken);
		adminToken = adminTokenObject.get("token").toString();
		
		// Test 1 get all questionnaire without token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/questionnaire"))
		.andDo(print())
		.andExpect(status().isUnauthorized());
		
		// Test 2 get all questionnaire with token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/questionnaire").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isOk());
		
		// Test 3 Create new questionnaire
		this.mockMvc
		.perform(MockMvcRequestBuilders
				.post("/questionnaire")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(questionnaire))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isCreated())
		.andReturn();
		
		// Test 4 Delete another questionnaire with user privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/questionnaire/2").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isForbidden());
		
		// Test 5 Delete another questionnaire with admin privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/questionnaire/2").header("Authorization", "Bearer " + adminToken))
		.andDo(print())
		.andExpect(status().isOk());
	}

	@Test
	public void testQuestion() throws Exception {
		String token = null;
		String adminToken = null;
		JSONObject userObject = new JSONObject();
		JSONObject adminObject = new JSONObject();
		JSONObject questionObject = new JSONObject();
		JSONObject tokenObject;
		JSONObject adminTokenObject;
		
		userObject.put("username", "NewAcc");
		userObject.put("password", "password");
		adminObject.put("username", "Salim");
		adminObject.put("password", "password");
		
		questionObject.put("content", "Première question du questionnaire 2");
		questionObject.put("multipleAnswers", 0);
		questionObject.put("placement", 1);
		questionObject.put("questionnaire_id", 2);

		String user = userObject.toJSONString();
		String admin = adminObject.toJSONString();
		String question = questionObject.toJSONString();
		
		// Authenticate to user
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(user))
				.andDo(MockMvcResultHandlers.print())
	            .andExpect(status().isOk())
	            .andReturn();
		
		token = result.getResponse().getContentAsString();
		tokenObject = (JSONObject) new JSONParser().parse(token);
		token = tokenObject.get("token").toString();
		
		// Authenticate to admin
		MvcResult adminResult = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(admin))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn();
				
		adminToken = adminResult.getResponse().getContentAsString();
		adminTokenObject = (JSONObject) new JSONParser().parse(adminToken);
		adminToken = adminTokenObject.get("token").toString();
		
		// Test 1 get all question without token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/question"))
		.andDo(print())
		.andExpect(status().isUnauthorized());
		
		// Test 2 get all question with token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/question").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isOk());
		
		// Test 3 Create new question
		this.mockMvc
		.perform(MockMvcRequestBuilders
				.post("/question")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(question))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isCreated())
		.andReturn();
		
		// Test 4 Delete another question with user privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/question/2").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isForbidden());
		
		// Test 5 Delete another question with admin privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/question/2").header("Authorization", "Bearer " + adminToken))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void testQuestionnaireSuccess() throws Exception {
		String token = null;
		String adminToken = null;
		JSONObject userObject = new JSONObject();
		JSONObject adminObject = new JSONObject();
		JSONObject tokenObject;
		JSONObject adminTokenObject;
		
		userObject.put("username", "NewAcc");
		userObject.put("password", "password");
		adminObject.put("username", "Salim");
		adminObject.put("password", "password");

		String user = userObject.toJSONString();
		String admin = adminObject.toJSONString();
		
		String questionnaireSuccess = "{ 'user' : { 'id': 1}, 'questionnaire' : { 'id' : 1 } }";
		questionnaireSuccess = questionnaireSuccess.replaceAll("'", "\"");
		
		
		// Authenticate to user
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(user))
				.andDo(MockMvcResultHandlers.print())
	            .andExpect(status().isOk())
	            .andReturn();
		
		token = result.getResponse().getContentAsString();
		tokenObject = (JSONObject) new JSONParser().parse(token);
		token = tokenObject.get("token").toString();
		
		// Authenticate to admin
		MvcResult adminResult = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(admin))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn();
				
		adminToken = adminResult.getResponse().getContentAsString();
		adminTokenObject = (JSONObject) new JSONParser().parse(adminToken);
		adminToken = adminTokenObject.get("token").toString();
		
		// Test 1 get all questionnaireSuccess without token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/questionnaireSuccess"))
		.andDo(print())
		.andExpect(status().isUnauthorized());
		
		// Test 2 get all questionnaireSuccess with token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/questionnaireSuccess").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isOk());
		
		// Test 3 Create new questionnaireSuccess
		this.mockMvc
		.perform(MockMvcRequestBuilders
				.post("/questionnaireSuccess")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(questionnaireSuccess))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isCreated())
		.andReturn();
		
		// Test 4 Delete another questionnaireSuccess with user privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/questionnaireSuccess/1").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isForbidden());
		
		// Test 5 Delete another questionnaireSuccess with admin privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/questionnaireSuccess/1").header("Authorization", "Bearer " + adminToken))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void testSurveyAnswer() throws Exception {
		String token = null;
		String adminToken = null;
		JSONObject userObject = new JSONObject();
		JSONObject adminObject = new JSONObject();
		JSONObject tokenObject;
		JSONObject adminTokenObject;
		
		userObject.put("username", "NewAcc");
		userObject.put("password", "password");
		adminObject.put("username", "Salim");
		adminObject.put("password", "password");

		String user = userObject.toJSONString();
		String admin = adminObject.toJSONString();
		
		String surveyAnswer = "{ 'user' : { 'id': 1}, 'surveyChoice' : { 'id' : 1 } }";
		surveyAnswer = surveyAnswer.replaceAll("'", "\"");
		
		
		// Authenticate to user
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(user))
				.andDo(MockMvcResultHandlers.print())
	            .andExpect(status().isOk())
	            .andReturn();
		
		token = result.getResponse().getContentAsString();
		tokenObject = (JSONObject) new JSONParser().parse(token);
		token = tokenObject.get("token").toString();
		
		// Authenticate to admin
		MvcResult adminResult = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(admin))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn();
				
		adminToken = adminResult.getResponse().getContentAsString();
		adminTokenObject = (JSONObject) new JSONParser().parse(adminToken);
		adminToken = adminTokenObject.get("token").toString();
		
		// Test 1 get all Survey Answer without token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/surveyAnswer"))
		.andDo(print())
		.andExpect(status().isUnauthorized());
		
		// Test 2 get all Survey Answer with token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/surveyAnswer").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isOk());
		
		// Test 3 Create new Survey Answer
		this.mockMvc
		.perform(MockMvcRequestBuilders
				.post("/surveyAnswer")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(surveyAnswer))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isCreated())
		.andReturn();
		
		// Test 4 Delete another Survey Answer with admin privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/surveyAnswer/1").header("Authorization", "Bearer " + adminToken))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void testSurveyChoice() throws Exception {
		String token = null;
		String adminToken = null;
		JSONObject userObject = new JSONObject();
		JSONObject adminObject = new JSONObject();
		JSONObject surveyChoiceObject = new JSONObject();
		JSONObject tokenObject;
		JSONObject adminTokenObject;
		
		userObject.put("username", "NewAcc");
		userObject.put("password", "password");
		adminObject.put("username", "Salim");
		adminObject.put("password", "password");
		
		surveyChoiceObject.put("content", "Premier choix du sondage 3");
		surveyChoiceObject.put("placement", 1);
		surveyChoiceObject.put("survey_id", 3);

		String user = userObject.toJSONString();
		String admin = adminObject.toJSONString();
		String surveyChoice = surveyChoiceObject.toJSONString();
		
		// Authenticate to user
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(user))
				.andDo(MockMvcResultHandlers.print())
	            .andExpect(status().isOk())
	            .andReturn();
		
		token = result.getResponse().getContentAsString();
		tokenObject = (JSONObject) new JSONParser().parse(token);
		token = tokenObject.get("token").toString();
		
		// Authenticate to admin
		MvcResult adminResult = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(admin))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn();
				
		adminToken = adminResult.getResponse().getContentAsString();
		adminTokenObject = (JSONObject) new JSONParser().parse(adminToken);
		adminToken = adminTokenObject.get("token").toString();
		
		// Test 1 get all Survey Choice without token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/surveyChoice"))
		.andDo(print())
		.andExpect(status().isUnauthorized());
		
		// Test 2 get all Survey Choice with token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/surveyChoice").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isOk());
		
		// Test 3 Create new Survey Choice
		this.mockMvc
		.perform(MockMvcRequestBuilders
				.post("/surveyChoice")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(surveyChoice))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isCreated())
		.andReturn();
		
		// Test 4 Delete another Survey Choice with user privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/surveyChoice/2").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isForbidden());
		
		// Test 5 Delete another Survey Choice with admin privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/surveyChoice/2").header("Authorization", "Bearer " + adminToken))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void testSurvey() throws Exception {
		String token = null;
		String adminToken = null;
		JSONObject userObject = new JSONObject();
		JSONObject adminObject = new JSONObject();
		JSONObject surveyObject = new JSONObject();
		JSONObject tokenObject;
		JSONObject adminTokenObject;
		
		userObject.put("username", "NewAcc");
		userObject.put("password", "password");
		adminObject.put("username", "Salim");
		adminObject.put("password", "password");
		
		surveyObject.put("title", "Nouveau sondage");
		surveyObject.put("description", "Nouveau sondage non premium");
		surveyObject.put("active_date", 20);
		surveyObject.put("premium", 0);

		String user = userObject.toJSONString();
		String admin = adminObject.toJSONString();
		String survey = surveyObject.toJSONString();
		
		// Authenticate to user
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(user))
				.andDo(MockMvcResultHandlers.print())
	            .andExpect(status().isOk())
	            .andReturn();
		
		token = result.getResponse().getContentAsString();
		tokenObject = (JSONObject) new JSONParser().parse(token);
		token = tokenObject.get("token").toString();
		
		// Authenticate to admin
		MvcResult adminResult = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(admin))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn();
				
		adminToken = adminResult.getResponse().getContentAsString();
		adminTokenObject = (JSONObject) new JSONParser().parse(adminToken);
		adminToken = adminTokenObject.get("token").toString();
		
		// Test 1 get all survey without token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/survey"))
		.andDo(print())
		.andExpect(status().isUnauthorized());
		
		// Test 2 get all survey with token
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/survey").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isOk());
		
		// Test 3 Create new survey
		this.mockMvc
		.perform(MockMvcRequestBuilders
				.post("/survey")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(survey))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isCreated())
		.andReturn();
		
		// Test 4 Delete another survey with user privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/survey/2").header("Authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isForbidden());
		
		// Test 5 Delete another survey with admin privilege
		this.mockMvc
		.perform(MockMvcRequestBuilders.delete("/survey/2").header("Authorization", "Bearer " + adminToken))
		.andDo(print())
		.andExpect(status().isOk());
	}
}
