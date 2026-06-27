package examples;

import jatymon.annotations.Typestate;

@Typestate("RemovableIterator")
public class RemovableIterator {

  public boolean hasNext() {
    return true;
  }

  public Object next() {
    return null;
  }

  public void remove() {
    /* Nothing to do here... */
  }
}
