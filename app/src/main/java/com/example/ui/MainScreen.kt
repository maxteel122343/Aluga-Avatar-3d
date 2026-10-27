package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SharedAccount
import com.example.data.repository.AccountRepository
import com.example.ui.screens.AccountDetailScreen
import com.example.ui.screens.CreatorPanelScreen
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.MyRentalsScreen
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.Gray500
import com.example.ui.theme.Gray900
import com.example.ui.theme.White

enum class ScreenTab {
  FEED,
  DETAIL,
  MY_RENTALS,
  CREATOR_PANEL
}

@Composable
fun MainScreen(
  repository: AccountRepository
) {
  val accounts by repository.accounts.collectAsState()
  val activeRentals by repository.activeRentals.collectAsState()

  var currentTab by remember { mutableStateOf(ScreenTab.DETAIL) }
  var selectedAccount by remember { mutableStateOf<SharedAccount?>(null) }

  // Initial account matches the screenshot
  val currentAccount = selectedAccount ?: accounts.firstOrNull() ?: return

  // Back handling: If on secondary tab, return to feed or detail
  BackHandler(enabled = currentTab != ScreenTab.FEED) {
    currentTab = ScreenTab.FEED
  }

  Scaffold(
    bottomBar = {
      NavigationBar(
        containerColor = White,
        tonalElevation = 6.dp
      ) {
        // Tab 1: Detalhe (O card exato da imagem)
        NavigationBarItem(
          selected = currentTab == ScreenTab.DETAIL,
          onClick = { currentTab = ScreenTab.DETAIL },
          icon = {
            Icon(
              imageVector = Icons.Default.ViewInAr,
              contentDescription = "Card Atual",
              modifier = Modifier.size(22.dp)
            )
          },
          label = { Text("Card 3D", fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = BluePrimary,
            selectedTextColor = BluePrimary,
            unselectedIconColor = Gray500,
            unselectedTextColor = Gray500,
            indicatorColor = White
          ),
          modifier = Modifier.testTag("nav_detail")
        )

        // Tab 2: Feed
        NavigationBarItem(
          selected = currentTab == ScreenTab.FEED,
          onClick = { currentTab = ScreenTab.FEED },
          icon = {
            Icon(
              imageVector = Icons.Default.ShoppingBag,
              contentDescription = "Feed",
              modifier = Modifier.size(22.dp)
            )
          },
          label = { Text("Explorar", fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = BluePrimary,
            selectedTextColor = BluePrimary,
            unselectedIconColor = Gray500,
            unselectedTextColor = Gray500,
            indicatorColor = White
          ),
          modifier = Modifier.testTag("nav_feed")
        )

        // Tab 3: Meus Aluguéis
        NavigationBarItem(
          selected = currentTab == ScreenTab.MY_RENTALS,
          onClick = { currentTab = ScreenTab.MY_RENTALS },
          icon = {
            BadgedBox(
              badge = {
                if (activeRentals.isNotEmpty()) {
                  Badge { Text("${activeRentals.size}") }
                }
              }
            ) {
              Icon(
                imageVector = Icons.Default.Key,
                contentDescription = "Meus Aluguéis",
                modifier = Modifier.size(22.dp)
              )
            }
          },
          label = { Text("Aluguéis", fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = BluePrimary,
            selectedTextColor = BluePrimary,
            unselectedIconColor = Gray500,
            unselectedTextColor = Gray500,
            indicatorColor = White
          ),
          modifier = Modifier.testTag("nav_rentals")
        )

        // Tab 4: Criar Card
        NavigationBarItem(
          selected = currentTab == ScreenTab.CREATOR_PANEL,
          onClick = { currentTab = ScreenTab.CREATOR_PANEL },
          icon = {
            Icon(
              imageVector = Icons.Default.AddCircle,
              contentDescription = "Criar Card",
              modifier = Modifier.size(22.dp)
            )
          },
          label = { Text("Criar Card", fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = BluePrimary,
            selectedTextColor = BluePrimary,
            unselectedIconColor = Gray500,
            unselectedTextColor = Gray500,
            indicatorColor = White
          ),
          modifier = Modifier.testTag("nav_creator")
        )
      }
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      when (currentTab) {
        ScreenTab.DETAIL -> {
          AccountDetailScreen(
            account = currentAccount,
            repository = repository,
            onNavigateBack = { currentTab = ScreenTab.FEED },
            onGoToRentals = { currentTab = ScreenTab.MY_RENTALS }
          )
        }

        ScreenTab.FEED -> {
          FeedScreen(
            repository = repository,
            onSelectAccount = { account ->
              selectedAccount = account
              currentTab = ScreenTab.DETAIL
            },
            onCreateCardClick = { currentTab = ScreenTab.CREATOR_PANEL }
          )
        }

        ScreenTab.MY_RENTALS -> {
          MyRentalsScreen(
            repository = repository,
            onExploreClick = { currentTab = ScreenTab.FEED }
          )
        }

        ScreenTab.CREATOR_PANEL -> {
          CreatorPanelScreen(
            repository = repository,
            onAccountCreated = { newAccount ->
              selectedAccount = newAccount
              currentTab = ScreenTab.DETAIL
            }
          )
        }
      }
    }
  }
}
