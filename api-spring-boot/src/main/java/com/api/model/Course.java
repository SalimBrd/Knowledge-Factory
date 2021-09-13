package com.api.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Course {

	public Course() {
		this.setDifficulty(Difficulty.EASY);
	}
	
	public Course(Integer id) {
		this.id = id;
		this.setDifficulty(Difficulty.EASY);
	}
	
	public enum Difficulty {
		EASY, MEDIUM, HARD
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length=100)
	private String title;
	
	@Column(nullable = false)
	private String suggestedHours;
	
	@Enumerated(EnumType.STRING)
	private Difficulty difficulty;
	
	private Boolean premium;
	
	@JsonManagedReference(value="course-questionnaire")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
	private List<Questionnaire> questionnaires;
	
	@JsonManagedReference(value="course-diploma")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
	private List<Diploma> diplomas;
	
	private Date creationDate;
	
	private Date updatedDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSuggestedHours() {
		return suggestedHours;
	}

	public void setSuggestedHours(String suggestedHours) {
		this.suggestedHours = suggestedHours;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public Boolean isPremium() {
		return premium;
	}

	public void setPremium(Boolean premium) {
		this.premium = premium;
	}

	public List<Questionnaire> getQuestionnaires() {
		return questionnaires;
	}

	public void setQuestionnaires(List<Questionnaire> questionnaires) {
		this.questionnaires = questionnaires;
	}

	public List<Diploma> getDiplomas() {
		return diplomas;
	}

	public void setDiplomas(List<Diploma> diplomas) {
		this.diplomas = diplomas;
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
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((difficulty == null) ? 0 : difficulty.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (premium ? 1231 : 1237);
		result = prime * result + ((suggestedHours == null) ? 0 : suggestedHours.hashCode());
		result = prime * result + ((updatedDate == null) ? 0 : updatedDate.hashCode());
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
		Course other = (Course) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (difficulty != other.difficulty)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (premium != other.premium)
			return false;
		if (suggestedHours == null) {
			if (other.suggestedHours != null)
				return false;
		} else if (!suggestedHours.equals(other.suggestedHours))
			return false;
		if (updatedDate == null) {
			if (other.updatedDate != null)
				return false;
		} else if (!updatedDate.equals(other.updatedDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", suggestedHours=" + suggestedHours + ", difficulty=" + difficulty + ", premium="
				+ premium + ", creationDate=" + creationDate + ", updatedDate=" + updatedDate + "]";
	}
	
}
