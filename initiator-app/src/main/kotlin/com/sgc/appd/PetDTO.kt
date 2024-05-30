package com.sgc.appd


/*@Table(name = "petsdb")*/
data class PetDTO(

    val id: Long? = null,
    val name: String? = null,
    val type: PetEnum? = null
)