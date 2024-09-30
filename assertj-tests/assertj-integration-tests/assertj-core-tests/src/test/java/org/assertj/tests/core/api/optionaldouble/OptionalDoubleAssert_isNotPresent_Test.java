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
package org.assertj.tests.core.api.optionaldouble;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.OptionalShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.OptionalDouble;

import org.junit.jupiter.api.Test;

@SuppressWarnings("DataFlowIssue")
class OptionalDoubleAssert_isNotPresent_Test {

  @Test
  void should_pass_if_OptionalDouble_is_empty() {
    assertThat(OptionalDouble.empty()).isNotPresent();
  }

  @SuppressWarnings("OptionalAssignedToNull")
  @Test
  void should_fail_when_OptionalDouble_is_null() {
    // GIVEN
    OptionalDouble nullActual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(nullActual).isNotPresent());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_OptionalDouble_is_present() {
    // GIVEN
    OptionalDouble actual = OptionalDouble.of(10.0);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isNotPresent());
    // THEN
    then(assertionError).hasMessage(shouldBeEmpty(actual).create());
  }
}