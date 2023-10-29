import dev.openclosed.squall.api.text.json.JsonReader;
import dev.openclosed.squall.api.text.json.JsonWriter;

module dev.openclosed.squall.service.json {
    requires dev.openclosed.squall.api;
    requires com.fasterxml.jackson.databind;

    provides JsonReader
        with dev.openclosed.squall.service.json.JacksonJsonReader;

    provides JsonWriter
        with dev.openclosed.squall.service.json.JacksonJsonWriter;
}
