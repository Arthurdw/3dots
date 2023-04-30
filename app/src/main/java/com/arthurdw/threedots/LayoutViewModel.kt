package com.arthurdw.threedots

import com.arthurdw.threedots.data.AppContainer
import com.arthurdw.threedots.utils.BaseViewModel

class LayoutViewModel(private val container: AppContainer) : BaseViewModel() {
    fun signOut() {
        wrapRepositoryAction {
            container.offlineRepository.deleteSessionToken()
        }
    }

    companion object {
        val Factory = createFactoryContainer<LayoutViewModel>()
    }
}
