package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IncludedItem
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.Gray500
import com.example.ui.theme.Gray900
import com.example.ui.theme.White
import kotlin.math.roundToInt

@Composable
fun PedestalView(
  avatarRes: Int,
  selectedItem: IncludedItem?,
  onRentClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var rotationAngle by remember { mutableFloatStateOf(0f) }
  var isDragging by remember { mutableStateOf(false) }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(340.dp)
      .pointerInput(Unit) {
        detectHorizontalDragGestures(
          onDragStart = { isDragging = true },
          onDragEnd = { isDragging = false },
          onDragCancel = { isDragging = false },
          onHorizontalDrag = { _, dragAmount ->
            // Smooth horizontal 3D rotation
            rotationAngle = (rotationAngle + dragAmount * 0.45f)
          }
        )
      },
    contentAlignment = Alignment.BottomCenter
  ) {
    // Soft Pedestal Base Shadow & Circular White Pedestal
    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 12.dp)
        .width(220.dp)
        .height(36.dp)
        .shadow(elevation = 14.dp, shape = CircleShape, spotColor = Color(0x33000000))
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFFFFFFF), Color(0xFFE5E7EB))
          ),
          shape = CircleShape
        )
        .border(width = 1.dp, color = Color(0xFFD1D5DB), shape = CircleShape)
    )

    // Secondary subtle rim for 3D depth
    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 26.dp)
        .width(180.dp)
        .height(20.dp)
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFFAFAFA), Color(0xFFF3F4F6))
          ),
          shape = CircleShape
        )
    )

    // Character Container with 3D Perspective Rotation
    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 22.dp)
        .width(230.dp)
        .height(310.dp)
        .graphicsLayer {
          rotationY = rotationAngle
          cameraDistance = 16f * density
        },
      contentAlignment = Alignment.BottomCenter
    ) {
      Image(
        painter = painterResource(id = avatarRes),
        contentDescription = "Avatar no pedestal",
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize()
      )
    }

    // 3D Interactive rotation hint indicator
    Surface(
      color = Color(0x99000000),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier
        .align(Alignment.TopStart)
        .padding(start = 16.dp, top = 12.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Refresh,
          contentDescription = "Girar 3D",
          tint = White,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = if (isDragging) "Girando..." else "Arraste para girar 3D",
          color = White,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium
        )
      }
    }

    // Item Equipped Floating Feedback Badge
    AnimatedVisibility(
      visible = selectedItem != null,
      enter = scaleIn() + fadeIn(),
      exit = scaleOut() + fadeOut(),
      modifier = Modifier
        .align(Alignment.TopCenter)
        .padding(top = 8.dp)
    ) {
      Surface(
        color = Color(0xFFEFF6FF),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BluePrimary),
        shadowElevation = 4.dp
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Icon(
            imageVector = Icons.Rounded.AutoAwesome,
            contentDescription = null,
            tint = BluePrimary,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "${selectedItem?.name} equipado!",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = BluePrimary
          )
        }
      }
    }

    // Floating "Alugar" Button on the right of the character (EXACTLY AS IN SCREENSHOT)
    Button(
      onClick = onRentClick,
      colors = ButtonDefaults.buttonColors(
        containerColor = BluePrimary,
        contentColor = White
      ),
      shape = RoundedCornerShape(14.dp),
      elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .padding(end = 16.dp, top = 20.dp)
        .height(48.dp)
        .testTag("pedestal_rent_button")
    ) {
      Icon(
        imageVector = Icons.Default.ShoppingBag,
        contentDescription = "Alugar",
        tint = White,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "Alugar",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = White
      )
    }
  }
}
