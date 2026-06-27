package examples;

import jatymon.annotations.Typestate;

@Typestate("JavaIterator")
public class JavaIterator {

  public boolean hasNext() {
    return true;
  }

  public Object next() {
    return null;
  }
}
