package me.arrowsome.vinted_hw

sealed class Result <out T, out V> {
    data class Success <out R> (val data: R) : Result <R, Nothing>()
    data class Failure <out E> (val error: E) : Result <Nothing, E>()
}