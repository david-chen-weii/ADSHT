package com.thur.demo.model.post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/*
 * @Getter
 * 
 * @Setter
 * 
 * @NoArgsConstructor
 * 
 * @Entity
 * 
 * @Table(name = "like")
 * public class Like {
 * 
 * @Id
 * private Integer id;
 * 
 * @MapsId
 * 
 * @OneToOne
 * 
 * @JoinColumn(name = "LikeId")
 * private Post post;
 * }
 */