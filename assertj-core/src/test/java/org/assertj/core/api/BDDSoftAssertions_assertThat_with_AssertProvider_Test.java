/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;

import org.assertj.core.internal.Strings;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BDDSoftAssertions#then(AssertProvider)}.
 * @author Dmytro Nosan
 */
class BDDSoftAssertions_assertThat_with_AssertProvider_Test {

  private final BDDSoftAssertions softly = new BDDSoftAssertions();

  @Test
  void should_work_with_custom_assert_provider() {
    // WHEN
    softly.then(new TestedObject("click"))
          .hasText("foo")
          .hasText("ick")
          .hasText("bar")
          .hasText("click");
    // THEN
    List<Throwable> errors = softly.errorsCollected();
    then(errors).hasSize(2);
    then(errors.get(0)).hasMessageStartingWith("\n"
                                               + "Expecting actual:\n"
                                               + "  \"click\"\n"
                                               + "to contain:\n"
                                               + "  \"foo\" ");
    then(errors.get(1)).hasMessageStartingWith("\n"
                                               + "Expecting actual:\n"
                                               + "  \"click\"\n"
                                               + "to contain:\n"
                                               + "  \"bar\" ");
  }

  private static class TestedObject implements AssertProvider<TestedObjectAssert> {
    private final String text;

    TestedObject(String text) {
      this.text = text;
    }

    public TestedObjectAssert assertThat() {
      return new TestedObjectAssert(this);
    }
  }

  private static class TestedObjectAssert extends AbstractAssert<TestedObjectAssert, TestedObject> {
    private final Strings strings = Strings.instance();

    TestedObjectAssert(TestedObject testedObject) {
      super(testedObject, TestedObjectAssert.class);
    }

    TestedObjectAssert hasText(String text) {
      strings.assertContains(info, actual.text, text);
      return this;
    }
  }
}
