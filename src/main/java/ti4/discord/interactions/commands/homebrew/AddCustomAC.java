package ti4.discord.interactions.commands.homebrew;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import ti4.discord.interactions.commands.GameStateSubcommand;
import ti4.game.persistence.GameCustomCardService;
import ti4.image.Mapper;
import ti4.message.MessageHelper;
import ti4.model.ActionCardModel;
import ti4.model.Source.ComponentSource;

class AddCustomAC extends GameStateSubcommand {

    AddCustomAC() {
        super("add_custom_ac", "Add a custom action card to the game deck", true, true);
        addOptions(new OptionData(OptionType.STRING, "id", "Unique card ID (e.g. my-card-1)").setRequired(true));
        addOptions(new OptionData(OptionType.STRING, "name", "Card name").setRequired(true));
        addOptions(new OptionData(OptionType.STRING, "text", "Card text").setRequired(true));
        addOptions(new OptionData(OptionType.STRING, "phase", "Play phase (e.g. Action)").setRequired(true));
        addOptions(new OptionData(OptionType.STRING, "window", "Play window (e.g. Action)").setRequired(true));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String id = event.getOption("id").getAsString();
        String name = event.getOption("name").getAsString();
        String text = event.getOption("text").getAsString();
        String phase = event.getOption("phase").getAsString();
        String window = event.getOption("window").getAsString();

        if (Mapper.isValidActionCard(id)) {
            MessageHelper.sendMessageToChannel(event.getChannel(), "A card with id `" + id + "` already exists.");
            return;
        }

        ActionCardModel model = new ActionCardModel();
        model.setAlias(id);
        model.setName(name);
        model.setText(text);
        model.setPhase(phase);
        model.setWindow(window);
        model.setSource(ComponentSource.other);

        GameCustomCardService.addActionCard(getGame().getName(), model);
        getGame().addACToGame(id);

        MessageHelper.sendMessageToChannel(event.getChannel(),
                "Custom card `" + id + "` (**" + name + "**) added to the deck.");
    }

    @Override
    public boolean isSuspicious(SlashCommandInteractionEvent event) {
        return true;
    }
}
