package com.example.practicanavegacionpantallas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

private val ColoresRutaNica = lightColorScheme(
    primary = Color(0xFF006D5B),
    secondary = Color(0xFFFFA726),
    tertiary = Color(0xFF0077B6),
    background = Color(0xFFF5F7F2),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color(0xFF2B1A00),
    onBackground = Color(0xFF1F2923),
    onSurface = Color(0xFF1F2923)
)

sealed class Pantalla(
    val ruta: String,
    val etiqueta: String,
    val icono: String
) {
    object Inicio : Pantalla("inicio", "Inicio", "🏠")
    object Destinos : Pantalla("destinos", "Destinos", "🧭")
    object MiRuta : Pantalla("mi_ruta", "Mi ruta", "🎒")
}

data class Destino(
    val id: String,
    val nombre: String,
    val ubicacion: String,
    val descripcion: String,
    val duracion: String,
    val precio: String,
    val precioNumero: Int,
    val calificacion: String,
    val etiquetas: List<String>
)

val destinosRutaNica = listOf(
    Destino(
        id = "laguna_apoyo",
        nombre = "Laguna de Apoyo",
        ubicacion = "Masaya",
        descripcion = "Ideal para nadar, descansar, tomar fotos y pasar un día tranquilo rodeado de naturaleza.",
        duracion = "1 día",
        precio = "Desde C$350",
        precioNumero = 350,
        calificacion = "4.8",
        etiquetas = listOf("Naturaleza", "Relajante", "Familiar")
    ),
    Destino(
        id = "volcan_masaya",
        nombre = "Volcán Masaya",
        ubicacion = "Masaya",
        descripcion = "Una experiencia perfecta para observar el cráter activo y vivir una aventura diferente.",
        duracion = "3 horas",
        precio = "Desde C$250",
        precioNumero = 250,
        calificacion = "4.9",
        etiquetas = listOf("Aventura", "Nocturno", "Volcán")
    ),
    Destino(
        id = "granada_colonial",
        nombre = "Granada Colonial",
        ubicacion = "Granada",
        descripcion = "Un recorrido por calles históricas, arquitectura colonial, gastronomía local y cultura nicaragüense.",
        duracion = "1 día",
        precio = "Desde C$500",
        precioNumero = 500,
        calificacion = "4.7",
        etiquetas = listOf("Cultura", "Historia", "Ciudad")
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RutaNicaApp()
        }
    }
}

