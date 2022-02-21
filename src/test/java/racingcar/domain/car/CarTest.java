package racingcar.domain.car;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import racingcar.exception.car.CarNameExceptionStatus;
import racingcar.exception.car.WrongCarNameException;

class CarTest {

    private static final int CAR_INITIALIZED_LOCATION = 0;
    private static final int LOCATION_INCREASING_COUNT = 1;

    private void exceptionTest(final String name, final String errorMessage) {
        assertThatThrownBy(() -> new Car(name))
                .isInstanceOf(WrongCarNameException.class)
                .hasMessageContaining(errorMessage);
    }

    @DisplayName("자동차 이름은 NULL이 될 수 없다.")
    @ParameterizedTest(name = "[{index}] 자동차 이름 : {0}")
    @NullSource
    void carNameNullExceptionTest(final String name) {
        final String errorMessage = CarNameExceptionStatus.NAME_IS_NULL.getMessage();
        exceptionTest(name, errorMessage);
    }

    @DisplayName("자동차 이름은 공백이 될 수 없다.")
    @ParameterizedTest(name = "[{index}] 자동차 이름 : \"{0}\"")
    @EmptySource
    void carNameEmptyExceptionTest(final String name) {
        final String errorMessage = CarNameExceptionStatus.NAME_IS_EMPTY.getMessage();
        exceptionTest(name, errorMessage);
    }

    @DisplayName("자동차 이름은 5자를 넘길 수 없다.")
    @ParameterizedTest(name = "[{index}] 자동차 이름 : \"{0}\"")
    @ValueSource(strings = {"123456", "1234567", "12345678"})
    void carNameTooLongExceptionTest(final String name) {
        final String errorMessage = CarNameExceptionStatus.NAME_IS_TOO_LONG.getMessage();
        exceptionTest(name, errorMessage);
    }

    @DisplayName("생성자 기능 테스트")
    @ParameterizedTest(name = "[{index}] 자동차 이름 : \"{0}\"")
    @ValueSource(strings = {"aaa", "poby", "i  f", "hanul"})
    void constructorTest(final String name) {
        final Car car = new Car(name);
        assertThat(car.getName()).isEqualTo(name);
        assertThat(car.getLocation()).isEqualTo(CAR_INITIALIZED_LOCATION);
    }

    @DisplayName("조건을 만족한 경우, 자동차는 한번에 1만큼 이동할 수 있다.")
    @ParameterizedTest(name = "[{index}] 자동차 이름 : \"{0}\"")
    @MethodSource("provideForGoForwardTest")
    void goForwardTrueTest(final String name) {
        final Car car = new Car(name);
        final int carLocation = car.getLocation();
        car.goForward(() -> true);

        final int actual = car.getLocation();
        final int expected = carLocation + LOCATION_INCREASING_COUNT;
        assertThat(actual).isEqualTo(expected);
    }

    public static Stream<Arguments> provideForGoForwardTest() {
        return Stream.of(
                Arguments.of("aaa"),
                Arguments.of("poby"),
                Arguments.of("if"),
                Arguments.of("hanul"));
    }

    @DisplayName("조건을 만족하지 못한 경우, 자동차는 이동할 수 없다.")
    @ParameterizedTest(name = "[{index}] 자동차 이름 : \"{0}\"")
    @MethodSource("provideForGoForwardTest")
    void goForwardFalseTest(final String name) {
        final Car car = new Car(name);
        final int expected = car.getLocation();
        car.goForward(() -> false);

        final int actual = car.getLocation();
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("이동거리가 동일한지 확인하는 기능 테스트")
    @ParameterizedTest(name = "[{index}] 이동 기대 횟수 : {1}")
    @MethodSource(value = "provideForSameLocationTest")
    void isLocationSameTest(final String name, final int location) {
        final Car car = new Car(name);
        repeatGoForward(car, location);

        assertThat(car.isLocationSameWith(location)).isTrue();
    }

    public static Stream<Arguments> provideForSameLocationTest() {
        return Stream.of(
                Arguments.of("if", 3),
                Arguments.of("hanul", 5)
        );
    }

    private void repeatGoForward(final Car car, final int location) {
        for (int i = 0; i < location; i++) {
            car.goForward(() -> true);
        }
    }

}