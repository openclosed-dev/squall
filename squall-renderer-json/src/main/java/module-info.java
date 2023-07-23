import dev.openclosed.squall.renderer.json.JacksonJsonRendererFactory;

module dev.openclosed.squall.renderer.json {
    requires dev.openclosed.squall.api;
    requires com.fasterxml.jackson.databind;

    provides dev.openclosed.squall.api.renderer.RendererFactory
        with JacksonJsonRendererFactory;

}
