package view;

import dto.CarDto;
import java.util.HashMap;
import java.util.List;

public class OutputView {

    public void printRacingResult(HashMap<Integer, List<CarDto>> racingResult) {
        StringBuilder result = new StringBuilder();
        result.append("\n실행 결과\n");
        for (List<CarDto> entry : racingResult.values()) {
            result.append(makeResultPerAttempt(entry));
            result.append("\n");
        }
        System.out.print(result);
    }

    private String makeResultPerAttempt(List<CarDto> carDtos) {
        StringBuilder raceResult = new StringBuilder();
        for (CarDto carDto : carDtos) {
            raceResult.append(carDto.name() + " : " + "-".repeat(carDto.position()) + "\n");
        }
        return raceResult.toString();
    }

    public void printRacingWinners(List<CarDto> winners) {
        StringBuilder winnersResult = new StringBuilder();
        List<String> winnersName = winners.stream()
                .map(winner -> winner.name())
                .toList();
        winnersResult.append(String.join(", ", winnersName));
        winnersResult.append("가 최종 우승했습니다.");
        System.out.print(winnersResult.toString());
    }

    public void printErrorMessage(String message) {
        System.out.println("[ERROR] : " + message);
    }


}
