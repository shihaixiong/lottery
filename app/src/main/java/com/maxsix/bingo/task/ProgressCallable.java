package com.maxsix.bingo.task;

public interface ProgressCallable<T> {
    public T call(final com.maxsix.bingo.task.IProgressListener pProgressListener) throws Exception;
}
