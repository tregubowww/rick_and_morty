package ru.tregubowww.rick_and_morty.state

sealed class State {
    object Loading : State()
    object NotLoading : State()
    object Error : State()
}
