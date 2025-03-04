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

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link SoftAssertions#assertThat(AssertProvider)}.
 * @author Dmytro Nosan
 */
class SoftAssertions_assertThat_with_AssertProvider_Test {

  private final SoftAssertions softly = new SoftAssertions();

  @Test
  void should_work_with_assert_where_constructor_actual() {
    // WHEN
    softly.assertThat(new ActualList(Arrays.asList("hello", "world")))
          .contains("foo")
          .contains("hello")
          .contains("bar")
          .contains("world");
    // THEN
    List<Throwable> errors = softly.errorsCollected();
    then(errors).hasSize(2);
    then(errors.get(0)).hasMessageStartingWith("\n"
                                               + "Expecting ArrayList:\n"
                                               + "  [\"hello\", \"world\"]\n"
                                               + "to contain:\n"
                                               + "  [\"foo\"]\n"
                                               + "but could not find the following element(s):\n"
                                               + "  [\"foo\"]\n");
    then(errors.get(1)).hasMessageStartingWith("\n"
                                               + "Expecting ArrayList:\n"
                                               + "  [\"hello\", \"world\"]\n"
                                               + "to contain:\n"
                                               + "  [\"bar\"]\n"
                                               + "but could not find the following element(s):\n"
                                               + "  [\"bar\"]\n");
  }

  @Test
  void should_fail_with_assert_that_does_not_have_suitable_constructor() {
    assertThatIllegalArgumentException().isThrownBy(() -> softly.assertThat(new NoSuitableConstuctorObject())
                                                                .isInstanceOf(NoSuitableConstuctorObject.class))
                                        .withMessageContaining("has no suitable constructor");

  }

  @Test
  void should_fail_with_assert_that_does_not_extend_abstract_assert() {
    Assert<?, ?> customAssert = Mockito.mock(Assert.class);
    assertThatIllegalArgumentException().isThrownBy(() -> softly.assertThat((AssertProvider<Assert<?, ?>>) () -> customAssert)
                                                                .isNull())
                                        .withMessageContaining("The provided Assert instance must be an instance of "
                                                               + AbstractAssert.class);
  }

  @Test
  void should_work_with_class_assert() {
    softly.assertThat((AssertProvider<Assert<?, ?>>) () -> new ClassAssert(Cloneable.class))
          .isInstanceOf(ClassAssert.class);
    List<Throwable> errors = softly.errorsCollected();
    then(errors).hasSize(1);
  }

  @Test
  void should_work_with_assert_with_multiple_constructors() {
    softly.assertThat(new MultipleConstructorLongObject(10))
          .isBetween(11L, 20L)
          .isLessThan(9L)
          .isLessThan(11L);
    List<Throwable> errors = softly.errorsCollected();
    then(errors).hasSize(2);
  }

  @Test
  void should_work_with_assert_with_multiple_constructors_avoid_primitive_when_null() {
    softly.assertThat(new MultipleConstructorLongObject(null))
          .isBetween(11L, 20L)
          .isLessThan(9L)
          .isLessThan(11L);
    List<Throwable> errors = softly.errorsCollected();
    then(errors).hasSize(3);
  }

  private static class MultipleConstructorLongObject implements AssertProvider<MultipleConstructorLongAssert> {

    private Long value;
    private Long primitiveValue;

    public MultipleConstructorLongObject(Long value) {
      this.value = value;
    }

    public MultipleConstructorLongObject(long primitiveValue) {
      this.primitiveValue = primitiveValue;
    }

    @Override
    public MultipleConstructorLongAssert assertThat() {
      if (primitiveValue != null) {
        return new MultipleConstructorLongAssert(primitiveValue.longValue());
      }
      return new MultipleConstructorLongAssert(value);
    }
  }

  private static class MultipleConstructorLongAssert extends LongAssert {

    public MultipleConstructorLongAssert(Long actual) {
      super(actual);
    }

    public MultipleConstructorLongAssert(long actual) {
      super(actual);
    }
  }
  private static class NoSuitableConstuctorObject implements AssertProvider<NoSuitableConstructorObjectAssert> {

    @Override
    public NoSuitableConstructorObjectAssert assertThat() {
      return new NoSuitableConstructorObjectAssert(this, null);
    }
  }
  private static class NoSuitableConstructorObjectAssert
      extends AbstractAssert<NoSuitableConstructorObjectAssert, NoSuitableConstuctorObject> {

    protected NoSuitableConstructorObjectAssert(NoSuitableConstuctorObject testedObject, Throwable failure) {
      super(testedObject, NoSuitableConstructorObjectAssert.class);
    }
  }

  private static class ActualList implements AssertProvider<ActualListAssert> {

    private final List<String> elements;

    private ActualList(List<String> elements) {
      this.elements = elements;
    }

    @Override
    public ActualListAssert assertThat() {
      return new ActualListAssert(this.elements);
    }
  }

  private static class ActualListAssert extends ListAssert<String> {

    public ActualListAssert(List<? extends String> actual) {
      super(actual);
    }
  }

}
