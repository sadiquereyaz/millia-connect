package com.reyaz.milliaconnect1.navigation.graph

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.reyaz.feature.rent.domain.model.Property
import com.reyaz.feature.rent.presentation.property_detail_screen.PropertyDetailScreen
import com.reyaz.feature.rent.presentation.property_list_screen.PropertyListScreen
import com.reyaz.feature.rent.presentation.property_list_screen.PropertyListViewModel
import com.reyaz.feature.rent.presentation.property_post_screen.PropertyPostScreen
import com.reyaz.feature.rent.presentation.property_post_screen.PropertyPostViewModel
import constants.NavigationRoute
import com.reyaz.feature.result.presentation.ResultScreen
import com.reyaz.feature.result.presentation.ResultViewModel
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.propertyNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    composable(
        route = NavigationRoute.PropertyFeed.route,
        deepLinks = listOf(
            navDeepLink { uriPattern = NavigationRoute.Result.getDeepLink() }
        )
    ) {
        val viewModel: PropertyListViewModel = koinViewModel()
        PropertyListScreen(
            modifier = Modifier,
            viewModel = viewModel,
            onPostClick ={
                //on clicking it will take to the post screen where u are allowed to post
                navController.navigate("route i dont know because my mind is fucked in understanding naviagation")
            },
            onDetailViewClick={
                //on clicking this u can see the details on the new screen
                navController.navigate("same here ,will be managed by sadique bhai ")
            }
            // sare navigation wale lambda jisme navcontroller use hoga wo yhi se pass krna
            // aur jo function viewmodel me hai like button click pe execute krna h wo bhi yhi se pass krna
            /*
            openPdf = {
                navController.navigate(NavigationRoute.PdfViewer.createRoute(it))
            },
            onNavigateBack = {
                navController.popBackStack()
            },

            fo example:
            onClick = {username->
            viewmodel.onSigninClick(username)
            }
             */
        )
    }
    composable (
        route ="" //filhall nhi pataa hai baad m daal dunga route
    ){
        val viewModel: PropertyPostViewModel = koinViewModel()
        PropertyPostScreen(
            onPostClick = {property->
                //this is a lambda function which will be invoked when user click post button available
                //on the screen,before posting it will be checking whether it is logged in or not
                //if not logged in then invoke login else post the property--- i am not gettiing logic so iwill do it in
                //screen --after i will optimize it
                viewModel.postProperty(property)
            },
            viewModel
        )//on this screen u can post a property ,we will provide a form
        //which will have different field to be filled by user
    }

        composable(
            route = NavigationRoute.PropertyDetails("viewmodel.id  --> uistate se bhe le skte ho ya argument se").route,
        ){
            /// detail screeen ka composle rhega
            //backstack entry m store krke object ko pass kr denge but for now lets hardcode it
            val property = Property()
            PropertyDetailScreen(property)
        }
}