package com.example.exam.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "animals")
class AnimalEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_seq")
    @SequenceGenerator(name = "users_user_id_seq", allocationSize = 1)
    @Column(name = "animal_id")
    var id: Long? = null,

    @Column(name = "animal_name")
    val name: String,

    @Column(name = "animal_type")
    val type: String,

    @Column(name = "animal_breed")
    val breed: String,

    @Column(name = "animal_added")
    val added: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "animal_alive")
    val alive: Boolean? = true,

    @Column(name = "animal_age")
    val age: Int,

    @Column(name = "animal_health")
    val health: Int? = 100
) {
    override fun toString(): String {
        return "AnimalEntity(id=$id, name='$name', type='$type', breed='$breed', added=$added, alive=$alive, age=$age and health=$health)"
    }

}

