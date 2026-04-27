package ti4.discord.interactions.commands.homebrew;

import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import ti4.discord.interactions.commands.GameStateSubcommand;
import ti4.game.persistence.GameCustomCardService;
import ti4.image.Mapper;
import ti4.message.MessageHelper;
import ti4.model.Source.ComponentSource;
import ti4.model.StrategyCardModel;

class AddCustomSC extends GameStateSubcommand {

    AddCustomSC() {
        super("add_custom_sc", "Add a custom strategy card to the game", true, true);
        addOptions(new OptionData(OptionType.STRING, "id", "Unique card ID (e.g. my-sc-9)").setRequired(true));
        addOptions(new OptionData(OptionType.INTEGER, "initiative", "Initiative number").setRequired(true).setMinValue(1));
        addOptions(new OptionData(OptionType.STRING, "name", "Card name").setRequired(true));
        addOptions(new OptionData(OptionType.STRING, "primary", "Primary ability text").setRequired(true));
        addOptions(new OptionData(OptionType.STRING, "secondary", "Secondary ability text").setRequired(true));
        addOptions(new OptionData(OptionType.STRING, "colour", "Hex colour code (e.g. #9b59b6)"));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String id = event.getOption("id").getAsString();
        int initiative = event.getOption("initiative").getAsInt();
        String name = event.getOption("name").getAsString();
        String primary = event.getOption("primary").getAsString();
        String secondary = event.getOption("secondary").getAsString();
        String colour = event.getOption("colour", "#ffffff", OptionMapping::getAsString);

        String replacedNotice = "";
        if (Mapper.isValidStrategyCard(id)) {
            StrategyCardModel old = Mapper.getStrategyCard(id);
            replacedNotice = "\n> Replaced existing card **" + old.getName() + "** (initiative " + old.getInitiative() + ")."
                    + " To restore: `/homebrew add_custom_sc id:" + id + " initiative:" + old.getInitiative()
                    + " name:" + old.getName() + " primary:... secondary:...`";
        }

        StrategyCardModel model = new StrategyCardModel();
        model.setId(id);
        model.setInitiative(initiative);
        model.setName(name);
        model.setPrimaryTexts(List.of(primary));
        model.setSecondaryTexts(List.of(secondary));
        model.setColourHexCode(colour);
        model.setSource(ComponentSource.other);

        GameCustomCardService.addStrategyCard(getGame().getName(), model);
        getGame().addSC(initiative);

        MessageHelper.sendMessageToChannel(event.getChannel(),
                "Custom strategy card `" + id + "` (**" + name + "**, initiative " + initiative + ") added to the game." + replacedNotice);
    }

    @Override
    public boolean isSuspicious(SlashCommandInteractionEvent event) {
        return true;
    }
}
