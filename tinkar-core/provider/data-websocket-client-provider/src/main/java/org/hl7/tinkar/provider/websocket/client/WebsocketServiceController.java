package org.hl7.tinkar.provider.websocket.client;

import com.google.auto.service.AutoService;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;
import org.hl7.tinkar.common.service.DataServiceController;
import org.hl7.tinkar.common.service.DataServiceProperty;
import org.hl7.tinkar.common.service.PrimitiveDataService;
import org.hl7.tinkar.common.service.DataUriOption;
import org.hl7.tinkar.common.validation.ValidationRecord;
import org.hl7.tinkar.common.validation.ValidationSeverity;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

@AutoService(DataServiceController.class)
public class WebsocketServiceController implements DataServiceController<PrimitiveDataService> {
    public static final String CONTROLLER_NAME = "Websocket";
    private static final DataServiceProperty passwordProperty = new DataServiceProperty("password", true, true);
    MutableMap<DataServiceProperty, String> providerProperties = Maps.mutable.empty();
    {
        providerProperties.put(new DataServiceProperty("username", false, false), null);
        providerProperties.put(passwordProperty, null);
    }

    @Override
    public List<DataUriOption> providerOptions() {
        try {
            return List.of(new DataUriOption("localhost websocket", new URI("ws://127.0.0.1:8080/")));
        } catch (URISyntaxException e) {
           throw new RuntimeException(e);
        }
    }

    @Override
    public ValidationRecord[] validate(DataServiceProperty dataServiceProperty, Object value, Object target) {
        if (passwordProperty.equals(dataServiceProperty)) {
            if (value instanceof String password)  {
                if (password.isBlank()) {
                    return new ValidationRecord[] { new ValidationRecord(ValidationSeverity.ERROR,
                            "Password cannot be blank", target)};
                } else if (password.length() < 5)  {
                    return new ValidationRecord[] { new ValidationRecord(ValidationSeverity.ERROR,
                            "Password cannot be less than 5 characters", target)};
                } else if (password.length() < 8) {
                    return new ValidationRecord[] { new ValidationRecord(ValidationSeverity.WARNING,
                            "Password recommended to be 8 or more characters", target),
                            new ValidationRecord(ValidationSeverity.INFO,
                                    "Password is " + password.length() +
                                            " characters long", target),
                    };
                } else {
                    return new ValidationRecord[] { new ValidationRecord(ValidationSeverity.OK,
                            "Password OK", target)};
                }
            }
        }
        return new ValidationRecord[]{};
    }
    DataUriOption dataUriOption;
    @Override
    public String controllerName() {
        return CONTROLLER_NAME;
    }

    @Override
    public ImmutableMap<DataServiceProperty, String> providerProperties() {
        return providerProperties.toImmutable();
    }

    @Override
    public void setDataServiceProperty(DataServiceProperty key, String value) {
        providerProperties.put(key, value);
    }

    @Override
    public boolean isValidDataLocation(String name) {
        return name.toLowerCase(Locale.ROOT).startsWith("ws://");
    }

    @Override
    public void setDataUriOption(DataUriOption dataUriOption) {
        this.dataUriOption = dataUriOption;
    }

    @Override
    public Class<? extends PrimitiveDataService> serviceClass() {
        return DataProviderWebsocketClient.class;
    }

    @Override
    public boolean running() {
        return false;
    }

    @Override
    public void start() {
        try {
            DataProviderWebsocketClient client = new DataProviderWebsocketClient(dataUriOption.uri());
            client.launch(new String[]{});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void save() {

    }

    @Override
    public void reload() {

    }

    @Override
    public PrimitiveDataService provider() {
        return null;
    }

    @Override
    public String toString() {
        return "Websocket";
    }
}
