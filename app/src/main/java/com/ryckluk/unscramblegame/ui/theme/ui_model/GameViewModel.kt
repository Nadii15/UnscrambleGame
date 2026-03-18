package com.ryckluk.unscramblegame.ui.theme.ui_model

import androidx.lifecycle.ViewModel
import com.ryckluk.unscramblegame.data.GameUiState
import com.ryckluk.unscramblegame.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private lateinit var currentWord: String
    private var usedWords: MutableSet<String> = mutableSetOf()

    private fun shuffleCurrentWord(word: String): String {
        val tempWords = word.toCharArray()
        tempWords.shuffle()
        while (String(tempWords) == word) {
            tempWords.shuffle()
        }
        return String(tempWords)
    }

    private fun pickRandomWordAndShuffle(): String{
        currentWord = allWords.random()
        while (usedWords.contains(currentWord)){
            currentWord = allWords.random()
        }
        usedWords.add(currentWord)
        return shuffleCurrentWord(currentWord)
    }

    init {
        resetGame()
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(
            currentScrambledWord = pickRandomWordAndShuffle()
        )

    }
}