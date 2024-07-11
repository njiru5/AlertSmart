package net.ezra.ui.criminology.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel() {
    private val _name = MutableStateFlow("yng")
    private val _avatarUrl = MutableStateFlow("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQGtEolGdo2azvBIZT3BEBbP4nClhnBedEWA&s")

    val name: StateFlow<String> = _name
    val avatarUrl: StateFlow<String> = _avatarUrl

    fun updateName(newName: String) {
        _name.value = newName
    }

    fun updateAvatarUrl(newUrl: String) {
        _avatarUrl.value = newUrl
    }
}
