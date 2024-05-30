package com.sgc.appd.vertxdemo

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.vertx.core.AbstractVerticle
import mu.KotlinLogging
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class PetService(
    private val sessionFactory: Mutiny.SessionFactory
) : AbstractVerticle() {

    private val logger = KotlinLogging.logger {}

    fun getAllPets(): Uni<List<PetDTO>> {
        logger.info { "operation=getAllPetsBegin, layer=service" }
        val criteriaBuilder = sessionFactory.criteriaBuilder
        val query = criteriaBuilder.createQuery(PetDTO::class.java)
        //for some reason, root query must be specified, even if not unused
        val root = query.from(PetDTO::class.java)
        return sessionFactory.withTransaction { session ->
            logger.info { "operation=getAllPetsBegin, layer=hibernate" }
            session.createQuery(query).resultList
                .onItem().ifNull().continueWith{listOf()}
                .onItem().call(session::flush)
                .also {
                    logger.info { "operation=getAllPetsEnd, layer=hibernate, result=complete" }
                }
        }.also {
            logger.info { "operation=getAllPetsEnd, layer=service" }
        }
    }

    fun savePet(pet: PetDTO): Uni<Void> {
        logger.info { "operation=savePetBegin, layer=service pet=$pet" }
        return sessionFactory.withTransaction { session ->
            logger.info { "operation=savePetBegin, layer=hibernate" }
            session.persist(pet)
                .onItem().ignore().andContinueWithNull()
                .onItem().call(session::flush)
                .also {
                    logger.info { "operation=savePetEnd, layer=hibernate, result=complete" }
                }
        }.also {
            logger.info { "operation=savePetEnd, layer=service" }
        }
    }

    fun deleteAllPets(): Uni<List<PetDTO>> {
        logger.info { "operation=deleteAllPetsBegin, layer=service" }
        val criteriaBuilder = sessionFactory.criteriaBuilder
        val query = criteriaBuilder.createQuery(PetDTO::class.java)
        //for some reason, root query must be specified, even if not unused
        val root = query.from(PetDTO::class.java)
        return sessionFactory.withTransaction { session ->
            logger.info { "operation=deleteAllPetsBegin, layer=hibernate" }
            session.createQuery(query).resultList
                .onItem()
                // here we create Multi from Uni<List>
                .transformToMulti<PetDTO> { iterable: List<PetDTO> ->
                    Multi.createFrom().iterable(iterable)
                }
                .onItem()
                // delete each item and then transform it from Multi to Uni
                .transformToUniAndMerge<PetDTO> { toDelete: PetDTO ->
                    logger.info { "operation=deletePet, layer=hibernate, itemToDelete=$toDelete" }
                    // this is probably the worst way how to perform delete all, but might be suitable for some filtered operations
                    session.remove(toDelete).await().atMost(Duration.ofMillis(3000))
                    Uni.createFrom().item(toDelete)
                }
                // collect back to Uni<List>
                .collect().asList()
                .onItem().call(session::flush)
                .also {
                    logger.info { "operation=deleteAllPetsEnd, layer=hibernate, result=complete" }
                }
        }.also {
            logger.info { "operation=deleteAllPetsEnd, layer=service" }
        }
    }
}