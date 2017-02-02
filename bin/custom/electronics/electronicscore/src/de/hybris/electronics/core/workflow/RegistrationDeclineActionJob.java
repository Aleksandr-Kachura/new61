package de.hybris.electronics.core.workflow;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
// job for decline action
@Component
public class RegistrationDeclineActionJob extends AbstractMediaRegistrationAction
{
    private static final Logger LOG = Logger.getLogger(RegistrationDeclineActionJob.class);

    @Override
    public WorkflowDecisionModel perform(final WorkflowActionModel action)
    {
        final MediaModel media = getAttachedMedia(action);

        LOG.info("Media " + media.getCode() + " declined and will be removed.");

        getModelService().remove(media);

        for (final WorkflowDecisionModel decision : action.getDecisions())
        {
            return decision;
        }
        return null;
    }
}