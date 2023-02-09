package view.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class InputViewTest {

    private InputView inputView;

    void before(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        inputView = new InputView();
    }

    @Test
    void 자동차이름_입력값이_빈문자일_경우_오류를던진다() {
        //given
        final String input = "\n";
        before(input);
        //when then
        assertThatThrownBy(inputView::readCarNames).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(Validator.ERROR_HEADER)
            .hasMessageContaining(ErrorMessages.EMPTY_INPUT.getContent());
    }

    @Test
    void 자동차이름이_5자보다_길면_오류를던진다() {
        //given
        final String input = "make,takeasd";
        before(input);
        //when then
        assertThatThrownBy(inputView::readCarNames).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(Validator.ERROR_HEADER)
            .hasMessageContaining(ErrorMessages.NOT_PROPER_CAR_NAME_LENGTH.getContent());
    }

    @ValueSource(strings = {",make,take", "make,,take", "make,take,", ",,,"})
    @ParameterizedTest
    void 자동차이름이_빈문자열일시_오류를던진다(String input) {
        //given
        before(input);
        //when then
        assertThatThrownBy(inputView::readCarNames).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(Validator.ERROR_HEADER)
            .hasMessageContaining(ErrorMessages.NOT_PROPER_CAR_NAME_LENGTH.getContent());
    }

    @ValueSource(strings = {"-", "ma-e", "mak1-"})
    @ParameterizedTest
    void 자동차이름에_중복표시_구분자가_포함되면_오류를던진다(String input) {
        //given
        before(input);
        //when then
        assertThatThrownBy(inputView::readCarNames).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(Validator.ERROR_HEADER)
            .hasMessageContaining(ErrorMessages.CONTAINS_DELIMITER.getContent());
    }

    @Test
    void 자동차이름_입력값_앞뒤에_공백이존재하면_무시한다() {
        //given
        before("  one,two,three                  ");
        //when
        List<String> result = inputView.readCarNames();
        //then
        assertThat(result).isEqualTo(List.of("one", "two", "three"));
    }

    @MethodSource("correctCarNames")
    @ParameterizedTest
    void 올바른_자동차_이름_입력값을_받았을_때(List<String> carNames) {
        //given
        final String DELIMITER = ",";
        before(String.join(DELIMITER, carNames));
        //when
        List<String> result = inputView.readCarNames();
        //then
        assertThat(result).isEqualTo(carNames);
    }

    static Stream<Arguments> correctCarNames() {
        return Stream.of(
            Arguments.of(List.of("one")),
            Arguments.of(List.of("a", "b")),
            Arguments.of(List.of("a b", "ab wd")),
            Arguments.of(List.of("가나다라마", "마라다나가", "기니디리미"))
        );
    }
}