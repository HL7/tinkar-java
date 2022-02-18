package org.hl7.tinkar.common.service;

import com.google.auto.service.AutoService;
import org.eclipse.collections.api.list.primitive.IntList;
import org.eclipse.collections.api.set.primitive.IntSet;
import org.hl7.tinkar.common.alert.AlertObject;
import org.hl7.tinkar.common.alert.AlertStreams;
import org.hl7.tinkar.common.id.IntIdCollection;
import org.hl7.tinkar.common.id.PublicId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.ToIntFunction;

public class PrimitiveData {
    private static final Logger LOG = LoggerFactory.getLogger(PrimitiveData.class);

    private static DataServiceController<PrimitiveDataService> controllerSingleton;
    private static DefaultDescriptionForNidService defaultDescriptionForNidServiceSingleton;
    private static PublicIdService publicIdServiceSingleton;
    private static PrimitiveData singleton;
    private static CopyOnWriteArrayList<SaveState> statesToSave = new CopyOnWriteArrayList<>();

    static {
        singleton = new PrimitiveData();
    }

    public PrimitiveData() {
        ServiceLoader<DefaultDescriptionForNidService> loader = ServiceLoader.load(DefaultDescriptionForNidService.class);
        PrimitiveData.defaultDescriptionForNidServiceSingleton = loader.findFirst().get();
        ServiceLoader<PublicIdService> publicIdLoader = ServiceLoader.load(PublicIdService.class);
        PrimitiveData.publicIdServiceSingleton = publicIdLoader.findFirst().get();
        LOG.info("Default desc service: " + defaultDescriptionForNidServiceSingleton);
    }

    public static void start() {
        controllerSingleton.start();
    }

    public static void stop() {
        SimpleIndeterminateTracker progressTask = new SimpleIndeterminateTracker("Stop primitive data provider");
        TinkExecutor.threadPool().submit(progressTask);
        try {
            save();
            if (controllerSingleton != null) {
                controllerSingleton.stop();
            }
        } catch (Throwable ex) {
            LOG.error(ex.getLocalizedMessage(), ex);
        } finally {
            progressTask.finished();
        }
    }

    public static void save() {
        if (controllerSingleton != null) {
            controllerSingleton.save();
        }
        for (SaveState state : statesToSave) {
            try {
                state.save();
            } catch (Exception e) {
                AlertStreams.getRoot().dispatch(AlertObject.makeError(e));
            }
        }
    }

    public static CopyOnWriteArrayList<SaveState> getStatesToSave() {
        return statesToSave;
    }

    public static void reload() {
        controllerSingleton.reload();
    }

    public static boolean running() {
        if (controllerSingleton == null) {
            return false;
        }
        return controllerSingleton.running();
    }

    public static List<DataServiceController> getControllerOptions() {
        final List<DataServiceController> dataServiceControllers = ServiceLoader.load(DataServiceController.class)
                .stream().map(dataServiceControllerProvider -> dataServiceControllerProvider.get()).toList();
        return dataServiceControllers;
    }

    public static void selectControllerByName(String name) {
        PrimitiveData.selectController((dataServiceController) -> {
            if (name.equals(dataServiceController.controllerName())) {
                return 1;
            }
            return -1;
        });
    }

    public static void selectController(ToIntFunction<DataServiceController<?>> scorer) {
        DataServiceController<PrimitiveDataService> topContender = null;
        int topScore = -1;
        int controllerCount = 0;
        ServiceLoader<DataServiceController> loader = ServiceLoader.load(DataServiceController.class);
        for (DataServiceController controller : loader) {
            if (PrimitiveDataService.class.isAssignableFrom(controller.serviceClass())) {
                controllerCount++;
                int score = scorer.applyAsInt(controller);
                if (score > topScore) {
                    topScore = score;
                    topContender = controller;
                }
            }
        }
        if (topScore > -1) {
            setController(topContender);
        } else {
            throw new IllegalStateException("No DataServiceController selected for provider. Tried " + controllerCount);
        }
    }

    public static DataServiceController getController() {
        return controllerSingleton;
    }

    public static void setController(DataServiceController controller) {
        controllerSingleton = controller;
    }

    public static String textFast(int nid) {
        return PrimitiveData.defaultDescriptionForNidServiceSingleton.textFast(nid);
    }

    public static String text(int nid) {
        Optional<String> textOptional = textOptional(nid);
        if (textOptional.isPresent()) {
            return textOptional.get();
        }
        return "<" + nid + ">";
    }

    public static Optional<String> textOptional(int nid) {
        return defaultDescriptionForNidServiceSingleton.textOptional(nid);
    }

    public static String textWithNid(int nid) {
        StringBuilder sb = new StringBuilder();
        textOptional(nid).ifPresent(s -> sb.append(s).append(" "));
        sb.append("<").append(nid).append(">");
        return sb.toString();
    }

    public static List<Optional<String>> optionalTextList(int... nids) {
        return defaultDescriptionForNidServiceSingleton.optionalTextList(nids);
    }

    public static List<Optional<String>> optionalTextList(IntIdCollection nids) {
        return defaultDescriptionForNidServiceSingleton.optionalTextList(nids);
    }

    public static List<Optional<String>> optionalTextList(IntList nids) {
        return defaultDescriptionForNidServiceSingleton.optionalTextList(nids);
    }

    public static List<Optional<String>> optionalTextList(IntSet nids) {
        return defaultDescriptionForNidServiceSingleton.optionalTextList(nids);
    }

    public static List<String> textList(int... nids) {
        return defaultDescriptionForNidServiceSingleton.textList(nids);
    }

    public static List<Optional<String>> textList(IntIdCollection nids) {
        return defaultDescriptionForNidServiceSingleton.textList(nids);
    }

    public static List<Optional<String>> textList(IntList nids) {
        return defaultDescriptionForNidServiceSingleton.textList(nids);
    }

    public static List<Optional<String>> textList(IntSet nids) {
        return defaultDescriptionForNidServiceSingleton.textList(nids);
    }

    public static PublicId publicId(int nid) {
        return publicIdServiceSingleton.publicId(nid);
    }

    public static int nid(PublicId publicId) {
        return get().nidForPublicId(publicId);
    }

    public static PrimitiveDataService get() {
        if (controllerSingleton != null) {
            return controllerSingleton.provider();
        }
        throw new IllegalStateException("No provider. Call Select provider prior to get()");
    }

    public static int nid(UUID... uuids) {
        return get().nidForUuids(uuids);
    }


    @AutoService(CachingService.class)
    public static class CacheProvider implements CachingService {

        @Override
        public void reset() {
            controllerSingleton = null;
            defaultDescriptionForNidServiceSingleton = null;
            publicIdServiceSingleton = null;
            statesToSave.clear();
            singleton = new PrimitiveData();
        }
    }

}
