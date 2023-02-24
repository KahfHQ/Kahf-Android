package org.thoughtcrime.securesms.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertEquals;

@RunWith(Parameterized.class)
public class SignalProxyUtilText_generateProxyUrl {

  private final String input;
  private final String output;

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        { "https://tube.kahf.co/#proxy.parker.org",     "https://tube.kahf.co/#proxy.parker.org" },
        { "https://tube.kahf.co/#proxy.parker.org:443", "https://tube.kahf.co/#proxy.parker.org" },
        { "kahf://tube.kahf.co/#proxy.parker.org",      "https://tube.kahf.co/#proxy.parker.org" },
        { "kahf://tube.kahf.co/#proxy.parker.org:443",  "https://tube.kahf.co/#proxy.parker.org" },
        { "proxy.parker.org",                          "https://tube.kahf.co/#proxy.parker.org" },
        { "proxy.parker.org:443",                      "https://tube.kahf.co/#proxy.parker.org" },
        { "x",                                         "https://tube.kahf.co/#x" },
        { "",                                          "https://tube.kahf.co/#" }
    });
  }

  public SignalProxyUtilText_generateProxyUrl(String input, String output) {
    this.input  = input;
    this.output = output;
  }

  @Test
  public void parse() {
    assertEquals(output, SignalProxyUtil.generateProxyUrl(input));
  }
}
