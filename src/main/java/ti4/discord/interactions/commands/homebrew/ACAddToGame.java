package ti4.discord.interactions.commands.homebrew;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import ti4.discord.interactions.commands.GameStateSubcommand;
import ti4.helpers.Constants;
import ti4.message.MessageHelper;

class ACAddToGame extends GameStateSubcommand {

    ACAddToGame() {
        super(Constants.ADD_AC_TO_GAME, "Add copies of an action card to the game deck", true, true);
        addOptions(new OptionData(OptionType.STRING, Constants.AC_ID, "Action card (search by id, name or source)")
                .setRequired(true)
                .setAutoComplete(true));
        addOptions(new OptionData(OptionType.INTEGER, "count", "Number of copies to add (default 1)")
                .setMinValue(1)
                .setMaxValue(10));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String id = event.getOption(Constants.AC_ID).getAsString();
        int count = event.getOption("count", 1, OptionMapping::getAsInt);
        int total = getGame().addACCopiesToGame(id, count);
        if (total == 0) {
            MessageHelper.sendMessageToChannel(event.getChannel(), "Action card not found: " + id);
        } else {
            MessageHelper.sendMessageToChannel(event.getChannel(),
                    "Added " + count + " cop" + (count == 1 ? "y" : "ies") + " of `" + id + "` to the deck. Total copies in deck: " + total);
        }
    }

    @Override
    public boolean isSuspicious(SlashCommandInteractionEvent event) {
        return true;
    }
}
