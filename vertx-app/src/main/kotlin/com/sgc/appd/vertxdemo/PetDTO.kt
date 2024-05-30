package com.sgc.appd.vertxdemo

import jakarta.persistence.*

@Entity
/*@Table(name = "petsdb")*/
data class PetDTO(
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column
    val id: Long? = null,
    @Column
    val name: String? = null,
    @Column
    val type: PetEnum? = null
)