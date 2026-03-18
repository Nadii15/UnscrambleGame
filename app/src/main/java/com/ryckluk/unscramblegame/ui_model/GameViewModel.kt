package com.ryckluk.unscramblegame.ui_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ryckluk.unscramblegame.data.GameUiState
import com.ryckluk.unscramblegame.data.MAX_NO_OF_WORDS
import com.ryckluk.unscramblegame.data.SCORE_INCREASE
import com.ryckluk.unscramblegame.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private lateinit var currentWord: String
    private var usedWords: MutableSet<String> = mutableSetOf()

    var userGuess by mutableStateOf("")
        private set

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
    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }
    private fun updateGameState(updatedScore: Int){
        if (usedWords.size == MAX_NO_OF_WORDS){
            _uiState.update { currentState -> currentState.copy(
                isGuessedWordWrong = false,
                score = updatedScore,
                isGameOver = true
            )
            }
        }else {
            _uiState.update { currentState -> currentState.copy(
                isGameOver = false,
                currentScrambledWord = pickRandomWordAndShuffle(),
                score = updatedScore,
                currentWordCount = currentState.currentWordCount +1
            ) }
        }
    }

    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            val updateScore = _uiState.value.score + SCORE_INCREASE
            updateGameState(updateScore)
        } else {
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        updateUserGuess("")
    }
    fun skipWord() {
        updateGameState(_uiState.value.score)

        updateUserGuess("")
    }


}