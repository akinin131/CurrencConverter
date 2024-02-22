package com.example.currencyconverter.botton_navigation

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.currencyconverter.ui.theme.PurpleGrey80

@Composable
fun BottomNavigationFun(
    navController: NavController
) {
    val listItem = listOf(
        BottomItem.screen1,
        BottomItem.screen2
    )

    BottomNavigation(
        backgroundColor = PurpleGrey80
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val backStateRoute = backStackEntry?.destination?.route
        listItem.forEach{item ->
            BottomNavigationItem(
                selected = backStateRoute == item.route,
                onClick = { navController.navigate(item.route)},
                icon = { Icon(
                    painter = painterResource(id = item.itemId),
                    contentDescription = "Icon"
                )
                },
                label = {
                    Text(
                        text = item.tittle,
                        fontSize = 9.sp
                    )
                },
                selectedContentColor = Color.DarkGray,
                unselectedContentColor = Color.Gray
            )
        }
    }
}


