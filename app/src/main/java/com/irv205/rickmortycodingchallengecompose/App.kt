/**
 * Archivo: App.kt
 * 
 * Propósito:
 * Esta es la clase Application principal de la app. Es el punto de entrada a nivel
 * de aplicación Android antes de que cualquier Activity, Service o BroadcastReceiver
 * sea creado.
 * 
 * ¿Por qué es importante?
 * - Android crea una instancia de esta clase cuando la app inicia.
 * - Aquí es donde configuramos dependencias globales (como Hilt para inyección de dependencias).
 * 
 * Anotación @HiltAndroidApp:
 * - Genera el componente raíz (SingletonComponent) de Hilt para toda la aplicación.
 * - Hace que todas las clases anotadas con @AndroidEntryPoint puedan recibir dependencias.
 * - Es obligatorio tenerla para usar Hilt en cualquier proyecto Android.
 */
package com.irv205.rickmortycodingchallengecompose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application()
