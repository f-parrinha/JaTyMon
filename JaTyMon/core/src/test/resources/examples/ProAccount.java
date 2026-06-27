package examples;

import jatymon.annotations.Typestate;

@Typestate("ProAccount")
public class ProAccount {

  public boolean canWithdraw(int val) {
    return true;
  }

  public boolean canTransfer(int val) {
    return true;
  }

  public void deposit(int val) {
    /* Nothing to do here... */
  }

  public void withdraw() {
    /* Nothing to do here... */
  }

  public void transfer(BaseAccount account) {
    /* Nothing to do here... */
  }
}
