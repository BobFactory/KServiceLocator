package com.creations.bawender.service_locator.helpers

import androidx.compose.runtime.Composable
import androidx.activity.ComponentActivity
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.creations.bawender.service_locator.ServiceLocator

inline fun <reified T : ViewModel> getFactory(): ViewModelProvider.Factory {
    val key = ServiceLocator.getKey<T>()

    return if (ServiceLocator.hasMappingFor(key)) {
        ServiceLocator.getMapping(key).get() as ViewModelProvider.Factory
    } else {
        error("Missing dependency for class ${T::class.simpleName}")
    }
}

inline fun <reified VM : ViewModel> ComponentActivity.serviceViewModel(): Lazy<VM> {

    return ViewModelLazy(
        VM::class,
        { this.viewModelStore },
        { getFactory<VM>() },
        { this.defaultViewModelCreationExtras }
    )
}

@Composable
public inline fun <reified VM : ViewModel> serviceViewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    key: String? = null,
    factory: ViewModelProvider.Factory? = getFactory<VM>(),
    extras: CreationExtras = if (viewModelStoreOwner is HasDefaultViewModelProviderFactory) {
        viewModelStoreOwner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }
): VM = androidx.lifecycle.viewmodel.compose.viewModel(
    VM::class.java,
    viewModelStoreOwner,
    key,
    factory,
    extras
)