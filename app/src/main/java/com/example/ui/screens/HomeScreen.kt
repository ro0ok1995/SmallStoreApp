package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.model.CustomerWithDebt
import com.example.data.localization.LocalStrings
import com.example.ui.components.AddEditCustomerDialog
import com.example.ui.components.SearchBar
import com.example.ui.theme.BrandBackgroundLight
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainerLight
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.FinancialDebt
import com.example.ui.theme.FinancialPayment
import com.example.ui.viewmodel.ShopViewModel

/**
 * SCREEN 1: HOME / MAIN DASHBOARD
 * Primary landing screen prioritizing customer search, customer list, outstanding debt, and fast navigation.
 */
@Composable
fun HomeScreen(
    viewModel: ShopViewModel,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    val customersWithDebt by viewModel.activeCustomersWithDebt.collectAsStateWithLifecycle()
    val searchQuery by viewModel.customerSearchQuery.collectAsStateWithLifecycle()
    val shopSettings by viewModel.shopSettings.collectAsStateWithLifecycle()
    val allActiveCustomers by viewModel.allActiveCustomers.collectAsStateWithLifecycle()

    var showAddCustomerDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // ==========================================
            // HEADER & SEARCH SECTION
            // ==========================================
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Transparent,
                    shadowElevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(BrandPrimary, BrandSecondary)
                                )
                            )
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 18.dp)
                    ) {
                        Column {
                            // Top Bar: Store Name & Quick Add Customer Action
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = shopSettings.storeName.ifBlank { strings.appName },
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        ),
                                        maxLines = 1
                                    )
                                    Text(
                                        text = strings.homeTitle,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color.White.copy(alpha = 0.85f),
                                            fontSize = 12.sp
                                        ),
                                        maxLines = 1
                                    )
                                }

                                IconButton(
                                    onClick = { showAddCustomerDialog = true },
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.18f))
                                        .testTag("home_add_customer_header_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = strings.addCustomer,
                                        tint = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Customer Search Field (Name & Phone with Country Code support)
                            SearchBar(
                                query = searchQuery,
                                onQueryChange = { viewModel.setCustomerSearchQuery(it) },
                                placeholder = strings.searchCustomerHint,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // List Title and Count indicator
            if (customersWithDebt.isNotEmpty() || searchQuery.isNotBlank()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = strings.customersList,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = BrandSecondary,
                                fontSize = 14.sp
                            )
                        )

                        Text(
                            text = "${customersWithDebt.size} ${strings.tabCustomers}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF757575),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            // ==========================================
            // CUSTOMER LIST OR EMPTY STATES
            // ==========================================
            if (allActiveCustomers.isEmpty() && searchQuery.isBlank()) {
                // Empty state when no customers exist yet
                item {
                    NoCustomersEmptyState(
                        onAddCustomer = { showAddCustomerDialog = true }
                    )
                }
            } else if (customersWithDebt.isEmpty() && searchQuery.isNotBlank()) {
                // Empty state when search yields no results
                item {
                    NoSearchResultsEmptyState(
                        searchQuery = searchQuery,
                        onClearSearch = { viewModel.setCustomerSearchQuery("") }
                    )
                }
            } else {
                // Vertically scrollable customer list
                itemsIndexed(
                    items = customersWithDebt,
                    key = { _, item -> item.customer.id }
                ) { index, item ->
                    CustomerCard(
                        serialNumber = index + 1,
                        customerWithDebt = item,
                        onClick = { viewModel.openCustomerDetails(item.customer.id) }
                    )
                }
            }
        }

        // Floating Action Button for fast customer creation
        FloatingActionButton(
            onClick = { showAddCustomerDialog = true },
            containerColor = BrandPrimary,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 85.dp, end = 20.dp)
                .size(56.dp)
                .testTag("home_fab_add_customer")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = strings.addCustomer,
                modifier = Modifier.size(28.dp)
            )
        }
    }

    // Customer creation dialog
    if (showAddCustomerDialog) {
        AddEditCustomerDialog(
            customer = null,
            onDismiss = { showAddCustomerDialog = false },
            onSave = { customer ->
                viewModel.saveCustomer(customer) {
                    showAddCustomerDialog = false
                }
            }
        )
    }
}

/**
 * Clean rounded customer card adhering strictly to the RTL/LTR layout requirements.
 *
 * Right side in RTL (Start in LTR):
 * - Automatically generated customer serial number inside a circle (①, ②, ③...).
 * - Customer name.
 * - Phone number if available (hidden if not available).
 *
 * Left side in RTL (End in LTR):
 * - Outstanding debt label and large, prominent amount in red.
 */
@Composable
fun CustomerCard(
    serialNumber: Int,
    customerWithDebt: CustomerWithDebt,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    val customer = customerWithDebt.customer
    val debt = customerWithDebt.outstandingDebt
    val hasDebt = debt.isPositive()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .clickable { onClick() }
            .testTag("customer_card_${customer.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Right Side in RTL (Start in LTR): Serial circle, Customer name, Phone
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                CustomerSerialBadge(serialNumber = serialNumber)

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = customer.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        maxLines = 1
                    )

                    if (customer.phone.isNotBlank()) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = customer.phone,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 13.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Left Side in RTL (End in LTR): Outstanding debt label and large readable value
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = strings.outstandingDebt,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (hasDebt) FinancialDebt.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = debt.format(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = if (hasDebt) FinancialDebt else FinancialPayment
                    ),
                    modifier = Modifier.testTag("customer_debt_${customer.id}")
                )
            }
        }
    }
}

/**
 * Automatically generated customer serial number inside a sleek circular container.
 */
@Composable
fun CustomerSerialBadge(
    serialNumber: Int,
    modifier: Modifier = Modifier
) {
    val symbol = when (serialNumber) {
        1 -> "①"
        2 -> "②"
        3 -> "③"
        4 -> "④"
        5 -> "⑤"
        6 -> "⑥"
        7 -> "⑦"
        8 -> "⑧"
        9 -> "⑨"
        10 -> "⑩"
        11 -> "⑪"
        12 -> "⑫"
        13 -> "⑬"
        14 -> "⑭"
        15 -> "⑮"
        16 -> "⑯"
        17 -> "⑰"
        18 -> "⑱"
        19 -> "⑲"
        20 -> "⑳"
        else -> "$serialNumber"
    }

    Box(
        modifier = modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = if (serialNumber <= 20) 17.sp else 13.sp
            )
        )
    }
}

/**
 * Empty state when no customers have been registered yet.
 */
@Composable
private fun NoCustomersEmptyState(
    onAddCustomer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = strings.noCustomersYet,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = strings.noCustomersYetDesc,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onAddCustomer,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .height(48.dp)
                    .testTag("empty_state_add_customer_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = strings.addCustomer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

/**
 * Empty state when search has no matching results.
 */
@Composable
private fun NoSearchResultsEmptyState(
    searchQuery: String,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SearchOff,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = strings.noMatchingCustomers,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1C1B1F)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "\"$searchQuery\"",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray,
                    fontSize = 13.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedButton(
                onClick = onClearSearch,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("clear_search_empty_btn")
            ) {
                Text(
                    text = strings.cancel,
                    color = BrandPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
