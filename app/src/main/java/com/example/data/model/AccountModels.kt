package com.example.data.model

data class IncludedItem(
  val id: String,
  val name: String,
  val iconRes: Int,
  val description: String = "",
  val poseName: String = ""
)

data class AccountCredentials(
  val username: String,
  val password: String,
  val pin: String = "4892",
  val server: String = "BR-SUL-01",
  val profileAssigned: String = "Perfil #2 (Privado)",
  val rules: String = "Não trocar a senha principal. Usar apenas o perfil atribuído. Não compartilhar com terceiros."
)

data class SharedAccount(
  val id: String,
  val title: String,
  val category: String,
  val description: String,
  val basePricePerDay: Double = 29.90,
  val currentUsers: Int = 2,
  val maxUsers: Int = 4,
  val rating: Double = 4.8,
  val viewsCount: String = "1.2k",
  var likesCount: Int = 342,
  var isLiked: Boolean = false,
  val rentedCount: Int = 12,
  val last24hRentalsText: String = "+2.345 alugaram nas últimas 24h",
  val avatarDrawableRes: Int,
  val itemsIncluded: List<IncludedItem>,
  val credentials: AccountCredentials,
  val isOccupied: Boolean = false
)

data class RentalCalculation(
  val days: Int,
  val people: Int,
  val rawTotal: Double,
  val discountPercent: Int,
  val discountAmount: Double,
  val finalPrice: Double,
  val formattedPrice: String
)

data class ActiveRental(
  val id: String,
  val accountId: String,
  val accountTitle: String,
  val avatarDrawableRes: Int,
  val days: Int,
  val peopleCount: Int,
  val totalPaid: Double,
  val startTimeMillis: Long,
  val durationDays: Int,
  val credentials: AccountCredentials
) {
  fun isExpired(): Boolean {
    val totalDurationMillis = durationDays.toLong() * 24 * 60 * 60 * 1000L
    return (System.currentTimeMillis() - startTimeMillis) >= totalDurationMillis
  }

  fun remainingMillis(): Long {
    val totalDurationMillis = durationDays.toLong() * 24 * 60 * 60 * 1000L
    val diff = totalDurationMillis - (System.currentTimeMillis() - startTimeMillis)
    return if (diff > 0) diff else 0L
  }
}
