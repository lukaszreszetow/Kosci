import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MockJavaPlayer implements Player {

    int roundCounter = 1;

    @Override
    public String getName() {
        int randomNumber = new Random().nextInt();
        return "fakePlayer" + randomNumber;
    }

    @Override
    public Map<String, Integer[]> onDiceRoll(int rollInRound, Integer[] rolls) {
        Integer[] integerMap = new Integer[1];
        integerMap[0] = roundCounter++;
        Map<String, Integer[]> map = new HashMap<>();
        map.put("leave", new Integer[0]);
        map.put("cross_out", integerMap);
        return map;
    }
}
