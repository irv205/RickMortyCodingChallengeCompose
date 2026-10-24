/**
 * Archivo: RepositoryModule.kt
 * 
 * Propósito:
 * Módulo de Hilt para vincular la interfaz del repositorio (Domain layer) con su implementación (Data layer).
 * 
 * ¿Por qué usar @Binds en lugar de @Provides?
 * - @Binds es más eficiente (no crea método, solo mapea interfaz -> implementación)
 * - Requiere que la clase sea abstracta o tener al menos un método abstracto
 * - Ideal cuando solo necesitamos "bindear" una interfaz a una clase concreta
 * 
 * Clean Architecture:
 * - Domain define la interfaz (AppRepository) - capa más interna, sin dependencias Android
 * - Data provee la implementación (AppRepositoryImpl) - conoce Retrofit, mappers, DTOs
 * - Esto permite invertir dependencias: Domain no depende de Data, Data depende de Domain
 * 
 * @Singleton: Una sola instancia del repositorio durante toda la vida de la app
 */
package com.irv205.rickmortycodingchallengecompose.core.di

import com.irv205.rickmortycodingchallengecompose.data.repository.AppRepositoryImpl
import com.irv205.rickmortycodingchallengecompose.domain.repository.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    /**
     * Vincula AppRepository (interfaz de Domain) con AppRepositoryImpl (implementación de Data).
     * Hilt usará AppRepositoryImpl cada vez que se solicite AppRepository.
     */
    @Binds
    @Singleton
    abstract fun bindAppRepository(
        appRepositoryImpl: AppRepositoryImpl
    ): AppRepository
}
