package org.thoughtcrime.securesms.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertEquals;

@RunWith(Parameterized.class)
public class SignalProxyUtilText_parseHostFromProxyDeepLink {

  private final String input;
  private final String output;

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        { "https://tube.kahf.co/#proxy.parker.org",     "proxy.parker.org" },
        { "https://tube.kahf.co/#proxy.parker.org:443", "proxy.parker.org" },
        { "kahf://tube.kahf.co/#proxy.parker.org",      "proxy.parker.org" },
        { "kahf://tube.kahf.co/#proxy.parker.org:443",  "proxy.parker.org" },
        { "https://tube.kahf.co/",                       null },
        { "https://tube.kahf.co/#",                      null },
        { "kahf://tube.kahf.co/",                        null },
        { "kahf://tube.kahf.co/#",                       null },
        { "http://tube.kahf.co/#proxy.parker.org",       null },
        { "tube.kahf.co/#proxy.parker.org",              null },
        { "",                                           null },
        { null,                                         null }
    });
  }

  public SignalProxyUtilText_parseHostFromProxyDeepLink(String input, String output) {
    this.input  = input;
    this.output = output;
  }

  @Test
  public void parse() {
    assertEquals(output, SignalProxyUtil.parseHostFromProxyDeepLink(input));
  }
}
