package uk.co.hmtt.cucumber.parallel.system;

import uk.co.hmtt.cucumber.parallel.model.Recorder;

public class FeatureManager {

    private static SynchronisedFile<Recorder> synchronisedFile = new SynchronisedFile<>();

    private FeatureManager() {}

    public static boolean featureNotAlreadyRun(final String name) {
        final Recorder read = synchronisedFile.read(Recorder.class);
        if (!read.getFeatures().contains(name)) {
            read.getFeatures().add(name);
            synchronisedFile.write(read);
            return true;
        } else {
            synchronisedFile.release();
            return false;
        }
    }

}
