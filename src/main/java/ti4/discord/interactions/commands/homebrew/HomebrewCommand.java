package ti4.discord.interactions.commands.homebrew;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ti4.discord.interactions.commands.ParentCommand;
import ti4.discord.interactions.commands.Subcommand;
import ti4.helpers.Constants;

public class HomebrewCommand implements ParentCommand {

    private final Map<String, Subcommand> subcommands = Stream.of(
                    new ACAddToGame(),
                    new AddCustomAC(),
                    new AddCustomSC())
            .collect(Collectors.toMap(Subcommand::getName, subcommand -> subcommand));

    @Override
    public String getName() {
        return Constants.HOMEBREW_COMMAND;
    }

    @Override
    public String getDescription() {
        return "Homebrew Commands";
    }

    @Override
    public Map<String, Subcommand> getSubcommands() {
        return subcommands;
    }
}
