package de.hybris.electronics.core.workflow;


import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
// check item type by media
public abstract class AbstractMediaRegistrationAction implements AutomatedWorkflowTemplateJob
{
    @Autowired
    private ModelService modelService;


    protected MediaModel getAttachedMedia(final WorkflowActionModel action)
    {
        final List<ItemModel> attachments = action.getAttachmentItems();
        if (attachments != null)
        {
            for (final ItemModel item : attachments)
            {
                if (item instanceof  MediaModel)
                {
                    return (MediaModel) item;
                }
            }
        }
        return null;
    }

    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }

    protected ModelService getModelService()
    {
        return this.modelService;
    }

}
