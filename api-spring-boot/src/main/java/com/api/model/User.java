package com.api.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class User {

	public User() {
		this.setRole(UserRole.ROLE_USER);
	}
	
	public User(Integer id) {
		this.id = id;
		this.setRole(UserRole.ROLE_USER);
	}

	public User(String username, String password) {
		this.setRole(UserRole.ROLE_USER);
		this.username = username;
		this.password = password;
	}

	public enum UserRole {
		ROLE_USER, ROLE_SUBSCRIBER, ROLE_ADMIN
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	private UserRole role;

	private Date creationDate;

	private Date updatedDate;
	
	@JsonManagedReference(value="user-address")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Address> addresses;
	
	@JsonManagedReference(value="user-bookOrder")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<BookOrder> bookOrders;
	
	@JsonManagedReference(value="user-diploma")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Diploma> diplomas;
	
	@JsonManagedReference(value="user-surveyAnswer")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<SurveyAnswer> surveyAnswers;
	
	@JsonManagedReference(value="user-questionnaireSuccess")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<QuestionnaireSuccess> questionnaireSuccesses;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	
	public List<BookOrder> getBookOrders() {
		return bookOrders;
	}

	public void setBookOrders(List<BookOrder> bookOrders) {
		this.bookOrders = bookOrders;
	}

	public List<Diploma> getDiplomas() {
		return diplomas;
	}

	public void setDiplomas(List<Diploma> diplomas) {
		this.diplomas = diplomas;
	}

	public List<SurveyAnswer> getSurveyAnswers() {
		return surveyAnswers;
	}

	public void setSurveyAnswers(List<SurveyAnswer> surveyAnswers) {
		this.surveyAnswers = surveyAnswers;
	}

	public List<QuestionnaireSuccess> getQuestionnaireSuccesses() {
		return questionnaireSuccesses;
	}

	public void setQuestionnaireSuccesses(List<QuestionnaireSuccess> questionnaireSuccesses) {
		this.questionnaireSuccesses = questionnaireSuccesses;
	}

	public boolean add(Address address) {
		return addresses.add(address);
	}

	public boolean remove(Address address) {
		return addresses.remove(address);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@PrePersist
	protected void onCreate() {
		this.updatedDate = this.creationDate = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedDate = new Date();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((updatedDate == null) ? 0 : updatedDate.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (role != other.role)
			return false;
		if (updatedDate == null) {
			if (other.updatedDate != null)
				return false;
		} else if (!updatedDate.equals(other.updatedDate))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [Id=" + id + ", username=" + username + ", password=" + password + ", role=" + role
				+ ", creationDate=" + creationDate + ", updatedDate=" + updatedDate + "]";
	}

}
