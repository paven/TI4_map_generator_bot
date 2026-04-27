package ti4.game.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.UtilityClass;
import ti4.helpers.Storage;
import ti4.image.Mapper;
import ti4.json.JsonMapperManager;
import ti4.logging.BotLogger;
import ti4.model.ActionCardModel;
import ti4.model.StrategyCardModel;
import tools.jackson.databind.json.JsonMapper;

@UtilityClass
public class GameCustomCardService {

    private static final JsonMapper mapper = JsonMapperManager.basic();

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomCards {
        private List<ActionCardModel> actionCards = new ArrayList<>();
        private List<StrategyCardModel> strategyCards = new ArrayList<>();
    }

    private static File sidecarFile(String gameName) {
        return new File(Storage.getGameFile(gameName + "_custom_cards.json").getPath());
    }

    public static CustomCards load(String gameName) {
        File file = sidecarFile(gameName);
        if (!file.exists()) return new CustomCards();
        try {
            return mapper.readValue(file, CustomCards.class);
        } catch (Exception e) {
            BotLogger.error(null, "Failed to load custom cards for game: " + gameName, e);
            return new CustomCards();
        }
    }

    public static void save(String gameName, CustomCards cards) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(sidecarFile(gameName), cards);
        } catch (Exception e) {
            BotLogger.error(null, "Failed to save custom cards for game: " + gameName, e);
        }
    }

    public static void registerIntoMapper(CustomCards cards) {
        cards.getActionCards().forEach(Mapper::registerActionCard);
        cards.getStrategyCards().forEach(Mapper::registerStrategyCard);
    }

    public static void addActionCard(String gameName, ActionCardModel model) {
        CustomCards cards = load(gameName);
        cards.getActionCards().removeIf(ac -> ac.getAlias().equals(model.getAlias()));
        cards.getActionCards().add(model);
        save(gameName, cards);
        Mapper.registerActionCard(model);
    }

    public static void addStrategyCard(String gameName, StrategyCardModel model) {
        CustomCards cards = load(gameName);
        cards.getStrategyCards().removeIf(sc -> sc.getId().equals(model.getId()));
        cards.getStrategyCards().add(model);
        save(gameName, cards);
        Mapper.registerStrategyCard(model);
    }
}
