package examples;

import jatymon.annotations.Typestate;

@Typestate("BaseIterator")
public class BaseIterator {
  public boolean hasNext() {
    /* Nothing to do here... */
    return true;
  }

  public Object next() {
    /* Nothing to do here... */
    return null;
  }
}