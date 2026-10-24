/**
 * Archivo: CharacterListItem.kt
 * 
 * Propósito:
 * Composable reutilizable que muestra un item individual en la lista de personajes.
 * Representa cada tarjeta con imagen, nombre y estado/especie.
 * 
 * Componentes usados:
 * - Card: Contenedor con elevación y bordes redondeados
 * - Row: Distribución horizontal (imagen | texto)
 * - AsyncImage (Coil): Carga imágenes asíncronas con caché automático
 * - clickable: Hace toda la tarjeta clickeable
 * 
 * Coil: Librería eficiente para cargar imágenes en Compose con soporte para caché,
 * placeholders, transformaciones, etc.
 */
package com.irv205.rickmortycodingchallengecompose.presentation.character.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.irv205.rickmortycodingchallengecompose.domain.model.Character
import com.irv205.rickmortycodingchallengecompose.ui.theme.RickMortyCodingChallengeComposeTheme

/**
 * Item de lista que muestra información resumida de un personaje.
 * 
 * @param character Datos del personaje a mostrar
 * @param onClick Callback ejecutado al hacer click en el item
 * @param modifier Modificador opcional para personalizar desde fuera
 */
@Composable
fun CharacterListItem(
    character: Character,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del personaje cargada con Coil
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            // Información textual del personaje
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${character.status} - ${character.species}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Preview para visualizar el item en modo claro
 */
@Preview(showBackground = true)
@Composable
fun CharacterListItemPreview() {
    RickMortyCodingChallengeComposeTheme {
        CharacterListItem(
            character = Character(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                originName = "Earth (C-137)",
                locationName = "Citadel of Ricks",
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                created = "2017-11-04T18:48:46.250Z"
            ),
            onClick = {}
        )
    }
}

/**
 * Preview para visualizar el item en modo oscuro
 */
@Preview(showBackground = true, name = "Dark")
@Composable
fun CharacterListItemDarkPreview() {
    RickMortyCodingChallengeComposeTheme(darkTheme = true) {
        CharacterListItem(
            character = Character(
                id = 2,
                name = "Morty Smith",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                originName = "unknown",
                locationName = "Citadel of Ricks",
                image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                created = "2017-11-04T18:50:21.651Z"
            ),
            onClick = {}
        )
    }
}
