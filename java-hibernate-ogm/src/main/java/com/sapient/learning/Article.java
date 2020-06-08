package com.sapient.learning;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Article {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String articleId;
	
	private String articleTitle;
	
	@ManyToOne
	private Author author;
	
	// constructors, getters and setters...
	
	Article() {
	}

	public Article(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	
	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		Article article = (Article) obj;
        return (article.articleId == this.articleId 
        		&& article.articleTitle == this.articleTitle);
	}

	@Override
	public int hashCode() {
		return this.articleTitle.hashCode();
	}
}