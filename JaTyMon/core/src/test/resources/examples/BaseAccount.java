package examples;

import jatymon.annotations.Typestate;

@Typestate("BaseAccount")
public class BaseAccount {
  public void deposit(int val) {
    /* Nothing to do here... */
  }

  public boolean canWithdraw(int val) {
    /* Nothing to do here... */
    return false;
  }

  public void withdraw() {
    /* Nothing to do here... */
  }
}