@Composable
fun RutaNicaApp() {
    val navController = rememberNavController()

    var destinoSeleccionadoId by rememberSaveable {
        mutableStateOf(destinosRutaNica.first().id)
    }

    var favoritosTexto by rememberSaveable {
        mutableStateOf("")
    }

    var recordatorioActivo by rememberSaveable {
        mutableStateOf(false)
    }

    var rutaConfirmada by rememberSaveable {
        mutableStateOf(false)
    }

    var mensajeEstado by rememberSaveable {
        mutableStateOf("Bienvenido a Ruta Nica. Elegí un destino para comenzar tu recorrido.")
    }

    val favoritos = favoritosTexto
        .split("|")
        .filter { it.isNotBlank() }
        .toSet()

    val destinoSeleccionado = obtenerDestinoPorId(destinoSeleccionadoId)

    fun guardarFavoritos(nuevosFavoritos: Set<String>) {
        favoritosTexto = nuevosFavoritos.joinToString("|")
    }

    fun seleccionarDestino(destino: Destino) {
        destinoSeleccionadoId = destino.id
        rutaConfirmada = false
        mensajeEstado = "Seleccionaste ${destino.nombre} como destino principal."
    }

    fun alternarFavorito(destino: Destino) {
        val nuevosFavoritos = favoritos.toMutableSet()

        if (destino.id in nuevosFavoritos) {
            nuevosFavoritos.remove(destino.id)
            mensajeEstado = "Quitaste ${destino.nombre} de tus favoritos."
        } else {
            nuevosFavoritos.add(destino.id)
            mensajeEstado = "Agregaste ${destino.nombre} a tus favoritos."
        }

        guardarFavoritos(nuevosFavoritos)
    }

    fun sugerirDestinoEconomico() {
        val destinoEconomico = destinosRutaNica.minBy { it.precioNumero }
        destinoSeleccionadoId = destinoEconomico.id
        rutaConfirmada = false
        mensajeEstado = "Te sugerimos ${destinoEconomico.nombre} porque es el plan más económico."
    }

    fun confirmarRuta() {
        rutaConfirmada = true
        mensajeEstado = "Tu ruta hacia ${destinoSeleccionado.nombre} fue confirmada correctamente."
    }

    fun limpiarFavoritos() {
        favoritosTexto = ""
        mensajeEstado = "Se limpiaron todos tus destinos favoritos."
    }

    fun cambiarRecordatorio(activado: Boolean) {
        recordatorioActivo = activado
        mensajeEstado = if (activado) {
            "Recordatorio activado para tu visita a ${destinoSeleccionado.nombre}."
        } else {
            "Recordatorio desactivado."
        }
    }

    MaterialTheme(
        colorScheme = ColoresRutaNica
    ) {
        Scaffold(
            bottomBar = {
                BarraInferiorRutaNica(navController = navController)
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Pantalla.Inicio.ruta,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Pantalla.Inicio.ruta) {
                    PantallaInicio(
                        destinoSeleccionado = destinoSeleccionado,
                        cantidadFavoritos = favoritos.size,
                        mensajeEstado = mensajeEstado,
                        irADestinos = {
                            navController.navigate(Pantalla.Destinos.ruta) {
                                launchSingleTop = true
                            }
                        },
                        irAMiRuta = {
                            navController.navigate(Pantalla.MiRuta.ruta) {
                                launchSingleTop = true
                            }
                        },
                        elegirRecomendado = {
                            seleccionarDestino(destinosRutaNica.first())
                        },
                        sugerirEconomico = {
                            sugerirDestinoEconomico()
                        }
                    )
                }

                composable(Pantalla.Destinos.ruta) {
                    PantallaDestinos(
                        destinoSeleccionado = destinoSeleccionado,
                        favoritos = favoritos,
                        mensajeEstado = mensajeEstado,
                        onSeleccionarDestino = { destino ->
                            seleccionarDestino(destino)
                        },
                        onAlternarFavorito = { destino ->
                            alternarFavorito(destino)
                        },
                        irAMiRuta = {
                            navController.navigate(Pantalla.MiRuta.ruta) {
                                launchSingleTop = true
                            }
                        },
                        sugerirEconomico = {
                            sugerirDestinoEconomico()
                        }
                    )
                }

                composable(Pantalla.MiRuta.ruta) {
                    PantallaMiRuta(
                        destinoSeleccionado = destinoSeleccionado,
                        cantidadFavoritos = favoritos.size,
                        recordatorioActivo = recordatorioActivo,
                        rutaConfirmada = rutaConfirmada,
                        mensajeEstado = mensajeEstado,
                        onRecordatorioChange = { activado ->
                            cambiarRecordatorio(activado)
                        },
                        confirmarRuta = {
                            confirmarRuta()
                        },
                        limpiarFavoritos = {
                            limpiarFavoritos()
                        },
                        irADestinos = {
                            navController.navigate(Pantalla.Destinos.ruta) {
                                launchSingleTop = true
                            }
                        },
                        volverAInicio = {
                            navController.navigate(Pantalla.Inicio.ruta) {
                                popUpTo(Pantalla.Inicio.ruta) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    }
}

fun obtenerDestinoPorId(id: String): Destino {
    return destinosRutaNica.find { it.id == id } ?: destinosRutaNica.first()
}

@Composable
fun BarraInferiorRutaNica(
    navController: NavHostController
) {
    val pantallas = listOf(
        Pantalla.Inicio,
        Pantalla.Destinos,
        Pantalla.MiRuta
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        pantallas.forEach { pantalla ->
            NavigationBarItem(
                selected = rutaActual == pantalla.ruta,
                onClick = {
                    when (pantalla.ruta) {
                        Pantalla.Inicio.ruta -> {
                            navController.navigate(Pantalla.Inicio.ruta) {
                                popUpTo(Pantalla.Inicio.ruta) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }

                        Pantalla.Destinos.ruta -> {
                            navController.navigate(Pantalla.Destinos.ruta) {
                                popUpTo(Pantalla.Inicio.ruta) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }

                        Pantalla.MiRuta.ruta -> {
                            navController.navigate(Pantalla.MiRuta.ruta) {
                                popUpTo(Pantalla.Inicio.ruta) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                },
                icon = {
                    Text(
                        text = pantalla.icono,
                        fontSize = 22.sp
                    )
                },
                label = {
                    Text(text = pantalla.etiqueta)
                }
            )
        }
    }
}

@Composable
fun PantallaBase(
    titulo: String,
    subtitulo: String,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Column {
                Text(
                    text = titulo,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitulo,
                    fontSize = 15.sp,
                    color = Color(0xFF5A665F)
                )
            }

            contenido()
        }
    }
}

@Composable
fun PantallaInicio(
    destinoSeleccionado: Destino,
    cantidadFavoritos: Int,
    mensajeEstado: String,
    irADestinos: () -> Unit,
    irAMiRuta: () -> Unit,
    elegirRecomendado: () -> Unit,
    sugerirEconomico: () -> Unit
) {
    PantallaBase(
        titulo = "Ruta Nica",
        subtitulo = "Descubrí destinos, guardá favoritos y armá tu próxima salida."
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(245.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(0xFF006D5B),
                            Color(0xFF009688),
                            Color(0xFFFFB74D)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = "Destino actual",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = destinoSeleccionado.nombre,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${destinoSeleccionado.ubicacion} • ${destinoSeleccionado.duracion} • ${destinoSeleccionado.precio}",
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.95f)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = irADestinos,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF006D5B)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Explorar destinos",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TarjetaEstadistica(
                titulo = "Favoritos",
                valor = cantidadFavoritos.toString(),
                emoji = "⭐",
                modifier = Modifier.weight(1f)
            )

            TarjetaEstadistica(
                titulo = "Disponibles",
                valor = destinosRutaNica.size.toString(),
                emoji = "🧳",
                modifier = Modifier.weight(1f)
            )
        }

        TarjetaEstado(
            mensaje = mensajeEstado
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Acciones rápidas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Podés elegir un destino recomendado, buscar el plan más económico o revisar tu ruta actual.",
                    fontSize = 15.sp,
                    color = Color(0xFF5A665F)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = elegirRecomendado,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Elegir destino recomendado")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = sugerirEconomico,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Sugerir plan económico")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = irAMiRuta,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Ver mi ruta")
                }
            }
        }
    }
}

@Composable
fun PantallaDestinos(
    destinoSeleccionado: Destino,
    favoritos: Set<String>,
    mensajeEstado: String,
    onSeleccionarDestino: (Destino) -> Unit,
    onAlternarFavorito: (Destino) -> Unit,
    irAMiRuta: () -> Unit,
    sugerirEconomico: () -> Unit
) {
    PantallaBase(
        titulo = "Destinos",
        subtitulo = "Elegí el lugar que querés visitar y agregá favoritos."
    ) {
        TarjetaEstado(
            mensaje = mensajeEstado
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = sugerirEconomico,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Más económico")
            }

            OutlinedButton(
                onClick = irAMiRuta,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Mi ruta")
            }
        }

        destinosRutaNica.forEach { destino ->
            TarjetaDestino(
                destino = destino,
                seleccionado = destino.id == destinoSeleccionado.id,
                esFavorito = destino.id in favoritos,
                onSeleccionar = {
                    onSeleccionarDestino(destino)
                },
                onAlternarFavorito = {
                    onAlternarFavorito(destino)
                },
                irAMiRuta = irAMiRuta
            )
        }
    }
}

@Composable
fun PantallaMiRuta(
    destinoSeleccionado: Destino,
    cantidadFavoritos: Int,
    recordatorioActivo: Boolean,
    rutaConfirmada: Boolean,
    mensajeEstado: String,
    onRecordatorioChange: (Boolean) -> Unit,
    confirmarRuta: () -> Unit,
    limpiarFavoritos: () -> Unit,
    irADestinos: () -> Unit,
    volverAInicio: () -> Unit
) {
    PantallaBase(
        titulo = "Mi ruta",
        subtitulo = "Revisá tu destino, tus favoritos y el estado de tu salida."
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(86.dp)
                        .height(86.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFE0B2)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎒",
                        fontSize = 42.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Viajero Nica",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = if (rutaConfirmada) "Ruta confirmada" else "Ruta pendiente de confirmar",
                    fontSize = 14.sp,
                    color = if (rutaConfirmada) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color(0xFF6B756E)
                    },
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(22.dp))

                Separador()

                Spacer(modifier = Modifier.height(18.dp))

                InfoPerfil(
                    titulo = "Destino seleccionado",
                    valor = destinoSeleccionado.nombre
                )

                InfoPerfil(
                    titulo = "Ubicación",
                    valor = destinoSeleccionado.ubicacion
                )

                InfoPerfil(
                    titulo = "Duración estimada",
                    valor = destinoSeleccionado.duracion
                )

                InfoPerfil(
                    titulo = "Precio estimado",
                    valor = destinoSeleccionado.precio
                )

                InfoPerfil(
                    titulo = "Favoritos guardados",
                    valor = cantidadFavoritos.toString()
                )
            }
        }

        TarjetaEstado(
            mensaje = mensajeEstado
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
            )
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = recordatorioActivo,
                    onCheckedChange = onRecordatorioChange
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "Recordatorio de salida",
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (recordatorioActivo) {
                            "Te recordaremos revisar tu ruta antes de salir."
                        } else {
                            "Marcá esta opción si querés activar el aviso."
                        },
                        fontSize = 13.sp,
                        color = Color(0xFF5A665F)
                    )
                }
            }
        }

        Button(
            onClick = confirmarRuta,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = if (rutaConfirmada) "Ruta ya confirmada" else "Confirmar mi ruta",
                modifier = Modifier.padding(6.dp)
            )
        }

        OutlinedButton(
            onClick = irADestinos,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Cambiar destino",
                modifier = Modifier.padding(6.dp)
            )
        }

        if (cantidadFavoritos > 0) {
            OutlinedButton(
                onClick = limpiarFavoritos,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Limpiar favoritos",
                    modifier = Modifier.padding(6.dp)
                )
            }
        }

        OutlinedButton(
            onClick = volverAInicio,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Volver al inicio",
                modifier = Modifier.padding(6.dp)
            )
        }
    }
}

