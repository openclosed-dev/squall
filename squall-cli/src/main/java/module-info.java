open module dev.openclosed.squall.cli {
    requires transitive dev.openclosed.squall.api;
    requires info.picocli;

    // the only package exported
    exports dev.openclosed.squall.cli.spi;

    uses dev.openclosed.squall.cli.spi.BaseCommand;
    uses dev.openclosed.squall.cli.spi.MessagesProvider;

    provides dev.openclosed.squall.cli.spi.BaseCommand
        with dev.openclosed.squall.cli.command.sub.Init,
             dev.openclosed.squall.cli.command.sub.Render;

    provides dev.openclosed.squall.cli.spi.HelpMessagesProvider
        with dev.openclosed.squall.cli.spi.impl.DefaultResourceBundleProvider;

    provides dev.openclosed.squall.cli.spi.MessagesProvider
        with dev.openclosed.squall.cli.spi.impl.DefaultResourceBundleProvider;
}
