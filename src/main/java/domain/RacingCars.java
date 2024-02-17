package domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RacingCars {
    private final List<Car> cars;

    public RacingCars(String carNames) {
        this.cars = generateCars(carNames);
    }

    private List<Car> generateCars(String carNames) {
        return Arrays.stream(carNames.split(","))
                .map(Car::new)
                .collect(Collectors.toList());
    }

    public List<Car> getAllCars() {
        return cars;
    }

    public List<Car> getWinners() {
        int maxPosition = getMaxPosition();
        return cars.stream()
                .filter(car -> car.isSamePosition(maxPosition))
                .toList();
    }

    private int getMaxPosition() {
        return cars.stream()
                .mapToInt(car -> car.getPosition())
                .max()
                .orElse(0);
    }
}
