package com.breaktime.lab3.view.menu

import com.breaktime.lab3.view.base.BaseViewModel

class MenuViewModel(
) :
    BaseViewModel<MenuContract.Event, MenuContract.State, MenuContract.Effect>() {
    override fun createInitialState(): MenuContract.State {
        return MenuContract.State(
            MenuContract.MenuState.Idle
        )
    }

    override fun handleEvent(event: MenuContract.Event) {
        when (event) {
            is MenuContract.Event.OnEditProfileButtonClick -> {
                setState { copy(menuState = MenuContract.MenuState.EditProfile) }
            }
            is MenuContract.Event.OnCalculateBmiButtonClick -> {
                setState { copy(menuState = MenuContract.MenuState.CalculateBmi) }
            }
            is MenuContract.Event.OnAboutDeveloperButtonClick -> {
                setState { copy(menuState = MenuContract.MenuState.AboutDeveloper) }
            }
            is MenuContract.Event.OnGuideButtonClick -> {
                setState { copy(menuState = MenuContract.MenuState.Guide) }
            }
        }
    }

    override fun clearState() {
        setState { copy(menuState = MenuContract.MenuState.Idle) }
    }
}