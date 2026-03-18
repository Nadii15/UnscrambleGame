package com.ryckluk.unscramblegame

import androidx.lifecycle.viewmodel.compose.viewModel
import com.ryckluk.unscramblegame.data.MAX_NO_OF_WORDS
import com.ryckluk.unscramblegame.data.SCORE_INCREASE
import com.ryckluk.unscramblegame.ui_model.GameViewModel
import org.junit.Assert.*
import org.junit.Test

class GameViewModelTest {
    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord =
            viewModel.getUnscrambledWord(currentGameUiState.currentScrambledWord)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value

        assertEquals(SCORE_INCREASE, currentGameUiState.score)
        assertFalse(currentGameUiState.isGuessedWordWrong)
    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet() {
        val incorrectPlayerWord = "incorrect"
        viewModel.updateUserGuess(incorrectPlayerWord)
        viewModel.checkUserGuess()
        val currentGameUiState = viewModel.uiState.value
        assertEquals(0, currentGameUiState.score)
        assertTrue(currentGameUiState.isGuessedWordWrong)
    }

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val gameUiState = viewModel.uiState.value
        val unscrambledWords = getUnscrambledWord(gameUiState.currentScrambledWord)
        assertNotEquals(unscrambledWords,gameUiState.currentScrambledWord)
        assertTrue(gameUiState.currentWordCount == 1)
        assertTrue(gameUiState.score == 0)
        assertFalse(gameUiState.isGameOver)
    }

    private fun getUnscrambledWord(scranbledWord: String): String {
        return com.ryckluk.unscramblegame.data.allWords.firstOrNull { word ->
            scranbledWord.toSet() == word.toSet()
        } ?: ""

    }

    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly(){
        var expectedScore = 0
        var currentGameUiState = viewModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        repeat(MAX_NO_OF_WORDS){
            expectedScore += SCORE_INCREASE
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
            currentGameUiState = viewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
            assertEquals(expectedScore,currentGameUiState.score)
        }
        assertEquals(MAX_NO_OF_WORDS,currentGameUiState.currentScrambledWord)
        assertTrue(currentGameUiState.isGameOver)
    }


}