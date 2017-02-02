package de.hybris.electronics.core.workflow;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

// job for confirm action
@Component
public class RegistrationConfirmationActionJob extends AbstractMediaRegistrationAction {

    private static final Logger LOG = Logger.getLogger(RegistrationConfirmationActionJob.class);

    @Override
    public WorkflowDecisionModel perform(final WorkflowActionModel action) {
        final MediaModel media = getAttachedMedia(action);

        LOG.info("Media " + media.getCode() + " confirmed.");
        media.setStatus("confirm");
        getModelService().save(media);

        for (final WorkflowDecisionModel decision : action.getDecisions()) {
            return decision;
        }
        return null;
    }
}
