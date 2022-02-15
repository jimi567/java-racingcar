package racingcar.domain;

import racingcar.view.Output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

public class Cars {
	private static final String DELIMITER = ",";
	private static final int CAR_LIMIT = 2;
	private static final int DRIVE_FLAG = 3;
	private List<Car> cars = new ArrayList<>();

	public Cars(String names) {
		this(names.split(DELIMITER));
	}

	public Cars(String[] names) {
		checkValid(names);
		for (String name : names) {
			cars.add(createCar(name));
		}
	}

	public void play() {
		for (Car car : cars) {
			car.drive(hasNext());
		}
		Output.roundResult(cars);
	}

	public List<String> findWinners() {
		Car maxPositionCar = findMaxPositionCar();
		return findSamePositionCar(maxPositionCar);
	}

	private void checkValid(String[] names) {
		if (!isCars(names)) {
			throw new IllegalArgumentException("자동차를 두 개 이상 입력해주세요.");
		}
		if (isDuplicated(names)) {
			throw new IllegalArgumentException("자동차 이름을 모두 다르게 입력해주세요.");
		}
	}

	private boolean isCars(String[] names) {
		return names.length >= CAR_LIMIT;
	}

	private boolean isDuplicated(String[] names) {
		Set<String> carNames = new HashSet<>(Arrays.asList(names));
		return carNames.size() != names.length;
	}

	private Car createCar(String name) {
		return new Car(name.trim());
	}

	private boolean hasNext() {
		return generate() > DRIVE_FLAG;
	}

	private int generate() {
		return (int)(Math.random() * 100) % 10;
	}

	private Car findMaxPositionCar() {
		return cars.stream()
			.max(Car::compareTo)
			.orElseThrow(() -> new NoSuchElementException("max 값을 찾을 수 없습니다."));
	}

	private List<String> findSamePositionCar(Car target) {
		return cars.stream()
			.filter(car -> car.isSamePosition(target))
			.map(Car::getName)
			.collect(Collectors.toList());
	}
}