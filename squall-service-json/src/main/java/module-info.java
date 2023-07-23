module dev.openclosed.squall.service.json {
    requires dev.openclosed.squall.api;
    requires com.fasterxml.jackson.databind;

    provides dev.openclosed.squall.api.spi.JsonReader
        with dev.openclosed.squall.service.json.JacksonJsonReader;

    provides dev.openclosed.squall.api.spi.JsonWriter
        with dev.openclosed.squall.service.json.JacksonJsonWriter;
}
