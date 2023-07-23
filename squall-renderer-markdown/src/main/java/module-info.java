module dev.openclosed.squall.renderer.markdown {
    requires dev.openclosed.squall.api;

    provides dev.openclosed.squall.api.renderer.RendererFactory
        with dev.openclosed.squall.renderer.markdown.MarkdownRendererFactory;
}
