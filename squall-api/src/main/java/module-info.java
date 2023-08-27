module dev.openclosed.squall.api {

    exports dev.openclosed.squall.api.base;
    exports dev.openclosed.squall.api.config;
    exports dev.openclosed.squall.api.parser;
    exports dev.openclosed.squall.api.renderer;
    exports dev.openclosed.squall.api.renderer.support;
    exports dev.openclosed.squall.api.spi;
    exports dev.openclosed.squall.api.spec;
    exports dev.openclosed.squall.api.spec.builder;

    uses dev.openclosed.squall.api.config.ConfigLoader;
    uses dev.openclosed.squall.api.parser.SqlParserFactory;
    uses dev.openclosed.squall.api.parser.DocCommentHandler;
    uses dev.openclosed.squall.api.renderer.RendererFactory;
    uses dev.openclosed.squall.api.spec.ExpressionFactory;
    uses dev.openclosed.squall.api.spec.builder.DatabaseSpecBuilder;
    uses dev.openclosed.squall.api.spi.JsonReader;
    uses dev.openclosed.squall.api.spi.JsonWriter;
    uses dev.openclosed.squall.api.spi.RendererMessagesProvider;
}
