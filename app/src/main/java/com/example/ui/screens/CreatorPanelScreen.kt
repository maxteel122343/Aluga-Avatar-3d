package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.SharedAccount
import com.example.data.repository.AccountRepository
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.Gray100
import com.example.ui.theme.Gray200
import com.example.ui.theme.Gray50
import com.example.ui.theme.Gray500
import com.example.ui.theme.Gray700
import com.example.ui.theme.Gray900
import com.example.ui.theme.GreenIcon
import com.example.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorPanelScreen(
  repository: AccountRepository,
  onAccountCreated: (SharedAccount) -> Unit
) {
  val context = LocalContext.current

  var title by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Aventura & RPG") }
  var description by remember { mutableStateOf("") }
  var basePriceText by remember { mutableStateOf("29.90") }
  var maxPeople by remember { mutableIntStateOf(4) }
  var selectedAvatarRes by remember { mutableIntStateOf(R.drawable.avatar_character_pedestal) }

  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }

  val avatarOptions = listOf(
    Pair("Aventureiro 3D", R.drawable.avatar_character_pedestal),
    Pair("Gamer Neon", R.drawable.char_gamer),
    Pair("Cyber Robot", R.drawable.char_cyber)
  )

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Painel do Criador",
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = Gray900
            )
            Text(
              text = "Publicar novo card de conta compartilhada",
              fontSize = 12.sp,
              color = Gray500
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
      )
    },
    containerColor = White
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState())
        .padding(bottom = 80.dp)
    ) {
      Spacer(modifier = Modifier.height(10.dp))

      // Section 1: 3D Avatar Selection on Pedestal
      Text(
        text = "1. Escolha o Modelo 3D no Pedestal",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = Gray900
      )
      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        avatarOptions.forEach { (name, res) ->
          val isSelected = selectedAvatarRes == res
          Column(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(14.dp))
              .background(if (isSelected) Color(0xFFEFF6FF) else Gray50)
              .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) BluePrimary else Gray200,
                shape = RoundedCornerShape(14.dp)
              )
              .clickable { selectedAvatarRes = res }
              .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(10.dp)),
              contentAlignment = Alignment.Center
            ) {
              Image(
                painter = painterResource(id = res),
                contentDescription = name,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = name,
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) BluePrimary else Gray700
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Section 2: Account Details
      Text(
        text = "2. Dados da Conta Compartilhada",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = Gray900
      )
      Spacer(modifier = Modifier.height(8.dp))

      OutlinedTextField(
        value = title,
        onValueChange = { title = it },
        label = { Text("Nome da Conta / Passe") },
        placeholder = { Text("Ex: Aventureiro Deluxe Pass") },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("creator_title_input"),
        shape = RoundedCornerShape(12.dp)
      )

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = description,
        onValueChange = { description = it },
        label = { Text("Descrição dos benefícios") },
        placeholder = { Text("Descreva skins, vantagens e recursos incluídos...") },
        modifier = Modifier
          .fillMaxWidth()
          .height(90.dp)
          .testTag("creator_desc_input"),
        shape = RoundedCornerShape(12.dp),
        maxLines = 3
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = basePriceText,
          onValueChange = { basePriceText = it },
          label = { Text("Preço Base / Dia (R$)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
          value = maxPeople.toString(),
          onValueChange = { it.toIntOrNull()?.let { v -> maxPeople = v } },
          label = { Text("Limite Pessoas") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(12.dp)
        )
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Section 3: Credenciais de Entrega Automática
      Text(
        text = "3. Credenciais de Acesso (Entrega Automática)",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = Gray900
      )
      Text(
        text = "O sistema enviará esses dados automaticamente via webhook após o pagamento PIX.",
        fontSize = 12.sp,
        color = Gray500
      )
      Spacer(modifier = Modifier.height(8.dp))

      OutlinedTextField(
        value = username,
        onValueChange = { username = it },
        label = { Text("Usuário / Login") },
        placeholder = { Text("usuario_vip_2026") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
      )

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Senha da Conta") },
        placeholder = { Text("SenhaForte@998") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Submit Button
      Button(
        onClick = {
          if (title.isBlank()) {
            Toast.makeText(context, "Por favor insira o nome da conta", Toast.LENGTH_SHORT).show()
            return@Button
          }
          val price = basePriceText.toDoubleOrNull() ?: 29.90
          val created = repository.createAccount(
            title = title,
            category = category,
            description = description,
            basePrice = price,
            maxPeople = maxPeople,
            avatarRes = selectedAvatarRes,
            username = username,
            pass = password
          )
          Toast.makeText(context, "Card criado e publicado com sucesso no Feed!", Toast.LENGTH_LONG).show()
          onAccountCreated(created)
        },
        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary, contentColor = White),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("publish_card_button")
      ) {
        Icon(imageVector = Icons.Default.Publish, contentDescription = null, tint = White, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Publicar Card no Feed", fontSize = 15.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}
