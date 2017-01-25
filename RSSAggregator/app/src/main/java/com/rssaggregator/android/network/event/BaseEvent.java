package com.rssaggregator.android.network.event;

public abstract class BaseEvent<T> {
  private Throwable throwable;
  private T data;

  public BaseEvent(Throwable throwable) {
    this.throwable = throwable;
    this.data = null;
  }

  public BaseEvent(T data) {
    this.throwable = null;
    this.data = data;
  }

  public boolean isSuccess() {
    return throwable == null;
  }

  public Throwable getThrowable() {
    return throwable;
  }

  public T getData() {
    return data;
  }
}