/**
 * Provides command line interface.
 */
open module dev.openclosed.squall.cli {
    requires transitive dev.openclosed.squall.api;
    requires info.picocli;

    // the only package to be exported
    exports dev.openclosed.squall.cli.spi;

    uses dev.openclosed.squall.cli.spi.SubcommandProvider;
    uses dev.openclosed.squall.cli.spi.MessagesProvider;

    provides dev.openclosed.squall.cli.spi.SubcommandProvider
        with dev.openclosed.squall.cli.command.config.ConfigCommandProvider,
             dev.openclosed.squall.cli.command.doc.DocCommandProvider;

    provides dev.openclosed.squall.cli.spi.HelpMessagesProvider
        with dev.openclosed.squall.cli.spi.impl.DefaultResourceBundleProvider;

    provides dev.openclosed.squall.cli.spi.MessagesProvider
        with dev.openclosed.squall.cli.spi.impl.DefaultResourceBundleProvider;
}
