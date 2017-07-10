package com.maxsix.bingo.task;


public interface AsyncCallable<T> {
    public void call(final com.maxsix.bingo.task.Callback<T> pCallback, final com.maxsix.bingo.task.Callback<Exception> pExceptionCallback);
}