@Composable
fun TarjetaDestino(
    destino: Destino,
    seleccionado: Boolean,
    esFavorito: Boolean,
    onSeleccionar: () -> Unit,
    onAlternarFavorito: () -> Unit,
    irAMiRuta: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        border = if (seleccionado) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else {
            null
        },
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionado) {
                Color(0xFFE0F2F1)
            } else {
                Color.White
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = destino.nombre,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = destino.ubicacion,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = "⭐ ${destino.calificacion}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = destino.descripcion,
                fontSize = 15.sp,
                color = Color(0xFF5A665F)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                destino.etiquetas.take(2).forEach { etiqueta ->
                    EtiquetaChip(texto = etiqueta)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = destino.duracion,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4E5A53)
                )

                Text(
                    text = destino.precio,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onSeleccionar,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (seleccionado) "Elegido" else "Elegir"
                    )
                }

                OutlinedButton(
                    onClick = onAlternarFavorito,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (esFavorito) "Quitar ⭐" else "Favorito"
                    )
                }
            }

            if (seleccionado) {
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = irAMiRuta,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Ver este destino en mi ruta")
                }
            }
        }
    }
}

@Composable
fun TarjetaEstadistica(
    titulo: String,
    valor: String,
    emoji: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = emoji,
                fontSize = 26.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = valor,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = titulo,
                fontSize = 13.sp,
                color = Color(0xFF6B756E)
            )
        }
    }
}

@Composable
fun TarjetaEstado(
    mensaje: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF8E1)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "💬",
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = mensaje,
                fontSize = 14.sp,
                color = Color(0xFF5D4600),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun EtiquetaChip(
    texto: String
) {
    Surface(
        shape = RoundedCornerShape(50.dp),
        color = Color(0xFFFFF3E0)
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 13.sp,
            color = Color(0xFF7A4B00),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun InfoPerfil(
    titulo: String,
    valor: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = titulo,
            fontSize = 13.sp,
            color = Color(0xFF6B756E)
        )

        Text(
            text = valor,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun Separador() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0xFFE0E0E0))
    )
}