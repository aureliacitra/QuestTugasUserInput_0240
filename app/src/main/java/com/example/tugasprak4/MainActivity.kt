package com.example.tugasprak4

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                BiodataScreen()
            }
        }
    }
}

/** Helper: ukuran font dari dimens.xml (dimen sp -> TextUnit) tanpa hardcode. */
@Composable
private fun spResource(id: Int): TextUnit =
    TextUnit(dimensionResource(id).value, TextUnitType.Sp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiodataScreen() {
    val context = LocalContext.current
    // ---- State form ----
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    val selectedSkills = remember { mutableStateListOf<String>() }

    val isFormValid = firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            email.isNotBlank() &&
            birthDate.isNotBlank() &&
            experience.isNotBlank() &&
            selectedSkills.isNotEmpty()

    // ---- Gambar banner (img.png di res/drawable) ----
    val banners = listOf(
        R.drawable.img,
        R.drawable.img,
        R.drawable.img,
        R.drawable.img
    )
    val pagerState = rememberPagerState(pageCount = { banners.size })
    // Box terluar: Scaffold di lapisan bawah, Box form "mengapung" di atasnya
    Box(modifier = Modifier.fillMaxSize()) {

        // ===== LAPISAN BAWAH: Scaffold + carousel gambar =====
        Scaffold(
            containerColor = colorResource(R.color.screen_background)
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.banner_height))
            ) {
                HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                    Image(
                        painter = painterResource(banners[page]),
                        contentDescription = stringResource(R.string.cd_banner),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                // Indikator titik
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(dimensionResource(R.dimen.dot_padding)),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dot_spacing))
                ) {
                    repeat(banners.size) { index ->
                        val dotColor = if (pagerState.currentPage == index)
                            colorResource(R.color.dot_active)
                        else
                            colorResource(R.color.dot_inactive)
                        Box(
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.dot_size))
                                .clip(CircleShape)
                                .background(dotColor)
                        )
                    }
                }
            }
        }        // ===== LAPISAN ATAS: Box form mengapung di atas Scaffold =====
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = dimensionResource(R.dimen.card_top_offset)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = dimensionResource(R.dimen.card_corner),
                            topEnd = dimensionResource(R.dimen.card_corner)
                        )
                    )
                    .background(colorResource(R.color.card_background))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(dimensionResource(R.dimen.card_padding)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = stringResource(R.string.form_title),
                        color = colorResource(R.color.purple_title),
                        fontSize = spResource(R.dimen.title_size),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.spacing_small)))
                    Text(
                        text = stringResource(R.string.form_subtitle),
                        color = colorResource(R.color.text_secondary),
                        fontSize = spResource(R.dimen.subtitle_size),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.spacing_large)))


