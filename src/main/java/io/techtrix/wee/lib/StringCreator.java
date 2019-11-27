package io.techtrix.wee.lib;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class StringCreator {

  private StringCreator() {
  }

  /**
   * Just like {@link String#format(String, Object...)} but uses <code>{}</code> as the universal placeholder
   * <br/>
   * Usage:
   * <code>
   * String formatted = Strings.create("This is a {} pattern", "wonderful")
   * </code>
   *
   * @param format     A String template using the <code>{}</code> placeholder
   * @param parameters
   * @return
   */
  public static String create(String format, Object... parameters) {
    if (format == null) {
      return "";
    }

    return
        Stream
            .of(parameters)
            .map(Objects::toString)
            .collect(Collectors.reducing(
                format,
                (String soFar, String param) -> soFar.replaceFirst("\\{\\}", param)));
  }
}
