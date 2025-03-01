package com.example.common_mvvm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author y.yangel
 */
open class MvvmViewModel<State : MvvmState>(initialState: State) : ViewModel() {

    val internalState: MutableStateFlow<State> = MutableStateFlow(initialState)
}

interface MvvmState

val String.empty: String
    get() = ""